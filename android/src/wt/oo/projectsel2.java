package wt.oo;
import java.io.IOException;
import htp.htp;
import htp.path;
import htp.wt;
import htp.xwriter;
public class projectsel2 extends oo{
	static final long serialVersionUID=1;
	public wt about;
	public wt sel;
	@Override public void to(xwriter x) throws Throwable{
		x.nl().tago("select");
		x.attrdef(sel);
		x.attr("onchange","this._changed=true;ui.ax('"+wid()+" sel');this._changed=true;");
		x.tagoe();
		x.nl().tago("option").attr("value","").tagoe().p("::select").tagEnd("option");
		path pth=htp.path("/"+project.class.getName().replace('.','/'));
		String[] list=pth.list();
		for(String s:list){
			x.nl().tago("option").attr("value",s).tagoe().p(s).tagEnd("option");
		}
		x.nl().tagEnd("select");
		x.action_ax(this,"add","[+]");
		x.action_ax(this,"del","[-]");
		x.br().nl();
		x.span(about);
		//		about.to(x);
	}
	public void ax_sel(xwriter x,String[] p) throws Throwable{
		//		x.x_alert("sel");
		if(sel.toString().length()==0){
			about.setValue("");
		}else{
			path pth=htp.path("/"+project.class.getName().replace('.','/'));
			pth=pth.getPath(sel.toString());
			about.setFrom(pth);
		}
		about.x_updInnerHtml(x);
	}
	public void ax_add(xwriter x,String[] p){
		x.x_alert("add");
	}
	public void ax_del(xwriter x,String[] p) throws IOException{
		if(sel.toString().length()==0){
			return;
		}else{
			path pth=htp.path("/"+project.class.getName().replace('.','/'));
			pth=pth.getPath(sel.toString());
			if(pth.exists()){
				x.x_alert(pth.rm()?"deleted ok":"can not delete");
				x.x_reload();
				return;
			}
		}
	}
}
