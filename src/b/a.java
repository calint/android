package b;
import static b.b.strenc;
import static b.b.tobytes;
import static b.b.tostr;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
public class a implements Serializable{
	static final long serialVersionUID=1;
	public static void firewall(final a o)throws Throwable{
		final Class<? extends a>cls=o.getClass();
		if(cls.equals(a.class))return;
		final String clsnm=cls.getName();
		final int i=clsnm.lastIndexOf('.');
		final String pkgnm=i==-1?"":clsnm.substring(0,i);
		if(pkgnm.endsWith(".a")&&!req.get().session().bitshasall(2))throw new Error("firewalled1");
		if(clsnm.startsWith("a.localhost.")&&!req.get().ip().toString().equals("/0:0:0:0:0:0:0:1"))throw new Error("firewalled2");
	}
	{try{firewall(this);}catch(final Throwable t){throw new Error(t);}}
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
	public final a name(final String nm){this.nm=nm;return this;}
	public final a parent(){return pt;}
	public final a parent(final a a){pt=a;return this;}
	public final void attach(final a a,final String id){a.pt=this;a.nm=id;try{getClass().getField(id).set(this,a);}catch(final Throwable t){throw new Error(t);}}
	public final a chld(final String id){try{return (a)getClass().getField(id).get(this);}catch(Throwable e){}return chldq(id);}
	protected a chldq(final String id){return null;}
	protected void ev(final xwriter x,final a from,final Object o)throws Throwable{if(pt!=null)pt.ev(x,from,o);}
	final protected void ev(final xwriter x,final a from)throws Throwable{ev(x,from,null);}
	final protected void ev(final xwriter x)throws Throwable{ev(x,this,null);}

	
	public void to(final xwriter x)throws Throwable{if(s==null)return;x.p(s);}
	public final a set(final String s){this.s=s;return this;}
	public final a set(final int i){this.s=Integer.toString(i);return this;}
	public final a set(final long i){this.s=Long.toString(i);return this;}
	public final a clr(){return set(null);}
	public final boolean isempty(){return s==null||s.length()==0;}
	public final String toString(){return s==null?"":s;}
	public final int toint(){return isempty()?0:Integer.parseInt(toString());}
	
	
	
	
	
	final public void to(final OutputStream os)throws IOException{os.write(tobytes(tostr(s,"")));}//? impl s?.to(os)
	final public void to(final path p,final boolean append)throws IOException{final OutputStream os=p.outputstream(append);to(os);os.close();}
	final public void to(final path p)throws IOException{to(p,false);}
	final public a read(final path p)throws IOException{//? impl
		final ByteArrayOutputStream baos=new ByteArrayOutputStream((int)p.size());
		p.to(baos);
		baos.close();
		set(baos.toString(strenc));
		return this;
	}
	public long tolong(){return Long.parseLong(toString());}
}
