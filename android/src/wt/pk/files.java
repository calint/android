package wt.pk;
import htp.req;
import htp.wt;
import htp.xwriter;
import htpx.cli;
import htpx.osnewliner;
final public class files extends wt{
	private static final long serialVersionUID=1;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String[] qs=r.querystr().toLowerCase().replace(';',' ').replace('&',' ').trim().split("\\?");
		x.p("<pre><code><tt>");
		//x.p(qs).nl();
		new cli("sh",new osnewliner(){
			@Override public void on_newline(String line){
				String s=line.toLowerCase().trim();
				x.aBgn(s).p(s).aEnd().nl();
			};
		}).p("dpkg-query -L ").p(qs[0]).exit();
	}
}
