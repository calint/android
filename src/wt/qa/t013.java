package wt.qa;
import htp.htp;
import htp.path;
import htp.wt;
import htp.xwriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
public class t013 extends wt{
	private static final long serialVersionUID=1;
	@Override public void to(xwriter x) throws Throwable{
		path p=htp.path("qa/t001.txt");
		if(!p.exists()){
			return;
		}
		InputStream is=p.getFileInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf8"));
		for(String line=br.readLine();line!=null;line=br.readLine()){
			x.p(line).nl();
		}
		is.close();
	}
}