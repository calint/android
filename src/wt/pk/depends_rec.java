package wt.pk;
import htp.htp;
import htp.req;
import htp.wt;
import htp.xwriter;
import htpx.cli;
import htpx.osnewliner;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
final public class depends_rec extends wt{
	private static final long serialVersionUID=1;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final String qs=URLDecoder.decode(r.querystr(),"utf8").toLowerCase().replace(';',' ').replace('&',' ').trim();
		list.clear();
		depends(x,r,qs,"");
	}
	private List<String> list=new ArrayList<String>(htp.K);
	private void depends(final xwriter x,final req rt,final String qs,final String indent) throws Throwable{
		new cli("sh",new osnewliner(){
			@Override public void on_newline(String line) throws Throwable{
				String s=line.toLowerCase();
				int i=s.lastIndexOf(":");
				if(i==-1)
					return;
				if(s.indexOf("depends:")==-1)
					return;
				s=s.substring(i+1).trim();
				//System.out.println(s);
				for(int n=0;n<indent.length();n++)
					x.p("·");
				x.aBgn(rt.pathstr()+"?"+s).p(s).aEnd();
				if(list.contains(s)){
					x.p("*");
					x.br().nl();
					return;
				}else
					list.add(s);
				x.br().nl();
				depends(x,rt,s,indent+"  ");
			};
		}).p("apt-cache depends ").p(qs).exit();
	}
}
