package htpx;
import java.io.IOException;
import java.io.OutputStream;
public final class osvoid extends OutputStream{
	@Override final public void write(int c) throws IOException{
		throw new Error("cannot");
	}
	@Override final public void write(byte[] c) throws IOException{
		this.write(c,0,c.length);
	}
	@Override public void write(byte[] c,int off,int len) throws IOException{}
}
