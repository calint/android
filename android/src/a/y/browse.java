package a.y;
import a.diro;
import b.b;
public final class browse extends diro{
	static final long serialVersionUID=1;
	public browse(){
		root(b.path(""));
		bits(diro.BIT_ALL);
	}
}
