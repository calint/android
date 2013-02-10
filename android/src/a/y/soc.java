package a.y;
import static b.b.K;
import static b.b.M;
import java.io.OutputStream;
import b.a;
import b.xwriter;
public class soc extends a{
	static final long serialVersionUID=1;
	static final int ncores=16;
	static final int nregs=256;
	static final int nrombytes=256*K;
	static final int nrambytes=M;
	static class cpu{
		final int[]regs=new int[nregs];
		final core[]cores=new core[ncores];{for(int k=0;k<cores.length;k++)cores[k]=new core(this);}
		final int[]rom=new int[nrombytes];
		final int[]ram=new int[nrambytes];
		String prog;
		void load(final String src)throws Throwable{prog=src;}
		void run(final OutputStream os)throws Throwable{regs[0]++;os.write(prog.getBytes());}
		static class core{
			core(final cpu c){cpuregs=c.regs;cpuram=c.ram;}
			int[]cpuregs;
			int[]cpuram;
			final int[]regs=new int[nregs];
			void run(final String label){}
		}
		public String toString(){
			final xwriter x=new xwriter();
			for(final int i:regs)
				x.p(Integer.toHexString(i)).spc();
			return x.toString();
		}
	}
	final cpu c=new cpu();
	public void to(final xwriter x)throws Throwable{
		c.load("hello");
		c.run(x.outputstream());
		x.pre().pl(c.toString());
	}
}

