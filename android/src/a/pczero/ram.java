package a.pczero;
import b.a;
import b.xwriter;
final public class ram extends a{
	static final long serialVersionUID=1;
	final static int width=256;
	final static int height=128;	
	final int scl=2;
	final static int size=width*height;
	private short[]ram=new short[size];
	public ram(){rst();}
	static String labelrefresh="*";
	public void rst(){x=null;for(int i=0;i<ram.length;i++)ram[i]=0;}
	public void to(final xwriter x)throws Throwable{
//		x.p("<figure>");
		x.p("<canvas id=").p(id()).p(" width="+width*scl+" height="+height*scl+"></canvas>");
//		x.p("<figcaption>").ax(this,"rfh",labelrefresh).p("</figcaption>");
//		x.p("</figure>");
//		x.script();
//		xjs(x);
//		x.scriptEnd();
	}
	public void ax_rfh(final xwriter x,final String[]a){
		final String id=id();
		x.p("var d2=$('").p(id).p("').getContext('2d');");
		int cell=0;
		final int yw=height;//? size>>12;
		final int xw=width;
		for(int y=0;y<yw;y++){
			for(int xx=0;xx<xw;xx++){
				final short argb=ram[cell++];
				final String hex=Integer.toHexString(argb);
				x.p("d2.fillStyle='#"+x1.fld("000",hex)+"';");
				x.pl("d2.fillRect("+xx*scl+","+y*scl+","+scl+","+scl+");");				
			}
		}
//		x.p("}");
	}
	public short get(final int addr){return ram[addr];}
	xwriter x;// if set updates to ram display are written as js
	public void set(int addr,int value){
		ram[addr]=(short)value;
		if(x==null)return;
		final short argb=(short)value;
		final String hex=Integer.toHexString(argb);
		final String id=id();
		x.p("{var d2=$('").p(id).p("').getContext('2d');");
		x.p("d2.fillStyle='#"+x1.fld("000",hex)+"';");
		final int yy=addr/width;
		final int xx=addr%width;
		final int scl=2;
		x.p("d2.fillRect("+xx*scl+","+yy*scl+","+scl+","+scl+");");				
		x.pl("}");
	}
}
