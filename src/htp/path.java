package htp;
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
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
public final class path implements Serializable{
	static final long serialVersionUID=1;
	private File file;
	path(File f){file=f;}
	public InputStream getInputStream()throws FileNotFoundException{return new FileInputStream(file);}
	public FileInputStream getFileInputStream()throws IOException{return new FileInputStream(file);}
	public FileOutputStream getOutputStream(boolean append)throws IOException{mkbasedir();return new FileOutputStream(file,append);}
	public Reader getReader()throws IOException{return new InputStreamReader(getInputStream(),htp.strenc);}
	public Writer getWriter(final boolean append)throws IOException{mkbasedir();return new OutputStreamWriter(getOutputStream(append),htp.strenc);}
	public boolean rm()throws IOException{
		if(!file.exists())
			return true;
		if(file.isFile()){
			return file.delete();
		}
		for(File f:file.listFiles())
			if(!new path(f).rm())
				return false;
		return file.delete();
	}
	@Override public String toString(){
		return file.toString();
	}
	public void append(final String line,final String eol) throws IOException{
		if(!file.exists())
			file.getParentFile().mkdirs();
		final OutputStream os=getOutputStream(true);
		final byte[] ba=htp.tobytes(line);
		os.write(ba);
		if(eol!=null){
			final byte[] eosba=htp.tobytes(eol);
			os.write(eosba);
		}
		os.close();
	}
	//public this to(OutputStream os)throws{
	public path to(final OutputStream os) throws IOException{
		final InputStream is=getInputStream();
		htp.cp(is,os);
		is.close();
		return this;
	}
	@Override public boolean equals(final Object obj){
		if(!(obj instanceof path))
			return false;
		return ((path)obj).file.equals(file);
	}
//	private void assert_access() throws IOException{
//		String uri=file.toString().replace('\\','/');
//		if(uri.startsWith("./"))
//			uri=uri.substring(2);
//		String[] urils=uri.split("/");
//		if(urils.length==0)
//			urils=new String[]{""};
//		List<String> keys=req.get().session().accesskeys();
//		StringBuffer pathbf=new StringBuffer(htp.root_dir);
//		for(int n=0;n<urils.length;n++){
//			String s=urils[n];
//			if(pathbf.length()>0)
//				pathbf.append("/");
//			pathbf.append(s);
//			File f=new File(pathbf.toString());
//			File keysf;
//			if(f.isDirectory())
//				keysf=new File(f,".key");
//			else
//				continue;
//			if(!keysf.exists())
//				continue;
//			BufferedReader reader=new BufferedReader(new FileReader(keysf));
//			for(String line=reader.readLine().trim();line!=null;line=reader.readLine().trim()){
//				if(line.startsWith("#")){
//					continue;
//				}
//				if(keys.contains(line)){
//					return;
//				}
//			}
//			throw new Error("access denied "+uri);
//		}
//	}
	public boolean exists(){
		return file.exists();
	}
	public long getLastModified(){
		return file.lastModified();
	}
	public long getSize(){
		return file.length();
	}
	public boolean isFile(){
		return file.isFile();
	}
	public boolean isDirectory(){
		return file.isDirectory();
	}
	public void mkdirs() throws IOException{
		if(file.exists()&&file.isDirectory())
			return;
		if(!file.mkdirs())
			throw new IOException("could not create dir "+file);
	}
	public void mkbasedir() throws IOException{
		final File pf=file.getParentFile();
		if(pf!=null&&pf.isDirectory())
			return;
		if(!pf.mkdirs())
			throw new IOException("could not create basedir "+file);
	}
	public void mkfile() throws IOException{
		if(!file.createNewFile())
			throw new IOException("could not make file "+file);
	}
	public String getFullPath() throws IOException{
		return file.getCanonicalPath();
	}
	public path getPath(final String name) throws IOException{
		return new path(new File(file,name));
	}
	public path to(final ByteBuffer byteBuffer) throws IOException{
		FileInputStream fis=getFileInputStream();
		FileChannel channelFrom=fis.getChannel();
		channelFrom.read(byteBuffer);
		channelFrom.close();
		fis.close();
		return this;
	}
	@Override public final int hashCode(){return this.file.toString().hashCode();}
	public String getType(){
		final String fn=file.getName();
		int ix=fn.lastIndexOf('.');
		if(ix==-1)
			return "";
		return fn.substring(ix+1).toLowerCase();
	}
	public String getName(){return file.getName();}
	public String[]list(){final String[]f=file.list();if(f==null)return new String[0];return f;}
	public String[]list(FilenameFilter fnmf){final String[]f=file.list(fnmf);if(f==null)return new String[0];return f;}
	public boolean renameTo(final path nf){return file.renameTo(nf.file);}
	public void setLastModified(final long lastmod){file.setLastModified(lastmod);}
	public void setReadOnly(){file.setReadOnly();}
//	public void setExecutable(final boolean b){file.setExecutable(b);}
	public boolean isHidden(){return file.getName().charAt(0)=='.';}
	public String getHref(){
		String s=file.getPath().substring(htp.root_dir.length());
		if(s.startsWith("./"))
			s=s.substring(2);
		String[] parts=s.split("/");
		StringBuffer sb=new StringBuffer();
		for(String ss:parts){
			sb.append("/").append(htp.urlencode(ss));
		}
		return sb.toString();
	}
	public path getParent() throws IOException{File f=file.getParentFile();return f==null?null:new path(f);}
	public void to(final xwriter x) throws IOException{to(x.outputStream());}
	public Object readobj() throws IOException,ClassNotFoundException{
		final ObjectInputStream ois=new ObjectInputStream(getInputStream());
		final Object o=ois.readObject();
		ois.close();
		return o;
	}
	public void writeobj(final Object o) throws IOException{
		final ObjectOutputStream oos=new ObjectOutputStream(getOutputStream(false));
		oos.writeObject(o);
		oos.close();
	}
	public path writeba(final byte[]data) throws IOException{
		final OutputStream os=getOutputStream(false);
		os.write(data);
		os.close();
		return this;
	}
	public void writebb(final ByteBuffer byteBuffer)throws IOException{
		final FileOutputStream os=getOutputStream(false);
		final FileChannel fc=os.getChannel();
		int c=fc.write(byteBuffer);
		if(c!=byteBuffer.limit())
			throw new Error();
		os.close();
	}
	public FileChannel getFileChannel()throws IOException{return getOutputStream(false).getChannel();}
}
