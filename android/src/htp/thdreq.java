package htp;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
final class thdreq extends Thread{
	static LinkedList<thdreq>all=new LinkedList<thdreq>();
	private static int seq;
	req r;
	long t0;
	thdreq(final req r){
		super("t"+Integer.toString(seq++));
		this.r=r;
		synchronized(all){all.add(this);}
		start();
	}
	@Override public void run(){
		t0=System.currentTimeMillis();
		proc();
		while(htp.thread_pool){
			final LinkedList<req>pr=htp.pending_req();
			synchronized(pr){
				thdwatch.freethds++;
				while(pr.isEmpty())
					try{pr.wait();}catch(InterruptedException ok){}
				r=pr.removeFirst();
				if(r==null)throw new NullPointerException("s");
				thdwatch.freethds--;
			}
			proc();
			final long dt=System.currentTimeMillis()-t0;
			if(dt>htp.thread_pool_lftm)
				break;
		}
		synchronized(all){all.remove(this);}
	}
	private void proc(){
		try{
			if(r.is_waiting_run_page())
				r.run_page();
			else if(r.is_waiting_run_page_content())
				r.run_page_content();
			else
				throw new IllegalStateException();
			if(!r.is_connection_keep_alive()){
				r.close_socketChannel();
			}else{
				//? if r.buf_len!=0
				r.selectionKey.interestOps(SelectionKey.OP_READ);
				r.selectionKey.selector().wakeup();
			}
		}catch(Throwable e){
			r.close_socketChannel();
			htp.log(e);
		}
	}
}
