package a.y;
import static b.b.K;
import java.io.OutputStream;
import b.a;
import b.xwriter;
public class b8x extends a{
	static final long serialVersionUID=1;
	static byte[]ram=new byte[64*K];
	public static class a{
		protected short adr;
		public a(final int adr){this.adr=(short)adr;}
	}
	public static class s extends a{
		protected int len;
		public s(final int adr,final int len){super(adr);this.len=len;}
		public final void to(final OutputStream os)throws Throwable{os.write(ram,adr,len);}
		public final void to(final byte[]b,int offset){System.arraycopy(ram,adr,b,offset,len);}
	}
	private boolean mono;
	public void to(final xwriter x) throws Throwable{
		x.pre().ax(this,"rnd",":: rnd ").ax(this,"zero",":: zero ").ax(this,"mono"," :: "+(mono?"":"no ")+"mono").nl();
		int r=0;
		x.p("    ");
		for(int n=0;n<16;n++){
			x.p(":").p(padr("  ",Integer.toHexString(n)));
		}
		x.pl(":");
		for(int k=0;k<32;k++){
			x.p(padr("    ",Integer.toHexString(r)));
			for(int n=0;n<16;n++){
				final int ixr=r+n;
				final String c=padr("00",Integer.toHexString(ram[ixr]));
				final String c2=c.length()>2?c.substring(c.length()-2):c;
				if(!mono)
					x.p("<span style=color:#0").p(c2).p("000>");
				x.p("·").p(c2);
			}
			x.pl(":");
			r+=16;
		}
	}
	private static String padr(final String fld,final String dat){
		final int d=fld.length()-dat.length();
		final StringBuilder sb=new StringBuilder();
		if(d>0)
			sb.append(fld.substring(dat.length()));
		sb.append(dat);
		return sb.toString();
	}
	public void ax_rnd(final xwriter x,final String[]a){
		for(int n=0;n<ram.length;n++){
			ram[n]=(byte)b.b.rndint(0,0x100);
		}
		x.xreload();
	}
	public void ax_zero(final xwriter x,final String[]a){
		for(int n=0;n<ram.length;n++){
			ram[n]=0;
		}
		x.xreload();
	}
	public void ax_mono(final xwriter x,final String[]a){mono=!mono;x.xreload();}
}
