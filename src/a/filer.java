package a;
import b.a;
import b.req;
import b.xwriter;
public class filer extends a{
	static final long serialVersionUID=1;
	public diro d;
	{	d.bits(diro.BIT_ALL);
		d.root(req.get().session().path());
	}
	public void to(final xwriter x)throws Throwable{
		x.style().css("body","padding:0 17px 0 17px").styleEnd();
		d.to(x);
	}
//	public ed e;
//	private boolean ed;
//	public void to(final xwriter x)throws Throwable{
//		x.nl().tag("span",id());
//		x.rend(ed?e:d);
//		x.tagEnd("span");
//	}
//	protected void ev(final xwriter x,final a from,final Object o)throws Throwable{
//		if(from==d&&"c".equals(o)){
////			x.xalert("create");
//			e.path=d.path.get(d.q.toString());
//			if(!e.path.exists())
//				e.path.append("");
//			e.bd.read(e.path);
////			ed=true;
//			x.xua(this);
//			x.xfocus(d.q);
//		}else if(from==d&&"d".equals(o)){
//			d.path.get(d.q.toString()).mkdirs();
//			x.xua(this);
//		}else if(from==e&&"x".equals(o)){
//			ed=false;
//			x.xua(this);
//		}else super.ev(x,from,o);
//	}
}
