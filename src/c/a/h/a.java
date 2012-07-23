package c.a.h;
import java.io.File;
import b.osnl;
import b.thdwatch;
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
		dc.pl("http://"+dev.get_host_ip_address()+("80".equals(b.b.server_port)?"":(":"+b.b.server_port))+" @ "+new File(b.b.root_dir).getAbsolutePath());
		dc.pl("(input,output)=B("+thdwatch.input+","+thdwatch.output+")");
		if(dev.state().menu){
			thdwatch.print_fields2_to(new osnl(){public void on_newline(String line) throws Throwable{
				dc.pl(line);
			}},new byte[]{'\n'},new byte[]{'\n','\n'},"                ");
			dc.pl("");
		}else{
			thdwatch.print_fields_to(new osnl(){public void on_newline(String line) throws Throwable{
				dc.pl(line);
			}},"\n");
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
	public void run(){try{
			b.b.server_port="8888";
			b.b.root_dir="/sdcard/htp/";
			b.b.cache_files=true;
			b.b.cache_uris=true;
			b.b.thd_watch=false;
			b.b.thread_pool_size=4;
			b.b.main(new String[]{});
	}catch(final Throwable e){throw new Error(e);}}
}
