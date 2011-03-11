package wt.ix;
import htp.path;
import htpx.diro;
import java.io.IOException;

public class xdiro extends diro{
	private static final long serialVersionUID=1L;
	£ pt;
	public xdiro() throws Throwable{
//		session ses=req.get().session();
//		root=ses.path("ix");
//		path=ses.path("ix");
		bits=diro.BIT_ALLOW_FILE_LINK+diro.BIT_ALLOW_DIR_OPEN+diro.BIT_ALLOW_NAV_UP;
	}
	public void ltr(String qs) throws IOException{
		path=root.getPath(qs);
	}
	@Override protected void onPathEnter(path path){
		pt.qs=this.path.getName();
	}
	@Override protected void onPathUp(){
		pt.qs="";
	}
}