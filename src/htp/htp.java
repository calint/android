package htp;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
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
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
final public class htp{
	public final static String strenc="utf-8";
	public final static String q=" ڀ ";
	public final static String a=" ํ ";
	public final static int K=1024;
	public final static int M=K*K;
	public final static long G=K*M;
	public final static long T=K*G;
	public final static long P=K*T;
	public final static String rc_files_zip="/htprc/files.zip";
	public static String root_dir=".";
	public static String server_port="8082";
	public static boolean try_file=true;
	public static boolean cache_files=true;
	public static boolean thd_watch=true;
	public static int threads_min=4;
	public static int cache_files_hashlen=K;
	public static int cache_files_maxsize=64*K;
	public static long cache_files_validate_dt=1000;
	public static int transfer_file_write_size=64*K;
	public static int chunk_B=4*K;
	public static String default_directory_file="index.html";
	public static String default_package_class="£";
	public static boolean gc_before_stats=false;
	public static int hash_size_session_values=32;
	public static int hash_size_sessions_store=4*K;
	public static String hello="public domain server #1";
	public static String id="aaaa";
	public static int io_buf_B=64*K;
	public static String sessionfile="sessionfile";
	public static boolean sessionfile_load=true;
	public static String sessions_dir="u";
	public static String sessions_store="s";
	public static long thread_lftm=60*1000;
	public static boolean cacheu=true;
	public static boolean cacheu_tofile=true;
	public static String cacheu_dir="/cache/";
	public static final String web_widgets_package="wt.";
	public static String datetimefmtstr="yyyy-MM-dd HH:mm:ss.sss";
	public static PrintStream out=System.out;
	private static LinkedList<req>pending_req=new LinkedList<req>();
	public static void main(final String[] args) throws Throwable{
		if(args!=null){
			if(args.length>0&&args[0].equals("?")){
				System.out.println("example: java htp.htp 8082\n  starts server on port 8082");
				return;
			}
			if(args.length>0)
				server_port=args[0];
		}
		final File rootdir=new File(root_dir);
		if(!rootdir.exists()){
			if(!rootdir.mkdirs())
				throw new Error("could not make dir "+rootdir);
			final ZipInputStream zis=new ZipInputStream(htp.class.getResourceAsStream(rc_files_zip));
			for(ZipEntry ze=zis.getNextEntry();ze!=null;ze=zis.getNextEntry()){
				if(ze.isDirectory()) {
					new File(rootdir,ze.getName()).mkdirs();
					continue;
				}
				final File file=new File(rootdir,ze.getName());
				final BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
				htp.cp(zis,bos);
				bos.flush();
				bos.close();
				zis.closeEntry();
			}
			zis.close();
		}
		final ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		final InetSocketAddress inetSocketAddress=new InetSocketAddress(Integer.parseInt(server_port));
		final ServerSocket serverSocket=serverSocketChannel.socket();
		serverSocket.bind(inetSocketAddress);
		req.init_static();
		session.all_load();
		if(thd_watch)
			new thdwatch().start();
		final Selector selector=Selector.open();
		serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
		Runtime.getRuntime().addShutdownHook(new jvmshutdownhook());
		while(true){
			try{
				selector.select();
				thdwatch.select++;
				final Iterator<SelectionKey>iterator=selector.selectedKeys().iterator();
				while(iterator.hasNext()){
					thdwatch.ioevents++;
					final SelectionKey selectionKey=iterator.next();
					iterator.remove();
					if(selectionKey.isAcceptable()){
						thdwatch.iocon++;
						final req r=new req();
						r.socketChannel=serverSocketChannel.accept();
						r.socketChannel.configureBlocking(false);
						r.selectionKey=r.socketChannel.register(selector,SelectionKey.OP_READ,r);
						continue;
					}
					selectionKey.interestOps(0);
					final req r=(req)selectionKey.attachment();
					if(selectionKey.isReadable()){
						thdwatch.ioread++;
						proc(selectionKey,r);
						continue;
					}
					if(selectionKey.isWritable()){
						thdwatch.iowrite++;
						if(r.waiting_write()){
							synchronized(r){r.notify();}
							continue;
						}
						if(r.is_transfer()){
							if(!r.do_transfer()){
								selectionKey.interestOps(SelectionKey.OP_WRITE);
								continue;
							}
							if(!r.is_connection_keep_alive()){
								r.close_socketChannel();
								continue;
							}
							if(r.is_buf_empty()){
								selectionKey.interestOps(SelectionKey.OP_READ);
							}else
								proc(selectionKey,r);
							continue;
						}
						if(r.is_waiting_run_page()||r.is_waiting_run_page_content()){
							synchronized(pending_req){
								if(thdwatch.freethds<threads_min)
									new thdreq(r);
								else{
									pending_req.addLast(r);
									pending_req.notify();
								}
							}
						}
					}
				}
			}catch(Throwable e){
//				e.printStackTrace();
				log(e);
			}
		}
	}
	private static void proc(final SelectionKey selectionKey,final req r) throws Throwable{
		while(true){
			switch(r.parse()){
			case 0:selectionKey.interestOps(SelectionKey.OP_READ);return;
			case 1:selectionKey.interestOps(SelectionKey.OP_WRITE);return;
			case 2:
				if(r.is_connection_keep_alive()){
					if(r.is_buf_empty()){
						selectionKey.interestOps(SelectionKey.OP_READ);
						return;
					}
				}else{
					r.close_socketChannel();
					return;
				}
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
	public static int cp(final Reader in,final Writer out) throws Throwable{
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
		htp.out.println();
		htp.out.println(htp.stackTraceLine(e));
	}
	public static path path(final String path){
		if(path.indexOf("..")!=-1)
			throw new Error("illegal path "+path);
		return new path(new File(root_dir,path));
	}
	static LinkedList<req>pending_req(){return pending_req;}
	public static void stats_to(final OutputStream out) throws Throwable{
		PrintStream ps=new PrintStream(out);
		ps.println(hello);
		ps.println("             time: "+toLastModified(System.currentTimeMillis()));
		ps.println("             port: "+server_port);
		ps.println("          threads: "+thdreq.all.size());
		ps.println("        downloads: "+new File(root_dir).getCanonicalPath());
		ps.println("     sessions dir: "+new File(root_dir,sessions_dir).getCanonicalPath());
		ps.println("   sessions store: "+new File(root_dir,sessions_store).getCanonicalPath());
		ps.println("         sessions: "+session.all().size());
		Runtime rt=Runtime.getRuntime();
		if(gc_before_stats)
			rt.gc();
		long m1=rt.totalMemory();
		long m2=rt.freeMemory();
		ps.println("         ram used: "+((m1-m2)>>10)+" KB");
		ps.println("         ram free: "+(m2>>10)+" KB");
		ps.println("     cached files: "+(req.cachef_size()>>10)+" KB");
		ps.println("      cached uris: "+(req.cacheu_size()>>10)+" KB");
	}
	public static int rndint(final int from,final int tonotincl){return (int)(Math.random()*(tonotincl-from));}
	public static String stackTrace(final Throwable e){
		if(e==null)
			return null;
		final StringWriter sw=new StringWriter();
		final PrintWriter out=new PrintWriter(sw);
		e.printStackTrace(out);
		out.close();
		return sw.toString();
	}
	public static String stackTraceLine(final Throwable e){
		return stackTrace(e).replace('\n',' ').replace('\r',' ').replaceAll("\\s+"," ").replaceAll(" at "," @ ");
	}
	public static String toLastModified(final long t){
		final SimpleDateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		final String dateStr=format.format(new Date(t));
		return dateStr;
	}
	public static String urldecode(final String s){try{return URLDecoder.decode(s,strenc);}catch(UnsupportedEncodingException e){throw new Error(e);}}
	public static String urlencode(final String s){try{return URLEncoder.encode(s,strenc);}catch(UnsupportedEncodingException e){throw new Error(e);}}
	public static String tostr(final Serializable object,final String def){return object==null?def:object.toString();}
	public static byte[]tobytes(final String v){try{return v.getBytes(strenc);}catch(UnsupportedEncodingException e){throw new Error(e);}}
	public static String sessionhref(final String sessionid){return sessions_dir+"/"+sessionid+"/";}
	public static boolean isempty(final String s){return s==null||s.length()==0;}
	public static path path(final Class<?>cls){return path("/"+cls.getName().replace('.','/'));}
	public static void write(final wt w)throws Throwable{
		final path file=htp.path(w.getClass()).getPath(w.toString());
		final OutputStream os=file.getOutputStream(false);
		for(final Field f:w.getClass().getFields()){
			if(!wt.class.isAssignableFrom(f.getType()))
				continue;
			wt ww=(wt)f.get(w);
			if(ww==null)
				continue;
			os.write(htp.tobytes(f.getName()));
			os.write(":".getBytes());
			os.write(htp.tobytes(ww.toString()));
			os.write("\n".getBytes());
		}
		os.close();
	}
	public static void read(final wt w) throws Throwable{
		final Class<? extends wt>cls=w.getClass();
		final path p=htp.path(cls).getPath(w.toString());
		final Reader re=new InputStreamReader(p.getInputStream(),htp.strenc);
		int s=0;
		final StringBuffer sbname=new StringBuffer(256);
		final StringBuffer sbvalue=new StringBuffer(256);
		Field fld=null;
		while(true){
			final int ch=re.read();
			if(ch==-1)
				break;
			switch(s){
			case 0:
				if(ch==':'){
					fld=cls.getField(sbname.toString().trim());
					sbvalue.setLength(0);
					s=1;
				}else{
					sbname.append((char)ch);
				}
				break;
			case 1:
				if(ch=='\n'){
					wt ww=(wt)fld.get(w);
					ww.setValue(sbvalue.toString().trim());
					sbname.setLength(0);
					s=0;
				}else{
					sbvalue.append((char)ch);
				}
				break;
			}
		}
		re.close();
	}
	public static wt[]list(final Class<? extends wt>cls) throws Throwable{
		final String[]ls=htp.path(cls).list();
		final wt[]wtls=new wt[ls.length];
		for(int n=0;n<ls.length;n++){
			try{wtls[n]=cls.newInstance();}catch(Throwable t){throw new Error(t);}
			wtls[n].setValue(ls[n]);
			read(wtls[n]);
		}
		return wtls;
	}
}
