package a.y;

import java.lang.reflect.Field;
import b.a;
import b.xwriter;

public class gl extends a{static final long serialVersionUID=1;
	public boat boat;
	public final void to(final xwriter x)throws Throwable{x.pre();boat.rend(x,0);}
	public final void ax_(final xwriter x,String[]a){}
	public static class agl extends a{static final long serialVersionUID=1;
		public a p,a;
		public void rend(final xwriter x,final int indent)throws Throwable{
			x.spc(indent*2).p(name()).p(" {p(").rend(p).p(") a(").rend(a).pl(")}");
			for(final Field fld:getClass().getFields()){
				if(!agl.class.isAssignableFrom(fld.getType()))
					continue;
				final agl o=(agl)fld.get(this);
				o.rend(x,indent+1);
			}
		}
	}
	public static class boat extends agl{static final long serialVersionUID=1;
		public hull hull;
		public cabin cabin;
		public engine engine;
		public static class hull extends agl{static final long serialVersionUID=1;}
		public static class cabin extends agl{static final long serialVersionUID=1;}
		public static class engine extends agl{static final long serialVersionUID=1;
			public fen fen;
			public propeller propeller;
			{fen.p.set("0,0,1");propeller.p.set("0,0,-1");}
			public static class fen extends agl{static final long serialVersionUID=1;}
			public static class propeller extends agl{static final long serialVersionUID=1;}
		}
	}
}
