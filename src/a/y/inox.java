package a.y;

import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import b.a;
import b.b;
import b.path;
import b.req;
import b.session;
import b.xwriter;
public class inox extends a{
	static final long serialVersionUID=1L;
	private boolean links;
	private Map<String,Integer>ix=new HashMap<String,Integer>();
	private Map<String,Integer>ix1=new HashMap<String,Integer>();
	private LinkedList<String>obs=new LinkedList<String>();
	public a ob;
	public void to(final xwriter x)throws Throwable{
		x.pre().p(req.get().session().id()).ax(this,null,"::links").ax(this,"s","::save").ax(this,"l","::load").ax(this,"c","::clear").nl().focus(this).hr();
		x.p("read: ").inputText(ob,"line",this,"o").nl();
		x.table("tbl");
		x.tr("h");
		x.th().p("session");
		x.th().p("name");
		x.th().p("stream");
		x.nl();
		for(final String s:obs){
			x.tr().td();
			if(!links)
				x.p(s);
			else
				x.a(b.sessionhref(s),s);
			x.td();
			x.p(b.path(b.sessionhref(s)+"!/name").readstr());
			x.td("last");
			final path p=b.path(b.sessionhref(s)+"!/lino");
			if(p.isfile()){
				Integer pos=ix.get(s);
				if(pos==null)
					pos=new Integer(0);
//				final MappedByteBuffer bb=p.mappedbb(true,0,p.size());
//				bb.mark();
//				try{bb.position(pos.intValue());}catch(IllegalArgumentException e){}
//				int len=bb.remaining();
//				if(len==0){
//					bb.reset();
//					pos=ix1.get(s);
//					if(pos==null)
//						pos=new Integer(0);
//					try{bb.position(pos.intValue());}catch(IllegalArgumentException e){}
//					len=bb.remaining();
//				}
//				final byte[]ba=new byte[len];
//				bb.get(ba,0,len);
//				x.outputstream().write(ba);
//				ix.put(s,new Integer(bb.position()));
				ix1.put(s,pos);
			}
			x.nl();
		}
		x.tableEnd();
	}
	public void ax_(final xwriter x,final String[]a)throws Throwable{
		links=!links;
		x.xreload();
	}
	public void ax_s(final xwriter x,final String[]a)throws Throwable{
		final session s=req.get().session();
		s.path("inox/ix").writeobj(ix);		
		s.path("inox/ix1").writeobj(ix1);		
		s.path("inox/obs").writeobj(obs);		
		x.xalert("saved");
	}
	@SuppressWarnings("unchecked")
	public void ax_l(final xwriter x,final String[]a)throws Throwable{
		final session s=req.get().session();
		ix=(Map<String,Integer>)s.path("inox/ix").readobj();		
		ix1=(Map<String,Integer>)s.path("inox/ix1").readobj();
		obs=(LinkedList<String>)s.path("inox/obs").readobj();
		x.xreload();
		x.xalert("loaded");
	}
	public void ax_o(final xwriter x,final String[]a)throws Throwable{
		if(obs.contains(ob.toString())){
			x.xalert("not added cause have "+ob.toString());
			return;
		}
		obs.add(ob.toString());
		x.xreload();
		x.xalert("added "+ob.toString());
	}
	public void ax_c(final xwriter x,final String[]a)throws Throwable{
		obs.clear();
		x.xreload();
		x.xalert("cleared");
	}
}
