package wt.oo;
import htp.htp;
import htp.path;
import htp.req;
import htp.wt;
import htp.xwriter;
import htpx.diro;
public class £ extends wt{
	static final long serialVersionUID=1;
	public diro dir;
	@Override public void to(final xwriter x) throws Throwable{
		final String clsnm=req.get().querystr();
		x.p(clsnm).p(" ").a("/oo/"+clsnm,"[+]").br();
		final path path=htp.path("/"+getClass().getPackage().getName().replace('.','/')+"/"+clsnm.replace('.','/'));
		dir.setBits(diro.BIT_ALLOW_FILE_LINK);
//		dir.setBits(diro.BIT_ALLOW_DIR_OPEN+diro.BIT_ALLOW_FILE_LINK+diro.BIT_ALLOW_NAV_UP+diro.BIT_ALLOW_FILE_OPEN);
		dir.setRootPath(path);
		dir.to(x);
	}
}
