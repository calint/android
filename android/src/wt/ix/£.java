package wt.ix;
import htp.htp;
import htp.path;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
import htpx.stsb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
final public class £ extends wt{
	static final long serialVersionUID=1;
	public static List<String> query(final String qs) throws IOException{
		String[] q=qs.split(" ");
		if(q.length==1&&q[0].length()==0)
			q=new String[0];
		final String[] qsw=q;
		final BufferedReader keyword_ix_file_readers[]=new BufferedReader[qsw.length];
		//			final long keyword_ix_file_sizes[]=new long[qsw.length];
		long acc=0;
		boolean allhit=true;
		session ses=req.get().session();
		for(int i=0;i<qsw.length;i++){
			String dir=qsw[i].substring(0,1);
			path path=ses.path("ix/"+dir+"/"+qsw[i]);
			long filelen=path.getSize();
			acc+=filelen;
			//				keyword_ix_file_sizes[i]=filelen;
			if(filelen>0)
				keyword_ix_file_readers[i]=new BufferedReader(new InputStreamReader(path.getInputStream()));
			else
				allhit=false;
		}
		if(qsw.length==0)
			allhit=false;
		//		final long keyword_ix_sizes_total=acc;
		List<String> list=new ArrayList<String>(htp.K);
		// sample
		//| a | a | a |
		//| c | b | b |
		//| d | d | d |
		//| g | g | e |
		String hitstring="";
		int hitcount=0;
		int readerix=0;
		if(allhit){
			if(q.length==1){
				for(String line=keyword_ix_file_readers[readerix].readLine();line!=null;line=keyword_ix_file_readers[readerix].readLine())
					list.add(line);
			}else{
				while(true){
					String line=keyword_ix_file_readers[readerix].readLine();
					if(line==null)
						break;
					int cmp=line.compareToIgnoreCase(hitstring);
					if(cmp<0)
						continue;
					if(cmp>0){
						hitstring=line;
						readerix=(readerix+1)%keyword_ix_file_readers.length;
						hitcount=1;
						continue;
					}
					hitcount++;
					if(hitcount==keyword_ix_file_readers.length){
						list.add(line);
					}
					readerix=(readerix+1)%keyword_ix_file_readers.length;
				}
			}
		}
		for(int i=0;i<keyword_ix_file_readers.length;i++){
			if(keyword_ix_file_readers[i]!=null){
				keyword_ix_file_readers[i].close();
			}
		}
		return list;
	}
	public xdiro dr;
	String qs="";
	public wt sts;
	public £() throws IOException{
		dr.pt=this;
		dr.setRootPath(req.get().session().path(""));
	}
	public void ax_ltr(xwriter x,String[] args) throws IOException{
		qs=args[2];
		dr.ltr(qs);
		x.x_reload();
	}
	public void ax_reindex(xwriter x,String[] args) throws Throwable{
		session ses=req.get().session();
		path homepth=ses.path("");
		path ixpth=ses.path("ix");
		stsb stsb=new stsb(x,sts,500);
		indexwriter.reindex(ixpth,homepth,stsb);
		x.x_reload();
	}
	public void ax_stsx(xwriter x,String[] args) throws IOException,InterruptedException{
		sts.setValue("");
		sts.x_updInnerHtml(x);
	}
	@Override public void to(final xwriter x) throws Throwable{
		String wid=wid();
		x.action_ax(this,"reindex",":: reindex");
		x.p("  ").span(sts);
		if(sts.toString().length()>0)
			x.action_ax(this,"stsx","[·]");
//		x.br().br();
		x.br();
		path indexletters=req.get().session().path("ix");
		String[] ixletters=new String[0];
		if(indexletters.exists()){
			ixletters=indexletters.list();
		}
		//		String ss=htp.urldecode(htp.urlencode("helo åöä"));
		//		System.out.println(ss);
		if(ixletters.length>0){
			Arrays.sort(ixletters);
			x.p(" · ");
			for(int k=0;k<ixletters.length;k++){
				char ch=ixletters[k].charAt(0);
				String chstr=""+ch;
				boolean chselctd=chstr.equals(qs);
				x.aBgn("javascript:ui.ax('"+wid+" ltr "+chstr+"')");
				if(chselctd)
					x.p("<span style=\"color:#ff0000\">");
				x.p(ch);
				if(chselctd)
					x.p("</span>");
				x.aEnd();
				x.p(" · ");
			}
		}
//		x.br();
		x.wt(dr);
	}
}
