package b;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public final class sockio{
	private SocketChannel sc;
	private SelectionKey sk;
	sockio(final SocketChannel sc,final SelectionKey sk){this.sc=sc;this.sk=sk;}
	public int read(final ByteBuffer bb)throws IOException{return sc.read(bb);}
	public int write(final ByteBuffer bb)throws IOException{return sc.write(bb);}
	public void reqwrite(){sk.interestOps(SelectionKey.OP_WRITE);}
}
