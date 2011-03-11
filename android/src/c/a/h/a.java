package c.a.h;
import htp.htp;
import htp.thdwatch;
import htpx.osnewliner;
import java.io.File;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import c.a.cluket;
import c.a.state;
public class a implements cluket,Runnable{
	private static final long serialVersionUID=1L;
	private boolean started;
	private cluket bgcluket=new c.a.t.tapit();
//	private transient Typeface tf;
	public void paint(final state state,final Canvas canvas) throws Throwable{
		bgcluket.paint(state,canvas);
//		canvas.drawRGB(0,0,0);
		final Paint paint=new Paint();
		paint.setColor(0xffffffff);
		paint.setAntiAlias(true);
//		if(tf==null){
//			tf=state.device.getTypeface("slkscr");
//		}
		paint.setTextSize(7);
		final dc d=new dc(canvas,paint,33,12);
		d.pl("http://"+state.device.get_host_ip_address()+("80".equals(htp.server_port)?"":(":"+htp.server_port))+"/index.html");
		d.pl("@ "+new File(htp.root_dir).getAbsolutePath());
		d.pl("");
		htp.stats_to(new osnewliner(){@Override public void on_newline(String line) throws Throwable{
			d.pl(line);
		}});
		d.cr();
		thdwatch.print_fieldnames_to(new osnewliner(){@Override public void on_newline(String line) throws Throwable{
			d.pl(line);
		}},"\n");
		thdwatch.print_fields_to(new osnewliner(){@Override public void on_newline(String line) throws Throwable{
			d.pl(line);
		}},"\n");
		d.pl("");
		d.pl("io=B("+thdwatch.input+","+thdwatch.output+")");
		paint.setColor(0xff000000);
	}
	public void update(final state state)throws Throwable{
		if(!started){
			started=true;
			new Thread(this,this.getClass().getName()+".htp").start();
		}
		bgcluket.update(state);
		if((state.keys[KeyEvent.KEYCODE_A]&1)!=0){
			if((state.keys[KeyEvent.KEYCODE_A]&2)!=0){
				state.keys[KeyEvent.KEYCODE_A]=0;				
			}
		}
		if((state.keys[KeyEvent.KEYCODE_DPAD_RIGHT]&1)!=0){
			if((state.keys[KeyEvent.KEYCODE_DPAD_RIGHT]&2)!=0){
				state.keys[KeyEvent.KEYCODE_DPAD_RIGHT]=0;				
			}
		}
		if((state.keys[KeyEvent.KEYCODE_DPAD_LEFT]&1)!=0){
			if((state.keys[KeyEvent.KEYCODE_DPAD_LEFT]&2)!=0){
				state.keys[KeyEvent.KEYCODE_DPAD_LEFT]=0;				
			}
		}
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
