package htp;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
public final class req{
	public final static req get(){return ((thdreq)Thread.currentThread()).r;}
	final static byte[]h_http200="HTTP/1.1 200".getBytes();
	final static byte[]h_content_length="\r\nContent-Length: ".getBytes();
	final static byte[]h_last_modified="\r\nLast-Modified: ".getBytes();
	final static byte[]h_content_type="\r\nContent-Type: ".getBytes();
	final static byte[]hkp_connection_keep_alive="\r\nConnection: Keep-Alive".getBytes();
	final static byte[]ba_crlf2="\r\n\r\n".getBytes();
	private final static String axfld="_ax";
	private final static byte[]h_http206="HTTP/1.1 206".getBytes();
	private final static byte[]h_http304="HTTP/1.1 304".getBytes();
	private final static byte[]h_http404="HTTP/1.1 404".getBytes();
	private final static byte[]h_set_cookie="\r\nSet-Cookie: i=".getBytes();
	private final static byte[]hkp_transfer_encoding_chunked="\r\nTransfer-Encoding: chunked".getBytes();
	private final static byte[]h_accept_ranges_byte="\r\nAccept-Ranges: bytes".getBytes();
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
	private static final byte[]ba_wt_header="<html><head><link href=/default.css rel=stylesheet><script src=/default.js></script></head><body onload=ui.onload()><form onsubmit=return(false)>".getBytes();
	private static final byte[]ba_wt_footer="</form></body></html>".getBytes();
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
	private final static String text_html_utf8="text/html; charset=utf-8";
	private final static String text_plain="text/plain";
	private final static String text_plain_utf8="text/plain; charset=utf-8";
	private static LinkedHashMap<String,cachedresp>cachef;
	public final static long cachef_size(){
		if(cachef==null)
			return 0;
		long k=0;
		for(cachedresp e:cachef.values()){
			k+=e.byteBuffer().capacity();
		}
		return k;
	}
	
