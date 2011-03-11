package htp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public final class session implements Serializable{
	static final long serialVersionUID=1;
	private static Map<String,session>all=new HashMap<String,session>(htp.hash_size_sessions_store);//? conc
	static Map<String,session>all(){return all;}
	@SuppressWarnings("unchecked") static void all_load() throws Throwable{
		final File f=new File(htp.root_dir,htp.sessions_store);
		htp.out.println(htp.q+" loading sessions from "+f.getCanonicalPath());
		if(!f.exists()){
			htp.out.println(" "+htp.a+" not found");
			return;
		}
		final long t0=System.currentTimeMillis();
		final ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
		synchronized(all){all=(Map<String,session>)ois.readObject();}
		ois.close();
		final long dt=(System.currentTimeMillis()-t0);
		htp.out.println(" "+htp.a+" restored "+all().size()+" sessions in "+dt+" ms");
	}
	static void all_save() throws Throwable{
		//? session in homedir, load at need, store when inactive, save every x ms
		final File f=new File(htp.root_dir,htp.sessions_store);
		htp.out.println("\n"+htp.q+" saving "+all().size()+" sessions to "+f.getCanonicalPath());
		final long t0=System.currentTimeMillis();
		final ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(f));
		synchronized(all){oos.writeObject(all);}
		oos.close();
		final long dt=(System.currentTimeMillis()-t0);
		htp.out.println(" "+htp.a+" done in "+dt+" ms");
	}

	private String id;
	private HashMap<String,Serializable>kvp;
	int nreq;
	session(String id){this.id=id;kvp=new HashMap<String,Serializable>(htp.hash_size_session_values);}
	public Serializable get(final String key){return kvp.get(key);}
	public String href(){return "/"+htp.sessions_dir+"/"+id+"/";}
	public String id(){return id;}
	public Set<String>keyset(){return kvp.keySet();}
	public path path(final String path){return htp.path(href()+path);}
	public void put(final String key,final Serializable value){kvp.put(key,value);}
	public void save()throws IOException{path(htp.sessionfile).writeobj(this);}
}
