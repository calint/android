package wt.ix;
import htp.path;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class q extends wt{
	public static final long serialVersionUID=1;
	public static boolean empty_query_display_all=true;
	//	public String msg;
	public wt qf;
	public xwriter ia(xwriter x,String src,String pb,String title){
		return ia(x,src,pb,false,title);
	}
	public xwriter ia(xwriter x,String src,String pb,boolean pressed,String title){
		x.p("<a href=\"javascript:ui.ax('").p(pb).p("')\"");
		if(pressed)
			x.p(" class=prsd");
		if(title!=null&&title.length()!=0)
			x.p(" title=\"").p(title).p("\"");
		return x.p(">").p(src).p("</a>");
	}
	@Override public void to(final xwriter x) throws Throwable{
		final String qs=qf.toString();
		long t0=System.currentTimeMillis();
		List<String> ls=£.query(qs);
		long t1=System.currentTimeMillis();
		x.nl().p("“").inputText(qf,null,this,"q").focus(qf).p("” found ").p(ls.size()).p(" in ").p(t1-t0).p(" ms");
		x.br();
		//		w.p(Arrays.toString(keyword_ix_file_sizes));
		//		if(!msg.isEmpty()){
		//			w.p("<code><pre>").p(msg).p("</pre></code>");
		//			msg=null;
		//		}
		StringBuffer sb=new StringBuffer(256);
		sb.append("'").append(qs).append("' found ").append(ls.size()).append(" in ").append(t1-t0).append(" ms");
		//		sb.append("    keyword file sizes [");
		//		for(int n=0;n<keyword_ix_file_sizes.length;n++){
		//			sb.append(keyword_ix_file_sizes[n]).append(",");
		//		}
		//		sb.setLength(sb.length()-1);
		//		sb.append("] bytes ").append(keyword_ix_sizes_total);
		//w.br().nl().p(sb)
		//		String s1="list processing "+(t2-t1)+" ms";
		//		htp.out.println(cfg.a+sb+"\n "+cfg.a+s1);
		//		w.nl().p(s).nl();
		x.table("files").nl();
		session ses=req.get().session();
		//		String href=ses.href();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		NumberFormat lnf=new DecimalFormat("#,###,###,###,###");
		for(String s:ls){
			path p=ses.path(s);
			boolean isDir=p.isDirectory();
			String iconName;
			if(isDir)
				iconName="";
			else
				iconName="";
			x.tr().td("icns first").p(iconName).td("name");
			if(p.isFile())
				x.aBgn(p.getHref()).p(p.getName()).aEnd();
			else
				x.p(p.toString());
			x.tdEnd();
			x.td("date").p(sdf.format(new Date(p.getLastModified()))).tdEnd();
			x.td("size last").p(isDir?"----":lnf.format(p.getSize())).p(" B").tdEnd();
			x.trEnd().nl();
		}
		x.tableEnd();
	}
	public void ax_q(xwriter x,String[] p) throws Throwable{
		x.x_reload();
	}
}
