package wt.pk;
import htp.req;
import htp.wt;
import htp.xwriter;
import htpx.cli;
import htpx.osnewliner;
public class requires extends wt{
	private static final long serialVersionUID=1;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String[] qs=r.querystr().toLowerCase().replace(';',' ').replace('&',' ').trim().split("\\?");
		if(qs[0].length()==0)
			return;
		final StringBuffer sb=new StringBuffer(r.pathstr()).append("?");
		x.p("<pre><code><tt>").nl();
		final int sbl=sb.length();
		new cli("sh",new osnewliner(){
			@Override public void on_newline(String line){
				String s=line.toLowerCase();
				int i=s.lastIndexOf(":");
				if(i==-1)
					return;
				if(s.indexOf("depends:")==-1)
					return;
				s=s.substring(i+1).trim();
				sb.append(s);
				x.aBgn(sb.toString()).p(s).aEnd().nl();
				sb.setLength(sbl);
			};
		}).p("apt-cache -i depends ").p(qs[0]).exit();
	}
}
