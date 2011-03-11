package htpx;
import java.io.IOException;
import htp.wt;
import htp.xwriter;
final public class stsb{
	private xwriter x;
	private long update_intervall_ms;
	private long t0;
	private wt stswt;
	public stsb(final xwriter x,final wt stswt,final long update_intervall_ms) throws Throwable{
		this.x=x;
		this.stswt=stswt;
		this.update_intervall_ms=update_intervall_ms;
	}
	public void update(final String status) throws IOException{
		this.stswt.setValue(status);
		long t=System.currentTimeMillis();
		if((t-t0)<update_intervall_ms){
			return;
		}
		t0=t;
		stsupdate();
	}
	public void done() throws Throwable{stsupdate();}
	private void stsupdate() throws IOException{stswt.x_updInnerHtml(x);x.flush();}
}
