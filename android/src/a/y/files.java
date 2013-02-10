package a.y;
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
import a.x.jskeys;
import b.a;
import b.b;
import b.path;
import b.req;
import b.session;
import b.xwriter;
public class files extends a{
	static final long serialVersionUID=1;
	public static class £ extends a{
		static final long serialVersionUID=1;
		public void to(xwriter x) throws Throwable{
			x.p(getClass().getCanonicalName());
		}		
	}
	private static String axparamdec(final String param){return b.urldecode(param);}
	private static String axparamenc(final String param){return b.urlencode(param);}
	public a editor;
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
	public a searchwt;
	public files() throws IOException{
		session ses=req.get().session();
		pth=ses.path("");
		pthhome=ses.path("");
	}
	public void ax_clrsrch(xwriter x){
		searchwt.set("");
		x.xreload();
	}
	public void ax_f1(final xwriter x,final String[]p){
		if(pth.equals(pthhome))
			return;
		pth=pth.parent();
		x.xreload();
	}
	public void ax_f2(final xwriter x,final String[]p){
		f2=!f2;
		x.xreload();
	}
	public void ax_f3(final xwriter x,final String[]p){
		f3=!f3;
		x.xreload();
	}
	public void ax_f4(final xwriter x,final String[]p)throws IOException{
		f4=!f4;
		if(!f4)
			editor.to(pth);
		x.xreload();
	}
	public void ax_f5(final xwriter x,final String[]p)throws FileNotFoundException,IOException{
		if(!pth.isfile())
			return;
		f4=!f4;
		if(!f4)
			editor.to(pth.outputstream(false));
		x.xreload();
		//		x.x_alert(p[0]+" "+p[1]);
	}
	//	public void ax_f5(xwriter xw) throws IOException{
	//		xw.x_reload();
	//	}
	public void ax_f6(xwriter xw,String[] p) throws IOException{
		pth.get(searchwt.toString()).mkdirs();
		xw.xreload();
	}
	public void ax_f7(xwriter xw,String[] p) throws IOException{
		pth.get(searchwt.toString()).mkfile();
		xw.xreload();
	}
	public void ax_f8(xwriter xw,String[] p) throws IOException{
		if(pth.equals(pthhome))
			return;
		if(pth.rm())
			pth=pth.parent();
		xw.xreload();
	}
	public void ax_navHome(xwriter x) throws IOException{}
	public void ax_op(final xwriter x,final String[]p)throws IOException{
		String nm=p[2];
		String nmdec=axparamdec(nm);
		pth=pth.get(nmdec);
		x.xreload();
	}
	public void ax_rm(final xwriter xw,final String[]args) throws Throwable{
		//		String nm=URLDecoder.decode(args[2],"utf8");
		//		checkNameEnc(nm);
		//		filepath().file(nm).rm();
		//		xw.x_reload();
	}
	public void ax_srchupd(final xwriter x,final String[]p){
		x.xreload();
	}
	private String btoa(final long n){
		return nfs.format(n)+" B";
	}
	private String getHref(){
		return pth.toString();
	}
	public String getHrefHomeRel(){
		final String href=pth.uri();
		final String base=req.get().session().href();
		final int baselen=base.length();
		if(href.length()<=baselen)
			return"";
		return href.substring(baselen);
	}
	private void group(final String[]files){
		Arrays.sort(files,new Comparator<String>(){public int compare(final String a,final String b){
				final boolean da=pth.get(a).isdir();
				final boolean db=pth.get(b).isdir();
				if(da&&db)
					return 0;
				if(!da&&!db)
					return 0;
				if(da&&!db)
					return 1;
				if(!da&&db)
					return -1;
				return 0;
		}});
	}
	public xwriter ia(final xwriter x,final String ax,final String label,final String title,final boolean pressed){
		x.p("<a href=\"javascript:$x('").p(ax).p("')\"");
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
	private String[]list(){
		final boolean all=searchwt.isempty();
		if(all)
			return pth.list();
		final String searchstr=searchwt.toString();
		final String[]words=searchstr.split(" ");
		final StringBuilder sb=new StringBuilder();
		for(final String w:words)
			sb.append(".*").append(w);
		sb.append(".*");
		final Pattern pattern=Pattern.compile(sb.toString(),Pattern.CASE_INSENSITIVE);
		final String[]files=pth.list(new FilenameFilter(){public boolean accept(final File dir,final String name){
				final Matcher matcher=pattern.matcher(name);
				if(matcher.matches())
					return true;
				return false;
		}});
		return files;
	}
	private void sort(final String[]files){
		Arrays.sort(files,new Comparator<String>(){public int compare(final String a,final String b){
				return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
		}});
	}
	public void to(final xwriter x)throws Throwable{
		x.style();
		x.css("table.files","margin-top:3px;width:50%;white-space:pre;border-top:1px solid #aaeaea;border-bottom:1px solid #aaeaea;background:#fafafa;background: #ffffff;");
		x.css("table.files th","background: #eaedea;border-bottom: 1px solid #aaeaea;text-align:left;padding-left:10px;");
		x.css("table.files tr.evn","background:#eaedea");
		x.css("table.files td","border-bottom:1px dotted #ffffff;border-bottom: 1px dotted #aaeaef;vertical-align:bottom;padding-bottom:1px;padding-left:7px;padding-right:7px;padding-top:11px;");
		x.css("table.files td.first","border-left:0px;border-right: 1px dotted #aaeaef;");
		x.css("table.files td.icns","width:16px;padding:3px;");
		x.css("table.files td.icns img","vertical-align: bottom;");
		x.css("table.files td.name","");
		x.css("table.files td.date","width:77px;text-align:right;padding-right:13px;");
		x.css("table.files td.size","width:53px;text-align:right;padding-right:0px;");
		x.css("table.files td:last-sibbling","border-left:1px dotted #aeeaae;");

		final String wid=id();
		x.nl();
		final jskeys js=new jskeys(x);
		js.open();
		for(int n=0;n<10;n++)
			js.add("c"+n,"ui.ax('"+wid+" f"+n+"')");
		js.close();
		x.nl();
		x.p(getHref());
		x.div("icons");
//		ia(x,wid+" f1","1","stock_left","ctl+1",f1);
		ia(x,wid+" f1","1","ctl+1",f1);
		if(pth.isdir()){
			ia(x,wid+" f2","2","ctl+2",f2);
			ia(x,wid+" f3","3","ctl+3",f3);
		}
		if(pth.isfile()){
			ia(x,wid+" f4","4","ctl+4",f4);
			ia(x,wid+" f5","5","ctl+5",false);
		}
		if(pth.isdir()){
			ia(x,wid+" f6","6","ctl+6",false);
			ia(x,wid+" f7","7","ctl+7",false);
		}
		ia(x,wid+" f8","8","ctl+8",false);
		//	ia(x,"6",wid+" f6",f6,"ctl+6");
		if(pth.isdir()){
			x.br().inputText(searchwt,null,this,"srchupd").focus(searchwt);
		}
		x.divEnd();
		if(pth.isdir())
			x.a("javascript:ui.ax('"+id()+" clrsrch')","[·]");
		if(pth.isfile()){
			if(f4){
				//				pth.to(editor);
				editor.read(pth);
				x.inputTextArea(editor,"editor");
				x.focus(editor);
			}else{
				x.pre();
				pth.to(x);
				x.preEnd();
			}
		}
		if(!pth.isdir()){
			return;
		}
		final String[]files=list();
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
			path p=pth.get(files[i]);
			String name=p.name();
			final String nameEnc=axparamenc(name);
			final boolean isDir=p.isdir();
			String iconName;
			if(isDir)
				iconName="[&nbsp;⧉&nbsp;]";
			else
				iconName="[&nbsp;☼&nbsp;]";
			x.tr();
			x.td("icns").p("<a href=\"javascript:$x('").p(wid).p(" op ").p(nameEnc).p("')\">").p(iconName).aEnd().tdEnd();
			x.td("type").p(isDir?"dir":p.type()).tdEnd();
			x.td("name");
			int ix=name.lastIndexOf('.');
			if(ix!=-1)
				name=name.substring(0,ix);
			if(p.isfile()){
				x.a(pth+"/"+nameEnc).p(name).aEnd();
			}else
				x.p(name);
			x.tdEnd();
			x.td("date").p(ttoa(p.lastmod(),2)).tdEnd();
			total+=p.size();
			x.td("size last").p(isDir?"--":btoa(p.size())).tdEnd();
			x.trEnd().nl();
		}
		x.tr().td().tdEnd().td().tdEnd().td().tdEnd().td().tdEnd().td("size bold").p(nfs.format(total)).p(" B").tdEnd().trEnd().nl();
		x.tableEnd().nl();
	}
	private String ttoa(final long t,final int prec){
		return sdf.format(new Date(t));
	}
}
