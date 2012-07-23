package b;
import static b.b.path;
import static b.b.strenc;
import static b.b.tobytes;
import static b.b.tostr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Field;
public class a implements Serializable{
	static final long serialVersionUID=1;
	private a pt;
	private String nm;
	private String s;
//	public boolean equals(final Object o){
//		if(!(o instanceof a))
//			return false;
//		final a a=(a)o;
//		if(a.pt!=pt)return false;
//		if(a.nm!=null&&!a.nm.equals(nm))return false;
//		if(a.s!=null&&!a.s.equals(s))return false;
//		return true;
//	}
	public a(){init();}
	public a(final a parent,final String name){pt=parent;nm=name;init();}
	public a(final a parent,final String name,final String value){pt=parent;nm=name;s=value;init();}
	private void init(){
//		try{for(final Field fld:getClass().getDeclaredFields()){
		try{for(final Field fld:getClass().getFields()){
			if(!a.class.isAssignableFrom(fld.getType()))
				continue;
//			if(fld.getName().equals("pt"))
//				continue;
//			fld.setAccessible(true);
			a a=(a)fld.get(this);
			if(a==null)
				a=(a)fld.getType().newInstance();
			fld.set(this,a);
			a.nm=fld.getName();
			a.pt=this;				
		}}catch(final Throwable e){throw new Error(e);}
	}
	public final String id(){
		String s=nm;
		for(a p=this;p.pt!=null;p=p.pt)
			s=tostr(p.pt.nm,"")+"_"+s;
		return tostr(s,"_");
	}
	public final String name(){return nm;}
	public final a parent(){return pt;}
	public final a chld(final String id){try{return (a)getClass().getField(id).get(this);}catch(Throwable e){}return chldq(id);}
	protected a chldq(final String id){return null;}
	protected void ev(final xwriter x,final a from,final Object o)throws Throwable{if(pt!=null)pt.ev(x,from,o);}

	
	public void to(final xwriter x)throws Throwable{if(s==null)return;x.p(s);}
	public final a set(final String s){this.s=s;return this;}
	public final a clr(){return set(null);}
	public final boolean isempty(){return s==null||s.length()==0;}
	public final String toString(){return s==null?"":s;}
	public final int toint(){return isempty()?0:Integer.parseInt(toString());}
	
	
	
	
	
	
	public final void to(final OutputStream os)throws IOException{os.write(tobytes(tostr(s,"")));}//? impl s?.to(os)
	final public void to(final path p)throws IOException{to(p,false);}
	final public void to(final path p,final boolean append)throws IOException{final OutputStream os=p.outputstream(append);to(os);os.close();}
	final public a read(final path p)throws IOException{//? impl
		final ByteArrayOutputStream baos=new ByteArrayOutputStream((int)p.size());
		p.to(baos);
		baos.close();
		set(baos.toString(strenc));
		return this;
	}
	final private static byte[]bafieldsep=tobytes(": ");
	final private static byte[]balinesep=tobytes("\n");
	final public void objsave(final boolean sessionstore)throws Throwable{
		final Class<? extends a>cls=getClass();
		final path file=objroot(cls,sessionstore).get(toString());
		final OutputStream os=file.outputstream(false);
		for(final Field f:cls.getFields()){
			if(!a.class.isAssignableFrom(f.getType()))
				continue;
			final a ww=(a)f.get(this);
			os.write(tobytes(f.getName()));
			os.write(bafieldsep);
			if(ww!=null)
				os.write(tobytes(ww.toString().replace('\n','\07')));
			os.write(balinesep);
		}
		os.close();
	}
	final public void objload(final boolean sessionstore)throws Throwable{
		final Class<? extends a>cls=getClass();
		final path p=objroot(cls,sessionstore).get(toString());
		
		final Reader re=new InputStreamReader(p.inputstream(),strenc);
		try{int s=0;
			final StringBuilder sbname=new StringBuilder(256);
			final StringBuilder sbvalue=new StringBuilder(256);
			Field fld=null;
			while(true){
				final int ch=re.read();
				if(ch==-1)
					break;
				switch(s){
				case 0:
					if(ch==bafieldsep[0]){
						fld=cls.getField(sbname.toString().trim());
						sbvalue.setLength(0);
						s=1;
					}else
						sbname.append((char)ch);
					break;
				case 1:
					if(ch==balinesep[0]){
						final a ww=(a)fld.get(this);
						ww.set(sbvalue.toString().trim().replace('\07','\n'));
						sbname.setLength(0);
						s=0;
					}else
						sbvalue.append((char)ch);
					break;
				default:throw new Error();
				}
			}
		}finally{
			re.close();
		}
	}
	final public static a[]objls(final Class<?>cls,boolean sessionstore)throws Throwable{
		final String[]ls=objroot(cls,sessionstore).list();
		final a[]wtls=new a[ls.length];
		for(int n=0;n<ls.length;n++){
			final a w;
			try{w=(a)cls.newInstance();}catch(final Throwable t){throw new Error(t);}
			w.set(ls[n]);
			w.objload(sessionstore);
			wtls[n]=w;
		}
		return wtls;
	}
	private static path objroot(final Class<?>cls,final boolean sessionstore){return sessionstore?req.get().session().path(cls.getName().replace('.','/')):path(cls);}
	private path objroot(final boolean sessionstore){return objroot(getClass(),sessionstore);}
	final public boolean objdelete(final boolean sessionstore)throws IOException{return objroot(sessionstore).get(toString()).rm();}
	final public boolean objexists(final boolean sessionstore)throws IOException{return objroot(sessionstore).get(toString()).exists();}
}
