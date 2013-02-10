package a.y;

import java.util.Arrays;
import java.util.Iterator;
import b.a;
import b.xwriter;

public class myinventory extends a{
	static final long serialVersionUID=1L;
	public void to(final xwriter x)throws Throwable{
		x.pre();
		x.p("inventory").nl();
		for(final Iterator<String>i=ls();i.hasNext();)
			x.p("· ").p(i.next()).nl();
	}
	protected Iterator<String>ls(){
		return Arrays.asList(
				"papper tissues",
				"soap (sodium)",
				"cotton balls",
				"cotton sticks",
				"wet wipes (desinfectant)",
				"lotion (canola/olive/peanut)",
				"tooth brush",
				"tooth paste",
				"zip lock bag",
				"notebook",
				"pencil",
				"eraser",
				"rolling paper",
				"tobacco",
				"tobacco pouch"
				).iterator();
	}
}
