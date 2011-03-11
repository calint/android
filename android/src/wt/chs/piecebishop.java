package wt.chs;
import htp.xwriter;
public class piecebishop extends piece{
	private static String[] pieces={"45px-Chess_bdt45.svg.png","45px-Chess_blt45.svg.png"};
	private static final long serialVersionUID=1L;
	public piecebishop(int color){
		super(color);
	}
	@Override public void to(xwriter x) throws Throwable{
		x.p("<img src=\"").p("/chs/").p(pieces[color]).p("\">");
	}
}
