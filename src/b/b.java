package b;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TimeZone;
final public class b{
	public final static String strenc="utf-8";
	public final static String q=" ڀ ";
	public final static String a=" ํ ";
	public final static int K=1024;
	public final static int M=K*K;
	public final static long G=K*M;
	public final static long T=K*G;
	public final static long P=K*T;
	public final static String pathsep="/";
//	public final static String rc_files_zip="/htprc/files.zip";
	public static String hello="public domain server #1";
	public static String id="aaaa";
	public static String root_dir=".";
	public static String server_port="8888";
	public static boolean try_file=true;
	public static boolean try_rc=true;
	public static boolean thd_watch=true;
	public static boolean thread_pool=true;
	public static int thread_pool_size=8;
	public static long thread_pool_lftm=60*1000;
	public static boolean cache_uris=true;
	public static boolean cache_files=true;
	public static int cache_files_hashlen=K;
	public static int cache_files_maxsize=64*K;
	public static long cache_files_validate_dt=1000;
	public static int transfer_file_write_size=64*K;
	public static int io_buf_B=64*K;
	public static int chunk_B=64*K;
	public static String default_directory_file="index.html";
	public static String default_package_class="$";
	public static boolean gc_before_stats=false;
	public static int hash_size_session_values=32;
	public static int hash_size_sessions_store=4*K;
	public static String sessionfile="session.ser";
	public static boolean sessionfile_load=true;
	public static String sessions_dir="u";
	public static boolean cacheu_tofile=true;
	public static String cacheu_dir="/cache/";
	public static final String webobjpkg="a.";
	public static String datetimefmtstr="yyyy-MM-dd HH:mm:ss.sss";
	public static long resources_lastmod=0;
	public static Set<String>resources_paths=new HashSet<String>(Arrays.asList("x.js","x.css","upload.jar"));
	public static boolean enable_upload=true;
	public static boolean enable_ssl=false;
	public static boolean enable_cluster=false;
	public static long timeatload=System.currentTimeMillis();
	public static String timeatloadstrhtp=tolastmodstr(timeatload);
	private final static LinkedList<req>pending_req=new LinkedList<req>();
	public static PrintStream out=System.out;
	public static void class_printopts(final Class<?>cls)throws IllegalArgumentException,IllegalAccessException{
		for(final Field f:cls.getFields()){
			final Object o=f.get(null);
			out.print(f.getName());
			out.print("=");
			out.print(f.getType().getName());
			out.print("(");
			out.print(o==null?"":o.toString().replaceAll("\\n","\\\\n"));
			out.println(")");
		}
	}
	public static boolean class_init(final Class<?>cls,final String[]args)throws SecurityException,NoSuchFieldException,IllegalArgumentException,IllegalAccessException{
		if(args==null||args.length==0)
			return true;
		if("-1".equals(args[0])){
			class_printopts(cls);
			return false;
		}
		for(int i=0;i<args.length;i+=2){
			final String fldnm=args[i];
			final Field fld=cls.getField(fldnm);
			final String val=args[i+1];
			final Class<?>fldcls=fld.getType();
			if(fldcls.isAssignableFrom(String.class))
				fld.set(null,val);
			else if(fldcls.isAssignableFrom(int.class))
				fld.set(null,Integer.parseInt(val));
			else if(fldcls.isAssignableFrom(boolean.class))
				fld.set(null,"1".equals(val)||"true".equals(val)||"yes".equals(val)?Boolean.TRUE:Boolean.FALSE);
			else if(fldcls.isAssignableFrom(long.class))
				fld.set(null,Long.parseLong(val));
		}
		return true;
	}
	public static void main(final String[]args)throws Throwable{
		if(!class_init(b.class,args))
			return;
//		final File rootdir=new File(root_dir);
//		if(!rootdir.exists()){
//			if(!rootdir.mkdirs())
//				throw new Error("cannot mkdir "+rootdir);
//			final ZipInputStream zis=new ZipInputStream(htp.class.getResourceAsStream(rc_files_zip));
//			for(ZipEntry ze=zis.getNextEntry();ze!=null;ze=zis.getNextEntry()){
//				if(ze.isDirectory()) {
//					new File(rootdir,ze.getName()).mkdirs();
//					continue;
//				}
//				final File file=new File(rootdir,ze.getName());
//				final BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
//				htp.cp(zis,bos);
//				bos.flush();
//				bos.close();
//				zis.closeEntry();
//			}
//			zis.close();
//		}
		resources_lastmod=System.currentTimeMillis();
		final ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		final InetSocketAddress inetSocketAddress=new InetSocketAddress(Integer.parseInt(server_port));
		final ServerSocket serverSocket=serverSocketChannel.socket();
		serverSocket.bind(inetSocketAddress);
		req.init_static();
		if(thd_watch)
			new thdwatch().start();
		final Selector selector=Selector.open();
		serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
		Runtime.getRuntime().addShutdownHook(new jvmsdh());
		while(true){try{
			selector.select();
			thdwatch.select++;
			final Iterator<SelectionKey>iterator=selector.selectedKeys().iterator();
			while(iterator.hasNext()){
				thdwatch.ioevents++;
				final SelectionKey sk=iterator.next();
				iterator.remove();
				if(sk.isAcceptable()){
					thdwatch.iocon++;
					final req r=new req();
					r.sockch=serverSocketChannel.accept();
					r.sockch.configureBlocking(false);
					r.selkey=r.sockch.register(selector,SelectionKey.OP_READ,r);
					continue;
				}
				sk.interestOps(0);
				final req r=(req)sk.attachment();
				if(sk.isReadable()){
					thdwatch.ioread++;
					read(sk,r);
					continue;
				}
				if(sk.isWritable()){
					thdwatch.iowrite++;
					if(r.is_sock()){
						switch(r.sock_prop_write()){
						case read:sk.interestOps(SelectionKey.OP_READ);break;
						case write:sk.interestOps(SelectionKey.OP_WRITE);break;
						case close:r.close();break;
						default:throw new Error();
						}
						continue;
					}
					if(r.is_waiting_write()){
						synchronized(r){r.notify();}
						continue;
					}
					if(r.is_transfer()){
						if(!r.do_transfer()){
							sk.interestOps(SelectionKey.OP_WRITE);
							continue;
						}
						if(!r.is_connection_keep_alive()){
							r.close();
							continue;
						}
						if(r.is_buf_empty()){
							sk.interestOps(SelectionKey.OP_READ);
							continue;
						}
						read(sk,r);
						continue;
					}
					if(r.is_waiting_run_page()||r.is_waiting_run_page_content()){
						if(!b.thread_pool||thdreq.all.size()<thread_pool_size){
							new thdreq(r);
							continue;
						}
						synchronized(pending_req){
							pending_req.addLast(r);
							pending_req.notify();
						}
					}
				}
			}}catch(final Throwable e){
				log(e);
			}
		}
	}
	private static void read(final SelectionKey sk,final req r)throws Throwable{
		if(r.is_sock()){
			switch(r.sock_prop_read()){
			case read:sk.interestOps(SelectionKey.OP_READ);return;
			case write:sk.interestOps(SelectionKey.OP_WRITE);return;
			case close:r.close();return;
			case wait:sk.interestOps(0);return;
			case cont:return;
			default:throw new Error();
			}
		}
		while(true)switch(r.parse()){
		case 0:sk.interestOps(SelectionKey.OP_READ);return;
		case 1:sk.interestOps(SelectionKey.OP_WRITE);return;
		case 2:
			if(r.is_connection_keep_alive()){
				if(r.is_buf_empty()){
					sk.interestOps(SelectionKey.OP_READ);
					return;
				}
			}else{
				r.close();
				return;
			}
		}
	}
	public static int cp(final InputStream in,final OutputStream out) throws IOException{
		int n=0;
		final byte[]buf=new byte[io_buf_B];
		while(true){
			final int count=in.read(buf);
			if(count<=0)
				break;
			out.write(buf,0,count);
			n+=count;
		}
		return n;
	}
	public static int cp(final Reader in,final Writer out)throws IOException{
		int n=0;
		final char[]buf=new char[io_buf_B];
		while(true){
			final int count=in.read(buf);
			if(count<=0)
				break;
			out.write(buf,0,count);
			n+=count;
		}
		return n;
	}
	public static synchronized void log(final Throwable t){
		Throwable e=t;
		if(t instanceof InvocationTargetException)
			e=((InvocationTargetException)t).getCause();
		while(e.getCause()!=null)
			e=e.getCause();
		if(e instanceof java.nio.channels.CancelledKeyException)
			return;
		if(e instanceof java.nio.channels.ClosedChannelException)
			return;
		if(e instanceof java.io.IOException){
			if("Broken pipe".equals(e.getMessage()))
				return;
			if("Connection reset by peer".equals(e.getMessage()))
				return;
			if("An existing connection was forcibly closed by the remote host".equals(e.getMessage()))
				return;
		}
		b.out.println();
		b.out.println(b.stacktraceline(e));
	}
	public static path path(){return new path(new File(root_dir));}
	public static path path(final String path){
		if(path.indexOf("..")!=-1)
			throw new Error("illegal path "+path);
		return new path(new File(root_dir,path));
	}
	static LinkedList<req>pending_req(){return pending_req;}
	public static void stats_to(final OutputStream out)throws Throwable{
		final PrintStream ps=new PrintStream(out);
		ps.println(hello);
		ps.println("             time: "+tolastmodstr(System.currentTimeMillis()));
		ps.println("             port: "+server_port);
		ps.println("          threads: "+thdreq.all.size());
		ps.println("            input: "+(thdwatch.input>>10)+" KB");
		ps.println("           output: "+(thdwatch.output>>10)+" KB");
		ps.println("        downloads: "+new File(root_dir).getCanonicalPath());
		ps.println("     sessions dir: "+new File(root_dir,sessions_dir).getCanonicalPath());
		ps.println("         sessions: "+session.all().size());
		ps.println("     cached files: "+(req.cachef_size()>>10)+" KB");
		ps.println("      cached uris: "+(req.cacheu_size()>>10)+" KB");
		ps.println("        classpath: "+System.getProperty("java.class.path"));
		final Runtime rt=Runtime.getRuntime();
		if(gc_before_stats)
			rt.gc();
		final long m1=rt.totalMemory();
		final long m2=rt.freeMemory();
		ps.println("         ram used: "+((m1-m2)>>10)+" KB");
		ps.println("         ram free: "+(m2>>10)+" KB");
	}
	public static int rndint(final int from,final int tonotincl){return (int)(Math.random()*(tonotincl-from)+from);}
	public static String stacktrace(final Throwable e){
//		if(e==null)
//			return null;
		final StringWriter sw=new StringWriter();
		final PrintWriter out=new PrintWriter(sw);
		e.printStackTrace(out);
		out.close();
		return sw.toString();
	}
	public static String stacktraceline(final Throwable e){return stacktrace(e).replace('\n',' ').replace('\r',' ').replaceAll("\\s+"," ").replaceAll(" at "," @ ");}
	public static String tolastmodstr(final long t){
		final SimpleDateFormat sdf=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		final String s=sdf.format(new Date(t));
		return s;
	}
	public static String urldecode(final String s){try{return URLDecoder.decode(s,strenc);}catch(UnsupportedEncodingException e){throw new Error(e);}}
	public static String urlencode(final String s){try{return URLEncoder.encode(s,strenc);}catch(UnsupportedEncodingException e){throw new Error(e);}}
	public static String tostr(final Object object,final String def){return object==null?def:object.toString();}
	public static byte[]tobytes(final String v){try{return v.getBytes(strenc);}catch(UnsupportedEncodingException e){throw new Error(e);}}
	public static String sessionhref(final String sessionid){return sessions_dir+"/"+sessionid+"/";}
	public static boolean isempty(final String s){return s==null||s.length()==0;}
//	public static path path(final Class<?>cls){return path("/"+cls.getName().replace('.','/'));}
	public static String isempty(final String o,final String def){return isempty(o)?def:o;}
	public static Set<String>sessionsids(){return Collections.unmodifiableSet(session.all().keySet());}//?
//	public static boolean page_footer_ommit=false;
	public static long sessionbits(final String sesid){
		//? file(system){sha1(sessionid),bits}
		if(sesid.equals(""))return 2;
		return 0;
	}
}
