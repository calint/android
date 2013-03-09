package a.pczero;
import a.x.jskeys;
import b.a;
import b.path;
import b.req;
import b.xwriter;
public class xm extends a{
	static final long serialVersionUID=1;
	void pramble(final xwriter x){
		x.pl("vintage 16 bit computing - bitoid quad core ibobs");
		x.pl("  16b instruction znxr ci.. aaaa dddd ");
		x.pl(" "+x1.strdatasize(ram.size)+" x 16b video ram "+ram.width+"x"+ram.height);
//		x.pl(" "+x1.strdatasize(ram2.size)+" x 16b data");
		x.pl(" "+size+" threads x");
		x.pl("   "+core[0].regs.size+" registers");
		x.pl("   "+x1.strdatasize3(core[0].rom.size)+"x 16b code cache");
		x.pl("   16 sprites");
//		x.pl(" pre wait/resume instructions");
//		x.pl(" and right display functioning");
//		x.pl(" coki3d displays");
		x.nl();
		x1.prambleops(x);
	}
	public ram ram;
//	public ram ram2;
	public a sts;
	final static int size=4;
	private x1[]core=new x1[size];
	{for(int i=0;i<core.length;i++){
		final x1 c=new x1();
		c.ram=ram;
		c.coreid.set(""+(char)('a'+i));
		c.displaybits=16;
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
			x.el(this,"display:inline-table;border:0px solid red;box-shadow:0 0 1em rgba(0,0,0,1)");
//			x.nl().style();
//			x.nl().cssfont("tini","/ttf/tini.ttf");
//			x.nl().css("body","background:#222;font-size:4px;font-family:tini;margin:1em 0 0 1em");
//			x.nl().cssfont("tini","/ttf/tini.ttf");
//			x.nl().styleEnd();
			x.nl().style("body","font-size:11px;margin:0 0 0 1em;color:#444;background:#111;text-align:center");
			final String id=id();
			final jskeys jskeys=new jskeys(x);
			x.nl().script();
			jskeys.add("cS","$x('"+id+" s')");//save
			jskeys.add("cL","$x('"+id+" l')");//load
			jskeys.add("cT","$x('"+id+" t')");//step
			jskeys.add("cR","$x('"+id+" r')");//reset
			jskeys.add("cF","$x('"+id+" f')");//frame
			jskeys.add("cG","$x('"+id+" g')");//go
			jskeys.add("cU","$x('"+id+" u')");//run
			jskeys.add("cO","$x('"+id+" c')");//compile
			jskeys.add("cD","$x('"+ram.id()+" rfh')");
			jskeys.add("cB","$x('"+id+" b')");//run to break point
			x.scriptEnd().nl();
		}else
			x.el(this);
		x.pre();
		pramble(x);
		x.style(ram,"border:1px solid #030;display:inline-table");
		ram.to(x);
//		x.style(ram2,"border:1px solid #300;display:inline-table");
//		ram2.to(x);
		x.nl();
		x.ax(this,"d"," display");
		x.ax(this,"l"," load");
		x.ax(this,"c"," compile");
		x.ax(this,"r"," reset");
		x.ax(this,"f"," frame");
		x.ax(this,"s"," save");
		x.ax(this,"u"," run");
		x.ax(this,"t"," step");
		x.ax(this,"g"," go");
		x.ax(this,"b"," tobrkpoint");
		x.nl();
		x.span(sts,"font-weight:bold");
		x.nl();
		for(final x1 c:core){
			x.style(c,"display:inline-table;text-align:left;vertical-align:top");
			c.to(x);
			x.spc();
		}
		x.elend();
	}
	synchronized public void ax_l(xwriter x,String[]a)throws Throwable{
		int i=0;
		for(final x1 c:core){
			final path p=req.get().session().path(getClass().getName()).get("c"+i+".src");
			if(p.exists()){
				c.src.txt.from(p);
				c.sts.set("loaded "+p.name());
			}else{
				c.src.txt.set("..");
				c.sts.set("loaded default");
			}
			if(x!=null){
				x.xuo(c);
			}
			i++;
		}
		sts.set("loaded");
		if(x==null)return;
		x.xu(sts);
//		for(final x1 c:core)x.xu(c);
	}
	synchronized public void ax_c(xwriter x,String[]a)throws Throwable{
		for(final x1 e:core){
			e.ax_compile(null,null);
		}
		sts.set("compiled");
		if(x==null)return;
		x.xu(sts);
		for(final x1 c:core)x.xu(c);
	}
	synchronized public void ax_t(xwriter x,String[]a)throws Throwable{
		int i=0;
		for(final x1 c:core)
			c.ax_n(x,null);
		if(x==null)return;
		x.xu(sts.set("stepped"));
	}
	synchronized public void ax_d(xwriter x,String[]a)throws Throwable{
		ram.ax_rfh(x,a);
	}

