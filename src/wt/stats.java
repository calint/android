package wt;
import htp.cacheable;
import htp.htp;
import htp.wt;
import htp.xwriter;
public class stats extends wt implements cacheable{
	static final long serialVersionUID=1;
	@Override public String fileType(){return "txt";}
	@Override public String contentType(){return "text/plain;charset=utf8";}
	@Override public boolean cacheforeachuser(){return false;}
	@Override public String lastMod(){return null;}
	@Override public long lastModChk_ms(){return 1000;}
	@Override public void to(xwriter x) throws Throwable{
		htp.stats_to(x.outputStream());
	}
}
