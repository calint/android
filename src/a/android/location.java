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
	x.p(loc.latitude).spc().p(loc.longitude).nl();
	x.pl(" accuracy="+loc.accuracy_m+" m");
	x.a("https://maps.google.com/maps?q="+loc.latitude+","+loc.longitude,":: view on google maps");
//	x.pl(" bearing="+loc.bearing_deg+" degrees").nl();
}}