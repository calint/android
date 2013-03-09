package a.pczero;
import a.x.jskeys;
import b.a;
import b.path;
import b.req;
import b.xwriter;
final public class x4 extends a{
	private void pramble(final xwriter x){
		for(int k=0;k<48;k++)x.p('-');x.nl();
		x.pl(" vintage multi core computing - 20 bit");
		for(int k=0;k<48;k++)x.p('-');x.nl();
		x.pl("  shared source");
		x.pl("  64 x 20b code");
		x.pl("  "+x1.strdatasize(ram.size)+" x 16b shared ram");
		x.pl("  "+core1.regs.size+" x 16b regs");
		x.pl("  4 cores");
		x.pl("  op zncr ix.. aaaa dddd");
//		x.el("border:0px dotted grey;text-align:left;display:block;width:48em;margin-left:auto;margin-right:auto");
		x1.prambleops(x);
		for(int k=0;k<48;k++)x.p('-');x.nl();
	}
	private static String rom_src="pczerox.rom.src";
	static final long serialVersionUID=1;
//	x1[]core=new x1[4];
//	{for(int i=0;i<core.length;i++)core[i]=new x1();}
	public x1 core1;
	public x1 core2;
	public x1 core3;
	public x1 core4;
	public ram vram;
	public a src;
	public a sts;
	public x4(){core1.mode=core2.mode=core3.mode=core4.mode=1;
	core1.ram=core2.ram=core3.ram=core4.ram=vram;}
	public void to(final xwriter x)throws Throwable{
		x.el(this);
		if(pt()==null){
			x.nl().script();
			final jskeys jskeys=new jskeys(x);
			final String id=id();
			jskeys.add("cS","$x('"+id+" save')");//save
			jskeys.add("cL","$x('"+id+" l')");//load
			jskeys.add("cT","$x('"+id+" n')");//step
			jskeys.add("cG","$x('"+id+" g')");//go
			jskeys.add("cR","$x('"+id+" r')");//reset
			x.scriptEnd();
			x.nl().style("body","text-align:center;color:#666;box-shadow:0 0 17px rgba(0,0,0,.5);border-radius:1px;padding:1em;margin-left:auto;margin-right:auto;background:#444;width:40em;border-right:1px dotted black;border-left:1px dotted black");
			x.nl().style();
			x.nl().css("a","color:#004");
			x.nl().css("a","-webkit-transition: all .4s ease-in-out;");
			x.nl().css("a","-moz-transition: all .4s ease-in-out;");
			x.nl().css("a","-o-transition: all .4s ease-in-out;");
			x.nl().css("a","transition: all .4s ease-in-out;");
			x.nl().css("a:focus,a:hover,a:active","color:red;text-shadow:0 0 .1em rgba(0,0,0,.5);");
			x.nl().styleEnd();
		}
		x.nl().pre();
		x.ax(this,"l"," load");
		x.ax(this,"r"," reset");
		x.ax(this,"g"," go");
		x.ax(this,"n"," step");
		x.spc().span(sts);
		x.nl().style("table.x4 tr td","padding-right:1em");
		x.nl().style("table.x4 tr td:first-child","padding-right:1em;text-align:right");
		x.nl().table("x4").tr();
		x.td();
		x.p("display").nl();
		vram.to(x);
		x.style(vram,"display:block;border-top:1px dotted #322;border-right:1px dotted #222;width:16em;height:16em;");//margin-left:auto;margin-right:auto
		x.td();
		core1.rendpanel(x);
		x.td();
		core2.rendpanel(x);
		x.td();
		core3.rendpanel(x);
		x.td();
		core4.rendpanel(x);
		x.tr();
		x.td();
		x.p("source").ax(this,"l"," load").ax(this,"save"," save").nl();
		x.inputTextArea(src);
		x.style(src,"color:#666;width:16em;height:64em;resize:none;border-right:1px dotted #222;border-top:1px dotted #223;");
		x.td();
		core1.rendrom(x);
		x.td();
		core2.rendrom(x);
		x.td();
		core3.rendrom(x);
		x.td();
		core4.rendrom(x);
		x.tableEnd();
		x.elend();
		if(pt()==null){
			x.el("color:#000;text-align:left;margin:1em;#333");
			pramble(x);
			x.elend();
		}
		x.style(this,"font-size:4px;font-family:tini");
	}
	public void ax_r(xwriter x,String[]a)throws Throwable{
		core1.ax_r(x,a);
		core2.ax_r(x,a);
		core3.ax_r(x,a);
		core4.ax_r(x,a);
		x.xu(vram);
		x.xu(sts.set("reseted"));
	}
	public void ax_l(xwriter x,String[]a)throws Throwable{
		final path p=req.get().session().path(rom_src);
		if(p.exists())		
			x.xu(src.from(p));
		else
			x.xu(src.from(getClass().getResourceAsStream(rom_src)));
		
		x.xu(sts.set("load compile.")).flush();
		x.xu(core1.coreid.set("a"));
		core1.src.set(src.toString());
		core1.ax_compile(x,a);
		
		x.xu(sts.set("load compile..")).flush();
		x.xu(core2.coreid.set("b"));
		core2.src.set(src.toString());
		core2.ax_compile(x,a);
		
		x.xu(sts.set("load compile...")).flush();
		x.xu(core3.coreid.set("c"));
		core3.src.set(src.toString());
		core3.ax_compile(x,a);
		
		x.xu(sts.set("load compile....")).flush();
		x.xu(core4.coreid.set("d"));
		core4.src.set(src.toString());
		core4.ax_compile(x,a);
		
		x.xu(sts.set("compiled")).flush();

		x.xu(core1.rom);
		x.xu(core2.rom);
		x.xu(core3.rom);
		x.xu(core4.rom);

//		ax_r(x,a);
		
		x.xu(sts.set("file "+p.name())).flush();
	}
	public void ax_save(xwriter x,String[]a)throws Throwable{
		final path p=req.get().session().path(rom_src);
		src.to(p);
		ax_l(x,a);
		x.xu(sts.set("file "+p.name())).flush();
	}
	public void ax_g(xwriter x,String[]a)throws Throwable{
		while(true){
			core1.step();
			core2.step();
			core3.step();
			core4.step();
			if(x==null)return;
			x.xuo(core1).xuo(core2).xuo(core3).xuo(core4);

			core1.rom.xfocusline(x);
			core2.rom.xfocusline(x);
			core3.rom.xfocusline(x);
			core4.rom.xfocusline(x);
			x.xu(vram);
			x.xu(sts.set("stepped "+core1.lino.get(core1.pcr)));
//			x.xu(sts.set("going"));
			x.flush();
			Thread.sleep(500);
		}
	}
}
