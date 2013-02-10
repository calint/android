package a.y;
import static b.b.M;
import b.a;
import b.xwriter;
public class rofs extends a{static final long serialVersionUID=1;
	static class size{long l;}
	static class chksum extends size{byte[]b=new byte[16];}
	static class label{byte[]b=new byte[8];}
	static class record extends chksum{label label=new label();}
	static class dir{
		private record[]r=new record[1*M];
		
		interface f{void a(final record r);}
		void foreach(f f){
			for(int n=0;n<r.length;n++){
				final record rc=r[n];
				if(rc!=null)
					f.a(rc);
			}
		}
		record getbychksum(final byte[]cs){
			return null;
		}
	}
	public a sk;
	public a s;
	public a view;
	private dir d=new dir();
	public void to(final xwriter x) throws Throwable{
		x.pl(getClass().getName());
		x.pre().inputInt(sk).spc().inputText(s,null,null,null).spc().ax(this,null,"::wrt").nl().nl();
		x.spanh(view);
		x.script().xu(view,ls().toString()).scriptEnd();
	}
	private xwriter ls(){
		final xwriter x=new xwriter();
		d.foreach(new dir.f(){public void a(final record r){
			x.pl(new String(r.label.b).trim());
		}});
		return x;
	}
	private final static byte[]zeros=new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public void ax_(final xwriter x,final String[]a){
		x.p("//").pl(getClass().getName());
		final int i=sk.toint();
		record rc=d.r[i];
		if(rc==null)
			rc=d.r[i]=new record();
		final byte[]dst=rc.label.b;
		final byte[]src=s.toString().getBytes();
		final int o=dst.length-src.length;
		System.arraycopy(src,0,dst,0,o<0?dst.length:src.length);
		if(o>0)
			System.arraycopy(zeros,0,dst,src.length,o);
		x.xu(view,ls().toString());
	}
}