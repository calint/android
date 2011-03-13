package c.a.h;
import htp.htp;
import htp.thdwatch;
import htpx.osnewliner;
import java.io.File;
import c.a.cluket;
import c.a.dc;
import c.a.device;
public class a implements cluket,Runnable{
	static final long serialVersionUID=1;
	private boolean started;
	private cluket bgcluket=new c.a.t.tapit();
	private boolean bgcluketon=true;
	public void paint(final device dev,final dc dc) throws Throwable{
		if(bgcluketon)
			bgcluket.paint(dev,dc);
		else
			dc.clr(0,0,0);
		dc.brush(0xffffffff,true).textSize(7.0f).pos(0,23).dy(12);
		dc.pl("http://"+dev.get_host_ip_address()+("80".equals(htp.server_port)?"":(":"+htp.server_port))+" @ "+new File(htp.root_dir).getAbsolutePath());
		dc.pl("(input,output)=B("+thdwatch.input+","+thdwatch.output+")");
		if(dev.state().menu){
			thdwatch.print_fields2_to(new osnewliner(){public void on_newline(String line) throws Throwable{
				dc.pl(line);
			}},new byte[]{'\n'},new byte[]{'\n','\n'},"                ");
			dc.pl("");
		}else{
			htp.stats_to(new osnewliner(){public void on_newline(String line) throws Throwable{
				dc.pl(line);
			}});
			dc.cr();			
		}
	}
	public void update(final device dev)throws Throwable{
		if(!started){
			started=true;
			new Thread(this,this.getClass().getName()+".htp").start();
		}
		if(bgcluketon)
			bgcluket.update(dev);
	}
	@Override public void run(){
		try{
			htp.server_port="8888";
			htp.root_dir="/sdcard/htp/";
			htp.cache_files=false;
			htp.thd_watch=false;
			htp.threads_min=1;
			htp.main(new String[]{});
		}catch(Throwable e){
			throw new Error(e);
		}
	}
}
