package a.y;
import b.a;
import b.xwriter;
public final class costi extends a{static final long serialVersionUID=1L;
	static class num extends a{static final long serialVersionUID=1L;}
	final static class daily extends num{static final long serialVersionUID=1L;}
	final static class weekly extends num{static final long serialVersionUID=1L;}
	final static class monthly extends num{static final long serialVersionUID=1L;}
	final static class yearly extends num{static final long serialVersionUID=1L;}
	final static class cost extends num{static final long serialVersionUID=1L;
		public daily d;
		public weekly w;
		public monthly m;
		public yearly y;
		public void to(final xwriter x)throws Throwable{x.rend(d).rend(w).rend(m).rend(y);}
		public int sumyearly(){
			return d.toint()*365+w.toint()*52+m.toint()*12+y.toint();
		}
	}
	public void to(final xwriter x)throws Throwable{
		x.pre().pl("costi");
	}
}
