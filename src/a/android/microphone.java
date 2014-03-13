package a.android;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class microphone extends a{static final long serialVersionUID=1;public void to(final xwriter x)throws Throwable{
	final path p=req.get().session().path("recording.3gp");
	p.mkbasedir();
	c.a.activity.get().recorder_start(p.toString());
	Thread.sleep(5000);
	c.a.activity.get().recorder_stop();
	x.a(p.uri(),p.uri());
}}