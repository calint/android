package htp;
import java.io.IOException;
import java.io.OutputStream;
public final class osltgt extends OutputStream{
	private static final byte[]ba_html_gt="&gt;".getBytes();
	private static final byte[]ba_html_lt="&lt;".getBytes();
	private OutputStream os;
	public osltgt(OutputStream os){this.os=os;}
	@Override final public void write(int c) throws IOException{throw new Error("cannot");}
	@Override final public void write(byte[] c) throws IOException{write(c,0,c.length);}
	@Override public void write(byte[] c,int off,int len) throws IOException{
		int i=0;
		for(int n=0;n<len;n++){
			byte b=c[off+n];
			if(b=='<'){
				int l=n-i;
				if(l!=0)
					os.write(c,off+i,l);
				os.write(ba_html_lt);
				i=n+1;
			}else if(b=='>'){
				int l=n-i;
				if(l!=0)
					os.write(c,off+i,l);
				os.write(ba_html_gt);
				i=n+1;
			}
		}
		int l=len-i;
		if(l!=0)
			os.write(c,off+i,l);
	}
}
