package a;
import b.a;
import b.b;
import b.xwriter;
public class zncr_blinkydots extends a{
	static final long serialVersionUID=1;
	final static int ninstrwidth=16;
	final static int ninstr=32;
	private dot[][]ram=new dot[ninstr][ninstrwidth];
	{
		for(int k=0;k<ram.length;k++)
			for(int n=0;n<ram[k].length;n++)
				ram[k][n]=new dot(this,k+"$"+n);
	}
	protected a chldq(final String id){
		final String[]rc=id.split("\\$");
		final int r=Integer.parseInt(rc[0]);
		final int c=Integer.parseInt(rc[1]);
		return ram[r][c];
	}
	final public static class dot extends a{
		static final long serialVersionUID=1;
		{set(".");}
		public dot(final a pt,final String nm){
			super(pt,nm);
		}
		public void to(final xwriter x)throws Throwable{
			x.ax(this,b.isempty(toString(),"."));
//			x.p("<a href=javascript:$x('"+id()+"') id=").p(id()).p(">");
//			x.p(b.isempty(toString(),"."));
//			x.p("</a>");
		}
		public void ax_(final xwriter x,final String[]a)throws Throwable{
			x.xu(set(".".equals(toString())?"o":"."));
			ev(x);
		}
	}
	final static String str(final String[]a){
		return str(a,", ");
	}
	final static String padr(final String pad,final String s){
		final int p=pad.length()-s.length();
		if(p<1)
			return s;
		return pad.substring(0,p)+s;
	}
	final static String str(final String[]a,final String nl){
		final StringBuilder sb=new StringBuilder();
		for(final String s:a)
			sb.append(s).append(nl);
		sb.setLength(sb.length()-2);
		return sb.toString();
	}
	public void to(final xwriter x)throws Throwable{
		x.pre();
		x.style();
		x.css("div.lft","float:left;border:1px solid black");
		x.css("div.rht","float:right;border:1px solid green");
		x.css("div.dsp","border:1px solid blue");
		x.styleEnd();
//		x.p("   zncr xlwr").nl();
		x.div("lft");
		int row=0;
		for(final dot[]r:ram){
			x.p(padr("00",Integer.toHexString(row))).spc();
			int c=0;
			for(final dot px:r){
				px.to(x);
				if(++c%4==0)
					x.spc();
			}
			final String wid=id();
			x.spc().tago("span").attr("id",wid+row).tagoe().p(padr("0000",Integer.toHexString(rowint(row)))).tagEnd("span").nl();
			row++;
		}
		x.divEnd();
		x.div("dsp");
		x.p("middle div");
		x.divEnd();
		x.div("rht");
		x.p("pc:0000  zn:00");
		x.divEnd();
	}
	protected void ev(xwriter x,a from,Object o)throws Throwable{
		if(from instanceof dot){
			final String[]ix=from.name().split("\\$");
			final int row=Integer.parseInt(ix[0]);
			int acc=rowint(row);
			x.xu(id()+row,padr("0000",Integer.toHexString(acc)));
		}else
			super.ev(x,from,o);
	}
	private int rowint(final int row) {
		int acc=0;
		for(final dot d:ram[row]){
			acc<<=1;
			if("o".equals(d.toString())){
				acc|=1;
			}				
		}
		return acc;
	}
}
