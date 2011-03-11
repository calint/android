package wt.chs;
import java.io.IOException;
import htp.wt;
import htp.xwriter;
public class £ extends wt{
	static final long serialVersionUID=1;
	public board brd;
	public wt movefld;
	private square fromsq;
	public void ax_rst(final xwriter x,final String[] p){
		board_setup();
		x.x_reload();
	}
	public void ax_clr(final xwriter x,final String[] p){
		brd.clear();
		x.x_reload();
	}
	public void ax_mov(final xwriter x,final String[] p) throws IOException{
		//		x.x_alert(movefld.toString());
		//		movefld.x_setValue(x,"");
		final String m=movefld.toString().trim();
		if(m.length()==0)
			return;
		final char pc=m.charAt(0);
		final StringBuffer sb=new StringBuffer();
		int i=m.indexOf('x');
		final boolean takes=(i!=-1);
		if(takes){
			sb.append("takes ");
		}
		i=m.indexOf('+');
		final boolean check=(i!=-1);
		if(check){
			sb.append("check ");
		}
		i=m.indexOf('#');
		final boolean mate=(i!=-1);
		if(mate){
			sb.append("mate ");
		}
		i=m.indexOf('=');
		final boolean promotion=(i!=-1);
		if(promotion){
			sb.append("promotion ");
		}
		if(m.equals("O-O")){
			sb.append("kingside castling");
		}
		if(m.equals("O-O-O")){
			sb.append("queenside castling");
		}
		final boolean pawnmove=Character.isLowerCase(pc);
		if(pawnmove){
			sb.append("pawn move");
		}else{
			sb.append(pc+" piece move ");
		}
		String destsq;
		if(takes){
			destsq=m.substring(2,4);
		}else if(!pawnmove){
			destsq=m.substring(1,3);
		}else{
			destsq=m.substring(0,2);
		}
		sb.append("  destsq  ").append(destsq);
		if(pawnmove){
			if(!takes){				
			}else{}
		}
		x.x_alert(sb.toString());
		movefld.setValue("");
		x.x_reload();
	}
	public void ax_flp(final xwriter x,final String[] p){
		brd.flip();
		x.x_reload();
	}
	private void board_setup(){
		brd.clear();
		brd.square(0,0).setPiece(new piecerook(1));
		brd.square(0,1).setPiece(new pieceknight(1));
		brd.square(0,2).setPiece(new piecebishop(1));
		brd.square(0,3).setPiece(new piecequeen(1));
		brd.square(0,4).setPiece(new pieceking(1));
		brd.square(0,5).setPiece(new piecebishop(1));
		brd.square(0,6).setPiece(new pieceknight(1));
		brd.square(0,7).setPiece(new piecerook(1));
		for(int k=0;k<8;k++){
			brd.square(1,k).setPiece(new piecepawn(1));
		}
		brd.square(7,0).setPiece(new piecerook(0));
		brd.square(7,1).setPiece(new pieceknight(0));
		brd.square(7,2).setPiece(new piecebishop(0));
		brd.square(7,3).setPiece(new piecequeen(0));
		brd.square(7,4).setPiece(new pieceking(0));
		brd.square(7,5).setPiece(new piecebishop(0));
		brd.square(7,6).setPiece(new pieceknight(0));
		brd.square(7,7).setPiece(new piecerook(0));
		for(int k=0;k<8;k++){
			brd.square(6,k).setPiece(new piecepawn(0));
		}
	}
	@Override public void to(final xwriter x) throws Throwable{
		x.wt(brd);
		x.style().nl();
		x.p("input.movefld{width:80px;border:1px dotted black;}").nl();
		x.styleEnd().nl();
		x.action_ax(this,"clr","::clear").p(" ");
		x.action_ax(this,"rst","::reset").p(" ");
		x.action_ax(this,"flp","::flip").br();
		x.inputText(movefld,"movefld",this,"mov");
		x.focus(movefld);
		x.action_ax(this,"mov","::move").p(" ");
	}
	@Override public void onEvent(final xwriter x,final wt fromwt,final Object o) throws Throwable{
		if(fromwt instanceof square){
			final square sq=(square)fromwt;
			if(fromsq!=null){
				fromsq.setHighlight(false);
				final piece p=fromsq.getPiece();
				if(p!=null){
					fromsq.clear();
					sq.clear();
					sq.setPiece(p);
				}
				fromsq=null;
			}else{
				fromsq=(square)fromwt;
				fromsq.setHighlight(true);
			}
			x.x_reload();
		}else{
			super.onEvent(x,fromwt,o);
		}
	}
}
