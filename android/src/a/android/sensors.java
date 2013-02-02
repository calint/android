package a.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import b.a;
import b.bin;
import b.xwriter;
import c.a.activity;

public class sensors extends a implements bin{
	static final long serialVersionUID=1L;
	public String contenttype(){return "text/plain";}
	public void to(final xwriter x)throws Throwable{
		x.pl("getting sensor manager");
		final SensorManager sm=(SensorManager)activity.inst.getSystemService(Context.SENSOR_SERVICE);
		x.pl("getting sensor");
		final Sensor s=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		x.pl("register listener");
		sm.registerListener(new SensorEventListener(){
			public void onAccuracyChanged(final Sensor s,int a){}
			public void onSensorChanged(final SensorEvent e){
				final float sx=e.values[0];
				final float sy=e.values[1];
				final float sz=e.values[2];
				x.pl(sx+"  "+sy+"  "+sz).flush();
			}
		},s,SensorManager.SENSOR_DELAY_NORMAL);
		while(true)Thread.sleep(10000);
	}
}
