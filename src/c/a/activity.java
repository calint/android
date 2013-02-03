package c.a;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import b.path;
final public class activity extends Activity implements Runnable,device{
	public static int dbg_level=1;
	public static String cluketName="c.a.h.a";
	private state state=new state();
	private Thread thread;
	private boolean on;
	private SurfaceView surface;
	public final SurfaceView surfaceView(){return surface;}
	private static activity inst;
	final public static activity get(){return inst;}
	public activity(){
		inst=this;
		try{
			state.cluket=(cluket)Class.forName(cluketName).newInstance();
		}catch(Throwable t){
			throw new Error(t);
		}
	}
	private SurfaceView camview;
	public void camera_takepicture(final path pth)throws IOException{
//		setContentView(camview);
		final Camera cam=Camera.open();
		cam.setPreviewDisplay(camview.getHolder());
		cam.startPreview();
		cam.takePicture(null,null,new Camera.PictureCallback(){public void onPictureTaken(byte[]d,Camera c){
			System.out.println("cam jpeg data "+(d==null?0:d.length));
			try{pth.writeba(d);}catch(Throwable t){
				t.printStackTrace();
			}
			cam.stopPreview();
			cam.release();
			synchronized(cam){cam.notifyAll();}
		}});
		synchronized(cam){try{cam.wait(5000);}catch(InterruptedException e){}}		
		System.out.println("take picture done");
//		setContentView(surface);
	}
	///activity
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null)
			state=(state)savedInstanceState.getSerializable("state");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		surface=new SurfaceView(this);
		surface.getHolder().addCallback(new SurfaceHolder.Callback(){
			public void surfaceCreated(final SurfaceHolder holder){
				System.out.println("surface created");
			}
			public void surfaceChanged(final SurfaceHolder holder,final int format,final int width,final int height){
				System.out.println("surface changed");
				state.scr_w=width;
				state.scr_h=height;
			}
			public void surfaceDestroyed(final SurfaceHolder holder){
				System.out.println("surface destroyed");
			}
		});
		setContentView(surface);
		camview=new SurfaceView(this);
		camview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		camview.getHolder().addCallback(new SurfaceHolder.Callback(){
			public void surfaceCreated(final SurfaceHolder holder){
				System.out.println("camview surface created");
			}
			public void surfaceChanged(final SurfaceHolder holder,final int format,final int width,final int height){
				System.out.println("camview surface changed");
				state.scr_w=width;
				state.scr_h=height;
			}
			public void surfaceDestroyed(final SurfaceHolder holder){
				System.out.println("camview surface destroyed");
			}
		});
	}
	protected void onPause(){
		super.onPause();
		System.out.println("onpause");
		thread_stop();
	}
	protected void onResume(){
		super.onResume();
		System.out.println("onresume");
		final Window w=getWindow();
		w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		w.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		thread_start();
	}
	public void onSaveInstanceState(final Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		System.out.println("onsaveinstancestate");
		thread_stop();
		savedInstanceState.putSerializable("state",state);
	}
	public boolean dispatchKeyEvent(final KeyEvent event){
		dbg(1,event.toString());
		if(event.getRepeatCount()!=0)
			return true;
		final int keyCode=event.getKeyCode();
		if(keyCode==KeyEvent.KEYCODE_MENU){
			state.menu=!state.menu;
			return true;
		}
		final int action=event.getAction();
		if(action==KeyEvent.ACTION_DOWN){
			state.keys[keyCode]|=1;
		} else if(action==KeyEvent.ACTION_UP){
			state.keys[keyCode]|=2;
		}
		return true;
	}
	public boolean dispatchTouchEvent(final MotionEvent event){
		state.touch_x=event.getX();
		state.touch_y=event.getY();
		return true;
	}
	
	///device
	public String get_host_ip_address(){
		final WifiManager wifiManager=(WifiManager)getSystemService(WIFI_SERVICE);
		final WifiInfo wifiInfo=wifiManager.getConnectionInfo();
		final int ipAddress = wifiInfo.getIpAddress();
		final String ip=String.format(Locale.US,"%d.%d.%d.%d",(ipAddress&0xff),(ipAddress>>8&0xff),(ipAddress>>16&0xff),(ipAddress>>24&0xff));
		return ip;
	}
	public state state(){return state;}
	
	///runnable
	public void run(){
		System.out.println("run");
		long fps_t0=0;
		int fps_c=0;
		float fps=0;
		final StringBuffer status=new StringBuffer();
		final Paint paint=new Paint();
		paint.setColor(0xffffffff);
		final NumberFormat fmt=new DecimalFormat("#,###.0");
		final Paint paint_keydown=new Paint();
		paint_keydown.setColor(0xffff0000);
		state.device=this;
		while(on){
			final Canvas canvas=surface.getHolder().lockCanvas();
			if(canvas==null){
				try{Thread.sleep(1000);}catch(InterruptedException ignored){}
				continue;
			}
			final long t0=System.currentTimeMillis();
			fps_c++;
			state.frameno++;
			final dc d=new dc(canvas);
			try{state.cluket.paint(this,d);}catch(Throwable e){throw new Error(e);}
			float x=0f,y=0f;
			for(final byte b:state.keys){
				if((b&1)!=0){
					canvas.drawPoint(x,y,paint_keydown);
				}else{
					canvas.drawPoint(x,y,paint);
				}
				x+=2.0f;
			}
			try{state.cluket.update(this);}catch(Throwable e){throw new Error(e);}
			final long t1=System.currentTimeMillis();
			final long fps_dt=t1-fps_t0;
			if(fps_dt>1000){
				fps=1000.0f*fps_c/fps_dt;
				fps_c=0;
				fps_t0=t1;
			}
			final long dt=t1-t0;
			state.t+=dt;
			final long t_sleep=state.dt_ms-dt;
			status.setLength(0);
			status.append("frame#=").append(state.frameno).append("  fpswish=").append(fmt.format(1.0f/state.dt)).append("  fps=").append(fmt.format(fps)).append("  sleep=").append(t_sleep);
			canvas.drawText(status.toString(),0,11,paint);
			surface.getHolder().unlockCanvasAndPost(canvas);
			if(t_sleep>0)
				try{Thread.sleep(t_sleep);}catch(InterruptedException ignored){}
		}
	}
	public void run2(){
//		b.b.server_port="8888";
		b.b.root_dir=new File(Environment.getExternalStorageDirectory().getPath(),"htp").getPath();
//		b.b.cache_files=true;
//		b.b.cache_uris=true;
		b.b.thd_watch=false;
//		b.b.thread_pool_size=4;
		try{b.b.main(new String[]{});}catch(Throwable t){throw new Error(t);}
	}
	
	///
	private void thread_start(){
		thread=new Thread(this,cluketName);
		on=true;
		thread.start();
	}
	private void thread_stop(){
		if(thread==null)
			return;
		on=false;
		thread.interrupt();
		try{thread.join();}catch(InterruptedException ignored){}
		thread=null;
	}
	public static void dbg(final int level,final String line){
		if(dbg_level==0)
			return;
		if(dbg_level>=level)
			Log.i("dbg",line);
	}
}
