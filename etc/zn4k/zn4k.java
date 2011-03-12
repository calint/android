package zn4k;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JFrame;

final public class zn4k implements Runnable{
	final int width=256;
	final int height=256;
	long t;
	long dt=1000/24;
	JFrame frame;
	Random random=new Random(0);
	public static void main(String[] args){
		new zn4k();
	}
	public zn4k(){
		frame=new JFrame("zn4k");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(width,height);
		frame.setVisible(true);
		Thread thread=new Thread(this,getClass().getName());
		thread.start();
	}
	public void run(){
		Dimension dimension=frame.getSize();
		BufferedImage image=new BufferedImage(dimension.width,dimension.height,BufferedImage.TYPE_INT_ARGB);
		while(true){
			long t0=System.currentTimeMillis();
			tick();
//			tck();
			image.setRGB(0,0,width,height,ram,0,width);
			frame.getGraphics().drawImage(image,0,0,width,height,null);
			long t1=System.currentTimeMillis();
			long dt0=t1-t0;
			long sleep=dt-dt0;
			System.out.println(sleep);
			if(sleep>0)
				try{
					Thread.sleep(sleep);
				}catch(InterruptedException ignored){}
		}
	}
	int color;
	public void tick(){
		for(int n=0;n<ram.length;n++){
			ram[n]=random.nextInt();
		}
	}
	// z n c r  l x x x  i....... i....... n i a h  ....
	// e e a e  o        m        m        o n d l
	// r g l t  o        m        m        t c d f
	// o a l u  p        
	//   t   r
	//   i   n
	//   v 
	//   r
	int ip;
	int ir;
	int zncr,xopp,imm1,imm2;
	int reg1;
	int reg2;
	int rr;
	int reg_ram_read;
	int opp_write;
	int reg_inc_dec;
	int wbf;
	int gpr[]=new int[256];
	int[] rom=new int[4*1024];
	int[] ram=new int[width*height];
	
	public final static int BIT_1=1;
	public final static int BIT_2=2;
	public final static int BIT_3=4;
	public final static int BIT_4=8;
	public final static int BIT_5=16;
	public final static int BIT_6=32;
	public final static int BIT_7=64;
	public final static int BIT_8=128;
	boolean isbitset(int ir,int bit){
		return (ir&bit)!=0;
	}
	{
		rom[0]=0xffff0201;
		rom[1]=0x00ff0201;
		rom[2]=0x00000201;
		
	}
	public void tck(){
		ir=rom[ip];
		
		ip++;
		
		imm1=(int)(ir&0xff);
		ir>>=8;
		imm2=(int)(ir&0xff);
		ir>>=8;
		reg_ram_read=(int)(ir&0xff);
		ir>>=8;
		opp_write=(int)(ir&0xff);

		reg1=imm1;
		reg2=imm2;
		if(isbitset(reg_ram_read,BIT_1))
			reg1=gpr[imm1];
		if(isbitset(reg_ram_read,BIT_2))
			reg2=gpr[imm2];

		if(isbitset(reg_ram_read,BIT_3))
			reg1=ram[reg1];
		if(isbitset(reg_ram_read,BIT_4))
			reg2=ram[reg2];

		if(isbitset(reg_ram_read,BIT_5))
			reg1=~reg1;
		if(isbitset(reg_ram_read,BIT_6))
			reg2=~reg2;

		if(isbitset(reg_ram_read,BIT_7))
			reg1++;
		if(isbitset(reg_ram_read,BIT_8))
			reg2++;

		if(isbitset(opp_write,BIT_1))
			rr=reg1+reg2;
		if(isbitset(opp_write,BIT_2))
			rr>>=1;
		if(isbitset(opp_write,BIT_3))
			rr>>=32;
		if(isbitset(opp_write,BIT_4))
			rr>>=1;
			
		if(isbitset(opp_write,BIT_5))
			gpr[imm1]=(int)rr;
		if(isbitset(opp_write,BIT_6))
			gpr[imm2]=(int)rr;
		
		if(isbitset(opp_write,BIT_7))
			ram[reg1]=(int)rr;
		if(isbitset(opp_write,BIT_8))
			ram[reg2]=(int)rr;

		if(isbitset(reg_inc_dec,BIT_1))
			gpr[imm1]++;
		if(isbitset(reg_inc_dec,BIT_2))
			gpr[imm2]++;
		if(isbitset(reg_inc_dec,BIT_3))
			gpr[imm1]--;
		if(isbitset(reg_inc_dec,BIT_4))
			gpr[imm2]--;

	}
}
