package a.y.craftytrainer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

class crafty{
	public static String crafty="crafty";
	public int srchdpth=10;
	private final Process p;
	private final OutputStream os;
	private final InputStream is;
	private final Scanner sc;
	public crafty(){try{
		final ProcessBuilder pb=new ProcessBuilder(crafty);
		pb.directory(new File("."));
		pb.redirectErrorStream(true);
		p=pb.start();
		is=p.getInputStream();
		os=p.getOutputStream();
		os.write(("log off\nsd "+srchdpth+"\nanalyze\n").getBytes());
		os.flush();
		sc=new Scanner(is);
		sc.findWithinHorizon("analyze\\.White\\(1\\): ",0);
	}catch(final Throwable t){throw new Error(t);}}
	public void reset()throws IOException{
		os.write("reset 1\n".getBytes());
		os.flush();
		scantillnextinput();			
	}
	public String move(final String mv)throws Throwable{
		os.write((mv+"\n").getBytes());
		os.flush();
		sc.findWithinHorizon("->",0);
		final String ln=sc.nextLine();
		scantillnextinput();
		return ln;
	}
	public void back()throws Throwable{
		os.write("\n".getBytes());
		os.flush();
		scantillnextinput();
	}
	public void diagram(final OutputStream out)throws Throwable{
		final PrintWriter pw=new PrintWriter(out,true);
		os.write("d\n".getBytes());
		os.flush();
		sc.nextLine();
		sc.nextLine();
		for(int i=0;i<8*2+2;i++){
			final String s1=sc.nextLine();
			pw.println(s1);
		}
		scantillnextinput();
	}
	private void scantillnextinput(){
		sc.findWithinHorizon("analyze\\.(White|Black)\\(\\d+\\): ",0);
	}
}