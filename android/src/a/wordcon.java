package a;
import java.util.HashSet;
import java.util.Set;

import b.a;
import b.xwriter;
public class wordcon extends a{
	static final long serialVersionUID=1;
	public a txt;
	public void to(final xwriter x) throws Throwable{
		x.ax(this,"ok",":: wordcon");
		x.inputTextArea(txt,"editor").br();
	}
	public void ax_ok(final xwriter x,final String[]p){
		final Set<String>set=new HashSet<String>();
		final String[]lines=txt.toString().split("\\n");
		int lineno=0;
		for(final String line:lines){
			lineno++;
			if(line.length()==0)
				continue;
			final String[]words=line.split(" ");
			set.clear();
			for(final String word:words){
				if(word.length()==0)
					continue;
				if(set.contains(word)){
					x.xalert("repetition of word “"+word+"” on line "+lineno);
					return;
				}
				set.add(word);
			}
		}
		x.xalert("ok");
	}
}
