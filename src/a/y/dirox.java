package a.y;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import b.a;
import b.b;
import b.path;
import b.xwriter;
public class dirox extends a{
	private static final long serialVersionUID=1;
	public final static int BIT_ALLOW_QUERY=1;
	public final static int BIT_ALLOW_FILE_LINK=2;
	public final static int BIT_ALLOW_DIR_ENTER=4;
	public final static int BIT_ALLOW_DIR_UP=8;
	public final static int BIT_ALLOW_FILE_CREATE=16;
	public final static int BIT_ALLOW_FILE_EDIT=32;
	public final static int BIT_ALLOW_DELETE=64;
	public final static int BIT_DISP_PATH=128;
	public final static int BIT_ALLOW_DIR_CREATE=256;
	public final static int BIT_ALL=511;
	public a q;
	protected final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	protected final NumberFormat nf=new DecimalFormat("###,###,###,###");
	protected int bits=BIT_ALL;
	protected path root=b.path();
	protected path path=root;
	protected boolean sort=true;
	protected boolean sort_dirsfirst=true;
	
	public final void root(final path root){this.root=root;if(!path.isin(root))path=root;}
	public final void bits(final int bits){this.bits=bits;}
	public final boolean hasbit(final int i){return (bits&i)!=0;}
	final public void to(final xwriter x) throws Throwable{
		x.tag("span",id());
		final String[]files;
		if(b.isempty(query))
			files=path.list();
		else
			files=path.list(new FilenameFilter(){public boolean accept(File f,String s){
				return s.startsWith(query);
			}});
		if(sort)
			sort(files);
		if(sort_dirsfirst)
			sort_dirsfirst(files);
		x.style();
		x.css("table.f","margin-left:auto;margin-right:auto");
		x.css("table.f tr:first-child","border:0;border-bottom:1px solid green;border-top:1px solid green");
		x.css("table.f tr:last-child td","border:0;border-top:1px solid green");
		x.css("table.f th:first-child","border-right:1px dotted green");
		x.css("table.f td","padding:5px;vertical-align:middle;border-left:1px dotted grey;border-bottom:1px dotted grey");
		x.css("table.f td:first-child","border-left:0");
		x.css("table.f td.icns","text-align:center");
		x.css("table.f td.act","border:0");
		x.css("table.f td.size","text-align:right");
		x.css("table.f td.total","font-weight:bold");
		x.css("table.f td.name","min-width:100px");
		x.css("table.f th","padding:3px;text-align:left;background:#f0f0f0;color:black");
		if(hasbit(BIT_ALLOW_QUERY))
			x.css(q,"background:yellow;border:1px dotted green;float:right;text-align:right;width:111px;margin-left:22px");
		x.styleEnd();
		x.table("f").nl();
		x.tr().th();
		if(hasbit(BIT_ALLOW_DIR_UP))
			if(!path.equals(root))
				x.ax(this,"up","••");
		x.th(3);
		if(hasbit(BIT_DISP_PATH)&&path.isin(root)){
			String pp=path.fullpath().substring(root.fullpath().length());
			x.p(pp);
		}
		if(hasbit(BIT_ALLOW_QUERY)&&path.isdir())
			x.inputax(q,null,this,null).focus(q);
		if(hasbit(BIT_ALLOW_FILE_CREATE)&&path.isdir())
			x.ax(this,"c"," • create");
		if(hasbit(BIT_ALLOW_DIR_CREATE)&&path.isdir())
			x.ax(this,"d"," • newdir");
		if(ed!=null){
			x.spc().ax(this,"sv"," • save");
		}
		x.th();
		x.nl();
		long total_bytes=0;
		for(final String filenm:files){
			final path p=path.get(filenm);
			final String name=p.name();
			final String nameenc=b.urlencode(name);
			final boolean isdir=p.isdir();
			x.tr();
			x.td("icns");
			if(isdir)
				if((bits&BIT_ALLOW_DIR_ENTER)!=0)
					x.ax(this,"e "+nameenc,"⧉");
				else
					x.p("⧉");
			else
				if((bits&BIT_ALLOW_FILE_EDIT)!=0)
					x.ax(this,"e "+nameenc,"♢");
				else
					x.p("♢");
//				x.p("<a href=\"javascript:ui.ax('").p(wid).p(" s ").p(nameEnc).p("')\">").p("↓").p("</a> ");
//				x.p("<a href=\"javascript:ui.ax('").p(wid).p(" r ").p(nameEnc).p("')\">").p("ĸ").p("</a> ");
			x.td("name");
			if((bits&BIT_ALLOW_FILE_LINK)!=0&&p.isfile())
				x.a(p.uri(),name);
			else
				x.p(name);
			x.td("date").p(ttoa(p.lastmod()));
			final long size=p.size();
			if(p.isfile()){
				total_bytes+=size;
			}
			x.td("size").p(isdir?"--":btoa(size));
			x.td("act");
			if(hasbit(BIT_ALLOW_DELETE)){
				x.ax(this,"rm "+nameenc,"x");
			}
			x.nl();
		}
		if(path.isdir()){
			x.tr().td().td().td().td("total size").p(nf.format(total_bytes)).nl();
		}
		x.tableEnd();
		if(path.isfile()){
			x.pre().nl().flush();
			if(ed!=null){
				ed.to(x);
				//x.focus((a)ed);
			}
//			path.to(new osltgt(x.outputstream()));
		}
		x.nl();
		x.spanEnd();
	}
	public void ax_sv(final xwriter x,final String[]a)throws Throwable{
		if(ed!=null){
			ed.save();
			x.xalert("saved");
			return;
		}
		x.xalert("ed is null");
	}
	public interface editor{
		void init(final path p)throws Throwable;
		void load()throws Throwable;
		void save()throws Throwable;
		void to(final xwriter x)throws Throwable;
	}
	public editor ed;
	public static class editortxtarea extends a implements editor{
		static final long serialVersionUID=1;
		private path p;
		public editortxtarea(){}
		public editortxtarea(final a pt,final String nm){super(pt,nm);}
		public void init(final path p){this.p=p;}
		public void load()throws Throwable{read(p);}
		public void save()throws Throwable{to(p);}
		public void to(final xwriter x)throws Throwable{
			x.style();
			x.css(this,"width:100%;height:400px;border:1px dotted green;font-family:monospace");
			x.styleEnd();
			x.inputTextArea(this);
		}
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
	private String query;
	public final void ax_(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_QUERY))throw new Error("notallowed");
		query=q.toString();
		x.xua(this);
		x.xfocus(q);
	}
	public final void ax_rm(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_DELETE))throw new Error("notallowed");
		final path p=path.get(b.urldecode(a[2]));
		if(!p.rm())throw new Error("cannotdelete");
		x.xua(this);
	}
	public final void ax_c(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_FILE_CREATE))throw new Error("notallowed");
		if(path.isfile())throw new Error("notadir");
		path=path.get(q.toString());
