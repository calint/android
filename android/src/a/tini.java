package a;
//import static b.b.*;
import b.a;
import b.osltgt;
import b.path;
import b.xwriter;
public class tini extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.cssfont("tini","/ttf/tini.ttf");
		x.css("*","font-family:tini;font-size:4px");
		x.styleEnd();
//		x.pre();
		x.pre().p("tini font").nl();
		
		print(new xwriter(new osltgt(x.outputstream())),b.b.path("/src"));
//		b.b.path("/src/a/tini.java").to(x);
	}
	private void print(final xwriter x,final path p)throws Throwable{
		if(p.isdir()){
			for(final String fn:p.list()){
				final path pp=p.get(fn);
				print(x,pp);
			}
		}else
			p.to(x);	
	}
	public void ax_(final xwriter x,final String[]a)throws Throwable{}
}
