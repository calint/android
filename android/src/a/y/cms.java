package a.y;
import java.io.IOException;
import b.a;
import b.path;
import b.req;
import b.xwriter;

public class cms extends a{
	private static final long serialVersionUID=1;
	public static String rootdir="cms";
	public a lvl0;
	public a lvl1;
	public a path;
	public a content;
	private path[]stkpath=new path[32];
	private int stkipath;
	{stkpath[0]=req.get().session().path(rootdir);}

	private path pathnow(){synchronized(stkpath){return stkpath[stkipath];}}
//	private void stkpathpush(final path fpth){synchronized(stkpath){
//		stkpath[++stkipath]=fpth;
//	}}

	public void to(final xwriter x)throws Throwable{
		x.pre();
		x.tago("div").attr("style","border:1px dotted green").tagoe();
		x.spanh(lvl0);
		x.divEnd();
		x.tago("div").attr("style","border:1px dotted brown").tagoe();
		x.span(lvl1);
		x.divEnd();
		x.span(path).nl();
		x.div("editor");
		x.span(content);
		x.divEnd();

		x.script().nl();
		rfshlvl(0,x);
		rfshcontent(x);
		rfshpath(x);
		x.scriptEnd();
	}
	private void rfshcontent(xwriter x)throws IOException{
		final path pix=pathnow().get("0");
		if(pix.isfile()){
			x.xu(content,pix);
		}else
			x.xu(content,"notfound: "+pix.toString());
	}
	private void rfshlvl(final int lvl,final xwriter x){
		final xwriter y=new xwriter();
		y.p(" · ");
		final path p=stkpath[lvl];
		for(final String nm:p.list()){
			final path f=p.get(nm);
			if(f.isfile())
				continue;
			final boolean sel=lvl+1>stkipath?false:f.equals(stkpath[lvl+1]);
			y.ax(this,"o "+lvl+" "+f.name(),(sel?"[":"")+f.name()+(sel?"]":"")).p(" · ");
		}
		if(lvl==0)
			x.xu(lvl0,y.toString());
		else if(lvl==1)
			x.xu(lvl1,y.toString());
	}
	private void rfshpath(xwriter x){
		final xwriter y=new xwriter();
		for(int n=0;n<=stkipath;n++)
			y.ax(this,"o "+(n-1)+" "+stkpath[n].name(),stkpath[n].name()).p(" → ");
		x.xu(path,y.toString());
	}
	public void ax_o(xwriter x,String[]a)throws IOException{
		final int lvl=Integer.parseInt(a[2]);
		if(lvl<0){
			stkipath=0;
			rfshpath(x);
			rfshlvl(0,x);
			rfshcontent(x);
			return;
		}
		stkpath[lvl+1]=stkpath[lvl].get(a[3]);
		stkipath=lvl+1;
		rfshpath(x);
		for(int n=0;n<=stkipath;n++)
			rfshlvl(n,x);
		rfshcontent(x);
	}
}
