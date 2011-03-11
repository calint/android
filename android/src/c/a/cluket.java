package c.a;
import java.io.Serializable;
import android.graphics.Canvas;
public interface cluket extends Serializable{
	void paint(final state state,final Canvas canvas)throws Throwable;
	void update(final state state)throws Throwable;
}