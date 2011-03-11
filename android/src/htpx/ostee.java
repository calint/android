package htpx;
import java.io.IOException;
import java.io.OutputStream;
public final class ostee extends OutputStream{
	private OutputStream os[];
	public ostee(OutputStream[] oss){
		this.os=oss;
	}
	@Override final public void write(int c) throws IOException{
		throw new Error("cannot");
	}
	@Override final public void write(byte[] c) throws IOException{
		this.write(c,0,c.length);
	}
	@Override public void write(byte[] c,int off,int len) throws IOException{
		for(int n=0;n<os.length;n++)
			os[n].write(c,off,len);
	}
}
