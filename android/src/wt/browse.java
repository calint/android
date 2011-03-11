package wt;
import htp.htp;
import htp.wt;
import htp.xwriter;
import htpx.diro;
public final class browse extends wt{
	public diro xdiro;
	static final long serialVersionUID=1;
	public browse(){
		xdiro.setRootPath(htp.path(""));
		xdiro.setBits(diro.BIT_ALLOW_DIR_OPEN+diro.BIT_ALLOW_FILE_LINK+diro.BIT_ALLOW_NAV_UP+diro.BIT_ALLOW_FILE_OPEN);
	}
	@Override public void to(final xwriter x) throws Throwable{x.wt(xdiro);}
}
