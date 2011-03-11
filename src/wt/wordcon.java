package wt;
import htp.wt;
import htp.xwriter;
import java.util.HashSet;
import java.util.Set;
public class wordcon extends wt{
	private static final long serialVersionUID=1;
	public wt txt;
	@Override public void to(xwriter x) throws Throwable{
		x.action_ax(this,"ok",":: wordcon");
		x.inputTextArea(txt,"editor").br();
	}
	public void ax_ok(xwriter x,String[] p){
		Set<String> set=new HashSet<String>();
		String[] lines=txt.toString().split("\\n");
		int lineno=0;
		for(String line:lines){
			lineno++;
			if(line.length()==0)
				continue;
			String[] words=line.split(" ");
			set.clear();
			for(String word:words){
				if(word.length()==0)
					continue;
				if(set.contains(word)){
					x.x_alert("repetition of word “"+word+"” on line "+lineno);
					return;
				}
				set.add(word);
			}
		}
		x.x_alert("ok");
	}
}
