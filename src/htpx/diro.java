package htpx;
import htp.htp;
import htp.osltgt;
import htp.path;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
public class diro extends wt{
	private static final long serialVersionUID=1;
	public final static int BIT_ALLOW_DIR_OPEN=1;
	public final static int BIT_ALLOW_FILE_OPEN=2;
	public final static int BIT_ALLOW_FILE_EDIT=4;
	public final static int BIT_ALLOW_FILE_DELETE=8;
	public final static int BIT_ALLOW_FILE_LINK=16;
	public final static int BIT_ALLOW_NAV_UP=32;
	protected int bits=BIT_ALLOW_FILE_LINK;
	protected path root;
	protected path path;
	protected boolean sort=true;
	protected boolean dirs_first=true;
	private SimpleDateFormat df=new SimpleDateFormat("--yyyy-MM-dd----HH:mm:ss.SSS----");
	private NumberFormat nf=new DecimalFormat("###,###,###,###");
	public void setBits(int bits){this.bits=bits;}
	@Override public void to(xwriter x) throws Throwable{
		final session ses=req.get().session();
		int rootpathnamelen=root.toString().length();
		String pathdisp=path.toString().substring(rootpathnamelen);
		//		if(!pathdisp.isEmpty())
		//			x.div("path").p(pathdisp).divEnd();
		final String[] files=path.list();
		final String wid=wid();
		if(files!=null){
			if(sort)
				sort(files);
			if(dirs_first)
				sortDirsFirst(ses,files);
			x.table("files").nl();
			x.tr().th();
			if((bits&BIT_ALLOW_NAV_UP)!=0){
				x.aBgn("javascript:ui.ax('"+wid+" up')").p("«").aEnd();
			}
			x.p(pathdisp);
			x.thEnd();
			x.th().thEnd();
			x.th().thEnd();
			x.th().thEnd();
			x.trEnd().nl();
			long total_bytes=0;
			for(final String filenm:files){
				final path p=path.getPath(filenm);
				final String name=p.getName();
				final String nameenc=htp.urlencode(name);
				final boolean isDir=p.isDirectory();
				String icon;
				x.tr();
				x.td("icns first");
				if(isDir){
					icon="";
					if((bits&BIT_ALLOW_DIR_OPEN)!=0)
						x.a_ax(this,"e",nameenc);
					x.p(icon);
					if((bits&BIT_ALLOW_DIR_OPEN)!=0)
						x.p("</a> ");
				}else{
					icon="";
					if((bits&BIT_ALLOW_FILE_OPEN)!=0)
						x.a_ax(this,"e",nameenc);
					x.p(icon);
					if((bits&BIT_ALLOW_FILE_OPEN)!=0)
						x.p("</a> ");
				}
				//				x.p("<a href=\"javascript:ui.ax('").p(wid).p(" s ").p(nameEnc).p("')\">").p("↓").p("</a> ");
				//				x.p("<a href=\"javascript:ui.ax('").p(wid).p(" r ").p(nameEnc).p("')\">").p("ĸ").p("</a> ");
				x.td("name");
				if((bits&BIT_ALLOW_FILE_LINK)!=0&&p.isFile()){
					x.aBgn(p.getHref()).p(name).aEnd();
				}else{
					x.p(name);
				}
				x.tdEnd();
				x.td("date").p(ttoa(p.getLastModified())).tdEnd();
				final long size=p.getSize();
				total_bytes+=size;
				x.td("size last").p(isDir?"--":btoa(size)).tdEnd();
				x.trEnd().nl();
			}
			if(path.isDirectory()){
				x.tr().td().tdEnd();
				x.td().tdEnd();
				x.td().tdEnd();
				x.td("size last").p(nf.format(total_bytes)).tdEnd();
				x.trEnd().nl();
			}
			x.tableEnd();
			if(path.isFile()){
				x.pre().nl().flush();
				path.to(new osltgt(x.outputStream()));
			}
		}
		x.nl();
	}
	private void sortDirsFirst(final session ses,String[] files){
		Arrays.sort(files,new Comparator<String>(){
			@Override public int compare(String a,String b){
				try{
					boolean da=ses.path(a).isDirectory();
					boolean db=ses.path(b).isDirectory();
					if(da&&db)
						return 0;
					if(!da&&!db)
						return 0;
					if(da&&!db)
						return -1;
					if(!da&&db)
						return 1;
					return 0;
				}catch(Throwable t){
					throw new Error(t);
				}
			}
		});
	}
	private void sort(String[] files){
		Arrays.sort(files,new Comparator<String>(){
			@Override public int compare(String a,String b){
				return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
			}
		});
	}
	private String ttoa(long ms){
		return df.format(ms);
	}
	private String btoa(long n){
		return nf.format(n);
	}
	public final void ax_e(xwriter x,String[] p) throws IOException{
		String namedec=htp.urldecode(p[2]);
		path=path.getPath(namedec);
		onPathEnter(path);
		x.x_reload();
	}
	protected void onPathEnter(path path){}
	public final void ax_up(xwriter x,String[] args) throws IOException{
		path p=path.getParent();
		if(p==null)
			return;
		if(p.getFullPath().startsWith(root.getFullPath())){
			path=p;
		}else{
			path=root;
		}
		onPathUp();
		x.x_reload();
	}
	protected void onPathUp(){}
	public void setRootPath(path root){
		this.root=root;
		this.path=root;
	}
}
