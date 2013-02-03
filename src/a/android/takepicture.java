package a.android;

import b.a;
import b.bin;
import b.req;
import b.xwriter;
import c.a.activity;

public class takepicture extends a implements bin{
	static final long serialVersionUID=1L;
	public String contenttype(){return "text/plain";}
	public void to(final xwriter x)throws Throwable{
		activity.get().camera_takepicture(req.get().session().path("cam"+System.currentTimeMillis()+".jpg"));
	}
}
