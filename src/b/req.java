package b;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
public final class req{
	public final static req get(){return((thdreq)Thread.currentThread()).r;}
	private static Map<String,chdresp>cachef;
	private static Map<String,chdresp>cacheu;
	static void init_static(){
		if(b.cache_files)
			cachef=Collections.synchronizedMap(new LinkedHashMap<String,chdresp>(b.cache_files_hashlen));
		if(b.cache_uris)
			cacheu=Collections.synchronizedMap(new LinkedHashMap<String,chdresp>());			
	}
	final static byte[]h_http200="HTTP/1.1 200".getBytes();
	final static byte[]h_content_length="\r\nContent-Length: ".getBytes();
	final static byte[]h_last_modified="\r\nLast-Modified: ".getBytes();
	final static byte[]h_content_type="\r\nContent-Type: ".getBytes();
	final static byte[]hkp_connection_keep_alive="\r\nConnection: Keep-Alive".getBytes();
	final static byte[]ba_crlf2="\r\n\r\n".getBytes();
	private final static String axfld="$";
	private final static byte[]h_http206="HTTP/1.1 206".getBytes();
	private final static byte[]h_http304="HTTP/1.1 304".getBytes();
	private final static byte[]h_http404="HTTP/1.1 404".getBytes();
	private final static byte[]h_set_cookie="\r\nSet-Cookie: i=".getBytes();
	private final static byte[]hkp_transfer_encoding_chunked="\r\nTransfer-Encoding: chunked".getBytes();
	private final static byte[]hkp_accept_ranges_byte="\r\nAccept-Ranges: bytes".getBytes();
	private final static byte[]h_content_range="\r\nContent-Range: ".getBytes();
	private final static byte[]hk_cookie_append=";path=/;expires=Thu, 31-Dec-2020 00:00:00 GMT;".getBytes();
	private final static String hk_connection="connection";
	private final static String hk_content_length="content-length";
	private final static String hk_content_type="content-type";
	private final static String hk_cookie="cookie";
	private final static String hk_if_modified_since="if-modified-since";
	private final static String hv_keep_alive="keep-alive";
	private final static String s_bytes_="bytes ";
	private final static String s_eq="=";
	private final static String s_minus="-";
	private final static String s_range="range";
	private final static String s_slash="/";
//	private static final byte[]ba_page_header="<!doctype html><link rel=stylesheet href=/x.css><script src=/x.js></script><body onload=$l()><form onsubmit=return(false)>".getBytes();
	private static final byte[]ba_page_header="<!doctype html><link rel=stylesheet href=/x.css><script src=/x.js></script><body onload=$l()>".getBytes();
//	private static final byte[]ba_page_footer="".getBytes();
	private final static int state_method=1;
	private final static int state_uri=2;
	private final static int state_prot=3;
	private final static int state_header_name=4;
	private final static int state_header_value=5;
	private final static int state_content_read=6;
	private final static int state_transfer_file=7;
	private final static int state_transfer_buffers=8;
	private final static int state_waiting_run_page=9;
	private final static int state_waiting_run_page_content=10;
	private final static int state_run_page=11;
	private final static int state_run_page_content=12;
	private final static int state_content_upload=13;
	private final static int state_content_upload_done=14;
	private final static int state_sock=15;
	private final static String text_html_utf8="text/html;charset=utf-8";
	private final static String text_plain="text/plain";
	private final static String text_plain_utf8="text/plain;charset=utf-8";
	private int state=state_method;
	private ByteBuffer bb=ByteBuffer.allocate(b.chunk_B);
	private ByteBuffer bb_content;
	private byte[]ba;
	private int ba_len;
	private int ba_off;
	private boolean connection_keep_alive;
	private StringBuilder sb_path=new StringBuilder(256);
	private String path_s;
	private String query_s;
	private path pth;
	private StringBuilder sb_header_name=new StringBuilder(32);
	private StringBuilder sb_header_value=new StringBuilder(256);
	private Map<String,String>hdrs=new HashMap<String,String>();
	private String sesid;
	private session ses;
	private boolean sesid_set;
	private String contentType;
	private long contentLength;
	private Map<String,String>content=new HashMap<String,String>();
	private ByteBuffer[]transfer_buffers;
	private long transfer_buffers_remaining;
	private FileChannel transfer_file_channel;
	private long transfer_file_position;
	private long transfer_file_remaining;
	private boolean waiting_write;
	SelectionKey selkey;
	SocketChannel sockch;
	private path upload_path;
	private FileChannel upload_channel;
	private String upload_lastmod_s;
	private sock sck;
	public session session(){return ses;}
	public String path(){return path_s;}
	public String query(){return query_s;}
	boolean is_waiting_write(){return waiting_write;}
	void waiting_write(final boolean b){waiting_write=b;}
	boolean is_connection_keep_alive(){return connection_keep_alive;}
	boolean is_transfer(){return state==state_transfer_file||state==state_transfer_buffers;}
	boolean is_waiting_run_page(){return state==state_waiting_run_page;}
	boolean is_waiting_run_page_content(){return state==state_waiting_run_page_content;}
	void close(){try{sockch.close();}catch(final Throwable t){b.log(t);}}
	boolean is_buf_empty(){return ba_len==0;}
	private void reply(final byte[]resultcode,final byte[]lastMod,final byte[]contentType,final byte[]content)throws IOException{
		final ByteBuffer[]bb=new ByteBuffer[16];
		int bi=0;
		bb[bi++]=ByteBuffer.wrap(resultcode);
		if(sesid_set){
			bb[bi++]=ByteBuffer.wrap(h_set_cookie);
			bb[bi++]=ByteBuffer.wrap(sesid.getBytes());
			bb[bi++]=ByteBuffer.wrap(hk_cookie_append);
			sesid_set=false;
		}
		if(lastMod!=null){
			bb[bi++]=ByteBuffer.wrap(h_last_modified);
			bb[bi++]=ByteBuffer.wrap(lastMod);			
		}
		if(contentType!=null){
			bb[bi++]=ByteBuffer.wrap(h_content_type);
			bb[bi++]=ByteBuffer.wrap(contentType);			
		}
		if(content!=null){
			bb[bi++]=ByteBuffer.wrap(h_content_length);
			bb[bi++]=ByteBuffer.wrap(Long.toString(content.length).getBytes());
		}
		if(connection_keep_alive)
			bb[bi++]=ByteBuffer.wrap(hkp_connection_keep_alive);
		bb[bi++]=ByteBuffer.wrap(ba_crlf2);
		if(content!=null)
			bb[bi++]=ByteBuffer.wrap(content);
		final long n=sendpacket(bb,bi);
		thdwatch.output+=n;
		state=state_method;
	}
	private void do_after_header()throws Throwable{
//		assertaccess();
		final String ka=hdrs.get(hk_connection);
		if(ka!=null)
			connection_keep_alive=hv_keep_alive.equalsIgnoreCase(ka);
		content.clear();
		final String contentLength_s=hdrs.get(hk_content_length);
		if(contentLength_s!=null){
			if(!decodecookie())
				throw new Error("nocookie");
			contentLength=Long.parseLong(contentLength_s);
			contentType=hdrs.get(hk_content_type);
			if(contentType.startsWith("file;")){
				if(!b.enable_upload)throw new Error("uploadsdisabled");
				final String[]q=contentType.split(";");
				upload_lastmod_s=q.length>1?q[1]:new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss.SSS",Locale.US).format(new Date());
//				final String range=q[3];
//				final String md5=q[1];
//				final String datecrt=q[4];
//				final String size=q[2];
//				final String name=q[6];
				upload_path=b.path(b.sessions_dir).get(sesid).get(path_s);
				upload_channel=upload_path.filechannel();
				
				bb.position(ba_off);
				state=state_content_upload;
				parse_content_upload();
				return;
			}else if(contentType.startsWith("dir;")){
				if(!b.enable_upload)throw new Error("uploadsdisabled");
				final String[]q=contentType.split(";");
				final String lastmod_s=q[1];
				final path p=b.path(b.sessions_dir).get(sesid).get(path_s);
				if(!p.exists())
					p.mkdirs();
				if(!p.isdir())
					throw new Error("isnotdir: "+p);
				final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss.SSS",Locale.US);
				try{p.lastmod(df.parse(lastmod_s).getTime());}catch(final ParseException e){throw new Error(e);}
				state=state_method;
				return;
			}
			bb_content=ByteBuffer.allocate((int)contentLength);
			state=state_content_read;
			return;
		}
		pth=b.path(path_s);
		if(b.try_file&&try_file())
			return;
		if(b.try_rc&&try_rc())
			return;
		decodecookie();
		state=state_waiting_run_page;
		if(!b.cache_uris)
			return;
		if(ses==null)
			ses=session.all().get(sesid);
		if(ses==null)
			return;
		if(!sesid.equals(ses.id()))
			throw new Error("cookiechangeduringconnection");
		final a a=(a)ses.get(path_s);
		if(a==null)
			return;
		if(!(a instanceof cacheable))
			return;
		final cacheable ac=(cacheable)a;
		final String ifmodsince=hdrs.get(hk_if_modified_since);
		final String lastmod=ac.lastmod();
		if(ifmodsince!=null&&ifmodsince.equals(lastmod)){
			reply(h_http304,null,null,null);
			return;
		}
		String key=sb_path.toString();
		if(ac.cacheforeachuser())
			key=req.get().session().id()+"~"+key;
		final chdresp c=cacheu.get(key);
		if(c==null)
			return;
		if(!c.isvalid(System.currentTimeMillis()))
			return;
		reply2(c);
		thdwatch.cacheuhits++;
	}
	private void reply(final cacheable cw)throws Throwable{
		final String ifmodsince=hdrs.get(hk_if_modified_since);
		final String lastmod=cw.lastmod();
		if(ifmodsince!=null&&ifmodsince.equals(lastmod)){
			reply(h_http304,null,null,null);
			return;
		}
		final long t=System.currentTimeMillis();
		String key=sb_path.toString();
		if(cw.cacheforeachuser())
			key=req.get().session().id()+"~"+key;
		chdresp c=cacheu.get(key);
		if(c==null){
			c=new chdresp(cw,key);
			cacheu.put(key,c);
		}
		c.validate(t,ifmodsince);
		reply2(c);
	}
//	private void assertaccess(){
//		if(path_s.startsWith("/localhost")&&!sockch.socket().getRemoteSocketAddress().toString().startsWith("/0:0:0:0:0:0:0:1"))
//			throw new Error("may only be accessed from localhost");
//	}
	private boolean decodecookie(){
		sesid=hdrs.get(hk_cookie);
		if(sesid==null)
			return false;
		final String[]c1=sesid.split(";");
		for(String cc:c1){
			cc=cc.trim();
			if(cc.startsWith("i=")){
				final String[]c2=cc.split("i=");
				if(c2.length<2)
					throw new Error("invalid cookie: "+sesid);
				sesid=c2[1];
				sesid_set=false;
				return true;
			}
		}
		return false;
	}
	private void do_after_prot()throws Throwable{
		thdwatch.reqs++;
		final String s=b.urldecode(sb_path.toString());
		final int i=s.indexOf('?');
		if(i==-1){
			path_s=s;
			query_s="";
		}else{
			path_s=s.substring(0,i);
			query_s=s.substring(i+1);
		}
		hdrs.clear();
		state=state_header_name;
		return;
	}
	boolean do_transfer()throws Throwable{
		if(state==state_transfer_file){
			return do_transfer_file();
		}else if(state==state_transfer_buffers){
			return do_transfer_buffers();
		}else
			throw new Error();
	}
	private boolean do_transfer_buffers()throws Throwable{
		while(transfer_buffers_remaining!=0){
			final long c=sockch.write(transfer_buffers);
			if(c==0)
				return false;
			transfer_buffers_remaining-=c;
			thdwatch.output+=c;
		}
		state=state_method;
		return true;
	}
	private boolean do_transfer_file()throws IOException{
		//? heavy
//		final long buf_size=this.sockch.socket().getSendBufferSize();
		final int buf_size=b.transfer_file_write_size;
		while(transfer_file_remaining!=0){
			try{
				final long c=transfer_file_channel.transferTo(transfer_file_position,buf_size,sockch);
				if(c==0)
					return false;
				transfer_file_position+=c;
				transfer_file_remaining-=c;
				thdwatch.output+=c;
			}catch(final IOException e){
				//?
				//? differingmessagedependingonplatform
//				if("sendfile failed: EAGAIN (Try again)".equals(e.getMessage())){
//					thdwatch.eagain++;
//					b.log(e);
//					return false;
//				}
//				throw e;
				thdwatch.eagain++;
				return false;

			}
		}
		transfer_file_channel.close();
		state=state_method;
		return true;
	}
	private String mkcookieid(){
		final SimpleDateFormat sdf=new SimpleDateFormat("yyMMdd-hhmmss.SSS-",Locale.US);
		final StringBuilder sb=new StringBuilder(b.id).append("-").append(sdf.format(new Date()));
		final String alf="0123456789abcdef";
		for(int n=0;n<8;n++)
			sb.append(alf.charAt(b.rndint(0,alf.length())));
		return sb.toString();
	}
	int parse()throws Throwable{
		while(true){
			if(ba_len==0){
				bb.clear();
				final int c=sockch.read(bb);
				if(c==-1){
					connection_keep_alive=false;
					return 2;
				}
				if(c==0)
					return 0;
				thdwatch.input+=c;
				bb.flip();
				ba=bb.array();
				ba_off=bb.position();
				ba_len=bb.remaining();
			}
			while(ba_len>0){
				switch(state){
				case state_method:parse_method();break;
				case state_uri:parse_uri();break;
				case state_prot:parse_prot();break;
				case state_header_name:parse_header_name();break;
				case state_header_value:parse_header_value();break;
				case state_content_read:parse_content_read();break;
				case state_content_upload:parse_content_upload();break;
				default:throw new Error();
				}
				if(state==state_transfer_buffers||state==state_waiting_run_page||state==state_waiting_run_page_content||state==state_content_upload_done)
					break;
			}
			if(state==state_content_upload_done){
				state=state_method;
				if(ba_len==0)
					return 2;
				else
					continue;
			}
			if(state==state_waiting_run_page||state==state_waiting_run_page_content)
				return 1;
			if(is_transfer()){
				if(do_transfer())
					if(ba_len==0)
						return 2;
					else
						continue;
				return 1;
			}
		}
	}
	private void parse_content_upload()throws IOException{
		final long diff=contentLength-bb.remaining();
		final int c;
		if(diff<0){
			final long lim=bb.limit();
			bb.limit((int)(lim+diff));
			c=upload_channel.write(bb);
			bb.limit((int)lim);
		}else{
			c=upload_channel.write(bb);
		}
		contentLength-=c;ba_len-=c;ba_off+=c;
		if(contentLength==0){
			upload_channel.close();
			final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss.SSS",Locale.US);
			try{upload_path.lastmod(df.parse(upload_lastmod_s).getTime());}catch(final ParseException e){throw new Error(e);}
			state=state_content_upload_done;
		}
	}
	private void parse_content() throws UnsupportedEncodingException,IOException{
		final byte[]ba=bb_content.array();
		int i=0;
		String name="";
		int s=0;
		int k=0;for(final byte c:ba){
			switch(s){
			case 0:if(c=='='){name=new String(ba,i,(k-i),b.strenc);i=k+1;s=1;}break;
			case 1:if(c=='\r'){final String value=new String(ba,i,(k-i),b.strenc);content.put(name,value);i=k+1;s=0;}break;
			default:throw new Error();
			}
			k++;}
		bb_content=null;
	}
	private void parse_content_read()throws IOException{
		final int c=(int)(ba_len>contentLength?contentLength:ba_len);
		bb_content.put(ba,ba_off,c);
		contentLength-=c;ba_off+=c;ba_len-=c;
		if(contentLength==0){
			bb_content.flip();
			state=state_waiting_run_page_content;
		}
	}
	private void parse_header_name()throws Throwable{
		while(ba_len!=0){
			final byte b=ba[ba_off++];
			ba_len--;
			if(b==':'){state=state_header_value;break;
			}else if(b=='\n'){do_after_header();return;
			}else{sb_header_name.append((char)b);}
		}
	}
	private void parse_header_value(){
		while(ba_len!=0){
			final byte b=ba[ba_off++];
			ba_len--;
			if(b=='\n'){
				hdrs.put(sb_header_name.toString().trim().toLowerCase(Locale.US),sb_header_value.toString().trim());
				sb_header_name.setLength(0);
				sb_header_value.setLength(0);
				state=state_header_name;
				break;
			}
			sb_header_value.append((char)b);
		}
	}
	private void parse_method(){
		while(ba_len!=0){
			final byte b=ba[ba_off++];
			ba_len--;
			if(b==' '){state=state_uri;sb_path.setLength(0);break;}
		}
	}
	private void parse_prot()throws Throwable{
		while(ba_len!=0){
			final byte b=ba[ba_off++];
			ba_len--;
			if(b=='\n'){do_after_prot();break;}
		}
	}
	private void parse_uri(){
		while(ba_len!=0){
			final byte b=ba[ba_off++];
			ba_len--;
			if(b==' '){state=state_prot;break;}
			sb_path.append((char)b);
		}
	}
	private oschunked reply_chunked(final byte[]hdr,final String contentType,final String lastmod)throws Throwable{
		final ByteBuffer[]bb_reply=new ByteBuffer[11];
		int bbi=0;
		bb_reply[bbi++]=ByteBuffer.wrap(hdr);
		if(sesid_set){
			bb_reply[bbi++]=ByteBuffer.wrap(h_set_cookie);
			bb_reply[bbi++]=ByteBuffer.wrap(sesid.getBytes());
			bb_reply[bbi++]=ByteBuffer.wrap(hk_cookie_append);
			sesid_set=false;
		}
		if(connection_keep_alive)
			bb_reply[bbi++]=ByteBuffer.wrap(hkp_connection_keep_alive);
		if(contentType!=null){
			bb_reply[bbi++]=ByteBuffer.wrap(h_content_type);
			bb_reply[bbi++]=ByteBuffer.wrap(contentType.getBytes());
		}
		if(lastmod!=null){
			bb_reply[bbi++]=ByteBuffer.wrap(h_last_modified);
			bb_reply[bbi++]=ByteBuffer.wrap(lastmod.getBytes());			
		}
		bb_reply[bbi++]=ByteBuffer.wrap(hkp_transfer_encoding_chunked);
		bb_reply[bbi++]=ByteBuffer.wrap(ba_crlf2);
		thdwatch.output+=sendpacket(bb_reply,bbi);
		return new oschunked(this,b.chunk_B);
	}
	private void resp_page()throws Throwable{
		if(sesid!=null){
			//? sessiongetandloadracing
			ses=session.all().get(sesid);
			if(ses==null&&b.sessionfile_load){
				final path sespth=b.path(b.sessionhref(sesid)+b.sessionfile);
				if(sespth.exists()){
					try{ses=(session)sespth.readobj();}catch(final Throwable t){//? upgser
						b.log(new Error("could not read session created new "+sespth));
						ses=new session(sesid);
					}
					ses.bits(b.sessionbits(sesid));
					session.all().put(sesid,ses);
				}
			}
		}else{
			sesid=mkcookieid();
			sesid_set=true;
			ses=null;
		}
		if(ses==null){
			ses=new session(sesid);
			ses.bits(b.sessionbits(sesid));
			session.all().put(ses.id(),ses);
			thdwatch.sessions++;
		}
		ses.nreq++;
		a w=(a)ses.get(path_s);
		if(w==null){
			String cn=path_s.replace('/','.');
			while(cn.startsWith("."))
				cn=cn.substring(1);
			try{w=(a)Class.forName(b.webobjpkg+cn).newInstance();}catch(Throwable e){
			try{final String clsnm=b.webobjpkg+cn+(cn.length()==0?"":".")+b.default_package_class;
				w=(a)Class.forName(clsnm).newInstance();
			}catch(Throwable e1){
				final oschunked os=reply_chunked(h_http404,text_plain_utf8,null);
				new xwriter(os).p(path_s).nl().nl().p(b.stacktraceline(e)).nl().nl().p(b.stacktraceline(e1)).nl();
				os.finish();
				return;
			}}
			if(w instanceof sock){
				final sock s=(sock)w;
				state=state_sock;
				sck=s;
				switch(s.sockinit(hdrs,new sockio(sockch,selkey))){
				case read:selkey.interestOps(SelectionKey.OP_READ);selkey.selector().wakeup();break;
				case write:selkey.interestOps(SelectionKey.OP_WRITE);selkey.selector().wakeup();break;
				case close:sockch.close();break;
				case wait:selkey.interestOps(0);break;
				default:throw new IllegalStateException();
				}
				return;
//				new Thread(new Runnable(){public void run(){}});
			}
			ses.put(path_s,w);
		}
		if(!content.isEmpty()){
			String ax="";
			for(final Map.Entry<String,String>me:content.entrySet()){
				if(axfld.equals(me.getKey())){
					ax=me.getValue();
					continue;
				}
				final String[]pth=me.getKey().split("_");
				a ww=w;
				for(int n=1;n<pth.length;n++){
					ww=ww.chld(pth[n]);
					if(ww==null)
						throw new Error("not found: "+me.getKey());
				}
				ww.set(me.getValue());
			}
			final String[]axa=ax.split(" ");
			if(axa.length>0){
				final String[]pth=axa[0].split("_");
				for(int n=1;n<pth.length;n++)
					w=w.chld(pth[n]);
				final oschunked os=reply_chunked(h_http200,text_html_utf8,null);
				final xwriter x=new xwriter(os);
				if(w==null){
					x.xalert("widget not found:\n"+axa[0]);
					os.finish();
					return;
				}
				final String methodName="ax_"+(axa.length>1?axa[1]:"");
				final Object[]params=new Object[]{x,axa};
				final Method method;
				try{method=w.getClass().getMethod(methodName,new Class[]{xwriter.class,String[].class});
				try{method.invoke(w,params);}catch(final InvocationTargetException e){
					b.log(e);
					final String str=b.stacktraceline(e.getTargetException());
					x.xalert(str);
				}}catch(NoSuchMethodException e){
					x.xalert("method not found:\n"+w.getClass().getName()+"."+methodName+"(xwriter,String[])");
				}
				os.finish();
			}
			return;
		}
		if(b.cache_uris){
			if(w instanceof cacheable){
				final cacheable cw=(cacheable)w;
				reply(cw);
				thdwatch.cacheuhits++;
				return;
			}
		}
		final boolean isbin=w instanceof bin;
		final oschunked os=reply_chunked(h_http200,isbin?((bin)w).contenttype():text_html_utf8,null);
		if(!isbin)
			os.write(ba_page_header);
		final xwriter x=new xwriter(os);
		try{w.to(x);}catch(final Throwable t){b.log(t);x.pre().p(b.stacktrace(t));}
//		if(!isbin)
//			if(!b.page_footer_ommit)
//				os.write(ba_page_footer);
		os.finish();
	}
	private void reply2(final chdresp c) {
		final ByteBuffer bb=ByteBuffer.allocate(512+(int)c.byteBuffer().limit());
		//? bba
		bb.put(h_http200);
		bb.put(h_content_length).put(Long.toString(c.byteBuffer().limit()).getBytes());
		if(c.lastModified()!=null)
			bb.put(h_last_modified).put(c.lastModified().getBytes());
		bb.put(hkp_connection_keep_alive);
		if(sesid_set){
			for(final ByteBuffer b:new ByteBuffer[]{ByteBuffer.wrap(h_set_cookie),ByteBuffer.wrap(sesid.getBytes()),ByteBuffer.wrap(hk_cookie_append)}){
				bb.put(b);
			}
			sesid_set=false;
		}
		bb.put(req.ba_crlf2);
		bb.flip();
		set_state_transfer_buffers(new ByteBuffer[]{bb,c.byteBuffer().slice()});
	}
	void run_page()throws Throwable{
		state=state_run_page;
		resp_page();
		if(is_sock()){
			thdwatch.socks++;
			return;
		}
		if(state==state_run_page)
			state=state_method;
		thdwatch.pages++;
	}
	void run_page_content()throws Throwable{
		state=state_run_page_content;
		if(!contentType.startsWith(text_plain))
			throw new Error("only "+text_plain+" post allowed");
		parse_content();
		resp_page();
		if(state==state_run_page_content)
			state=state_method;
		thdwatch.posts++;
	}
	private void set_state_transfer_buffers(final ByteBuffer[]bba){
		long remaining=0;
		for(final ByteBuffer b:bba)
			remaining+=b.remaining();
		transfer_buffers=bba;
		transfer_buffers_remaining=remaining;
		state=state_transfer_buffers;
	}
	
