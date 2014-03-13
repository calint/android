package a.android;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class camera extends a{static final long serialVersionUID=1;public void to(final xwriter x)throws Throwable{
	final path p=req.get().session().path("camera.jpg");
	p.mkbasedir();
	c.a.activity.get().camera_takepicture(p.toString());
	x.a(p.uri(),p.uri());
}}