	synchronized public void ax_g(xwriter x,String[]a)throws Throwable{
		x.xu(sts.set("going"));
		while(true){
			ax_t(x,a);
			x.flush();
			Thread.sleep(500);
		}
	}
	/**run to break point*/
	synchronized public void ax_b(xwriter x,String[]a)throws Throwable{
		final long t0=System.currentTimeMillis();
		final long instr0=core[0].mtrinstr;
		for(final x1 c:core)c.sts.clr();
		while(true){
			boolean go=true;
			for(final x1 c:core){
				c.step();
				final int srclno=c.lino.get(c.pcr+1);
				if(c.src.isonbrkpt(srclno)){
					c.sts.set("breakpoint @ "+srclno);
					go=false;
				}
			}
			if(!go)break;
		}
		final long dt=System.currentTimeMillis()-t0;
		final long dinstr=core[0].mtrinstr-instr0;
		sts.set("core0 "+dinstr+" instr, "+dt+" ms");
		if(x==null)return;
		x.xu(sts);
		for(final x1 c:core){
			x.xu(c);
			c.xfocusline(x);
		}
		ram.ax_rfh(x,a);
	}
	/**run*/
	synchronized public void ax_u(xwriter x,String[]a)throws Throwable{
		long t0=System.currentTimeMillis();
		long minstr=0;for(final x1 c:core){minstr+=c.mtrinstr;c.sts.clr();}
		final long mframes=core[0].mtrframes;
		long dt;
		final long runms=1000;
		x.xu(sts.set("running "+runms+" ms"));
		x.flush();
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
		sts.set(x1.strdatasize3((long)dminstr*1000/dt)+"ips, "+x1.strdatasize3((long)dmframes*1000/dt)+"fps");
		x.xu(sts);
		for(final x1 c:core){
			x.xu(c);
			c.xfocusline(x);
		}
		snapshot();
		ram.ax_rfh(x,a);
	}
	/**run core frame*/
	synchronized public void ax_f(xwriter x,String[]a)throws Throwable{
		long t0=System.currentTimeMillis();
		final int waitforcore=0;
		long minstr=core[waitforcore].mtrinstr;
		long dt;
		final long runms=1000;
		boolean timedout=false;
		while(true){
			for(final x1 c:core)c.step();
			final long t1=System.currentTimeMillis();
			dt=t1-t0;
			if(core[waitforcore].wasrerun){
				core[waitforcore].wasrerun=false;
				break;
			}
			if(dt>runms){
				timedout=true;
				break;
			}
		}
		final long dminstr=core[waitforcore].mtrinstr-minstr;
		if(x==null)return;
		sts.set("core "+waitforcore+": "+(timedout?"timed-out ":"")+dminstr+" instr, "+dt+"ms, "+dminstr/ram.size+" instr/px");
		x.xu(sts);
		for(final x1 c:core){
			x.xu(c);
			c.xfocusline(x);
		}
//		snapshot();
		ram.ax_rfh(x,a);
	}

	synchronized public void ax_r(xwriter x,String[]a)throws Throwable{
		for(final x1 c:core){
			c.ax_r(null,null);
			c.src.edit=false;
		}
		core[0].ax_r(null,null);//hack copies rom to ram
		sts.set("reseted");
		if(x==null)return;
		x.xu(sts);
		for(final x1 c:core)x.xu(c);
		sts.set("reseting");
		x.xu(sts);
		x.flush();
		ram.ax_rfh(x,null);
		x.xu(sts.set("reseted"));
	}
	//. translucent floating div with source over coki3d dislpay
	synchronized public void ax_s(xwriter x,String[]a)throws Throwable{
		int i=0;
		for(final x1 c:core){
			final path p=req.get().session().path(getClass().getName()).get("c"+(i++)+".src");
			c.src.txt.to(p);
		}
		sts.set("saved in "+getClass().getName());
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
	protected void ev(xwriter x,a from,Object o)throws Throwable{
		if(x==null){
			final int i=((Integer)o).intValue();
			core[i].notify=true;
		}else super.ev(x,from,o);
	}
}
