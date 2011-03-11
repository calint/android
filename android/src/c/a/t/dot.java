package c.a.t;

import java.io.Serializable;
import android.graphics.Canvas;
import android.graphics.Paint;

public class dot implements Serializable{
	private static final long serialVersionUID=1L;
	public static int alpha=0xc0;
	public static boolean anti_alias=true;
	float x,y,r;
	int argb;
	long t0;

	public dot(float x,float y,float r,int argb,long t0){
		super();
		this.x=x;
		this.y=y;
		this.r=r;
		this.t0=t0;
		this.argb=argb;
	}

	public void paint(Canvas c){
		final Paint paint=new Paint();
		paint.setColor(argb);
		paint.setAntiAlias(anti_alias);
		paint.setAlpha(alpha);
		c.drawCircle(x,y,r,paint);
	}
}
