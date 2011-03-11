package wt.pk;
import htp.req;
import htp.wt;
import htp.xwriter;
import htpx.cli;
import htpx.osnewliner;
import java.net.URLDecoder;
final public class all extends wt{
	private static final long serialVersionUID=1;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String qs=URLDecoder.decode(r.querystr(),"utf8").toLowerCase().replace(';',' ').replace('&',' ').trim();
		if(qs.length()==0)
			return;
		x.p("<pre><code><tt>");
		new cli("sh",new osnewliner(){
			@Override public void on_newline(String line){
				String s=line.trim().toLowerCase();
				if(!s.startsWith(qs))
					return;
				x.p("<a href=\"").p(getClass().getName().substring(htp.htp.web_widgets_package.length()).replace('.','/'));
				if(qs!=null)
					x.p("?").p(qs);
				x.p("\">").p(s).aEnd().nl();
			};
		}).p("apt-cache pkgnames ").p(qs).exit();
	}
}
