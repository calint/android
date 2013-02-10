package a.y;
import b.a;
import b.xwriter;
public class gg extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x) throws Throwable{
		x.style();
		x.cssfont("slk","/slkscr.ttf").nl();
		x.css("table td","border:1px dotted black").nl();
		x.css("html","font-family:monospace;font-size:13px").nl();
		x.styleEnd();
		x.tag("h1").p("header level 1").tagEnd("h1").nl();
		x.p("<p>a super simple data system gg file system").br().nl();
		x.tag("h2").p("header level 2").tagEnd("h2").nl();
		x.p("<p>a super simple data system gg file system").br().nl();
		x.tag("h3").p("header level 3").tagEnd("h3").nl();
		x.p("<p>a super simple data system gg file system").br().nl();
		x.tag("h4").p("header level 4").tagEnd("h4").nl();
		x.p("<p>a super simple data system gg file system").br().nl();
		x.tag("h5").p("header level 5").tagEnd("h5").nl();
		x.p("<p>a super simple data system gg file system").br().nl();
		x.tag("h6").p("header level 6").tagEnd("h6").nl();
		x.p("<p>a super simple data system gg file system").br().nl();
		x.tag("h7").p("header level 7").tagEnd("h7").nl();
		x.table().nl();
		x.tr().td().p("hello").td().spc(3).td().ax(this,"link").td().p("world").nl();
		x.ul().li().p("list").li().p("another").li().p("").ulEnd().nl();
		x.td().p("<img src=/img1.459x469.png><img src=/img2.771x251.png>").nl();
		x.td().p("<img src=/img2.771x251.png>").nl();
		x.tableEnd().nl();
		x.pre().p("pre-formatted    text with  spaces");
	}
	public static class item extends a{
		static final long serialVersionUID=1;
		public a bits; //bits
		public a address; //address
		public a size; //int
		public a parent; //address
		public a owner; //ref

		public a created; //time
		public a modified; //time
		public a deleted; //time
		
		public a list; //acl
		public a view; //acl
		public a append; //acl
		public a edit; //acl
		public a remove; //acl
		
		public a name;
	}
}
