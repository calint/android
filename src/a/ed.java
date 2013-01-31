package a;
import b.a;
import b.path;
import b.xwriter;
public class ed extends a{
	static final long serialVersionUID=1;
	public path path;
	public a bd;
	public void to(final xwriter x)throws Throwable{
		x.ax(this,"x","••");
		x.p("|");
		x.ax(this,"s","⌾");
		x.ax(this,"sx","⌕");
		x.p("|");
		x.p(path.fullpath()).spc();
		x.style().css("textarea.ed","border-top:3px double green;width:100%;height:640px").styleEnd();
		x.inputTextArea(bd,"ed");
		x.focus(bd);
//		x.tago("p").attr("contentEditable","true").tagoe().tagEnd("p");
	}
	public void ax_(final xwriter x,final String[]a)throws Throwable{}
	public void ax_x(final xwriter x,final String[]a)throws Throwable{ev(x,this,"x");}
	public void ax_s(final xwriter x,final String[]a)throws Throwable{bd.to(path);}
	public void ax_sx(final xwriter x,final String[]a)throws Throwable{ax_s(x,a);ev(x,this,"x");}
}
