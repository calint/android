package a.y;

import java.util.HashMap;
import java.util.Map;
import b.a;
import b.xwriter;
public class frameless extends a{static final long serialVersionUID=1;
	public win w;{
		w.add(new win(w,"a").dim(10,10,30,30,1));
		w.add(new win(w,"b").dim(45,10,30,30,2));
	}
	public void to(final xwriter x)throws Throwable{
//		x.pre().ax(this,"r","»reset").nl().nl();
		w.to(x);
	}
	
	public static class win extends a{
		static final long serialVersionUID=1;
		private int wi=640,hi=400,z=0;
		private int top=100,lft=100;
		private int bo=1;
		private String colr="green";
		private Map<String,win>wins=new HashMap<String,win>();
		protected a chldq(final String id){return wins.get(id);}
		public win(){}
		public win(final a parent,final String name){
			super(parent,name);
		}
		public win dim(final int top,final int lft,final int wi,final int hi,final int z){
			this.wi=wi;this.hi=hi;this.top=top;this.lft=lft;this.z=z;
			return this;
		}
		public void add(final win ch){
			wins.put(ch.name(),ch);
		}
		public void to(final xwriter x)throws Throwable{
			final String wid=id();
			x.tago("div").p(" id=").p(wid).p(" name=").p(wid).p(" style='position:absolute;border:").p(bo).p("px dotted ").p(colr)
			.p("' onclick=$x('").p(wid).p("') onmouseover=\"$x('"+wid+" mo');return true\" onmouseout=\"$x('"+wid+" mt');return true\" ondragstart=\"$x('"+wid+" d');return true\" ondragend=\"$x('"+wid+" e');return true\" draggable=true>");
			for(final win w:wins.values()){
				w.to(x);
				x.script();w.ax_(x,null);x.scriptEnd();
			}
			x.divEnd();
			x.script();ax_(x,null);x.scriptEnd();
		}
		public void ax_(final xwriter x,final String[]a){
//			set("id:"+wid()+"\nx:"+lft+"\ny:"+top);
//			top+=Math.random()*10-5;
//			lft+=Math.random()*10-5;
//			wi+=Math.random()*10-5;
//			hi+=Math.random()*10-5;
			x.p("{var s=$('").p(id()).p("').style;\ns.width=\"").p(wi).pl("px\";");
			x.p("s.zIndex=").p(z).pl(";");
			x.p("s.height=\"").p(hi).pl("px\";");
			x.p("s.left=\"").p(lft).pl("px\";");
			x.p("s.top=\"").p(top).pl("px\";}");
//			x.xupd(this);
		}
		public void ax_mo(final xwriter x,final String[]a){
			x.p("//mouseover ").p(id());
		}
		public void ax_mt(final xwriter x,final String[]a){
			x.p("//mouseout ").p(id());
		}
		public void ax_d(final xwriter x,final String[]a){
//			set("dragstart\nid:"+wid()+"\nx:"+lft+"\ny:"+top);
			x.p("//dragstart");
//			x.xupd(this);
		}
		public void ax_e(final xwriter x,final String[]a){
//			set("dragend\nid:"+wid()+"\nx:"+lft+"\ny:"+top);
			x.p("//dragstop");
//			x.xupd(this);
		}
	}
}
