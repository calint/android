package a.qa;
import b.*;
import java.io.*;
import java.text.*;
import java.util.*;
public class diro extends a{static final long serialVersionUID=1;
	public final static int BIT_ALLOW_QUERY=1;
	public final static int BIT_ALLOW_FILE_LINK=2;
	public final static int BIT_ALLOW_DIR_ENTER=4;
	public final static int BIT_ALLOW_DIR_UP=8;
	public final static int BIT_ALLOW_FILE_OPEN=16;
	public final static int BIT_ALLOW_FILE_EDIT=32;
	public final static int BIT_ALLOW_FILE_DELETE=64;
	public final static int BIT_ALLOW_FILE_CREATE=128;
	public final static int BIT_ALLOW_DIR_CREATE=256;
	public final static int BIT_ALLOW_DIR_DELETE=512;
	public final static int BIT_ALLOW_FILE_MODIFY=1024;
//	public final static int BIT_ALLOW_SELECT=2048;
//	public final static int BIT_ALLOW_MOVE=4096;
//	public final static int BIT_ALLOW_RENAME=8192;
//	public final static int BIT_ALLOW_COPY=16384;
	public final static int BIT_DISP_PATH=32768;
	public final static int BIT_ALL=-1;
	public a q;
	protected final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
	protected final NumberFormat nf=new DecimalFormat("###,###,###,###");
	protected int bits=BIT_DISP_PATH|BIT_ALLOW_QUERY|BIT_ALLOW_FILE_LINK|BIT_ALLOW_DIR_ENTER|BIT_ALLOW_DIR_UP;
//	protected int bits=BIT_ALL;
	protected path root=b.path();
	protected path path=root;
	protected boolean sort=true;
	protected boolean sort_dirsfirst=true;
	public a bd;
	final public void root(final path root){this.root=root;if(!path.isin(root))path=root;}
	final public void bits(final int bits){this.bits=bits;}
	final public boolean hasbit(final int i){return(bits&i)!=0;}
	final public void to(final xwriter x) throws Throwable{
		x.tago("span").attr("id",id()).tagoe();
		final String[]files;
		final boolean isfile=path.isfile();
		final String query=q.toString();
		if(b.isempty(query))files=path.list();else files=path.list(new FilenameFilter(){public boolean accept(File f,String s){
				return s.startsWith(query);
			}});
		if(sort)sort(files);
		if(sort_dirsfirst)sort_dirsfirst(files);
		x.style();
		x.css("table.f","margin-left:auto;margin-right:auto");
		x.css("table.f tr:first-child","border:0;border-bottom:1px solid green;border-top:1px solid #070");
		x.css("table.f tr:last-child td","border:0;border-top:1px solid #040");
		x.css("table.f th:first-child","border-right:1px dotted #ccc");
		x.css("table.f td","padding:.5em;vertical-align:middle;border-left:1px dotted #ccc;border-bottom:1px dotted #ccc");
		x.css("table.f td:first-child","border-left:0");
		x.css("table.f td.icns","text-align:center");
		x.css("table.f td.size","text-align:right");
		x.css("table.f td.total","font-weight:bold");
		x.css("table.f td.name","min-width:100px");
		x.css("table.f th","padding:.5em;text-align:left;background:#f0f0f0;color:black");
		if(hasbit(BIT_ALLOW_QUERY))x.css(q,"float:right;background:yellow;border:1px dotted #555;text-align:right;width:10em;margin-left:1em");
		x.styleEnd();
		x.table("f").nl();
		x.tr().th();
		if(hasbit(BIT_ALLOW_DIR_UP))if(!path.equals(root))x.ax(this,"up","••");
		final boolean acttd=hasbit(BIT_ALLOW_FILE_CREATE)||hasbit(BIT_ALLOW_DIR_CREATE);
		x.th(acttd?4:3);
		if(hasbit(BIT_DISP_PATH))if(path.isin(root)){
			final String pp=path.fullpath().substring(root.fullpath().length());
			x.tago("span").attr("style","float:left").tagoe();
			x.p(pp);
			x.spanEnd();
		}
		final String icnfile="◻";
		final String icndir="⧉";
		final String icndel="x";
//		final String icnsel="s";
//		final String icnren="r";
		x.tago("span").attr("style","margin-left:22px;float:right").tagoe();
		if(isfile){
			x.ax(this,"s",icnfile).ax(this,"sx","▣").tage("span").nl();
		}else{
			if(hasbit(BIT_ALLOW_QUERY))x.inputax(q,null,this,null).focus(q);
			if(hasbit(BIT_ALLOW_FILE_CREATE))x.ax(this,"c",icnfile);
			if(hasbit(BIT_ALLOW_DIR_CREATE))x.ax(this,"d",icndir);
			x.tage("span").nl();
			long total_bytes=0;
			for(final String filenm:files){
				final path p=path.get(filenm);
				final String name=p.name();
				final String nameenc=b.urlencode(name);
				final boolean isdir=p.isdir();
				x.tr().td("icns");
				if(isdir)if((bits&BIT_ALLOW_DIR_ENTER)!=0)x.ax(this,"e "+nameenc,icndir);else x.p(icndir);
				else if((bits&BIT_ALLOW_FILE_OPEN)!=0)x.ax(this,"e "+nameenc,icnfile);else x.p(icnfile);
				x.td("name");
				if((bits&BIT_ALLOW_FILE_LINK)!=0&&p.isfile())x.a(p.uri(), name);else x.p(name);
				if(p.isfile()&&hasbit(BIT_ALLOW_FILE_DELETE))x.td("del").ax(this,"r "+nameenc,icndel);
				if(p.isdir()&&hasbit(BIT_ALLOW_DIR_DELETE))x.td("del").ax(this,"r "+nameenc,icndel);
//				if(hasbit(BIT_ALLOW_SELECT))x.ax(this,"se "+nameenc,icnsel);
//				if(hasbit(BIT_ALLOW_RENAME))x.ax(this,"ren "+nameenc,icnren);
				x.td("date").p(ttoa(p.lastmod()));
				final long size=p.size();
				if(p.isfile())total_bytes+=size;
				x.td("size").p(isdir?"--":btoa(size));
				x.nl();
			}
			x.tr().td().td().td();
			if(acttd)x.td();
			x.td("total size last").p(nf.format(total_bytes));
			x.nl();
		}
		x.tableEnd();
		if(isfile){
			x.style().css(bd,"width:100%;height:100%;border:1px dotted green").styleEnd();
			x.inputTextArea(bd,"ed");
			x.focus(bd);
		}else{
			x.focus(q);
		}
		x.nl();
		x.spanEnd();
	}
	private void sort_dirsfirst(final String[]files){
		Arrays.sort(files,new Comparator<String>(){public int compare(final String a,final String b){try{
			final boolean da=path.get(a).isdir();
			final boolean db=path.get(b).isdir();
			if(da&&db)return 0;
			if(!da&&!db)return 0;
			if(da&&!db)return -1;
			if(!da&&db)return 1;
			return 0;
		}catch(final Throwable t){throw new Error(t);}}});
	}
	private void sort(final String[]files){
		Arrays.sort(files,new Comparator<String>(){public int compare(final String a,final String b){
			return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
		}});
	}
	final protected String ttoa(final long ms){return df.format(ms);}
	final protected String btoa(final long n){return nf.format(n);}
	synchronized public final void ax_(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_QUERY))throw new Error("notallowed");
		x.xuo(this);
		x.xfocus(q);
	}
	synchronized public final void ax_e(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_ENTER))throw new Error("notallowed");
		final String namedec=b.urldecode(a[2]);
		path=path.get(namedec);
		if(path.isfile())bd.from(path);
		x.xu(this);
 		x.xfocus(path.isfile()?bd:q);		
	}
	synchronized public final void ax_up(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_UP))throw new Error("notallowed");
		final path p=path.parent();
		if(p==null)return;
		path=p.isin(root)?p:root;
		x.xu(this);
		x.xfocus(q);
	}
	synchronized public final void ax_c(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_FILE_CREATE))throw new Error("notallowed");
		if(q.toString().length()==0){x.xalert("enter name");x.xfocus(q);return;}
		path=path.get(q.toString());
		if(!path.exists())path.append("");//? path.mkfile
		bd.from(path);
		x.xu(this);
		x.xfocus(bd);
	}
	synchronized public final void ax_d(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_CREATE))throw new Error("notallowed");
		if(q.toString().length()==0){x.xalert("enter name");x.xfocus(q);return;}
		path.get(q.toString()).mkdirs();
		x.xu(this);
	}
	synchronized public final void ax_r(final xwriter x,final String[]a)throws Throwable{
		final path p=path.get(b.urldecode(a[2]));
		if(path.isfile()&&!hasbit(BIT_ALLOW_FILE_DELETE))throw new Error("notallowed");
		if(path.isdir()&&!hasbit(BIT_ALLOW_DIR_DELETE))throw new Error("notallowed");//? onlydir
		p.rm();
		x.xu(this);
		x.xfocus(q);
	}
	synchronized public void ax_s(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_FILE_MODIFY))throw new Error("notallowed");
		bd.to(path);
	}
	synchronized public void ax_sx(final xwriter x,final String[]a)throws Throwable{ax_s(x,a);ax_up(x,a);}
//	synchronized public void ax_ren(final xwriter x,final String[]a)throws Throwable{
//		if(!hasbit(BIT_ALLOW_RENAME))throw new Error("notallowed");
//		final String nm=b.urldecode(a[2]);
//		if(!path.get(nm).rename(path.get(selection.rnm.toString()))){
//			x.xalert("could not rename '"+nm+"' to '"+selection.rnm+"'");
//		}
//		x.xuo(this);
//	}
}
