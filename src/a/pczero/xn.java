package a.pczero;
import a.x.jskeys;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class xn extends a{
	static final long serialVersionUID=1;
	void pramble(final xwriter x){
		x.pl("zn rix vintage multi core computing - notinca 16b ");
		x.pl(" "+x1.strdatasize(ram.size)+" x 16b data");
		x.pl(" "+size+" threads x");
		x.pl("   "+core[0].regs.size+" registers");
		x.pl("   "+x1.strdatasize3(core[0].rom.size)+"instructions");
		x.pl(" pre hlt version");
	}
	public ram ram;
//	public ram ram2;
	public a sts;
	final static int size=16;
	private x1[]core=new x1[size];
	{for(int i=0;i<core.length;i++){
		final x1 c=new x1();
		c.ram=ram;
		c.coreid.set(""+(char)('a'+i));
		c.displaybits=8;
		c.pt(this).nm("core"+i);
		core[i]=c;
	}}
//	private srcviwr[]srcs=new srcviwr[size];
//	{for(int i=0;i<srcs.length;i++)srcs[i]=(srcviwr)ax1[i].src.pt(this).nm("src"+i);}
	protected a chldq(final String id){
//		if(id.startsWith("src")){
//			return srcs[Integer.parseInt(id.substring("src".length()))];
//		}else 
		if(id.startsWith("core")){
			return core[Integer.parseInt(id.substring("core".length()))];
		}else
			return super.chldq(id);
	}
	public void to(final xwriter x)throws Throwable{
		if(pt()==null){
			x.el(this);
//			x.nl().style();
//			x.nl().cssfont("tini","/ttf/tini.ttf");
//			x.nl().css("body","background:#222;font-size:4px;font-family:tini;margin:1em 0 0 1em");
//			x.nl().cssfont("tini","/ttf/tini.ttf");
//			x.nl().styleEnd();
			x.nl().style("body","color:#343;background:#222;font-size:4px;font-family:tini;margin:1em 0 0 1em");
			final String id=id();
			final jskeys jskeys=new jskeys(x);
			x.nl().script();
			jskeys.add("cS","$x('"+id+" s')");
			jskeys.add("cL","$x('"+id+" l')");
			jskeys.add("cT","$x('"+id+" t')");
			jskeys.add("cR","$x('"+id+" r')");
			jskeys.add("cF","$x('"+id+" f')");
			jskeys.add("cG","$x('"+id+" g')");
			jskeys.add("cU","$x('"+id+" u')");
			jskeys.add("cO","$x('"+id+" c')");
			jskeys.add("cD","$x('"+ram.id()+" rfh')");
			x.scriptEnd().nl();
		}else
			x.el(this);
		x.pre();
		pramble(x);
		x.nl().table().tr();
		x.nl().td(16);
		x.table().tr().td("text-align:center",null);
		x.style(ram,"border:1px solid #030");
		ram.to(x);
		x.td("text-align:center",null);
//		ram2.to(x);
//		x.style(ram2,"border:1px solid #300");
		x.nl().td("text-align:left;vertical-align:bottom;padding-right:1em",null);
		x.span(sts).nl();
		x.ax(this,"l"," load").nl();
		x.ax(this,"c"," compile").nl();
		x.ax(this,"r"," reset").nl();
		x.ax(this,"t"," step").nl();
		x.ax(this,"g"," go").nl();
		x.ax(this,"f"," frame").nl();
		x.ax(this,"u"," run").nl();
		x.ax(this,"s"," save").nl();
		x.tableEnd();
		x.tr();
		for(final x1 c:core){
			x.td("width:20em",null);
			c.to(x);
		}
		x.tableEnd();
		x.elend();
	}
	synchronized public void ax_l(xwriter x,String[]a)throws Throwable{
		int i=0;
		for(final x1 c:core){
			final path p=req.get().session().path("xn").get("c"+i+".src");
			if(p.exists())
				c.src.txt.from(p);
			else
				c.src.txt.set("..");
			if(x!=null)
				x.xuo(c);
			i++;
		}
		sts.set("loaded");
		if(x==null)return;
		x.xu(sts);
	}
	synchronized public void ax_c(xwriter x,String[]a)throws Throwable{
		for(final x1 e:core){
			e.ax_compile(null,null);
		}
		sts.set("compiled");
		if(x==null)return;
		x.xu(sts);
	}
	synchronized public void ax_t(xwriter x,String[]a)throws Throwable{
		int i=0;
		for(final x1 c:core)
			c.ax_n(x,null);
		sts.set("stepped");
	}
	synchronized public void ax_g(xwriter x,String[]a)throws Throwable{
		x.xu(sts.set("going"));
		while(true){
			ax_t(x,a);
			x.flush();
			Thread.sleep(500);
		}
	}
	/**run*/
	synchronized public void ax_u(xwriter x,String[]a)throws Throwable{
		long t0=System.currentTimeMillis();
		long minstr=0;for(final x1 c:core){minstr+=c.mtrinstr;c.sts.clr();}
		final long mframes=core[0].mtrframes;
		long dt;
		final long runms=1000;
		while(true){
			for(final x1 c:core)c.step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(dt>runms)
				break;
		}
		long minstr1=0;for(final x1 c:core)minstr1+=c.mtrinstr;
		final long dminstr=minstr1-minstr;
		final long dmframes=core[0].mtrframes-mframes;
		if(x==null)return;
		sts.set(x1.strdatasize3((long)dminstr*1000/dt)+"ips\n"+x1.strdatasize3((long)dmframes*1000/dt)+"fps");
		x.xu(sts);
		for(final x1 c:core){
			x.xu(c);
			c.xfocusline(x);
		}
		snapshot();
		ram.ax_rfh(x,a);
	}

	synchronized public void ax_r(xwriter x,String[]a)throws Throwable{
		for(final x1 e:core)
			e.ax_r(null,null);
		sts.set("reseted");
		if(x==null)return;
		x.xuo(this);
	}
	synchronized public void ax_s(xwriter x,String[]a)throws Throwable{
		int i=0;
		for(final x1 c:core){
			final path p=req.get().session().path("xn").get("c"+(i++)+".src");
			c.src.txt.to(p);
		}
		sts.set("saved in xn");
		if(x==null)return;
		x.xu(sts);
	}
	private void snapshot()throws Throwable{
//	    final BufferedImage bi=new BufferedImage(ram.width,ram.height,BufferedImage.TYPE_USHORT_GRAY);
//	    final DataBuffer db=bi.getData().getDataBuffer();
//	    for(int i=0;i<ram.size;i++){
//	    	db.setElem(i,0xffffffff);
//	    }
//	    final path p=req.get().session().path("xn/snp/img.png");
//	    ImageIO.write(bi,"png",p.outputstream(false));
	}
}
