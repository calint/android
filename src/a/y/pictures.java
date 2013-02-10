package a.y;
import b.a;
import b.b;
import b.path;
import b.xwriter;
public class pictures extends a{
	static final long serialVersionUID=1;
	private path p=b.path("chs");
	public void path(final path p){this.p=p;}
	public path path(){return p;}
	public void to(final xwriter x)throws Throwable{
		if(p==null)return;
		if(!p.isdir())return;
		for(final String fn:p.list()){
			x.tago("img").attr("src",p.get(fn).uri()).tagoe();
		}
	}
}
