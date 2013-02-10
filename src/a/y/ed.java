package a.y;
//import static b.b.*;
import b.a;
import b.xwriter;
public class ed extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x)throws Throwable{
		x.style().css(this,"border-bottom:1px dotted green").styleEnd();
		x.tago("ed").attrdef(this).attr("contentEditable","true");
		final String id=id();
//		x.attr("onfocus","$d(this+' focus');");
		x.attr("oninput","ui.qpb(this.id);return true;");
		x.attr("onblur","if(!ui.qpbhas(this.id))return true;$x('"+id+"');return true;");
		x.tagoe();
		x.p(this.toString());
		x.tagEnd("ed");
	}
	public void ax_(final xwriter x,final String[]a)throws Throwable{
		x.pl("//"+this.id()+"="+toString());
//		x.xu(this,"hello world");
	}
}
