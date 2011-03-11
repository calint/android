package c.a.h;

import android.graphics.Canvas;
import android.graphics.Paint;

final class dc{
	private int x,y,dy;
	private Canvas canvas;
	private Paint paint;
	public dc(Canvas canvas,final Paint paint, final int y,final int dy){this.canvas=canvas;this.y=y;this.dy=dy;this.paint=paint;}
	public dc pl(final String line){
		canvas.drawText(line,x,y,paint);
		return cr();
	}
	public dc cr(){y+=dy;return this;}
}