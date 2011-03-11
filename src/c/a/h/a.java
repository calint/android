package c.a.h;

import htp.htp;
import java.io.File;
import java.net.SocketException;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import c.a.state;
import c.a.cluket;

public class a implements cluket,Runnable{
	private static final long serialVersionUID=1L;
	private boolean started;
	private String ip;
	@Override public void run(){
		try{
			htp.main(new String[]{"8082"});
		}catch(Throwable e){
			throw new Error(e);
		}
	}
	public void paint(final state state,final Canvas canvas) throws SocketException{
		canvas.drawRGB(0,0,0);
		final Paint paint=new Paint();
		paint.setColor(0xffffffff);
		paint.setAntiAlias(true);
		canvas.drawText(ip+" @ "+new File(htp.root_dir).getAbsolutePath(),0,state.scr_h-10,paint);
		paint.setColor(0xff000000);
	}
	
	public void update(final state state){
		if(!started){
			started=true;
			ip=state.device.get_host_ip_address();
			new Thread(this).start();
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
