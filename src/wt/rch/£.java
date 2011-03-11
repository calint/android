package wt.rch;
import htp.path;
import htp.req;
import htp.session;
import htp.wt;
import htp.xwriter;
import htpx.diro;
import htpx.stsb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class £ extends wt{
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
	private int filearched;
	private int filedups;
	private int fileerrs;
	public wt sts;
	long bytecount;
	long bytestoprocess;
	public diro dirox;
	public £() throws IOException{
		path inboxpth=req.get().session().path(inbox_dir);
		dirox.setRootPath(inboxpth);
	}
	@Override public void to(xwriter x) throws Throwable{
//		path inboxpth=req.get().session().path(inbox_dir);
//		dirox.setRootPath(inboxpth);
		x.action_ax(this,"rch"," :: archive");
		x.action_ax(this,"store"," :: store");
		x.br().wt(sts);
		x.action_ax(this,"clrsts","[·]");
		x.br().wt(dirox);
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
	public void ax_rch(xwriter x,String[] p) throws Throwable{
		session ses=req.get().session();
		stsb stsb=new stsb(x,sts,500);
		path inbx=ses.path(inbox_dir);
		if(!inbx.exists()){
			inbx.mkdirs();
			return;
		}
		//		bytestoprocess=path_size_bytes(inbx,0,ps);		
		path store=ses.path(store_dir);
		store.mkdirs();
		path ixfile=ses.path(index_file);
		PrintStream ixps=new PrintStream(ixfile.getOutputStream(true));
		path errfile=ses.path(err_file);
		PrintStream errps=new PrintStream(errfile.getOutputStream(true));
		path dupfile=ses.path(dup_file);
		PrintStream dupps=new PrintStream(dupfile.getOutputStream(true));
		MessageDigest md=MessageDigest.getInstance(£.checksum_alg);
		path root=inbx;
		procdir(x,root,store,md,ixps,errps,dupps,stsb);
		ixps.close();
		errps.close();
		dupps.close();
		updatests(stsb);
		sts.x_setValue(x,"done. "+sts.toString());
		x.x_reload();
	}
	private void procdir(xwriter x,path root,path store,MessageDigest md,PrintStream ixps,PrintStream errps,PrintStream dupps,stsb pb) throws Throwable{
		//		if(root.getName().startsWith("."))
		//			return;
		pb.update("dir "+root);
		String[] list=root.list();
		for(int i=0;i<list.length;i++){
			path file=root.getPath(list[i]);
			if(file.isDirectory()){
				procdir(x,file,store,md,ixps,errps,dupps,pb);
				file.rm();
				//					errps.println(dfmt.format(new Date())+": could not delete "+file.fullPath());
				continue;
			}
			//			if(file.getName().startsWith("."))
			//				continue;
			filecount++;
			String ext=file.getType();
			InputStream fi;
			try{
				fi=file.getInputStream();
			}catch(FileNotFoundException e){
				fileerrs++;
				errps.println(dfmt.format(new Date())+": could not open "+file.getFullPath());
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
			byte hash[]=md.digest();
			String hashstr=hashstr(hash);
			String hashfilename=filename_for_hash(hashstr);
			path newfile=req.get().session().path(store_dir+hashfilename+(ext.length()==0?"":("."+ext)));
			if(newfile.exists()){
				filedups++;
				dupps.println(dfmt.format(new Date())+": "+hashstr);
				dupps.flush();
				file.rm();
				//					throw new Error(dfmt.format(new Date())+": could not delete "+file.fullPath());
				continue;
			}
			long lastmod=file.getLastModified();
			newfile.mkbasedir();
			if(!file.renameTo(newfile))
				throw new Error(dfmt.format(new Date())+": could not mv "+file+" "+newfile);
			newfile.setLastModified(lastmod);
			newfile.setReadOnly();
//			newfile.setExecutable(false);
			ixps.print(hashstr);
			ixps.print("----");
			ixps.print(nfmtlen.format(newfile.getSize()));
			ixps.print("----");
			ixps.print(dfmt.format(lastmod));
			ixps.print("----");
//			ixps.print(dfmt.format(new Date()));
			ixps.print("----:----:----:---:---:---:");
			ixps.print("-------------------------------:---------------:-------:---:-::");
			ixps.print(ext);
			ixps.println();
			ixps.flush();
			filearched++;
		}
	}
	private void updatests(stsb pb) throws Throwable{
		pb.update("processed "+filecount+" files   "+nfmtsts.format(bytecount)+" bytes of "+nfmtsts.format(bytestoprocess)+" bytes");
	}
	static String filename_for_hash(String hash){
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
	private static String hashstr(byte[] hash){
		StringBuffer sb=new StringBuffer(hash.length*2);
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
