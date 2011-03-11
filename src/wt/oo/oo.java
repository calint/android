package wt.oo;
import htp.htp;
import htp.path;
import htp.wt;
import htp.xwriter;
import java.text.SimpleDateFormat;
import java.util.Date;
public class oo extends wt{
	static final long serialVersionUID=1;
	protected int seq;
	public wt name;
	public wt created;
	public wt updated;
	public wt sts;
	public oo(){
		init();
	}
	protected void init(){
		created.setValue(new SimpleDateFormat(htp.datetimefmtstr).format(new Date()));
		updated.setValue("");
	}
	@Override public void to(xwriter x) throws Throwable{
		x.p("name").p(": ").inputText(name,"line",this,"load").action_ax(this,"load","::load").action_ax(this,"save","::save").p("  ").span(sts).br();
		x.p("created").p(": ").span(created).br();
		if(updated.toString().length()==0)
			x.p("updated").p(": ").p("n/a").br();
		else
			x.p("updated").p(": ").span(updated).br();
		x.focus(name);
	}
	public void ax_load(xwriter x,String[] p) throws Throwable{
		if(name.toString().trim().length()==0){
			sts.setValue("enter name");
			sts.x_updInnerHtml(x);
			x.x_focus(name);
			return;
		}
		path file=htp.path(getClass()).getPath(toString());
		if(!file.exists()){
			init();
			created.x_updInnerHtml(x);
			updated.x_updInnerHtml(x);
			sts.setValue("does not exist");
			sts.x_updInnerHtml(x);
			x.x_focus(name);
			return;
		}
		htp.read(this);
		sts.x_setValue(x,"loaded at "+new SimpleDateFormat(htp.datetimefmtstr).format(new Date()));
		x.x_reload();
	}
	public void ax_save(xwriter x,String[] p) throws Throwable{
		if(name.toString().trim().length()==0){
			x.x_alert("enter name");
			x.x_focus(name);
			return;
		}
		final SimpleDateFormat sdf=new SimpleDateFormat(htp.datetimefmtstr);
		final String s=sdf.format(new Date());
		updated.setValue(s);
		updated.x_updInnerHtml(x);
		sts.setValue("");
		htp.write(this);
		sts.setValue("save at "+sdf.format(new Date()));
		sts.x_updInnerHtml(x);
		x.x_alert("saved");
		//		ax_load(x,p);
	}
	@Override public final String toString(){return name.toString();}
}