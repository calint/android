package htp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
final class cachedresp{
	private static int hdrlencap=8*64;

	private path path;
	private cacheable cacheable;
	private long lastModified;
	private String lastModified_s;
	private long ts;
	private long dt;
	private ByteBuffer byteBuffer;
	private int hdrinsertionix;
	private String contentType;
	private String key;
	
	cachedresp(final path path) throws IOException{
		this.path=path;
		validate_file(System.currentTimeMillis());
	}
	cachedresp(final cacheable cacheable,final String key){
		this.cacheable=cacheable;
		this.key=key;
		dt=cacheable.lastModChk_ms();
	}
	boolean ifNotModSince(final String ifModSince){return ifModSince.equals(lastModified_s);}
	ByteBuffer byteBuffer(){return byteBuffer;}
	int hdrinsertionix(){return hdrinsertionix;}
	String contentType(){return contentType;}
	String lastModified(){return lastModified_s;}
	boolean validate_file(final long t) throws IOException{
		if(t-ts<htp.cache_files_validate_dt)
			return true;
		ts=t;
		if(!path.exists())
			return false;
		long path_lastModified=path.getLastModified();
		if(path_lastModified==lastModified)
			return true;
		long path_len=path.getSize();
		byteBuffer=ByteBuffer.allocateDirect(hdrlencap+(int)path_len);
		byteBuffer.put(req.h_http200);
		byteBuffer.put(req.h_content_length).put(Long.toString(path_len).getBytes());
		lastModified_s=htp.toLastModified(path_lastModified);
		byteBuffer.put(req.h_last_modified).put(lastModified_s.getBytes());
		byteBuffer.put(req.hkp_connection_keep_alive);
		hdrinsertionix=byteBuffer.position();
		byteBuffer.put(req.ba_crlf2);
		path.to(byteBuffer);
		byteBuffer.flip();
		lastModified=path_lastModified;
		return true;
	}
	void validate_cacheable(final long t,final String lm)throws Throwable{
		if(t-this.ts<this.dt)
			return;
		lastModified_s=cacheable.lastMod();
		if(lastModified_s!=null&&lastModified_s.equals(lm))
			return;
		contentType=cacheable.contentType();
		final ByteArrayOutputStream baos=new ByteArrayOutputStream(htp.io_buf_B);
		((wt)cacheable).to(new xwriter(baos));
		byteBuffer=ByteBuffer.wrap(baos.toByteArray());
		ts=t;
		if(htp.cacheu_tofile)
			htp.path(htp.cacheu_dir+key+"."+cacheable.fileType()).writebb(byteBuffer);
	}
}
