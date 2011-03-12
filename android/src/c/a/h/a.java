package c.a.h;
import htp.htp;
import htp.thdwatch;
import htpx.osnewliner;
import java.io.File;
import c.a.cluket;
import c.a.dc;
import c.a.state;
public class a implements cluket,Runnable{
	static final long serialVersionUID=1;
	private boolean started;
	private cluket bgcluket=new c.a.t.tapit();
	private boolean bgcluketon=true;
	public void paint(final state state,final dc dc) throws Throwable{
		if(bgcluketon)
			bgcluket.paint(state,dc);
		else
			dc.clr(0,0,0);
		dc.brush(0xffffffff,true).textSize(7.0f).pos(0,33).dy(12);
		dc.pl("http://"+state.device.get_host_ip_address()+("80".equals(htp.server_port)?"":(":"+htp.server_port)));
		dc.pl(" @ "+new File(htp.root_dir).getAbsolutePath()).p("  ").pl("io=B("+thdwatch.input+","+thdwatch.output+")");
		dc.pl("");
		htp.stats_to(new osnewliner(){public void on_newline(String line) throws Throwable{
			dc.pl(line);
		}});
		dc.cr();
		thdwatch.print_fieldnames_to(new osnewliner(){public void on_newline(String line) throws Throwable{
			dc.pl(line);
		}},"\n");
		thdwatch.print_fields_to(new osnewliner(){public void on_newline(String line) throws Throwable{
			dc.pl(line);
		}},"\n");
		dc.pl("");
	}
	public void update(final state state)throws Throwable{
		if(!started){
			started=true;
			new Thread(this,this.getClass().getName()+".htp").start();
		}
		if(bgcluketon)
			bgcluket.update(state);
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
