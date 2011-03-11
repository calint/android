package wt.ix;
import htp.htp;
import htp.path;
import htpx.stsb;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
final public class indexwriter extends Writer{
	public static String[]inctypes={"","txt","html","htm","java","h","c","cc","cpp"};
	private StringBuffer sb;
	private path ixstore;
	private path file_path;
	private Map<String,String> tokens;
	private int token_size_max=255;
	private int token_size_min=1;
	private int count_tokens=0;
	private int count_files=0;
	private int count_tokens_found=0;
	private long count_bytes=0;
	private int pathsubstrix;
	private NumberFormat nf=new DecimalFormat("#,###,###,###,###");
	public static void reindex(final path ixstorepath,final path rootpath,final stsb pb) throws Throwable{
		long t0=System.currentTimeMillis();
		pb.update(htp.a+" reindexing "+rootpath);
		ixstorepath.rm();
		pb.update(htp.a+" index deleted");
		indexwriter indexer=new indexwriter(rootpath,ixstorepath);
		indexer.traverse(rootpath,pb);
		pb.update(htp.a+" indexed "+indexer.count_bytes+" bytes in "+indexer.count_files+" files, found "+indexer.count_tokens_found+" words in "+(System.currentTimeMillis()-t0)+" milliseconds.");
		pb.done();
	}
	public indexwriter(final path docroot,final path ixstore){
		this.ixstore=ixstore;
		sb=new StringBuffer(token_size_max);
		tokens=new HashMap<String,String>(htp.K);
		pathsubstrix=docroot.toString().length()+1;
	}
	private void enter_path(final path p){
		tokens.clear();
		file_path=p;
		count_files++;
	}
	@Override public void write(final char[]c,final int off,final int len) throws IOException{
		for(int n=0;n<len;n++){
			char ch=c[off+n];
			ch=Character.toLowerCase(ch);
			if(Character.isLetterOrDigit(ch)){
				sb.append(ch);
				continue;
			}
			if(ch=='+'){
				sb.append(ch);
				continue;
			}
			if(ch=='#'){
				sb.append(ch);
				continue;
			}
			if(sb.length()==0)
				continue;
			onToken(sb.toString());
			sb.setLength(0);
		}
		count_bytes+=len;
	}
	private void onToken(final String token) throws IOException{
		count_tokens++;
		final int len=token.length();
		if(len<token_size_min)
			return;
		if(len>token_size_max)
			return;
		if(tokens.containsKey(token))
			return;
		tokens.put(token,token);
		count_tokens_found++;
		final path fileixpath=ixstore.getPath(token.substring(0,1)+"/"+token);
		final Writer wr=fileixpath.getWriter(true);
		String f=file_path+"\n";
		f=f.substring(pathsubstrix);
		wr.write(f);
		wr.close();
	}
	private void traverse(final path rootpath,final stsb pb) throws Throwable{
		pb.update(htp.a+" indexed "+nf.format(count_bytes)+" B. processing "+rootpath);
		final String[] dir=rootpath.list();
		if(dir==null)
			return;
		Arrays.sort(dir,new Comparator<String>(){
			public int compare(String a,String b){return a.toLowerCase().compareTo(b.toLowerCase());}
		});
		for(int i=0;i<dir.length;i++){
			final path p=rootpath.getPath(dir[i]);
			if(p.isHidden())
				continue;
			if(p.equals(ixstore))
				continue;
			if(p.isDirectory()){
				traverse(p,pb);
				continue;
			}
			final String type=p.getType();
			if(!inctype(type))
				continue;
			pb.update(htp.a+" indexed "+nf.format(count_bytes)+" B. processing "+p);
			enter_path(p);
			write(p.getName()+"\n");
			final Reader r=p.getReader();
			htp.cp(r,this);
			r.close();
		}
	}
	private boolean inctype(final String type){for(final String s:inctypes)if(type.equals(s))return true;return false;}
	@Override public void flush() throws IOException{}
	@Override public void close() throws IOException{}
}
