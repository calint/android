package a.qa;
import b.a;
import b.req;
import b.xwriter;
public class t014 extends a{
	static final long serialVersionUID=1;
	public a s;
	public void to(xwriter x)throws Throwable{
		if(req.get().query().equals("rst"))
			s.set("");
		x.p(req.get().session().href()).br();
		x.p("ĸoö: ");
		x.inputText(s,null,this,"a").p(" ").ax(this,"a","post");
	}
	public void ax_a(xwriter x,String[] p) throws Throwable{
		req.get().session().path("test.txt").append(s.toString(),"\n");
		x.xfocus(s);
	}
}