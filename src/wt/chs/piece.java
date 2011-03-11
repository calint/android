package wt.chs;
import htp.wt;
import htp.xwriter;
public abstract class piece extends wt{
	static final long serialVersionUID=1;
	protected int color;
	public piece(int color){
		super();
		this.color=color;
	}
	@Override public abstract void to(xwriter x) throws Throwable;
}
