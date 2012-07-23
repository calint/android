package b;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
final public class thdwatch extends Thread{
	public static long ms;
	public static long mem;
	public static long input;
	public static long output;
	public static long freethds;
	public static long que;
	public static long sessions;
	public static long select;
	public static long ioevents;
	public static long iocon;
	public static long ioread;
	public static long iowrite;
	public static long reqs;
	public static long cachefhits;
	public static long cacheuhits;
	public static long files;
	public static long pages;
	public static long posts;
	public static long socks;
	public static long _cachef;
	public static long _threads;
	public static long _memfree;
	public static int _prevry=100;
	public static long _t0=System.currentTimeMillis();
	public static PrintStream _out=System.out;
	private long _t;
	static boolean _stop;
	final static Field[]_fields=thdwatch.class.getDeclaredFields();
	public thdwatch(){super("watch");}
	final static String _pad="       ";
	public void run(){
		while(!_stop)try{
			sleep(_prevry);
			if(b.thd_watch==false)
				continue;
			ms=System.currentTimeMillis()-_t0;
			if(ms-_t>2000){
				_t=ms;
				_out.println("\n\n");
				b.stats_to(_out);
				_out.println();
				print_fieldnames_to(_out,"\n");
			}
			_threads=thdreq.all.size();
			Runtime rt=Runtime.getRuntime();
			_memfree=rt.freeMemory();
			mem=rt.totalMemory()-_memfree;
			que=b.pending_req().size();
			print_fields_to(_out,"\r");
		}catch(Throwable t){t.printStackTrace();}
	}
	public static void print_fieldnames_to(final OutputStream os,final String eol)throws IOException{
		for(int n=0;n<_fields.length;n++){
			Field field=_fields[n];
			String s=field.getName();
			if(s.startsWith("_"))
				continue;
			if(s.length()>_pad.length())
				s=s.substring(0,_pad.length());
			os.write(_pad.substring(0,_pad.length()-s.length()).getBytes());
			os.write(s.getBytes());
			os.write(" ".getBytes());
		}
		os.write(eol.getBytes());
	}
	public static void print_fields_to(final OutputStream os,final String eol)throws IllegalAccessException,IOException{
		for(int n=0;n<_fields.length;n++){
			Field field=_fields[n];
			String s=field.getName();
			if(s.startsWith("_"))
				continue;
			s=field.get(null).toString();
			if(s.length()>_pad.length())
				s=s.substring(0,_pad.length());
			os.write(_pad.substring(0,_pad.length()-s.length()).getBytes());
			os.write(s.getBytes());
			os.write(" ".getBytes());
		}
		os.write(eol.getBytes());
	}
	public static void print_fields2_to(final osnl os,final byte[]ba_eol,final byte[]ba_eor,final String pad)throws IOException,IllegalArgumentException,IllegalAccessException{
		for(int n=0;n<_fields.length;n++){
			Field field=_fields[n];
			String s=field.getName();
			if(s.startsWith("_"))
				continue;
			if(s.length()>pad.length())
				s=s.substring(0,pad.length());
			os.write(pad.substring(0,pad.length()-s.length()).getBytes());
			os.write(s.getBytes());
			os.write(": ".getBytes());
			s=field.get(null).toString();
			os.write(s.getBytes());
			os.write(ba_eol);
		}
		os.write(ba_eor);		
	}
}
