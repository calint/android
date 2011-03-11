package c.a;
import java.io.Serializable;
import android.view.KeyEvent;
final public class state implements Serializable{
	private static final long serialVersionUID=1L;
	public transient device device;
	public cluket cluket;
	public final byte[] keys=new byte[KeyEvent.MAX_KEYCODE];
	public long frameno;
	public long dt_ms=1000;
	public float dt=0.001f*dt_ms;
	public float touch_x;
	public float touch_y;
	public long t;
	public int scr_w;
	public int scr_h;
}