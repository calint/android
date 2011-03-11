package wt;
import htp.wt;
import htp.xwriter;
public class £ extends wt{
	static final long serialVersionUID=1;
	@Override public void to(xwriter x) throws Throwable{
		super.to(x);
		x.p(getClass().getPackage().getName());
		for(String p:pkgs()){
			x.a(p," :: "+p);
		}
	}
	protected String[]pkgs(){
		return new String[]{"chs","ix","pk","rch"};
	}
	protected int[]sizes(){
		return new int[]{1,2,3,4};		
	}
}
