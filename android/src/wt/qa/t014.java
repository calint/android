package wt.qa;
import htp.req;
import htp.wt;
import htp.xwriter;
public class t014 extends wt{
	static final long serialVersionUID=1;
	public wt s;
	@Override public void to(xwriter x) throws Throwable{
		if(req.get().querystr().equals("rst"))
			s.setValue("");
		x.p(req.get().session().href()).br();
		x.p("ĸoö: ");
		x.inputText(s,null,this,"a").p(" ").action_ax(this,"a","post");
	}
	public void ax_a(xwriter x,String[] p) throws Throwable{
		req.get().session().path("test.txt").append(s.toString(),"\n");
		s.x_focus(x);
	}
}
