package a.y;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.LinkedList;
import b.a;
import b.b;
import b.xwriter;
public class roome extends a{
	static final long serialVersionUID=1L;
	final public static class term{
		term esctop(){return this;}
		term tab(){return this;}
		term enter(){return this;}
		term spc(){return this;}
		String label(){return "";}
		term type(final String line){return this;}
		String value(){return "";}
	}
	public static class ob extends a{
		static final long serialVersionUID=1L;
		private String info;public String info(){return info;}
	}
	public static class link extends ob{
		static final long serialVersionUID=1L;
		private Collection<link>links=new LinkedList<link>();public Collection<link>links(){return links;}
	}
	public static class scene extends link{static final long serialVersionUID=1L;
		private Collection<ob>contains=new LinkedList<ob>();public Collection<ob>contains(){return contains;}
		private Collection<actor>actors=new LinkedList<actor>();public Collection<actor>actors(){return actors;}
	}
	public static class actor extends ob{static final long serialVersionUID=1L;}
	public static class terminal extends ob{static final long serialVersionUID=1L;}
	public static class om extends actor{static final long serialVersionUID=1L;}
	
	public static ob oo=new ob();
	private ob o=oo;
	public a in;
	public a con;
	private Collection<ob>inv=new LinkedList<ob>();
	public void to(final xwriter x)throws Throwable{
		x.pre();
		if(!inv.isEmpty())
			x.pl(inv.toString());
		x.inputText(in,null,this,"a").nl().span(con).nl();
		x.focus(in);
		x.title("roome");
		x.rend(o);
	}
	public void ax_a(xwriter x,String[]p){
		final String cmd=in.toString();
		if(b.isempty(cmd)){
			look();
			x.xu(con);
			return;
		}
		if(cmd.startsWith("l")){
			x.xu(con.set("look"));
		}else if(cmd.startsWith("g")){
			x.xu(con.set("go"));			
		}else if(cmd.startsWith("b")){
			x.xu(con.set("back"));			
		}else if(cmd.startsWith("s")){
			x.xu(con.set("select"));			
		}else if(cmd.startsWith("t")){
			x.xu(con.set("take"));			
		}else if(cmd.startsWith("d")){
			x.xu(con.set("drop"));			
		}else if(cmd.startsWith("c")){
			x.xu(con.set("copy"));			
		}else if(cmd.startsWith("s")){
			x.xu(con.set("say"));			
		}else if(cmd.startsWith("g")){
			x.xu(con.set("goto"));			
		}else if(cmd.startsWith("i")){
			x.xu(con.set("inventory"));			
		}
	}
	public void look(){
		final xwriter x=new xwriter(new ByteArrayOutputStream());
		x.pl("roome");
		x.pl("// keywords: look go back select take drop copy  say goto inventory");
		x.pl("//");
		x.pl("//");
		con.set(x.toString());
	}
}
