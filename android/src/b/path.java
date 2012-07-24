package b;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
public final class path implements Serializable{
	static final long serialVersionUID=1;
	private File file;
	path(final File f){file=f;}
	public InputStream inputstream()throws FileNotFoundException{return new FileInputStream(file);}
	public FileInputStream fileinputstream()throws IOException{return new FileInputStream(file);}
	public FileOutputStream outputstream(final boolean append)throws IOException{mkbasedir();return new FileOutputStream(file,append);}
	public Reader reader()throws IOException{return new InputStreamReader(inputstream(),b.strenc);}
	public Writer writer(final boolean append)throws IOException{mkbasedir();return new OutputStreamWriter(outputstream(append),b.strenc);}
	public boolean exists(){return file.exists();}
	public long lastmod(){return file.lastModified();}
	public long size(){return file.length();}
	public boolean isfile(){return file.isFile();}
	public boolean isdir(){
		if(!file.exists())
			return false;
		final boolean isd=file.isDirectory();
		return isd;}
	public String fullpath()throws IOException{return file.getCanonicalPath();}
	public path get(final String name){
		if(name==null||name.length()==0||name.equals(".")||name.indexOf("..")!=-1)
			throw new Error("illegal name: "+name);
		return new path(new File(file,name));
	}
	public String name(){return file.getName();}
	public String[]list(){final String[]f=file.list();if(f==null)return new String[0];return f;}
	public String[]list(final FilenameFilter fnmf){final String[]f=file.list(fnmf);if(f==null)return new String[0];return f;}
	public boolean rename(final path nf){return file.renameTo(nf.file);}
	public void lastmod(final long lastmod){if(!file.setLastModified(lastmod))throw new Error();}
	public void setreadonly(){if(!file.setReadOnly())throw new Error();}
//	public void executable(final boolean b){if(!file.setExecutable(b))throw new Error();}
	public boolean ishidden(){return file.getName().charAt(0)=='.';}
	public path parent(){final File f=file.getParentFile();return f==null?null:new path(f);}
	public void to(final xwriter x)throws IOException{to(x.outputstream());}
	public FileChannel filechannel()throws IOException{return outputstream(false).getChannel();}
	public final int hashCode(){return file.toString().hashCode();}
	public boolean equals(final Object obj){if(!(obj instanceof path))return false;return ((path)obj).file.equals(file);}
	public String toString(){
		final String fn=file.toString();
		if(fn.startsWith("./"))
			return fn.substring("./".length());
		return fn;
	}
	public void mkfile()throws IOException{if(!file.createNewFile())throw new IOException("cannot make file "+file);}
	public boolean rm()throws IOException{
		if(!file.exists())
			return true;
		if(file.isFile())
			return file.delete();
		for(final File f:file.listFiles())
			if(!new path(f).rm())
				return false;
		return file.delete();
	}
	public boolean rm(final sts st)throws Throwable{
		if(!file.exists())
			return true;
		st.setsts("deleteting "+file.toString());
		if(file.isFile())
			return file.delete();
		for(final File f:file.listFiles()){
			if(!new path(f).rm(st))
				return false;
		}
		return file.delete();
	}
	//? rm(proglog)
	public void append(final String line,final String eol)throws IOException{
		final File dir=file.getParentFile();
		if(dir!=null&&!dir.exists())
			if(!dir.mkdirs())
				throw new Error("cannot mkdir "+file.getParentFile());
		final OutputStream os=outputstream(true);
		final byte[]ba=b.tobytes(line);
		os.write(ba);
		if(eol!=null){
			final byte[]eosba=b.tobytes(eol);
			os.write(eosba);
		}
		os.close();
	}
	public void append(final String[]lines,final String eol)throws IOException{
		if(!file.exists())
			if(!file.getParentFile().mkdirs())
				throw new Error();
		final OutputStream os=outputstream(true);
		final byte[]eosba=eol!=null?b.tobytes(eol):null;
		for(final String line:lines){
			final byte[]ba=b.tobytes(line);
			os.write(ba);
			if(eol!=null)
				os.write(eosba);
		}
		os.close();
	}
	public path to(final OutputStream os)throws IOException{
		final InputStream is=inputstream();
		b.cp(is,os);
		is.close();
		return this;
	}
	public void mkdirs()throws IOException{
		if(file.exists()&&file.isDirectory())
			return;
		if(!file.mkdirs())
			throw new IOException("cannot create dir "+file);
	}
	public void mkbasedir()throws IOException{
		final File pf=file.getParentFile();
		if(pf!=null&&pf.isDirectory())
			return;
		if(pf==null)throw new Error();
		if(!pf.mkdirs())
			throw new IOException("cannot create basedir "+file);
	}
	public path to(final ByteBuffer byteBuffer)throws IOException{
		final FileInputStream fis=fileinputstream();
		final FileChannel channelFrom=fis.getChannel();
		channelFrom.read(byteBuffer);
		channelFrom.close();
		fis.close();
		return this;
	}
	public String type(){
		final String fn=file.getName();
		int ix=fn.lastIndexOf('.');
		if(ix==-1)return "";
		return fn.substring(ix+1).toLowerCase();
	}
	public String uri(){
		String s=file.getPath().substring(b.root_dir.length());
		while(s.startsWith("./"))s=s.substring(2);//?
		final String[]parts=s.split(b.pathsep);
		final StringBuilder sb=new StringBuilder();
		for(final String ss:parts)
			sb.append(b.urlencode(ss)).append(b.pathsep);
		sb.setLength(sb.length()-b.pathsep.length());
		return sb.toString();
	}
	public Object readobj()throws IOException,ClassNotFoundException{
		final ObjectInputStream ois=new ObjectInputStream(inputstream());
		try{final Object o=ois.readObject();return o;}finally{ois.close();}
	}
	public void writeobj(final Object o)throws IOException{
		final ObjectOutputStream oos=new ObjectOutputStream(outputstream(false));
		try{oos.writeObject(o);}finally{oos.close();}
	}
	public path writeba(final byte[]data)throws IOException{
		final OutputStream os=outputstream(false);
		try{os.write(data);return this;}finally{os.close();}
	}
	public void writebb(final ByteBuffer byteBuffer)throws IOException{
		final FileOutputStream os=outputstream(false);
		final FileChannel fc=os.getChannel();
		final int c=fc.write(byteBuffer);
		if(c!=byteBuffer.limit())
			throw new Error();
		os.close();
	}
	public MappedByteBuffer mappedbbrw(final int len_b)throws FileNotFoundException,IOException{
		return mappedbb(false,len_b);
	}
	public MappedByteBuffer mappedbb(final boolean ro,final int len_b)throws FileNotFoundException,IOException{
		final MappedByteBuffer out=new RandomAccessFile(toString(),ro?"r":"rw").getChannel().map(ro?FileChannel.MapMode.READ_ONLY:FileChannel.MapMode.READ_WRITE,0,len_b);
		return out;
	}
//	private void assert_access() throws IOException{
//	String uri=file.toString().replace('\\','/');
//	if(uri.startsWith("./"))
//		uri=uri.substring(2);
//	String[] urils=uri.split("/");
//	if(urils.length==0)
//		urils=new String[]{""};
//	List<String> keys=req.get().session().accesskeys();
//	StringBuffer pathbf=new StringBuffer(htp.root_dir);
//	for(int n=0;n<urils.length;n++){
//		String s=urils[n];
//		if(pathbf.length()>0)
//			pathbf.append("/");
//		pathbf.append(s);
//		File f=new File(pathbf.toString());
//		File keysf;
//		if(f.isDirectory())
//			keysf=new File(f,".key");
//		else
//			continue;
//		if(!keysf.exists())
//			continue;
//		BufferedReader reader=new BufferedReader(new FileReader(keysf));
//		for(String line=reader.readLine().trim();line!=null;line=reader.readLine().trim()){
//			if(line.startsWith("#")){
//				continue;
//			}
//			if(keys.contains(line)){
//				return;
//			}
//		}
//		throw new Error("access denied "+uri);
//	}
//}
	public String readstr()throws IOException{
		if(!isfile())
			return "";
		final ByteArrayOutputStream ba=new ByteArrayOutputStream((int)size());
		to(ba);
		return ba.toString("utf8");
	}
	public boolean isin(final path p){try{return fullpath().startsWith(p.fullpath());}catch(Throwable t){throw new Error(t);}}
}
