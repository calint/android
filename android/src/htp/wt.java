package htp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
public class wt implements Serializable{
	static final long serialVersionUID=1;
	private String nm;
	private wt pt;
	private Serializable value;
	public wt(){
		try{
			for(final Field fld:getClass().getFields()){
				if(!wt.class.isAssignableFrom(fld.getType()))
					continue;
				wt w=(wt)fld.get(this);
				if(w==null)
					w=(wt)fld.getType().newInstance();
				fld.set(this,w);
				w.nm=fld.getName();
				w.pt=this;				
			}
		}catch(Throwable e){
			throw new Error(e);
		}
	}
	public wt(final wt parent,final String name){this();pt=parent;nm=name;}
	public final String getName(){if(nm==null)return "";return nm;}
	public final wt getParent(){return pt;}
	public final void setValue(final String v){value=v;}
	final void ax(final xwriter x,final String[]pb) throws Throwable{
		if(pb.length==1)
			return;
		final String methodName="ax_"+pb[1];
		final Object[]params=new Object[]{x,pb};
		Method method=null;
		try{method=getClass().getMethod(methodName,new Class[]{xwriter.class,String[].class});}catch(NoSuchMethodException e){}
		if(method==null){
			x.x_alert("method not found:\n"+getClass().getName()+"."+methodName+"(xwriter,String[])");
			return;
		}
		try{
			method.invoke(this,params);
		}catch(InvocationTargetException e){
			htp.log(e);
			final String str=htp.stackTraceLine(e.getTargetException());
			x.x_alert(str);
		}
	}
	protected final wt childFind(final String id){
		final String wids=wid();
		if(id.equals(wids))
			return this;
		for(final Field fld:getClass().getFields()){
			if(!wt.class.isAssignableFrom(fld.getType()))
				continue;
			if(!fld.getName().equals(id))
				continue;
			final wt w;
			try{
				w=(wt)fld.get(this);
			}catch(Throwable e){
				throw new Error(e);
			}
			return w;
		}
		return childFindQuery(id);
	}
	protected wt childFindQuery(final String id){return null;}
	public final boolean isEmpty(){return value==null||value.toString().length()==0;}
	//?onpost(sb,map)
	final void onPost(final Map<String,String> post) throws Throwable{
		final String wid=wid();
		final String v=post.get(wid);
		if(v!=null)
			value=v;
		for(final Field fld:getClass().getFields()){
			if(!wt.class.isAssignableFrom(fld.getType()))
				continue;
			final wt w=(wt)fld.get(this);
			w.onPost(post);
		}
	}
	public final void setFrom(final path path) throws Throwable{
		final ByteArrayOutputStream baos=new ByteArrayOutputStream((int)path.getSize());
		path.to(baos);
		baos.close();
		setValue(baos.toString(htp.strenc));
	}
	
	public final void to(final OutputStream os)throws IOException{
		os.write(htp.tobytes(htp.tostr(value,"")));
	}
	public final void to(final path p)throws IOException{
		final OutputStream os=p.getOutputStream(false);
		to(os);
		os.close();
	}
	public void to(final xwriter x)throws Throwable{
		if(value==null)
			return;
		x.p(value.toString());
	}
	@Override public String toString(){
		if(value==null)
			return "";
		return value.toString();
	}
	public final String wid(){
		String s=nm;
		for(wt p=this;p.pt!=null;p=p.pt){
			s=htp.tostr(p.pt.nm,"")+"_"+s;
		}
		return htp.tostr(s,"_");
	}
	public final void x_focus(final xwriter x){
		x.p("ui.e_focus('").p(wid()).p("');").nl();
	}
	public final void x_setAttr(final xwriter x,final String attr,final String value) throws IOException{
		x.p("ui.e_seta('").p(wid()).p("','").p(attr).p("','");
		new osjsstr(x.outputStream()).write(htp.tobytes(value));
		x.p("');").nl();
	}
	public final void x_setValue(final xwriter x,final String v) throws IOException{
		value=v;
		x.p("ui.e_setv('").p(wid()).p("','");
		new osjsstr(x.outputStream()).write(htp.tobytes(v));
		x.p("');").nl();
	}
	public final void x_updInnerHtml(final xwriter x) throws IOException{
		x.p("ui.e_seth('").p(wid()).p("','");
		to(new osjsstr(x.outputStream()));
		x.p("');").nl();
	}
	public void onEvent(final xwriter x,final wt fromwt,final Object o)throws Throwable{
		if(pt!=null)
			pt.onEvent(x,fromwt,o);
	}
}
