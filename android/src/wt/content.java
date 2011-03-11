package wt;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
public class content extends wt{
	private static final long serialVersionUID=1L;
	public wt pathwt;
	public wt rendwt;
	@Override public void to(xwriter x) throws Throwable{
		x.inputText(pathwt,"search",this,"updsrch").focus(this.pathwt);
		session session=req.get().session();
		String[] files=session.path(pathwt.toString()).getParent().list();
		if(rendwt.toString().equals("1")){
			x.table().tr().td();
			x.ul();
			String pathwtstr=pathwt.toString();
			for(String f:files)
				x.nl().li(f.equals(pathwtstr)?"bold":"").action_ax(this,"view "+f,f);
			x.ulEnd();
			x.tdEnd().td();
			session.path(pathwtstr).to(x);
			x.tdEnd().trEnd().tableEnd();
		}else{
			String pathwtstr=pathwt.toString();
			for(String f:files)
				if(pathwtstr.equals(f))
					x.nl().p(" · ").p(f);
				else
					x.nl().p(" · ").action_ax(this,"view "+f,f);
					
			x.nl().br();
			session.path(pathwtstr).to(x);			
		}
	}
	public void ax_updsrch(xwriter x,String[] p){
		x.x_reload();
	}
	public void ax_view(xwriter x,String[] p){
		pathwt.setValue(p[2]);
		x.x_reload();
	}
}
