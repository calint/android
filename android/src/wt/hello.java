package wt;
import htp.htp;
import htp.osltgt;
import htp.req;
import htp.wt;
import htp.xwriter;
import htpx.cli;
import java.io.OutputStream;
import java.util.Iterator;
public class hello extends wt{
	private static final long serialVersionUID=1;
	private static String hr="\n-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --\n";
	@Override public void to(xwriter x) throws Throwable{
		x.p("<pre><code>");
		x.p("          session: ").p(req.get().session().href()).nl();
		htp.stats_to(x.outputStream());
		x.p("           server: ");
		final OutputStream s=new osltgt(x.outputStream());
		new cli("sh",s).p("uname -a").exit();
		x.p("      server home: ");
		new cli("sh",s).p("pwd").exit();
		x.p("        home size: ");
		new cli("sh",s).p("du -sh .").exit();
		x.p("          classes: ");
		new cli("sh",s).p("du -sh jar/").exit();
		x.p(hr).nl();
		new cli("sh",s).p("free -l").exit();
		x.p(hr).nl();
		new cli("sh",s).p("df -h").exit();
		x.p(hr).nl();
		new cli("sh",s).p("ifconfig").exit();
		x.p(hr).nl();
		new cli("sh",s).p("iwconfig").exit();
		x.p(hr).nl();
		for(final Iterator<?> i=System.getProperties().entrySet().iterator();i.hasNext();x.pl(i.next().toString()));
		x.p(hr).nl();
		new cli("sh",s).p("ps axjf").exit();
	}
}
