package a;
import b.a;
import b.xwriter;
public class wrds extends a{
	static final long serialVersionUID=1;
	String[]words=new String[]{
			"",
			"hello",
			"world"
		};
	int[][]merges=new int[][]{
			{0,0},
			{0,0},
			{0,0},
			{1,2},
			{3,3},
			{4,2},
			{5,2}
		};
	public a input;
	public a output;
	public void to(xwriter x){
		x.pre();
		x.p("words:");
		for(String s:words)
			x.spc().p(s);
		x.nl().nl();
		int k=0;
		for(int[]a:merges){
			x.p(k).p(" :");
			for(int i:a)
				x.spc().p(i);
			x.p(" : ");
			p(x,k++);
			x.nl();
		}
		x.nl().inputText(input,null,this,null).nl();
		x.div("editor2").output(output).divEnd();
		x.script().xfocus(input);
		ax_(x,null);
		x.scriptEnd();
	}
	public void ax_(xwriter x,String[]a){
		p(x.xub(output),input.toint());
		x.xube();
	}
	public void p(xwriter x,int i){
		if(i==0)
			return;
		int[]m=merges[i];
		if(m[0]==0&&m[1]==0){
			final String word=words[i];
			x.p(word);
			return;
		}
		p(x,m[0]);
		x.spc();
		p(x,m[1]);
		return;
	}
}
