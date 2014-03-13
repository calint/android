package a.android;
import b.a;
import b.xwriter;
import c.a.device;
public class orientation extends a{static final long serialVersionUID=1;public void to(final xwriter x)throws Throwable{
	final device.orientation o=c.a.activity.get().orientation();
	if(o==null){x.pl("orientation not available");return;}
	for(final float f:o.zxy)
		x.p(f).spc();
}}