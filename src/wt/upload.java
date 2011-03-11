package wt;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
public class upload extends wt{
	static final long serialVersionUID=1;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		final session s=r.session();
		final int width=512;
		final int height=512;
		x.br();
		x.tago("applet").attr("width",width).attr("height",height).attr("archive","/upload.jar").attr("code","applet.upload").tagoe().nl();
		x.tago("param").attr("name","host").attr("value",r.host()).tagoe().nl();
		x.tago("param").attr("name","port").attr("value",r.port()).tagoe().nl();
		x.tago("param").attr("name","session").attr("value",s.id()).tagoe().nl();
		x.tagEnd("applet");
	}
}