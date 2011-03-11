package wt.chs;
import htp.wt;
import htp.xwriter;
public class square extends wt{
	private static final long serialVersionUID=1L;
	private int color;
	private piece piece;
	private boolean highlight;
	public square(final board board,final String name,final int color){
		super(board,name);
		this.color=color;
	}
	public void clear(){this.piece=null;}
	public piece getPiece(){return piece;}
	public void setPiece(final piece piece){this.piece=piece;}
	public void setHighlight(final boolean b){highlight=b;}
	@Override public void to(final xwriter x) throws Throwable{
		StringBuffer sb=new StringBuffer();
		sb.append(color==0?"blk":"wht");
		if(highlight)
			sb.append(" hilite");
		x.td(sb.toString());
		x.aBgn("javascript:ui.ax('"+wid()+" clk')");
		if(piece!=null){
			x.wt(piece);
		}else{
			x.p("&nbsp;&nbsp;");
		}
		x.aEnd();
		x.tdEnd();
	}
	public void ax_clk(final xwriter x,final String[] p) throws Throwable{
		onEvent(x,this,null);
	}
}
