package wt;
import htp.htp;
import htp.path;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
public class save extends wt{
	static final long serialVersionUID=1;
	@Override public void to(xwriter x) throws Throwable{
		session s=req.get().session();
		long t0=System.currentTimeMillis();
		s.save();
		long dt=System.currentTimeMillis()-t0;
		path sp=s.path(htp.sessionfile);
		x.pre();
		x.p("sessionid:").pl(s.id());
		x.p("sessionpath:").pl(s.href());
		x.nl().p("wrote ").p(sp.getSize()).p("B ").p(sp.toString()).p(" in ").p(dt).p(" ms").nl();
		x.nl().pl("content:");
		for(String key:s.keyset()){
			x.p(key).p(":").pl(s.get(key).toString());
		}
	}
}
