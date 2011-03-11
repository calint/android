package wt.oo;
import htp.wt;
import htp.xwriter;
public class person extends oo{
	static final long serialVersionUID=1;
	public wt email;
	@Override protected void init(){
		super.init();
		email.setValue("");
	}
	@Override public void to(xwriter x) throws Throwable{
		super.to(x);
		x.p("email").p(":").inputText(email,"line",this,"save").br();
	}
}
