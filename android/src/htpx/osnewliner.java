package htpx;
import java.io.IOException;
import java.io.OutputStream;
public class osnewliner extends OutputStream{
	private StringBuffer line=new StringBuffer(256);
	public osnewliner(){}
	@Override final public void write(int c) throws IOException{
		throw new Error("cannot");
	}
	@Override final public void write(byte[] c) throws IOException{
		this.write(c,0,c.length);
	}
	@Override final public void write(byte[] c,int off,int len) throws IOException{
		for(int n=0;n<len;n++){
			byte b=c[off+n];
			if(b=='\n'){
				try{
					on_newline(line.toString());
				}catch(Throwable e){
					throw new Error(e);
				}
				line.setLength(0);
			}else
				line.append((char)b);
		}
	}
	public void on_newline(String line) throws Throwable{
		System.out.println(line);
	}
}
