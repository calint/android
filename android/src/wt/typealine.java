package wt;
import htp.htp;
import htp.req;
import htp.wt;
import htp.xwriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class typealine extends wt/*implements cacheable*/{
//	@Override public boolean cacheforeachuser(){return true;}
//	@Override public String contentType(){return "text/html; charset=utf-8;";}
//	@Override public String lastMod(){return htp.toLastModified(req.get().session().path("log.txt").getLastModified());}
//	@Override public long lastModChk_ms(){return 500;}
	public static final long serialVersionUID=1;
	public static final String glyphs="ᐖᐛツ";
	private static String glyph_random(){
		int i=htp.rndint(0,glyphs.length());
		return glyphs.substring(i,i+1);
	}
	public wt b;
	public wt d;
	public wt q;
	public wt s;
	public typealine() throws IOException{
		upd();
	}
	public void ax_a(xwriter x,String[] p) throws Throwable{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd---hh:mm:ss.SSS---");
		req.get().session().path("log.txt").append(simpleDateFormat.format(new Date())+s.toString(),"\n");
		upd();
		q.x_updInnerHtml(x);
		b.x_updInnerHtml(x);
		s.x_setValue(x,"");
		s.x_focus(x);
		// keywords: look go back select take drop copy
	}
	@Override public void to(xwriter x) throws Throwable{
		x.script().p("document.title='typealine';").scriptEnd();
		x.style();
		x.p("div.box{text-align:center;border:1px dotted blue;padding:7px;}");
		x.p("input.line{width:250px;}");
		x.styleEnd();
		x.p("<center>").nl().div("box");
		x.span(q).inputText(s,"line",this,"a").p(" ");
		x.actionBgn_ax(this,"a").span(b).actionEnd_ax();
		x.focus(s);
		x.divEnd();
	}
	private void upd() throws IOException{
		long len=req.get().session().path("log.txt").getSize();
		if(len>0)
			q.setValue(" ํ "+len+" ڀ ");
		else
			q.setValue(" ڀ ");
		b.setValue(glyph_random());
	}
}
