package htp;
public interface cacheable{
	String fileType();
	String contentType();
	long lastModChk_ms();
	String lastMod();
	boolean cacheforeachuser();
}