//		x.xalert(path.fullpath());
		ed=createed(path);
		if(ed==null){
			x.xalert("no editor for '"+path.name()+"'");
			ax_up(x,null);
			return;
		}
		ed.init(path);
		path.append("","");
		ed.load();
		x.xua(this);
		x.xfocus(path.isfile()?(a)ed:q);
	}
	public final void ax_d(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_CREATE))throw new Error("notallowed");
		path=path.get(q.toString());
		path.mkdirs();
		x.xua(this);
		x.xfocus(path.isfile()?(a)ed:q);
	}
	public final void ax_e(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_ENTER))throw new Error("notallowed");
		final String namedec=b.urldecode(a[2]);
		path=path.get(namedec);
		if(path.isfile()){
			if(!hasbit(BIT_ALLOW_FILE_EDIT))throw new Error("notallowed");
			ed=createed(path);
			if(ed==null){
				x.xalert("no editor for '"+path.name()+"'");
				ax_up(x,null);
				path=path.parent();
				return;
			}
			ed.init(path);
			ed.load();
		}		
		x.xua(this);
		x.xfocus(path.isfile()?(a)ed:q);
	}
	private static Map<String,List<Class<? extends editor>>>editorclses=new HashMap<String,List<Class<? extends editor>>>();
	static{
		editorclses.put("txt",Arrays.<Class<? extends editor>>asList(editortxtarea.class));
		editorclses.put("html",Arrays.<Class<? extends editor>>asList(editortxtarea.class));
	}
	private editor createed(final path p)throws Throwable{
		final String ext=p.type();
		final List<Class<? extends editor>>ls=editorclses.get(ext);
		if(ls==null)return null;
		final Class<? extends editor>cls=ls.get(0);
		if(cls==null)return null;
		return cls.getConstructor(a.class,String.class).newInstance(this,"ed");
	}
	public final void ax_up(final xwriter x,final String[]a)throws Throwable{
		if(!hasbit(BIT_ALLOW_DIR_UP))throw new Error("notallowed");
		final path p=path.parent();
		if(ed!=null)
			ed=null;
		if(p==null)
			return;
		path=p.isin(root)?p:root;
		x.xua(this);
		x.xfocus(q);
	}
}
