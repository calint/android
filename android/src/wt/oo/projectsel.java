package wt.oo;
import htp.htp;
import htp.path;
import htp.wt;
import htp.xwriter;
public class projectsel extends oo{
	static final long serialVersionUID=1;
	public wt sel;
	@Override public void to(xwriter x) throws Throwable{
		x.nl().tago("select");
		x.attrdef(sel);
		x.attr("onchange","this._changed=true");
		x.tagoe();
		x.nl().tago("option").attr("value","").tagoe().p("::select").tagEnd("option");
		path pth=htp.path("/"+project.class.getName().replace('.','/'));
		String[] list=pth.list();
		for(String s:list){
			x.nl().tago("option").attr("value",s).tagoe().p(s).tagEnd("option");
		}
		x.nl().tagEnd("select");
		x.action_ax(this,"add","[+]");
	}
	public void ax_add(xwriter x,String[] p){
		x.x_alert("add");
	}
}
