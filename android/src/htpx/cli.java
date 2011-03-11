package htpx;
import htp.htp;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
public final class cli implements Runnable{
	public static int stats_cli_actv=1;
	private Process prc;
	private PrintStream con;
	private InputStream is;
	private Thread thread=new Thread(this,cli.class.getName()+"-"+stats_cli_actv);
	private int exitValue;
	private OutputStream os;
	private String cmd;
	private StringBuffer sb=new StringBuffer();
	public cli(String cmd,OutputStream os) throws IOException{
		this.cmd=cmd;
		this.os=os;
		prc=Runtime.getRuntime().exec(cmd);
		is=prc.getInputStream();
		con=new PrintStream(prc.getOutputStream(),true);
		thread.start();
	}
	@Override public final void run(){
		stats_cli_actv++;
		try{htp.cp(is,os);}catch(Throwable t){htp.log(t);}//?
		try{exitValue=prc.waitFor();}catch(InterruptedException ok){}
		stats_cli_actv--;
	}
	public final int getExitValue(){return exitValue;}
	public final cli wait_for_cli(){try{thread.join();}catch(InterruptedException ok){}return this;}
	public final cli p(String sh){sb.append(sh);return this;}
	public final cli nl(){con.println(sb);htp.out.print(htp.q);htp.out.println(sb);sb.setLength(0);return this;}
	public cli exit(){return p(";exit").nl().wait_for_cli();}
	@Override public String toString(){return cmd;}
}
