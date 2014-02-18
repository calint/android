package a.rch;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import a.diro;
import a.x.xsts;
import b.a;
import b.path;
import b.req;
import b.session;
import b.xwriter;
public final class $ extends a{
	private static final long serialVersionUID=1L;
	public static String checksum_alg="md5";
	public static String inbox_dir="rch/inbox/";
	public static String store_dir="rch/store/";
	public static String index_file="rch/store.ix";
	public static String err_file="rch/store.err";
	public static String dup_file="rch/store.dup";
	public static PrintStream log=System.out;
	private SimpleDateFormat dfmt=new SimpleDateFormat("yyyy-MM-dd--hh:mm:ss.SSS");
	private NumberFormat nfmtlen=new DecimalFormat("0000000000000");
	private NumberFormat nfmtsts=new DecimalFormat("0,000,000,000,000");
	private byte buf[]=new byte[1024*1024];
	private int filecount;
//	private int filearched;
//	private int filedups;
//	private int fileerrs;
	public a sts;
	long bytecount;
	long bytestoprocess;
	public diro dirox;
	public $() throws IOException{
		final path inboxpth=req.get().session().path(inbox_dir);
		dirox.root(inboxpth);
	}
	public void to(final xwriter x) throws Throwable{
//		path inboxpth=req.get().session().path(inbox_dir);
//		dirox.setRootPath(inboxpth);
		final String sessionref=req.get().session().href();
		x.ax(this,"rch"," :: archive");
		x.ax(this,"store"," :: store");
		x.br();
		x.a(sessionref+index_file,"index").spc();
		x.a(sessionref+err_file,"errors").spc();
		x.a(sessionref+dup_file,"duplicates").spc();
		x.br().span(sts);
		x.ax(this,"clrsts","[·]");
		x.br().p("inbox: ").p(inbox_dir);
		x.br().p("store: ").p(store_dir);
		x.br().rend(dirox);
	}
	public void ax_clrsts(xwriter x,String[]p)throws Throwable{
		sts.set("");
		x.xu(sts);
	}
	//	private static long path_size_bytes(path p,long b,progstatus ps) throws IOException{
	//		if(p.isFile()){
	//			b+=p.getSize();
	//			if(ps!=null)
	//				ps.update("total "+b+" bytes");
	//			return b;
	//		}
	//
	//		if(ps!=null)
	//			ps.update("list "+p);
	//		
	//		String[] ls=p.list();
	//		for(String nm:ls){
	//			path pth=p.getPath(nm);
	//			b+=path_size_bytes(pth,b,ps);
	//			continue;
	//		}
	//		return b;
	//	}
	//		ByteArrayOutputStream baos=new ByteArrayOutputStream();
	//		cli cli=new cli("du -sb "+p.fullPath(),baos);
	//		cli.wait_for_cli();
	//		String dures=new String(baos.toByteArray());
	//		if(dures.isEmpty())
	//			return 0;
	//		String[] duparts=dures.split("\\s");
	//		return Long.parseLong(duparts[0]);
	//	}
	public void ax_rch(final xwriter x,final String[]p)throws Throwable{
		final session ses=req.get().session();
		final xsts stsb=new xsts(x,sts,500);
		final path inbx=ses.path(inbox_dir);
		if(!inbx.exists()){
			inbx.mkdirs();
			return;
		}
//		bytestoprocess=path_size_bytes(inbx,0,ps);
		bytestoprocess=0;
		final path store=ses.path(store_dir);
		store.mkdirs();
		final path ixfile=ses.path(index_file);
		final PrintStream ixps=new PrintStream(ixfile.outputstream(true));
		final path errfile=ses.path(err_file);
		final PrintStream errps=new PrintStream(errfile.outputstream(true));
		final path dupfile=ses.path(dup_file);
		final PrintStream dupps=new PrintStream(dupfile.outputstream(true));
		final MessageDigest md=MessageDigest.getInstance($.checksum_alg);
		final path root=inbx;
		filecount=0;
		bytecount=0;
		procdir(x,root,store,md,ixps,errps,dupps,stsb);
		ixps.close();
		errps.close();
		dupps.close();
		updatests(stsb);
		sts.set("done. "+sts.toString());
		x.xreload();
	}
	private void procdir(final xwriter x,final path root,final path store,final MessageDigest md,final PrintStream ixps,final PrintStream errps,final PrintStream dupps,final xsts pb)throws Throwable{
		//		if(root.getName().startsWith("."))
		//			return;
		pb.setsts("dir "+root);
		final String[]list=root.list();
		for(int i=0;i<list.length;i++){
			final path file=root.get(list[i]);
			if(file.isdir()){
				procdir(x,file,store,md,ixps,errps,dupps,pb);
				file.rm();
				//					errps.println(dfmt.format(new Date())+": could not delete "+file.fullPath());
				continue;
			}
			//			if(file.getName().startsWith("."))
			//				continue;
			filecount++;
			final String ext=file.type();
			final InputStream fi;
			try{
				fi=file.inputstream();
			}catch(FileNotFoundException e){
//				fileerrs++;
				errps.println(dfmt.format(new Date())+": could not open "+file.fullpath());
				continue;
			}
			int c=0;
			md.reset();
			while(true){
				c=fi.read(buf);
				if(c==-1)
					break;
				md.update(buf,0,c);
				bytecount+=c;
				updatests(pb);
			}
			fi.close();
			final byte[]hash=md.digest();
			final String hashstr=hashstr(hash);
			final String hashfilename=filename_for_hash(hashstr);
			final path newfile=req.get().session().path(store_dir+hashfilename+(ext.length()==0?"":("."+ext)));
			if(newfile.exists()){
//				filedups++;
				dupps.println(dfmt.format(new Date())+": "+hashstr);
				dupps.flush();
				file.rm();
				//					throw new Error(dfmt.format(new Date())+": could not delete "+file.fullPath());
				continue;
			}
			final long lastmod=file.lastmod();
			newfile.mkbasedir();
			if(!file.rename(newfile))
				throw new Error(dfmt.format(new Date())+": could not mv "+file+" "+newfile);
			newfile.lastmod(lastmod);
			newfile.setreadonly();
//			newfile.setExecutable(false);
			ixps.print(hashstr);
			ixps.print("----");
			ixps.print(nfmtlen.format(newfile.size()));
			ixps.print("----");
			ixps.print(dfmt.format(lastmod));
			ixps.print("----");
//			ixps.print(dfmt.format(new Date()));
			ixps.print("----:----:----:---:---:---:");
			ixps.print("-------------------------------:---------------:-------:---:-::");
			ixps.print(ext);
			ixps.println();
			ixps.flush();
//			filearched++;
		}
	}
	private void updatests(final xsts pb)throws Throwable{
		pb.setsts("processed " + filecount + " files   "
				+ nfmtsts.format(bytecount) + " bytes of "
				+ nfmtsts.format(bytestoprocess)+" bytes");
	}
	static String filename_for_hash(final String hash){
		//		StringBuffer sb=new StringBuffer();
		//		int n=hash.length();
		//		int i=0,j=3;
		//		while(true){
		//			if(j>n)
		//				break;
		//			sb.append(hash.substring(i,j));
		//			sb.append("/");
		//			i=j;
		//			j+=3;
		//		}
		//		if(i<n){
		//			sb.append(hash.substring(i,n));
		//			sb.append("/");
		//		}
		//		sb.setLength(sb.length()-1);
		//		return sb.toString();
		return hash;
	}
	private static String hashstr(final byte[]hash){
		final StringBuilder sb=new StringBuilder(hash.length*2);
		for(int i=0;i<hash.length;i++){
			byte b=hash[i];
			int hex=(b&0xf0)>>4;
			if(hex<10)
				sb.append((char)('0'+hex));
			else
				sb.append((char)('a'+(hex-10)));
			hex=b&0x0f;
			if(hex<10)
				sb.append((char)('0'+hex));
			else
				sb.append((char)('a'+(hex-10)));
		}
		return sb.toString();
	}
}
