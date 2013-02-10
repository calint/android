package a.y;
import b.a;
import b.xwriter;
public class znpcz extends a{
	static final long serialVersionUID=0;
	int[]ram=new int[1024*1024];
	int[]cod=new int[ram.length];
	int[]r=new int[256];
	int[]pcf=new int[256];
	int[]lpi=new int[256];
	int[]lpa=new int[256];
	int pc,lp,si;
	boolean fz,fn;
	public void to(final xwriter x) throws Throwable{
		x.pre().pl("pcz (pizzi)");
		x.p("   :");
		for(int xx=0;xx<16;xx++){
			x.p(lf("   ",Integer.toHexString(xx))).p(" :");
		}
		x.pl(" ");
		for(int y=0;y<16;y++){
			x.p(lf("  ",Integer.toHexString(y))).p(" :");
			for(int xx=0;xx<16;xx++){
				x.p(lf("0000",Integer.toHexString(r[y*16+xx]))).p(":");
			}
			x.pl(" ");
		}
		x.pl("").p("pcf:").p(lf("0000",Integer.toHexString(pc)));
		int ir=cod[pc];
		byte zncrxl=(byte)(ir&0xff);
		byte rxi=(byte)(ir>>8&0xff);
		byte ryi=(byte)(ir>>16&0xff);
//		byte rzi=(byte)(ir>>24&0xff);
		final byte zn=(byte)(zncrxl&(1|2));
		boolean done=false;
		switch(zn){
		case 0:break;
		case 1:
			if(!fz)
				done=true;
			break;
		case 2:
			if(fz||fn)
				done=true;
			break;
		case 3:break;
		}
		if(done){
			pc++;
			return;
		}
		if((zncrxl&12)!=0){//cr lda
			r[0xa]=ir>>8;
			pc++;
			return;
		}
		if((zncrxl&32)!=0){//loop
			lp++;
			final int n;
			if((zncrxl&16)!=0){//x imm
				n=(ir>>8)&0xffffff;
			}else{
				n=r[rxi];
			}
			lpi[lp]=n;
			pc++;
			lpa[lp]=pc;
			return;
		}
		if((zncrxl&4)!=0){//call
			pcf[si]=pc+1;
			pc=(ir>>8)&0xffffff;
			si++;
			return;
		}
		boolean canret=true;
		if((zncrxl&16)!=0){//nxt
			lpi[lp]--;
			if(lpi[lp]==0){
				lp--;
				pc++;
			}else{
				pc=lpa[lp];
				canret=false;
			}
		}
		if(canret&&(zncrxl&8)!=0){//ret
			si--;
			pc=pcf[si];
		}
		int vx=0,vy=0;
		//imm
		if((rxi&128)!=0)
			vx=rxi&7;
		if((ryi&128)!=0)
			vy=ryi&7;
		//reg
		if((rxi&64)!=0)
			vx=r[vx];
		if((ryi&64)!=0)
			vy=r[vy];
		//ram
		if((rxi&32)!=0)
			vx=ram[vx];
		if((ryi&32)!=0)
			vy=ram[vy];
		//rinc
		if((rxi&8)!=0)
			r[rxi&7]++;
		if((ryi&8)!=0)
			r[ryi&7]++;
		//neg
		if((rxi&16)!=0)
			vx=-vx;
		if((ryi&16)!=0)
			vy=-vy;
		
		int vd=vx+vy;
		fz=vd==0;
		fn=vd>0;
		
	}
	private String lf(String fld, String s){
		return fld.substring(s.length())+s;
	}
}
