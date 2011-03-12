package c.a;
import java.io.Serializable;
final public class state implements Serializable{
	static final long serialVersionUID=1;
	public transient device device;
	public cluket cluket;
	public final byte[] keys=new byte[128];
	public long frameno;
	public long dt_ms=1000;
	public float dt=0.001f*dt_ms;
	public float touch_x;
	public float touch_y;
	public long t;
	public int scr_w;
	public int scr_h;
}