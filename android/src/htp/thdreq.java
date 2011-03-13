package htp;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.LinkedList;
final class thdreq extends Thread{
	static ArrayList<thdreq>all=new ArrayList<thdreq>(htp.K);
	private static int seq;
	req r;
	long t0;
	thdreq(final req r){
		super("t"+Integer.toString(seq++));
		this.r=r;
		synchronized(all){all.add(this);}
		t0=System.currentTimeMillis();
		start();
	}
	@Override public void run(){
		thdwatch.threads++;
		if(r!=null)
			proc();			
		r=null;
		while(true){
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
			long dt=System.currentTimeMillis()-t0;
			if(dt>htp.thread_lftm)
				break;
		}
		thdwatch.threads--;
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
