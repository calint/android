package wt.chs;
import htp.xwriter;
public class pieceking extends piece{
	private static String[] pieces={"45px-Chess_kdt45.svg.png","45px-Chess_klt45.svg.png"};
	private static final long serialVersionUID=1L;
	public pieceking(int color){
		super(color);
	}
	@Override public void to(xwriter x) throws Throwable{
		x.p("<img src=\"").p("/chs/").p(pieces[color]).p("\">");
	}
}
