package htp;
import java.io.IOException;
import java.io.OutputStream;
public final class osjsstr extends OutputStream{
	private static final byte[] b_jsstr_sq="\\'".getBytes();
	private static final byte[] b_jsstr_nl="\\n".getBytes();
	private static final byte[] b_jsstr_bs="\\\\".getBytes();
	private OutputStream os;
	public osjsstr(OutputStream os){
		this.os=os;
	}
	@Override final public void write(int c) throws IOException{
		throw new Error("cannot");
	}
	@Override final public void write(byte[] c) throws IOException{
		write(c,0,c.length);
	}
	@Override public void write(byte[] c,int off,int len) throws IOException{
		int i=0;
		for(int n=0;n<len;n++){
			byte b=c[off+n];
			if(b=='\n'){
				int k=n-i;
				if(k!=0)
					os.write(c,off+i,k);
				os.write(b_jsstr_nl);
				i=n+1;
			}else if(b=='\''){
				int k=n-i;
				if(k!=0)
					os.write(c,off+i,k);
				os.write(b_jsstr_sq);
				i=n+1;
			}else if(b=='\\'){
				int k=n-i;
				if(k!=0)
					os.write(c,off+i,k);
				os.write(b_jsstr_bs);
				i=n+1;
			}
		}
		int k=len-i;
		if(k!=0)
			os.write(c,off+i,k);
	}
}