	private final static LinkedHashMap<String,cachedresp>cacheu=new LinkedHashMap<String,cachedresp>();
	public final static long cacheu_size(){
		if(cacheu==null)
			return 0;
		long k=0;
		//? sync
		for(final cachedresp e:cacheu.values()){
			if(e.byteBuffer()==null)
				continue;
			k+=e.byteBuffer().capacity();
		}
		return k;
	}
	private int state=state_method;
	private ByteBuffer bb_content;
	private byte[]buf;
	private int buf_len;
	private int buf_off;
	private ByteBuffer byteBuffer=ByteBuffer.allocate(htp.chunk_B);
	private boolean connection_keep_alive;
	private HashMap<String,String>content=new HashMap<String,String>();
	private long contentLength;
	private String contentType;
	private String cookie;
	private boolean cookie_set;
	private HashMap<String,String>hdrs=new HashMap<String,String>();;
	private String path_s;
	private path pth;
	private String query_s;
	private StringBuffer sb_header_name=new StringBuffer(32);
	private StringBuffer sb_header_value=new StringBuffer(128);
	private StringBuffer sb_uri=new StringBuffer(64);
	private session ses;
	private ByteBuffer[]transfer_buffers;
	private long transfer_buffers_remaining;
	private FileChannel transfer_file_channel;
	private FileInputStream transfer_file_is;
	private long transfer_file_position;
	private long transfer_file_remaining;
	private boolean waiting_write;
	SelectionKey selectionKey;
	SocketChannel socketChannel;
	private path upload_path;
	private FileChannel upload_channel;
	private String upload_lastmod_s;
	static void init_static(){
		if(htp.cache_files)
			cachef=new LinkedHashMap<String,cachedresp>(htp.cache_files_hashlen);
	}
	public session session(){return ses;}
	public String pathstr(){return path_s;}
	public String querystr(){return query_s;}
	boolean waiting_write(){return waiting_write;}
	void waiting_write(final boolean b){waiting_write=b;}
	boolean is_connection_keep_alive(){return connection_keep_alive;}
	boolean is_transfer(){return state==state_transfer_file||state==state_transfer_buffers;}
	boolean is_waiting_run_page(){return state==state_waiting_run_page;}
	boolean is_waiting_run_page_content(){return state==state_waiting_run_page_content;}
	void close_socketChannel(){try{socketChannel.close();}catch(Throwable t){htp.log(t);}}
	private void reply(final byte[]resultcode,final byte[]lastMod,final byte[]contentType,final byte[]content) throws IOException{
		final ByteBuffer[]bb=new ByteBuffer[16];
		int bi=0;
		bb[bi++]=ByteBuffer.wrap(resultcode);
		if(cookie_set){
			bb[bi++]=ByteBuffer.wrap(h_set_cookie);
			bb[bi++]=ByteBuffer.wrap(cookie.getBytes());
			bb[bi++]=ByteBuffer.wrap(hk_cookie_append);
			cookie_set=false;
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
			bb[bi++]=ByteBuffer.wrap(Integer.toString(content.length).getBytes());
		}
		if(connection_keep_alive){
			bb[bi++]=ByteBuffer.wrap(hkp_connection_keep_alive);
		}
		bb[bi++]=ByteBuffer.wrap(ba_crlf2);
		if(content!=null){
			bb[bi++]=ByteBuffer.wrap(content);
		}
		final long n=socketChannel.write(bb,0,bi);//? fitsinbuffer
		state=state_method;
		thdwatch.output+=n;
	}
	private void do_after_header() throws Throwable{
		final String ka=hdrs.get(hk_connection);
		if(ka!=null)
			connection_keep_alive=hv_keep_alive.equalsIgnoreCase(ka);
		pth=htp.path(path_s);
		content.clear();
		String contentLength_s=hdrs.get(hk_content_length);
		if(contentLength_s!=null){
			contentLength=Long.parseLong(contentLength_s);
			contentType=hdrs.get(hk_content_type);
			if(contentType.startsWith("file;")){
				final String[]q=contentType.split(";");
//				final String md5=q[1];
//				final String size=q[2];
//				final String range=q[3];
//				final String datecrt=q[4];
				upload_lastmod_s=q[5];
				final String pth=q[6];
				cookie=hdrs.get(hk_cookie);
				if(cookie==null){
					reply(h_http404,null,null,null);
					return;
				}
				String[]c1=cookie.split(";");
				for(String cc:c1){
					cc=cc.trim();
					if(cc.startsWith("i=")){
						c1=cc.split("i=");
						if(c1.length<2)
							throw new IllegalStateException("empty cookie");
						cookie=c1[1];
						cookie_set=false;
						break;
					}
				}
				if(htp.isempty(cookie)){
					reply(h_http404,null,null,null);
					return;
				}
				upload_path=htp.path(htp.sessions_dir+"/"+cookie+"/"+(htp.isempty(path_s)?"":(path_s+"/"))+pth);
				upload_channel=upload_path.getFileChannel();
				byteBuffer.position(buf_off);
				final int c=upload_channel.write(byteBuffer);
				contentLength-=c;
				buf_len-=c;
				state=state_content_upload;
				return;
			}
			bb_content=ByteBuffer.allocate((int)contentLength);
			state=state_content_read;
			return;
		}
		if(htp.try_file&&try_file())
			return;
		state=state_waiting_run_page;
	}
	private void do_after_prot() throws Throwable{
		thdwatch.reqs++;
		String s=htp.urldecode(sb_uri.toString());
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
	boolean do_transfer() throws Throwable{
		if(state==state_transfer_file){
			return do_transfer_file();
		}else if(state==state_transfer_buffers){
			return do_transfer_buffers();
		}else
			throw new Error();
	}
	private boolean do_transfer_buffers() throws Throwable{
		while(transfer_buffers_remaining!=0){
			final long c=socketChannel.write(transfer_buffers);
			if(c==0)
				return false;
			transfer_buffers_remaining-=c;
			thdwatch.output+=c;
		}
		state=state_method;
		return true;
	}
	private boolean do_transfer_file() throws Throwable{
		//			long buf_size=socketChannel.socket().getSendBufferSize();
		final int buf_size=htp.transfer_file_write_size;
		while(transfer_file_remaining!=0){
			final long c=transfer_file_channel.transferTo(transfer_file_position,buf_size,socketChannel);
			if(c==0)
				return false;
			transfer_file_position+=c;
			transfer_file_remaining-=c;
			thdwatch.output+=c;
		}
		transfer_file_is.close();
		state=state_method;
		return true;
	}
	private String mkcookieid(){
		final SimpleDateFormat sdf=new SimpleDateFormat("yyMMdd-hhmmss.SSS-");
		final StringBuffer sb=new StringBuffer(htp.id).append("-").append(sdf.format(new Date()));
		final String alf="0123456789abcdef";
		for(int n=0;n<8;n++)
			sb.append(alf.charAt((int)(Math.random()*16)));
		return sb.toString();
	}
	int parse() throws Throwable{
		while(true){
			if(buf_len==0){
				byteBuffer.clear();
				final int c=socketChannel.read(byteBuffer);
				if(c==-1){
					connection_keep_alive=false;
					return 2;
				}
				if(c==0)
					return 0;
				thdwatch.input+=c;
				byteBuffer.flip();
				buf=byteBuffer.array();
				buf_off=byteBuffer.position();
				buf_len=byteBuffer.remaining();
			}
			while(buf_len>0){
				switch(state){
				case state_method:parse_method();break;
				case state_uri:parse_uri();break;
				case state_prot:parse_prot();break;
				case state_header_name:parse_header_name();break;
				case state_header_value:parse_header_value();break;
				case state_content_read:parse_content_read();break;
				case state_content_upload:parse_content_upload();break;
				}
			}
			if(state==state_content_upload_done){
				reply(h_http200,null,null,null);
				state=state_method;
				return 2;
			}
			if(state==state_waiting_run_page||state==state_waiting_run_page_content)
				return 1;
			if(is_transfer()){
				if(do_transfer())
					return 2;
				return 1;
			}
		}
	}
	private void parse_content_upload()throws IOException{
		final int wrote=upload_channel.write(byteBuffer);
		contentLength-=wrote;
		buf_len-=wrote;
		if(contentLength==0){
			upload_channel.close();
			upload_channel=null;//? notneeded
			final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss.SSS");
			try{upload_path.setLastModified(df.parse(upload_lastmod_s).getTime());}catch(ParseException e){throw new Error(e);}
			state=state_content_upload_done;			
		}
	}
	private void parse_content() throws UnsupportedEncodingException,IOException{
		final byte[]ba=bb_content.array();
		int i=0;
		int k=0;
		String name="";
		int state=0;
		for(final byte b:ba){
			switch(state){
			case 0:
				if(b=='='){
					name=new String(ba,i,(k-i),htp.strenc);
					i=k+1;
					state=1;
				}
				break;
			case 1:
				if(b=='\r'){
					final String value=new String(ba,i,(k-i),htp.strenc);
					content.put(name,value);
					i=k+1;
					state=0;
				}
				break;
			}
			k++;
		}
		bb_content=null;
	}
	private void parse_content_read() throws UnsupportedEncodingException,IOException{
		bb_content.put(buf,buf_off,buf_len);
		contentLength-=buf_len;
		buf_len=0;
		if(contentLength==0){
			bb_content.flip();
			state=state_waiting_run_page_content;
		}
	}
	private void parse_header_name() throws Throwable{
		while(buf_len!=0){
			final byte b=buf[buf_off++];
			buf_len--;
			if(b==':'){
				state=state_header_value;
				break;
			}else if(b=='\n'){
				do_after_header();
				return;
			}else{
				sb_header_name.append((char)b);
			}
		}
	}
	private void parse_header_value(){
		while(buf_len!=0){
			final byte b=buf[buf_off++];
			buf_len--;
			if(b=='\n'){
				hdrs.put(sb_header_name.toString().trim().toLowerCase(),sb_header_value.toString().trim());
				sb_header_name.setLength(0);
				sb_header_value.setLength(0);
				state=state_header_name;
				break;
			}
			sb_header_value.append((char)b);
		}
	}
	private void parse_method(){
		while(buf_len!=0){
			final byte b=buf[buf_off++];
			buf_len--;
			if(b==' '){
				state=state_uri;
				sb_uri.setLength(0);
				break;
			}
		}
	}
	private void parse_prot() throws Throwable{
		while(buf_len!=0){
			final byte b=buf[buf_off++];
			buf_len--;
			if(b=='\n'){
				do_after_prot();
				break;
			}
		}
	}
	private void parse_uri(){
		while(buf_len!=0){
			final byte b=buf[buf_off++];
			buf_len--;
			if(b==' '){
				state=state_prot;
				break;
			}
			sb_uri.append((char)b);
		}
	}
	private oschunked reply_chunked(final byte[] hdr,final String contentType,final String lastmod) throws Throwable{
		final ByteBuffer[] bb_reply=new ByteBuffer[11];
		int bbi=0;
		bb_reply[bbi++]=ByteBuffer.wrap(hdr);
		if(cookie_set){
			bb_reply[bbi++]=ByteBuffer.wrap(h_set_cookie);
			bb_reply[bbi++]=ByteBuffer.wrap(cookie.getBytes());
			bb_reply[bbi++]=ByteBuffer.wrap(hk_cookie_append);
		}
		if(connection_keep_alive){
			bb_reply[bbi++]=ByteBuffer.wrap(hkp_connection_keep_alive);
		}
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
		socketChannel.write(bb_reply,0,bbi);//? fitsinbuffer
		return new oschunked(this,htp.chunk_B);
	}
	private void resp_page() throws Throwable{
		cookie=hdrs.get(hk_cookie);
		if(cookie!=null){
			String[]c1=cookie.split(";");
			for(String cc:c1){
				cc=cc.trim();
				if(cc.startsWith("i=")){
					c1=cc.split("i=");
					if(c1.length<2)
						throw new IllegalStateException("empty cookie");
					cookie=c1[1];
					cookie_set=false;
					break;
				}
			}
			if(ses==null){
				final Map<String,session>s=session.all();
				synchronized(s){ses=s.get(cookie);}
				if(htp.sessionfile_load){
					final path sespth=htp.path(htp.sessionhref(cookie)+htp.sessionfile);
					if(sespth.exists()){
						ses=(session)sespth.readobj();
						synchronized(s){s.put(cookie,ses);}
					}
				}
			}else	if(!cookie.equals(ses.id()))
				throw new Error("cookie change");
		}else{
			cookie=mkcookieid();
			cookie_set=true;
			ses=null;
		}
		if(ses==null){
			ses=new session(cookie);
			final Map<String,session>s=session.all();
			synchronized(s){s.put(ses.id(),ses);}
			thdwatch.sessions++;
		}
		ses.nreq++;
		wt w=(wt)ses.get(path_s);//? sync
		if(w==null){
			String cn=path_s.replace('/','.');
			if(cn.startsWith("."))
				cn=cn.substring(1);
			try{
				w=(wt)Class.forName(htp.web_widgets_package+cn).newInstance();
			}catch(Throwable e){
				try{
					String clsnm=htp.web_widgets_package+cn+(cn.length()==0?"":".")+htp.default_package_class;
					w=(wt)Class.forName(clsnm).newInstance();
				}catch(Throwable e1){
					oschunked os=reply_chunked(h_http404,text_plain_utf8,null);
					new xwriter(os).p(path_s).nl().p(htp.stackTraceLine(e)).nl().p(htp.stackTraceLine(e1)).nl();
					os.finish();
					return;
				}
			}
			ses.put(path_s,w);//? sync
		}
		if(!content.isEmpty()){
			w.onPost(content);
			final String ax=content.get(axfld);
			final String[]args=ax.split(" ");
			if(args.length>0){
				final String[]pth=args[0].split("_");
				for(int n=1;n<pth.length;n++){
					w=w.childFind(pth[n]);
				}
				final oschunked os=reply_chunked(h_http200,text_html_utf8,null);
				final xwriter x=new xwriter(os);
				if(w==null){
					x.x_alert("widget not found:\n"+args[0]);
					os.finish();
					return;
				}
				w.ax(x,args);
				os.finish();
			}
			return;
		}
		if(w instanceof cacheable){
			final cacheable cw=(cacheable)w;
			final String ifmodsince=hdrs.get(hk_if_modified_since);
			final String lastmod=cw.lastMod();
			if(ifmodsince!=null&&ifmodsince.equals(lastmod)){
				reply(h_http304,null,null,null);
				return;
			}
			final long t=System.currentTimeMillis();
			String key=sb_uri.toString();
			if(cw.cacheforeachuser())
				key=req.get().session().id()+"~"+key;
			cachedresp c;
			synchronized(cacheu){c=cacheu.get(key);}
			if(c==null){
				c=new cachedresp(cw,key);
				synchronized(cacheu){cacheu.put(key,c);}
			}
			c.validate_cacheable(t,ifmodsince);
	//		reply(c.byteBuffer());
			//? transferfile
			final oschunked os=reply_chunked(h_http200,c.contentType(),c.lastModified());
			os.write(c.byteBuffer().array());
			os.finish();
			return;
		}
		final oschunked os=reply_chunked(h_http200,text_html_utf8,null);
		final boolean stream=w instanceof stream;
		if(!stream)
			os.write(ba_wt_header);
		final xwriter x=new xwriter(os);
		try{w.to(x);}catch(final Throwable t){htp.log(t);x.pre().p(htp.stackTrace(t));}
		if(!stream)
			os.write(ba_wt_footer);
		os.finish();
	}
	void run_page() throws Throwable{
		state=state_run_page;
		resp_page();
		state=state_method;
		thdwatch.pages++;
	}
	void run_page_content() throws Throwable{
		state=state_run_page_content;
		if(!contentType.startsWith(text_plain))
			throw new IllegalStateException("only "+text_plain+" post allowed");
		parse_content();
		resp_page();
		state=state_method;
		thdwatch.posts++;
	}
	private void set_buffers_for_transfer(final ByteBuffer[] bba){
		long remaining=0;
		for(final ByteBuffer b:bba)
			remaining+=b.remaining();
		transfer_buffers=bba;
		transfer_buffers_remaining=remaining;
		state=state_transfer_buffers;
	}
	private boolean try_cache() throws Throwable{
		cachedresp cachedresp;
		boolean validated=false;
		synchronized(cachef){
			cachedresp=cachef.get(path_s);
			if(cachedresp==null){
				if(pth.isDirectory())
					pth=pth.getPath(htp.default_directory_file);
				if(!pth.exists())
					return false;
				if(pth.getSize()<=htp.cache_files_maxsize){
					cachedresp=new cachedresp(pth);
					validated=true;
					cachef.put(path_s,cachedresp);
					thdwatch._cachef++;
				}
			}
		}
		if(cachedresp==null)
			return false;
		if(!validated&&!cachedresp.validate_file(System.currentTimeMillis())){
			synchronized(cachef){cachef.remove(path_s);}
			thdwatch._cachef--;
			return true;
		}
		thdwatch.cachehits++;
		final String ifModSince=hdrs.get(hk_if_modified_since);
		if(ifModSince!=null&&cachedresp.ifNotModSince(ifModSince)){
			reply(h_http304,null,null,null);
			return true;
		}
		if(!cookie_set){
			set_buffers_for_transfer(new ByteBuffer[]{cachedresp.byteBuffer().slice()});
			return true;
		}
		cookie_set=false;
		final ByteBuffer[]byteBuffers=new ByteBuffer[]{cachedresp.byteBuffer().slice(),ByteBuffer.wrap(h_set_cookie),ByteBuffer.wrap(cookie.getBytes()),ByteBuffer.wrap(hk_cookie_append),cachedresp.byteBuffer().slice()};
		byteBuffers[0].limit(cachedresp.hdrinsertionix());
		byteBuffers[4].position(cachedresp.hdrinsertionix());
		set_buffers_for_transfer(byteBuffers);
		return true;
	}
	private boolean try_file() throws Throwable{
		if(cachef!=null)
			if(try_cache())
				return true;
		if(!pth.exists())
			return false;
		thdwatch.files++;
		final long lastmod_l=pth.getLastModified();
		final String lastmod_s=htp.toLastModified(lastmod_l);
		final String ifModSince=hdrs.get(hk_if_modified_since);
		if(ifModSince!=null&&ifModSince.equals(lastmod_s)){
			reply(h_http304,null,null,null);
			return true;
		}
		final long path_len=pth.getSize();
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
		bb[i++]=ByteBuffer.wrap(h_accept_ranges_byte);
		if(cookie_set){
			bb[i++]=ByteBuffer.wrap(h_set_cookie);
			bb[i++]=ByteBuffer.wrap(cookie.getBytes());
			bb[i++]=ByteBuffer.wrap(hk_cookie_append);
		}
		if(connection_keep_alive)
			bb[i++]=ByteBuffer.wrap(hkp_connection_keep_alive);
		bb[i++]=ByteBuffer.wrap(ba_crlf2);
		final long n=socketChannel.write(bb,0,i);//?
		thdwatch.output+=n;
		transfer_file_is=pth.getFileInputStream();
		transfer_file_channel=transfer_file_is.getChannel();
		transfer_file_position=range_from;
		transfer_file_remaining=path_len-range_from;
		state=state_transfer_file;
		return true;
	}
	@Override public String toString(){
		return new String(buf,buf_off,buf_len)+(bb_content==null?"":new String(bb_content.slice().array()));
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
	boolean is_buf_empty(){return buf_len==0;}
}
