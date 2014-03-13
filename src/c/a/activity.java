package c.a;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
final public class activity extends Activity implements Runnable,device,SensorEventListener{
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
		sensors_oncreate();
		camera_oncreate();
	}
	protected void onPause(){
		super.onPause();
		System.out.println("onpause");
		thread_stop();
		sensors_onpause();
	}
	protected void onResume(){
		super.onResume();
		System.out.println("onresume");
		final Window w=getWindow();
		w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		w.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		thread_start();
		sensors_onresume();
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
	
	
	
	
	public location location(){
		final LocationManager lm=(LocationManager)c.a.activity.get().getSystemService(Context.LOCATION_SERVICE);         
		final location l=new location();
		Location ll=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(ll==null)ll=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//		if(ll==null)ll=lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if(ll==null)return null;
		l.latitude = ll.getLatitude();
		l.longitude = ll.getLongitude();
		l.accuracy_m=ll.getAccuracy();
		l.time_ms=ll.getTime();
//		l.bearing_deg=ll.getBearing();
		return l;
	}
	public c.a.device.orientation orientation(){
		final device.orientation o=new device.orientation();
		SensorManager.getInclination(o.zxy);
		return o;
	}
	
	// sensors
	private SensorManager sensors;
	private Sensor sensor_accel;
	private Sensor sensor_geomag;
	private Sensor sensor_grav;
	private Sensor sensor_gyro;
	private Sensor sensor_linacc;
	private Sensor sensor_rotvec;
	private Sensor sensor_light;
	private Sensor sensor_proxim;
	private Sensor sensor_ambient_temp;
	private Sensor sensor_pressure;
	private Sensor sensor_rel_humidity;
	private void sensors_oncreate(){
		sensors=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		final List<Sensor>deviceSensors=sensors.getSensorList(Sensor.TYPE_ALL);
//		System.out.println("sensors: "+deviceSensors);
		for(final Sensor s:deviceSensors){
			System.out.println("sensor type "+s.getType()+"  name:"+s.getName());
		}
		sensor_light=sensors.getDefaultSensor(Sensor.TYPE_LIGHT);
		sensor_geomag=sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensor_proxim=sensors.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		sensor_accel=sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensor_grav=sensors.getDefaultSensor(Sensor.TYPE_GRAVITY);
		sensor_gyro=sensors.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		sensor_linacc=sensors.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		sensor_rotvec=sensors.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		sensor_ambient_temp=sensors.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		sensor_pressure=sensors.getDefaultSensor(Sensor.TYPE_PRESSURE);
		sensor_rel_humidity=sensors.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
	}
	private void sensors_onresume(){
		if(sensor_light!=null)sensors.registerListener(this, sensor_light,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_geomag!=null)sensors.registerListener(this, sensor_geomag,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_proxim!=null)sensors.registerListener(this, sensor_proxim,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_accel!=null)sensors.registerListener(this,sensor_accel,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_grav!=null)sensors.registerListener(this,sensor_grav,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_gyro!=null)sensors.registerListener(this,sensor_gyro,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_linacc!=null)sensors.registerListener(this,sensor_linacc,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_rotvec!=null)sensors.registerListener(this,sensor_rotvec,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_ambient_temp!=null)sensors.registerListener(this,sensor_ambient_temp,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_pressure!=null)sensors.registerListener(this,sensor_pressure,SensorManager.SENSOR_DELAY_NORMAL);
		if(sensor_rel_humidity!=null)sensors.registerListener(this,sensor_rel_humidity,SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	// SensorEventListener
	private void sensors_onpause(){
		sensors.unregisterListener(this);
	}
	@Override public final void onAccuracyChanged(final Sensor sensor,final int accuracy){
		System.out.println("onaccuracychanged "+accuracy);
	}
	@Override public final void onSensorChanged(final SensorEvent ev) {
		if(ev.sensor==sensor_geomag){
			System.out.print("geomag: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		} else if(ev.sensor==sensor_proxim){
			System.out.print("proxim: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		} else if(ev.sensor==sensor_accel){
			System.out.print("accel: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		}else if(ev.sensor==sensor_grav){
			System.out.print("grav: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		}else if(ev.sensor==sensor_gyro){
			System.out.print("gyro: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		}else if(ev.sensor==sensor_linacc){
			System.out.print("linacc: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		}else if(ev.sensor==sensor_rotvec){
			System.out.print("rotvec: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		}else if(ev.sensor==sensor_ambient_temp){
			System.out.print("ambtemp: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		}else if(ev.sensor==sensor_pressure){
			System.out.print("preassure: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		}else if(ev.sensor==sensor_rel_humidity){
			System.out.print("relhumidity: ");
			for(float f:ev.values)System.out.print(f+"  ");
			System.out.println();
		}else System.out.println("unknown event "+ev);
	}
	
	// recorder
	private MediaRecorder mRecorder=null;
	public void recorder_start(final String path)throws Throwable{
		mRecorder=new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(path);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.prepare();
		mRecorder.start();
	}
	public void recorder_stop()throws Throwable{
		mRecorder.stop();
		mRecorder.release();
		mRecorder=null;
	}
	
	// camera
	private Camera camera;
	private void camera_oncreate(){
		camera=Camera.open();
		Camera.Parameters parameters=camera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		camera.setParameters(parameters);
		final SurfaceView mview=new SurfaceView(getBaseContext());
		try{camera.setPreviewDisplay(mview.getHolder());}catch(final Throwable t){
			t.printStackTrace();
		}
	}
	public void camera_takepicture(final String path)throws Throwable{
//		camera=Camera.open();
//		Camera.Parameters parameters=camera.getParameters();
//		parameters.setPictureFormat(PixelFormat.JPEG);
//		camera.setParameters(parameters);
//		final SurfaceView mview=new SurfaceView(getBaseContext());
//		camera.setPreviewDisplay(mview.getHolder());
		camera.startPreview();
		camera.takePicture(null,null,new Camera.PictureCallback(){
			public void onPictureTaken(byte[]data,Camera camera){
				camera.stopPreview();
				FileOutputStream fos;
				try {
					fos=new FileOutputStream(path);
					fos.write(data);
					fos.close();
				}catch (final Throwable e){
					e.printStackTrace();
				}
			}
		});
	}
}
