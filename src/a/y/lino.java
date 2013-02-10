package a.y;
import java.io.BufferedReader;
import b.a;
import b.b;
import b.path;
import b.xwriter;

public class lino extends a{
	static final long serialVersionUID=1L;
	private boolean links;
	public void to(final xwriter x)throws Throwable{
		x.pre().ax(this,null,"::links").nl().focus(this).hr();
		for(final String s:b.sessionsids()){
			if(!links)
				x.p(s);
			else
				x.a(b.sessionhref(s),s);
			final path p=b.path(b.sessionhref(s)+"!/lino");
			if(p.isfile()){
				final BufferedReader rd=new BufferedReader(p.reader());
				final String firstline=rd.readLine();
				rd.close();
				x.tab();
				if(firstline==null){
					x.p("·");
				}else{
					x.p(firstline.length()<130?firstline:firstline.substring(0,130)+"...");
				}
			}
			x.nl();
		}
	}
	public void ax_(final xwriter x,final String[]a)throws Throwable{
		links=!links;
		x.xreload();
	}
}
