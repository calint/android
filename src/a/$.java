package a;
import b.a;
import b.req;
import b.xwriter;
public class $ extends a{
	static final long serialVersionUID=1;
	public diro d;{d.bits(diro.BIT_ALL);d.root(req.get().session().path());}
	public void to(final xwriter x)throws Throwable{
		x.style().css("body","padding:0 17px 0 17px").styleEnd();
		d.to(x);
	}
}
