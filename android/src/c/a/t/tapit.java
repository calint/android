package c.a.t;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import c.a.activity;

public class tapit implements activity.cluket{
	private static final long serialVersionUID=1L;
	public static float r=20.0f;
	public static float tgt_r_min=5;
	public static float tgt_r_max=20;
	public static long newtargetevery_ms_def=1000;
	public static long newtargetevery_ms=newtargetevery_ms_def;

	private LinkedList<dot> targets=new LinkedList<dot>();
	private int score;
	private int targetsleft;
	private Random random=new Random(0);
	private long target_t_ms;

	public void paint(final activity.state state,final Canvas canvas){
		canvas.drawRGB(0,0,0);

		for(dot t:targets)
			t.paint(canvas);

		final Paint paint=new Paint();
		paint.setColor(0xffffffff);
		canvas.drawText("score: "+score+"  tapits: "+targetsleft+"  rate: "+newtargetevery_ms+"  ªłþħª="+dot.alpha,0,state.scr_h-10,paint);

		paint.setColor(0xff000000);
		paint.setAntiAlias(true);
		canvas.drawCircle(state.touch_x,state.touch_y,r,paint);
	}
	
	public void update(final activity.state state){
		if((state.keys[KeyEvent.KEYCODE_A]&1)!=0){
			dot.anti_alias=!dot.anti_alias;
			if((state.keys[KeyEvent.KEYCODE_A]&2)!=0){
				state.keys[KeyEvent.KEYCODE_A]=0;				
			}
		}
		if((state.keys[KeyEvent.KEYCODE_DPAD_RIGHT]&1)!=0){
			dot.alpha+=(int)(0xf*state.dt)&0xff;
			if((state.keys[KeyEvent.KEYCODE_DPAD_RIGHT]&2)!=0){
				state.keys[KeyEvent.KEYCODE_DPAD_RIGHT]=0;				
			}
		}
		if((state.keys[KeyEvent.KEYCODE_DPAD_LEFT]&1)!=0){
			dot.alpha-=(int)(0xf*state.dt)&0xff;
			if((state.keys[KeyEvent.KEYCODE_DPAD_LEFT]&2)!=0){
				state.keys[KeyEvent.KEYCODE_DPAD_LEFT]=0;				
			}
		}
		
		target_t_ms+=state.dt_ms;
		if(target_t_ms>newtargetevery_ms){
			target_t_ms=0;
			targets.add(new dot(rnd(0,state.scr_w),rnd(0,state.scr_h),rnd(tgt_r_min,tgt_r_max),0xff000000+(int)rnd(0.0f,(float)0xffffff),state.t));
			targetsleft++;
		}

		for(Iterator<dot> i=targets.iterator();i.hasNext();){
			dot t=i.next();
			float dx=t.x-state.touch_x;
			float dy=t.y-state.touch_y;
			float dist2=dx*dx+dy*dy;
			if(dist2>(t.r*t.r)){
				continue;
			}
			i.remove();
			targetsleft--;
			score++;
		}

		newtargetevery_ms--;
		if(newtargetevery_ms<0)
			newtargetevery_ms=newtargetevery_ms_def;
	}
	private float rnd(float min,float maxexl){
		return random.nextFloat()*(maxexl-min)+min;
	}
}
