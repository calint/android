package a.android;
import b.a;
import b.xwriter;
import c.a.device;
public class location extends a{static final long serialVersionUID=1;public void to(final xwriter x)throws Throwable{
	final device.location loc=c.a.activity.get().location();
	if(loc==null){
		x.pl("location not available");
		return;
	}
	x.p(loc.latitude).spc().p(loc.longitude).spc().p(loc.accuracy_m).nl();
}}