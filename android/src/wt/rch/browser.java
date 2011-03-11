package wt.rch;
import htp.path;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class browser extends wt{
	private static final long serialVersionUID=1L;
	private long itemno=1;
	@Override public void to(xwriter x) throws Throwable{
		session ses=req.get().session();
		String basehref=ses.href();
		path p=ses.path("rch/store.ix");
		if(!p.exists()){
			x.p("no index");
			return;
		}
		InputStream is=p.getFileInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf8"));
		int lineno=0;
		for(String line=br.readLine();line!=null;line=br.readLine()){
			lineno++;
			if(itemno!=lineno)
				continue;
			String hash=line.substring(0,32);
			String type=line.substring(109);
			String size=line.substring(36,49);
			String datestr=line.substring(53,73);
			String ref=£.store_dir+£.filename_for_hash(hash)+"."+type;
			String href=basehref+ref;
			x.action_ax(this,"nxt","::next");
			x.action_ax(this,"prv","::previous");
			x.action_ax(this,"rm "+ref,"::delete");
			x.br().p("item #").p(itemno).p(" ").p(size).p(" ").p(datestr);
			x.br();
			x.p("<img src=\"").p(href).p("\">");
			break;
		}
		br.close();
	}
	public void ax_prv(xwriter x,String[] p){
		if(itemno>1)
			itemno--;
		x.x_reload();
	}
	public void ax_nxt(xwriter x,String[] p){
		itemno++;
		x.x_reload();
	}
	public void ax_rm(xwriter x,String[] p) throws IOException{
		path pth=req.get().session().path(p[2]);
		pth.rm();
		x.x_reload();
	}
}
