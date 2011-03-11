package wt;
import htp.htp;
import htp.path;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
import htpx.jskeys;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class files extends wt{
	public static class £ extends wt{
		static final long serialVersionUID=1;
		@Override public void to(xwriter x) throws Throwable{
			x.p(getClass().getCanonicalName());
		}		
	}
	private static final long serialVersionUID=1;
	private static String axparamdec(String param){return htp.urldecode(param);}
	private static String axparamenc(String param){return htp.urlencode(param);}
	public wt editor;
	private boolean f1=true;
	private boolean f2=true;
	private boolean f3=true;
	private boolean f4;
	//	private boolean f4;
	//	private boolean f5;
	private NumberFormat nfs=new DecimalFormat("#,###,###,###,###");
	private path pth;
	private path pthhome;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public wt searchwt;
	public files() throws IOException{
		session ses=req.get().session();
		pth=ses.path("");
		pthhome=ses.path("");
	}
	public void ax_clrsrch(xwriter x){
		searchwt.setValue("");
		x.x_reload();
	}
	public void ax_f1(xwriter x) throws IOException{
		if(pth.equals(pthhome))
			return;
		pth=pth.getParent();
		x.x_reload();
	}
	public void ax_f2(xwriter x){
		f2=!f2;
		x.x_reload();
	}
	public void ax_f3(xwriter x){
		f3=!f3;
		x.x_reload();
	}
	public void ax_f4(xwriter x) throws IOException{
		f4=!f4;
		if(!f4)
			editor.to(pth);
		x.x_reload();
	}
	public void ax_f5(xwriter x,String[] p) throws FileNotFoundException,IOException{
		if(!pth.isFile())
			return;
		f4=!f4;
		if(!f4)
			editor.to(pth.getOutputStream(false));
		x.x_reload();
		//		x.x_alert(p[0]+" "+p[1]);
	}
	//	public void ax_f5(xwriter xw) throws IOException{
	//		xw.x_reload();
	//	}
	public void ax_f6(xwriter xw,String[] p) throws IOException{
		pth.getPath(searchwt.toString()).mkdirs();
		xw.x_reload();
	}
	public void ax_f7(xwriter xw,String[] p) throws IOException{
		pth.getPath(searchwt.toString()).mkfile();
		xw.x_reload();
	}
	public void ax_f8(xwriter xw,String[] p) throws IOException{
		if(pth.equals(pthhome))
			return;
		if(pth.rm())
			pth=pth.getParent();
		xw.x_reload();
	}
	public void ax_navHome(xwriter x) throws IOException{}
	public void ax_op(xwriter x,String[] p) throws IOException{
		String nm=p[2];
		String nmdec=axparamdec(nm);
		pth=pth.getPath(nmdec);
		x.x_reload();
	}
	public void ax_rm(xwriter xw,String[] args) throws Throwable{
		//		String nm=URLDecoder.decode(args[2],"utf8");
		//		checkNameEnc(nm);
		//		filepath().file(nm).rm();
		//		xw.x_reload();
	}
	public void ax_srchupd(xwriter x,String[] p){
		x.x_reload();
	}
	private String btoa(long n){
		return nfs.format(n)+" B";
	}
	private String getHref(){
		return pth.toString();
	}
	public String getHrefHomeRel(){
		String href=pth.getHref();
		String base=req.get().session().href();
		int baselen=base.length();
		if(href.length()<=baselen)
			return "";
		return href.substring(baselen);
	}
	private void group(final String[] files){
		Arrays.sort(files,new Comparator<String>(){
			@Override public int compare(String a,String b){
				try{
					boolean da=pth.getPath(a).isDirectory();
					boolean db=pth.getPath(b).isDirectory();
					if(da&&db)
						return 0;
					if(!da&&!db)
						return 0;
					if(da&&!db)
						return 1;
					if(!da&&db)
						return -1;
					return 0;
				}catch(IOException e){
					throw new Error(e);
				}
			}
		});
	}
	public xwriter ia(xwriter x,String ax,String label,String title,boolean pressed){
		x.p("<a href=\"javascript:ui.ax('").p(ax).p("')\"");
		if(pressed){
			x.p(" class=prsd");
		}
		if(title!=null&&title.length()!=0){
			x.p(" title=\"").p(title).p("\"");
		}
		x.p(">").p(label).p("</a>");
		return x;
	}
//	private xwriter ia(xwriter x,String ax,String txt,String img,String title,boolean pressed){
//		x.p("<a href=\"javascript:ui.ax('").p(ax).p("')\"");
//		if(pressed){
//			x.p(" class=prsd");
//		}
//		if(title!=null&&title.length()!=0){
//			x.p(" title=\"").p(title).p("\"");
//		}
//		x.p(">").tagStart("img").attr("src","/u/i/"+img+".png").attr("width",32).attr("border",0).tagStartEnd().tagEnd("a");
//		return x;
//	}
	//	public void ax_f6(xwriter x,String[]p) throws FileNotFoundException, IOException{
	//		editor.to(pth.getOutputStream());
	//		x.x_reload();
	//		x.x_alert(p[0]+" "+p[1]);
	//	}
	private String[] list(){
		final boolean all=searchwt.isEmpty();
		if(all)
			return pth.list();
		final String searchstr=searchwt.toString();
		String[] words=searchstr.split(" ");
		StringBuffer sb=new StringBuffer();
		for(String w:words)
			sb.append(".*").append(w);
		sb.append(".*");
		final Pattern pattern=Pattern.compile(sb.toString(),Pattern.CASE_INSENSITIVE);
		String[] files=pth.list(new FilenameFilter(){
			@Override public boolean accept(File dir,String name){
				final Matcher matcher=pattern.matcher(name);
				if(matcher.matches())
					return true;
				return false;
			}
		});
		return files;
	}
	private void sort(String[] files){
		Arrays.sort(files,new Comparator<String>(){
			@Override public int compare(String a,String b){
				return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
			}
		});
	}
	@Override public void to(xwriter x) throws Throwable{
		String wid=wid();
		x.nl();
		jskeys js=new jskeys(x);
		js.open();
		for(int n=0;n<10;n++)
			js.add("c"+n,"ui.ax('"+wid+" f"+n+"')");
		js.close();
		x.nl();
		x.p(getHref());
		x.div("icons");
//		ia(x,wid+" f1","1","stock_left","ctl+1",f1);
		ia(x,wid+" f1","1","ctl+1",f1);
		if(pth.isDirectory()){
			ia(x,wid+" f2","2","ctl+2",f2);
			ia(x,wid+" f3","3","ctl+3",f3);
		}
		if(pth.isFile()){
			ia(x,wid+" f4","4","ctl+4",f4);
			ia(x,wid+" f5","5","ctl+5",false);
		}
		if(pth.isDirectory()){
			ia(x,wid+" f6","6","ctl+6",false);
			ia(x,wid+" f7","7","ctl+7",false);
		}
		ia(x,wid+" f8","8","ctl+8",false);
		//	ia(x,"6",wid+" f6",f6,"ctl+6");
		if(pth.isDirectory()){
			x.br().inputText(searchwt,null,this,"srchupd").focus(searchwt);
		}
		x.divEnd();
		if(pth.isDirectory())
			x.a("javascript:ui.ax('"+wid()+" clrsrch')","[·]");
		if(pth.isFile()){
			if(f4){
				//				pth.to(editor);
				editor.setFrom(pth);
				x.inputTextArea(editor,"editor");
				x.focus(editor);
			}else{
				x.pre();
				pth.to(x);
				x.preEnd();
			}
		}
		if(!pth.isDirectory()){
			return;
		}
		String[] files=list();
		if(f2)
			sort(files);
		if(f3)
			group(files);
		x.nl().table("files").nl();
		x.tr();
		x.th().thEnd();
		x.th().p("type").thEnd();
		x.th().p("title").thEnd();
		x.th().p("date").thEnd();
		x.th().p("size").thEnd();
		x.trEnd().nl();
		long total=0;
		for(int i=0;i<files.length;i++){
			path p=pth.getPath(files[i]);
			String name=p.getName();
			String nameEnc=axparamenc(name);
			boolean isDir=p.isDirectory();
			String iconName;
			if(isDir)
				iconName="[&nbsp;&nbsp;]";
			else
				iconName="[&nbsp;&nbsp;]";
			x.tr();
			x.td("icns first").p("<a href=\"javascript:ui.ax('").p(wid).p(" op ").p(nameEnc).p("')\">").p(iconName).aEnd().tdEnd();
			x.td("type").p(isDir?"dir":p.getType()).tdEnd();
			x.td("name");
			int ix=name.lastIndexOf('.');
			if(ix!=-1)
				name=name.substring(0,ix);
			if(p.isFile()){
				x.aBgn(pth+"/"+nameEnc).p(name).aEnd();
			}else
				x.p(name);
			x.tdEnd();
			x.td("date").p(ttoa(p.getLastModified(),2)).tdEnd();
			total+=p.getSize();
			x.td("size last").p(isDir?"--":btoa(p.getSize())).tdEnd();
			x.trEnd().nl();
		}
		x.tr().td().tdEnd().td().tdEnd().td().tdEnd().td().tdEnd().td("size last bold").p(nfs.format(total)).p(" B").tdEnd().trEnd().nl();
		x.tableEnd().nl();
	}
	private String ttoa(long t,int prec){
		return sdf.format(new Date(t));
	}
}
