package a.y.chs;
import b.xwriter;
public class piecequeen extends piece{
	private static String[] pieces={"45px-Chess_qdt45.svg.png","45px-Chess_qlt45.svg.png"};
	private static final long serialVersionUID=1L;
	public piecequeen(final int color){super(color);}
	public void to(final xwriter x)throws Throwable{x.p("<img src=\"").p("/chs/").p(pieces[color]).p("\">");}
}
