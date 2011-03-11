package wt;
import htp.path;
import htp.req;
import htp.stream;
import htp.wt;
import htp.xwriter;
public class uplq extends wt implements stream{
	static final long serialVersionUID=1;
	@Override public String contentType(){return "text/plain";}
	@Override public void to(final xwriter x) throws Throwable{
		final String qs=req.get().querystr();
		final String[]q=qs.split(";");
//		final String md5=q[1];
//		final String size=q[2];
//		final String range=q[3];
//		final String datecrt=q[4];
//		final String dateupd=q[5];
		final String pth=q[6];
//		x.p(md5).nl().p(size).nl().p(range).nl().p(datecrt).nl().p(dateupd).nl().p(pth).nl();
		final path p=req.get().session().path("upload").getPath(pth);
		if(!p.exists()){
			x.p("send");
			return;
		}
		x.p("have "+p.getSize());
	}
}