package htp;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
final class oschunked extends OutputStream{
	private static final ByteBuffer bb_eochunk=ByteBuffer.wrap("0\r\n\r\n".getBytes());
	private static final ByteBuffer bb_crnl=ByteBuffer.wrap("\r\n".getBytes());
	private req r;
	private int chunk_size_bytes;
	private byte[] chunkhx;
	private byte[] buf;
	private int bufi;
	oschunked(req r,int chunk_size_bytes) throws Throwable{
		this.r=r;
		this.chunk_size_bytes=chunk_size_bytes;
		this.chunkhx=(Integer.toHexString(chunk_size_bytes)+"\r\n").getBytes();
		this.buf=new byte[chunk_size_bytes];
	}
	@Override public String toString(){
		return new String(buf,0,bufi);
	}
	@Override public void write(int arg0) throws IOException{
		throw new UnsupportedOperationException();
	}
	@Override public void write(byte[] b) throws IOException{
		write(b,0,b.length);
	}
	@Override public void write(byte[] c,int off,int len) throws IOException{
		int remain=buf.length-bufi;
		if(len<=remain){
			System.arraycopy(c,off,buf,bufi,len);
			bufi+=len;
			return;
		}
		System.arraycopy(c,off,buf,bufi,remain);
		bufi+=remain;
		off+=remain;
		len-=remain;
		ByteBuffer[] bba=new ByteBuffer[]{ByteBuffer.wrap(chunkhx),ByteBuffer.wrap(buf,0,bufi),bb_crnl.slice()};
		write_blocking(bba);
		while(len>chunk_size_bytes){
			bba=new ByteBuffer[]{ByteBuffer.wrap(chunkhx),ByteBuffer.wrap(c,off,chunk_size_bytes),bb_crnl.slice()};
			write_blocking(bba);
			off+=chunk_size_bytes;
			len-=chunk_size_bytes;
		}
		if(len>0){
			System.arraycopy(c,off,buf,0,len);
			bufi=len;
		}
	}
	private void write_blocking(ByteBuffer[] bba) throws IOException{
		long remaining=0;
		for(ByteBuffer bb:bba)
			remaining+=bb.remaining();
		int i=0;
		while(remaining!=0){
			final long c=r.socketChannel.write(bba,i,bba.length-i);
			if(c==0){
				synchronized(r){
					r.waiting_write(true);
					r.selectionKey.interestOps(SelectionKey.OP_WRITE);
					r.selectionKey.selector().wakeup();
					try{r.wait();}catch(InterruptedException ok){}
					r.waiting_write(false);
				}
			}
			remaining-=c;
			thdwatch.output+=c;
		}
	}
	@Override public void flush() throws IOException{
		if(bufi==0)
			return;
		ByteBuffer[] bba=new ByteBuffer[]{ByteBuffer.wrap(Integer.toHexString(bufi).getBytes()),bb_crnl.slice(),ByteBuffer.wrap(buf,0,bufi),bb_crnl.slice()};
		write_blocking(bba);
		bufi=0;
	}
	void finish() throws InterruptedException,IOException{
		flush();
		ByteBuffer[] bba=new ByteBuffer[]{bb_eochunk.slice()};
		write_blocking(bba);
	}
}
