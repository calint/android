package c.a;
import android.graphics.Canvas;
import android.graphics.Paint;
public final class dc{
	private int x,y,dy;
	private final Canvas canvas;
	private final Paint paint;
	dc(final Canvas canvas){
		this.canvas=canvas;
		paint=new Paint();
		paint.setColor(0xffffffff);
	}
	public dc pos(final int x,final int y){this.x=x;this.y=y;return this;}
	public dc dy(final int dy){this.dy=dy;return this;}
	public dc p(final String string){canvas.drawText(string,x,y,paint);return this;}
	public dc cr(){y+=dy;return this;}
	public dc pl(final String string){return p(string).cr();}
	public dc clr(final int r,final int g,final int b){canvas.drawRGB(r,g,b);return this;}
	public dc drwcrcl(final float x,final float y,final float r){canvas.drawCircle(x,y,r,paint);return this;}
	public dc brush(int argb,boolean antialias){paint.setColor(argb);paint.setAntiAlias(antialias);return this;}
	public dc drwtxt(final int x,final int y,final String string){canvas.drawText(string,x,y,paint);return this;}
	public dc textSize(final float px){paint.setTextSize(px);return this;}
}