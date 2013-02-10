package a.y;
import b.a;
import b.req;
import b.session;
import b.xwriter;
public class content extends a{
	private static final long serialVersionUID=1L;
	public a pathwt;
	public a rendwt;
	public void to(xwriter x) throws Throwable{
		x.inputText(pathwt,"search",this,"updsrch").focus(this.pathwt);
		session session=req.get().session();
		String[] files=session.path(pathwt.toString()).parent().list();
		if(rendwt.toString().equals("1")){
			x.table().tr().td();
			x.ul();
			String pathwtstr=pathwt.toString();
			for(String f:files)
				x.nl().li(f.equals(pathwtstr)?"bold":"").ax(this,"view "+f,f);
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
					x.nl().p(" · ").ax(this,"view "+f,f);
					
			x.nl().br();
			session.path(pathwtstr).to(x);			
		}
	}
	public void ax_updsrch(xwriter x,String[] p){
		x.xreload();
	}
	public void ax_view(xwriter x,String[] p){
		pathwt.set(p[2]);
		x.xreload();
	}
}
