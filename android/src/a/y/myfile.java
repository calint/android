package a.y;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import b.a;
import b.b;
import b.path;
import b.req;
import b.xwriter;
public class myfile extends a{
	static final long serialVersionUID=1L;
	public a size;
	public a lastmod;
	private final path i=req.get().session().path("i");
	public myfile(){refreshinfo(null);}
	private void refreshinfo(xwriter x){
		if(i.exists()){
			size.set("0x"+Long.toHexString(i.size())+" B");
			lastmod.set(b.tolastmodstr(i.lastmod()));
		}else{
			size.set("0");
			lastmod.set("deleted");
		}
		if(x==null)
			return;
		x.xu(size).xu(lastmod);
	}
	public void to(xwriter x) throws Throwable {
		x.pre().p("myfile");
		x.ax(this,"c","·clear");
		x.ax(this,"f","·format");
		x.ax(this,"d","·delete");
		x.ax(this,"l","·load");
		x.ax(this,"n","·new");
		x.ax(this,"s","·save");
		x.nl();
		x.p("    href:").spc().p(i.uri()).nl();
		x.p("    size:").spc().span(size).nl();
		x.p(" lastmod:").spc().span(lastmod).nl();
		x.inputTextArea(this,"editor2");
		x.nl();
		x.p("</body></html>");
	}
	public void ax_c(xwriter x,String[]p)throws Throwable{
		if(i.exists())
			i.rm();
//		i.mappedbb(false,0,0).force();
		refreshinfo(x);
	}
	public void ax_l(xwriter x,String[]p)throws Throwable{x.xu(read(i));x.xfocus(this);}
	public void ax_s(xwriter x,String[]p)throws Throwable{to(i);refreshinfo(x);x.xfocus(this);}
	public void ax_n(xwriter x,String[]p){x.xu(clr());x.xfocus(this);}
	public void ax_d(xwriter x,String[]p)throws FileNotFoundException,IOException{
		if(!i.rm())
			x.xalert("could not delete file");
		refreshinfo(x);
	}
	public void ax_f(xwriter x,String[]p)throws Throwable{
		final byte[]data="0122333344444444555555555555555566666666666666666666666666666666".getBytes();
//		final MappedByteBuffer bb=i.mappedbb(false,0,data.length);
//		bb.put(data);
//		bb.force();
		refreshinfo(x);
	}
}