	private boolean try_rc()throws Throwable{
//		final String p=pth.toString();
		final String p=pth.name();
		if(!b.resources_paths.contains(p))
			return false;		
		final String rcpth="/"+req.class.getPackage().getName()+"/"+p;
		final InputStream is=req.class.getResourceAsStream(rcpth);
		if(is==null)
			return false;
		final chdresp c=new chdresp(is);
		synchronized(cachef){cachef.put(path_s,c);}
		reply(c);
		return true;
	}
	private boolean try_cache()throws Throwable{
		chdresp cachedresp;
		boolean validated=false;
		cachedresp=cachef.get(path_s);
		if(cachedresp==null){
			if(pth.isdir())
				pth=pth.get(b.default_directory_file);
			if(!pth.exists())
				return false;
			if(pth.size()<=b.cache_files_maxsize){
				cachedresp=new chdresp(pth);
				validated=true;
				cachef.put(path_s,cachedresp);
				thdwatch._cachef++;
			}
		}
		if(cachedresp==null)
			return false;
		if(!validated&&!cachedresp.validate(System.currentTimeMillis(),null)){
			synchronized(cachef){cachef.remove(path_s);}
			thdwatch._cachef--;
			return true;
		}
		reply(cachedresp);
		return true;
	}
	private void reply(final chdresp c) throws IOException {
		thdwatch.cachefhits++;
		final String ifModSince=hdrs.get(hk_if_modified_since);
		if(ifModSince!=null&&c.ifnotmodsince(ifModSince)){
			reply(h_http304,null,null,null);
			return;
		}
		if(!sesid_set){
			set_state_transfer_buffers(new ByteBuffer[]{c.byteBuffer().slice()});
			return;
		}
		sesid_set=false;
		final ByteBuffer[]bba=new ByteBuffer[]{c.byteBuffer().slice(),ByteBuffer.wrap(h_set_cookie),ByteBuffer.wrap(sesid.getBytes()),ByteBuffer.wrap(hk_cookie_append),c.byteBuffer().slice()};
		bba[0].limit(c.hdrinsertionix());
		bba[4].position(c.hdrinsertionix());
		set_state_transfer_buffers(bba);
	}
	private boolean try_file()throws Throwable{
		if(cachef!=null)
			if(try_cache())
				return true;
		if(!pth.exists())
			return false;
		if(!pth.isfile()){
			pth=pth.get(b.default_directory_file);
			if(!pth.exists()||!pth.isfile())
				return false;
		}
		thdwatch.files++;
		final long lastmod_l=pth.lastmod();
		final String lastmod_s=b.tolastmodstr(lastmod_l);
		final String ifModSince=hdrs.get(hk_if_modified_since);
		if(ifModSince!=null&&ifModSince.equals(lastmod_s)){
			reply(h_http304,null,null,null);
			return true;
		}
		final long path_len=pth.size();
		final String range_s=hdrs.get(s_range);
		final ByteBuffer[]bb=new ByteBuffer[16];
		int i=0;
		final long range_from;
		if(range_s!=null){
			final String[]s=range_s.split(s_eq);
			final String[]ss=s[1].split(s_minus);
			range_from=Long.parseLong(ss[0]);
			bb[i++]=ByteBuffer.wrap(h_http206);
			bb[i++]=ByteBuffer.wrap(h_content_length);
			bb[i++]=ByteBuffer.wrap(Long.toString(path_len-range_from).getBytes());
			bb[i++]=ByteBuffer.wrap(h_content_range);
			bb[i++]=ByteBuffer.wrap((s_bytes_+range_from+s_minus+path_len+s_slash+path_len).getBytes());
		}else{
			range_from=0;
			bb[i++]=ByteBuffer.wrap(h_http200);
			bb[i++]=ByteBuffer.wrap(h_content_length);
			bb[i++]=ByteBuffer.wrap(Long.toString(path_len).getBytes());
		}
		bb[i++]=ByteBuffer.wrap(h_last_modified);
		bb[i++]=ByteBuffer.wrap(lastmod_s.getBytes());
		bb[i++]=ByteBuffer.wrap(hkp_accept_ranges_byte);
		if(sesid_set){
			bb[i++]=ByteBuffer.wrap(h_set_cookie);
			bb[i++]=ByteBuffer.wrap(sesid.getBytes());
			bb[i++]=ByteBuffer.wrap(hk_cookie_append);
			sesid_set=false;
		}
		if(connection_keep_alive)
			bb[i++]=ByteBuffer.wrap(hkp_connection_keep_alive);
		bb[i++]=ByteBuffer.wrap(ba_crlf2);
		final long n=sendpacket(bb,i);
		thdwatch.output+=n;
		transfer_file_channel=pth.fileinputstream().getChannel();
		transfer_file_position=range_from;
		transfer_file_remaining=path_len-range_from;
		state=state_transfer_file;
		return true;
	}
	private int sendpacket(final ByteBuffer[]bb,int n)throws IOException{
		long tosend=0;
		for(int i=0;i<n;i++)
			tosend+=bb[i].remaining();
		final long c=sockch.write(bb,0,n);
		if(c!=tosend)
			b.log(new Error("sent "+c+" of "+tosend+" bytes"));//?
		return n;
	}
	public String toString(){
		return new String(ba,ba_off,ba_len)+(bb_content==null?"":new String(bb_content.slice().array()));
	}
	public String host(){
		final String h=hdrs.get("host");
		final String[]ha=h.split(":");
		return ha[0];
	}
	public int port(){
		final String h=hdrs.get("host");
		final String[]ha=h.split(":");
		if(ha.length<2)
			return 80;
		return Integer.parseInt(ha[1]);
	}
	public final static long cachef_size(){
		if(cachef==null)
			return 0;
		long k=0;
		//? sync(cachef)
		for(final chdresp e:cachef.values()){
			k+=e.byteBuffer().capacity();
		}
		return k;
	}
	public final static long cacheu_size(){
		if(cacheu==null)
			return 0;
		//? sync(cacheu)
		long k=0;
		for(final chdresp e:cacheu.values()){
			if(e.byteBuffer()==null)
				continue;
			k+=e.byteBuffer().capacity();
		}
		return k;
	}
	boolean is_sock(){return state==state_sock;}
	sock.op sock_prop_read()throws Throwable{return sck.read();}
	sock.op sock_prop_write()throws Throwable{return sck.write();}
	public InetAddress ip(){return sockch.socket().getInetAddress();}
}
