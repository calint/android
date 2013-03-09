package a.pczero;
import b.a;
import b.xwriter;
public class hex extends a{
	static final long serialVersionUID=1;
	public void to(final xwriter x){
		final String s=Integer.toHexString(toint());
//		if(s.length()>4)
//			s=s.substring(s.length()-4);
		x.p(x1.fld("0000",s));
	}
}
