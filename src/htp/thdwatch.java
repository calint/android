package htp;
import java.io.PrintStream;
import java.lang.reflect.Field;
final class thdwatch extends Thread{
	public static long ms;
	public static long mem;
	public static long input;
	public static long output;
	public static long que;
	public static long freethds;
	public static long sessions;
	public static long select;
	public static long ioevents;
	public static long iocon;
	public static long ioread;
	public static long iowrite;
	public static long reqs;
	public static long cachehits;
	public static long files;
	public static long pages;
	public static long posts;
	public static long _cachef;
	public static long _threads;
	public static long _memfree;
	public static int _prevry=100;
	public static long _t0=System.currentTimeMillis();
	public static PrintStream _out=System.out;
	private long _t;
	static boolean _stop;
	public thdwatch(){super("watch");}
	@Override public void run(){
		final Field[] fields=getClass().getDeclaredFields();
		final String pad="       ";
		while(!_stop)
			try{
				ms=System.currentTimeMillis()-_t0;
				if(ms-_t>2000){
					_t=ms;
					_out.println("\n\n");
					htp.stats_to(_out);
					_out.println();
					for(int n=0;n<fields.length;n++){
						Field field=fields[n];
						String s=field.getName();
						if(s.startsWith("_"))
							continue;
						if(s.length()>pad.length())
							s=s.substring(0,pad.length());
						_out.print(pad.substring(0,pad.length()-s.length()));
						_out.print(s);
						_out.print(" ");
					}
					_out.println();
				}
				_threads=thdreq.all.size();
				Runtime rt=Runtime.getRuntime();
				_memfree=rt.freeMemory();
				mem=rt.totalMemory()-_memfree;
				que=htp.pending_req().size();
				for(int n=0;n<fields.length;n++){
					Field field=fields[n];
					String s=field.getName();
					if(s.startsWith("_"))
						continue;
					s=field.get(this).toString();
					if(s.length()>pad.length())
						s=s.substring(0,pad.length());
					_out.print(pad.substring(0,pad.length()-s.length()));
					_out.print(s);
					_out.print(" ");
				}
				_out.print('\r');
				sleep(_prevry);
			}catch(Throwable t){
				t.printStackTrace();
			}
	}
}
