package wt.oo;
import htp.wt;
import htp.xwriter;
public class project extends oo{
	static final long serialVersionUID=1;
	public wt note;
	@Override public void to(xwriter x) throws Throwable{
		super.to(x);
		x.inputTextArea(note,"editor");
	}
}
