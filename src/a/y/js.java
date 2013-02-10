package a.y;

import java.io.InputStream;
import b.a;
import b.b;
import b.cacheable;
import b.xwriter;
public final class js extends a implements cacheable{
	static final long serialVersionUID=1;
	
	public String filetype(){return "js";}
	public String contenttype(){return "text/javascript";}
	public long lastmodupdms(){return 24*60*60*1000;}
	public String lastmod(){return b.timeatloadstrhtp;}
	public boolean cacheforeachuser(){return false;}

	public void to(xwriter x)throws Throwable{
		final InputStream is=getClass().getResourceAsStream("/htp/js.js");
		b.cp(is,x.outputstream());
		is.close();
	}
}
