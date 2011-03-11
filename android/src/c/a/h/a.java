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
	@Override public void run(){
		try{
			htp.server_port="8888";
			htp.root_dir="/sdcard/htp/";
			htp.cache_files=false;
			htp.thd_watch=false;
			htp.main(new String[]{});
		}catch(Throwable e){
			throw new Error(e);
		}
	}
	private static final class dot{
		int x,y,dy;
		dot pl(final Canvas canvas,final Paint paint,String line){
			canvas.drawText(line,x,y,paint);
			return nl();
		}
		dot nl(){y+=dy;return this;}
	}
	public void paint(final state state,final Canvas canvas) throws Throwable{
		canvas.drawRGB(0,0,0);
		final Paint paint=new Paint();
		paint.setColor(0xffffffff);
		paint.setAntiAlias(true);
		final dot d=new dot();
		d.y=33;
		d.dy=12;
		htp.stats_to(new osnewliner(){@Override public void on_newline(String line) throws Throwable{
			d.pl(canvas,paint,line);
		}});
		d.nl();
		thdwatch.print_fieldnames_to(new osnewliner(){@Override public void on_newline(String line) throws Throwable{
			d.pl(canvas,paint,line);
		}},"\n");
		thdwatch.print_fields_to(new osnewliner(){@Override public void on_newline(String line) throws Throwable{
			d.pl(canvas,paint,line);
		}},"\n");
		
		canvas.drawText("http://"+state.device.get_host_ip_address()+("80".equals(htp.server_port)?"":(":"+htp.server_port))+"/",0,state.scr_h-21,paint);
		canvas.drawText("io=("+thdwatch.input+","+thdwatch.output+")B   root:"+new File(htp.root_dir).getAbsolutePath(),0,state.scr_h-10,paint);
		paint.setColor(0xff000000);
	}
	
	public void update(final state state){
		if(!started){
			started=true;
			new Thread(this,this.getClass().getName()+".htp").start();
		}
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
}
