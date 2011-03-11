package wt;
import htp.wt;
import htp.xwriter;
public class utf extends wt{
	private static final long serialVersionUID=1;
	@Override public void to(xwriter x) throws Throwable{
		x.style();
		x.p("pre.glyphs{font-size:27px}");
		x.styleEnd();
		x.p("<center><pre><code>").pre("glyphs");
		char ch=0;
		int lines=0x20000/33;
		for(int n=0;n<lines;n++){
			for(int c=0;c<33;c++){
				x.p(ch);
				ch++;
			}
			x.pl("\n");
		}
		x.pl("\n</code></pre>");
	}
}
//ڀ