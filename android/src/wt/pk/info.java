package wt.pk;
import htp.osltgt;
import htp.req;
import htp.wt;
import htp.xwriter;
import htpx.cli;
final public class info extends wt{
	private static final long serialVersionUID=1;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String[] qs=r.querystr().toLowerCase().replace(';',' ').replace('&',' ').trim().split("\\?");
		x.p("<pre>").nl();
		if(qs[0].length()==0)
			return;
		new cli("sh",new osltgt(x.outputStream())).p("apt-cache show ").p(qs[0]).exit();
	}
}
