package a.pczero;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import b.a;
import b.xwriter;
final public class srcviwr extends a{
	static final long serialVersionUID=11;
	boolean edit=false;
	int focusline;
	private int lstfocusline=-1;
	String libgstep="#600";
	void xfocusline(xwriter x){
		if(edit)
			return;
		if(lstfocusline!=-1){
			x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lstfocusline-1)+"];e.style.backgroundColor=e._oldstyle;");			
		}
		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(focusline-1)+"];e._oldstyle=e.style.backgroundColor;e.style.backgroundColor='"+libgstep+"';");
		lstfocusline=focusline;
	}
	public a txt;
	public void to(final xwriter x)throws Throwable{
		x.el(this);
		x.p("source");
		x.ax(this,"f1"," edit");
		x.ax(this,"f2"," view");
//		x.el(brkpts);
//		x.p(brkptsset.toString());
//		x.elend();
		if(edit){
			x.nl();
			x.style(txt,"width:16em;height:64em;resize:none;border-left:1px dotted #333;color:#444;padding-right:1em");
			x.inputTextArea(txt);
			return;
		}
		final StringReader sr=new StringReader(txt.toString());
		final BufferedReader br=new BufferedReader(sr);
		x.tag("ol");
		int lno=1;
		final String id=id();
		for(String ln;(ln=br.readLine())!=null;lno++){
			x.tago("li").attr("lno",lno).attr("onclick=\"$x('"+id+" brk '+this.getAttribute('lno'))\"");
			final boolean brk=isonbrkpt(lno);
			if(brk)
				x.attr("style",listylebrkpt);
			x.tagoe().pl(ln);
		}
		x.tagEnd("ol");
		x.elend();
	}
	public a brkpts;
	private Set<Integer>brkptsset=new HashSet<Integer>();
	String listylebrkpt=libgstep;
	boolean isonbrkpt(final int srclno){
		return brkptsset.contains(srclno);
	}
	synchronized public void ax_brk(xwriter x,String[]a)throws Throwable{
		final int lno=Integer.parseInt(a[2]);
		if(brkptsset.contains(lno)){
			brkptsset.remove(lno);
			brkpts.set(brkptsset.toString());
//			x.xu(brkpts);
			x.xuo(this);
			return;
		}
		brkptsset.add(lno);
		brkpts.set(brkptsset.toString());
		x.pl("var e=$('"+id()+"').getElementsByTagName('ol')[0].getElementsByTagName('li')["+(lno-1)+"];e.style.backgroundColor='"+libgstep+"';");

//		x.xu(brkpts);
//		x.xuo(this);
	}
	synchronized public void ax_f1(xwriter x,String[]a)throws Throwable{
		if(edit)return;
		edit=true;
		x.xuo(this);
		x.xfocus(this);
	}
	synchronized public void ax_f2(xwriter x,String[]a)throws Throwable{
		if(!edit)return;
		edit=false;
		x.xuo(this);
	}
}
