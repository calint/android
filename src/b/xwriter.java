package b;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
public final class xwriter{
	public static String encquot(final String text){if(text==null)return"";return text.replaceAll("\"","&quot;");}
	private OutputStream os;
	public xwriter(final OutputStream os){this.os=os;}
	public xwriter(){os=new ByteArrayOutputStream();}
	public OutputStream outputstream(){return os;}
	public xwriter p(final String s){if(s==null)return this;try{os.write(b.tobytes(s));}catch(final IOException e){throw new Error(e);}return this;}
	public xwriter nl(){return p('\n');}
	public xwriter p(final char n){return p(Character.toString(n));}
	public xwriter p(final int n){return p(Integer.toString(n));}
	public xwriter p(final float n){return p(Float.toString(n));}
	public xwriter p(final long n){return p(Long.toString(n));}
	public xwriter pl(final String s){return p(s).nl();}
	public xwriter tag(final String name){return p("<").p(name).p(">");}
	public xwriter tag(final String name,final String id){return p("<").p(name).p(" id=").p(id).p(">");}
	public xwriter tago(final String name){return p("<").p(name);}
//	public xwriter tago(final String name,final String id){return p("<").p(name).p(" id=").p(id);}
	public xwriter attrdef(final a a){final String wid=a.id();return attr("id",wid);}
	public xwriter attr(final String name,final int value){return p(" ").p(name).p("=").p(value);}
	public xwriter attr(final String name,final String value){return p(" ").p(name).p("=\"").p(xwriter.encquot(value)).p("\"");}
	public xwriter attr(final String name){return p(" ").p(name);}
	public xwriter tagoe(){return p(">");}
	public xwriter tagoec(){return p("/>");}
	public xwriter tagEnd(final String name){return p("</").p(name).p(">");}
	public xwriter a(final String href){return tago("a").attr("href",href).tagoe();}
	public xwriter aEnd(){return tagEnd("a");}
	public xwriter a(final String href,final String txt){return a(href).p(txt).aEnd();}
	public xwriter ax(final a a,final String ax,final String html){
		final String wid=a.id();
		p("<a href=\"javascript:$x('").p(wid);
		if(!b.isempty(ax))
			p(" ").p(ax);
		p("')\"");
		if(ax==null)//? 
			p(" id=").p(wid);//?
		p(">").p(html).p("</a>");
		return this;
	}
	public xwriter ax(final a a,final String html){return ax(a,null,html);}
	public xwriter axBgn(final a a,final String args){return tago("a").attrdef(a).attr("href","javascript:$x('"+a.id()+" "+args+"')").tagoe();}
	public xwriter axBgn(final a a){return tago("a").attrdef(a).attr("href","javascript:$x('"+a.id()+"')").tagoe();}
	public xwriter axEnd(){return tagEnd("a");}
	public xwriter br(){return tag("br").nl();}
	public xwriter div(final String cls){return tago("div").attr("class",cls).tagoe();}
	public xwriter divEnd(){return tagEnd("div");}
	public xwriter focus(final a a){return script().p("$f('").p(a.id()).p("')").scriptEnd();}
	public xwriter inputInt(final a a){return tago("input").attr("value",a.toString()).attrdef(a).attr("type","text").attr("class","nbr").attr("size",5).attr("onchange","this._changed=true").tagoe();}
	public xwriter inputLng(final a a){return inputInt(a);}
	public xwriter pre(){return tag("pre");}
	public xwriter pre(final String cls){return tago("pre").attr("class",cls).tagoe();}
	public xwriter preEnd(){return tagEnd("pre");}
	public xwriter script(){return tag("script");}
	public xwriter scriptEnd(){return tagEnd("script");}
	public xwriter span(final a a){tago("span").attr("id",a.id()).tagoe();try{a.to(new osltgt(os));}catch(Throwable e){throw new Error(e);};return spanEnd();}
	public xwriter spanh(final a a){tago("span").attr("id",a.id()).tagoe();try{a.to(os);}catch(Throwable e){throw new Error(e);}return spanEnd();}
	public xwriter spanEnd(){return tagEnd("span");}
	public xwriter table(){return tag("table");}
	public xwriter table(final String cls){return tago("table").attr("class",cls).tagoe();}
	public xwriter tableEnd(){return tagEnd("table");}
	public xwriter style(){return tag("style");}
	public xwriter styleEnd(){return tagEnd("style");}
	public xwriter td(){return tag("td");}
	public xwriter td(final String cls){if(cls==null||cls.length()==0) return td();return tago("td").attr("class",cls).tagoe();}
	public xwriter tdEnd(){return tagEnd("td");}
	public xwriter th(){return tag("th");}
	public xwriter th(final int colspan){return tago("th").attr("colspan",colspan).tagoe();}
	public xwriter th(final String cls){return tago("th").attr("class",cls).tagoe();}
	public xwriter thEnd(){return tagEnd("th");}
	public xwriter tr(){return tag("tr");}
	public xwriter tr(final String cls){return tago("tr").attr("class",cls).tagoe();}
	public xwriter trEnd(){return tagEnd("tr");}
	public xwriter ul(){return tag("ul");}
	public xwriter ulEnd(){return tagEnd("ul");}
	public xwriter li(){return tag("li");}
	public xwriter li(final String cls){if(cls==null)return li();return tago("li").attr("class",cls).tagoe();}
	public xwriter code(){return tag("code");}
	public xwriter codeEnd(){return tagEnd("code");}
	public xwriter rend(final a a)throws Throwable{if(a==null)return this;a.to(this);return this;}
	public xwriter inputText(final a a,final String stylecls,final a axonreturn,final String axp){return inputText(a,stylecls,axonreturn,axp,null);}
	public xwriter inputText(final a a,final String stylecls,final a axonreturn,final String axp,final String txt){
		tago("input").attr("value",txt==null?a.toString():txt).attrdef(a).attr("type","text");
		if(stylecls!=null)
			attr("class",stylecls);
		if(axonreturn!=null){
			final String ax=axonreturn.id()+(axp!=null?(" "+axp):"");
			attr("onkeypress","return $r(event,this,'"+ax+"')");
		}
		attr("onchange","ui.qpb(this.id)");
		return tagoe();
	}
	public xwriter inputTextArea(final a a){return inputTextArea(a,null);}
	public xwriter inputTextArea(final a a,final String cls){
		tago("textarea").attrdef(a).attr("onchange","ui.qpb(this.id);return true;");
		if(cls!=null)
			attr("class",cls);
		tagoe();
		//? fld.to(new osltgt(x.outputStream()))
		String s=a.toString();
		s=s.replaceAll("\\<","&lt;");//?
		s=s.replaceAll("\\>","&gt;");
		return p(s).tagEnd("textarea");
	}
	public xwriter flush(){try{os.flush();}catch (IOException e){throw new Error(e);}return this;}
	public String toString(){return os.toString();}
	public xwriter hr(){return tag("hr");}
	public xwriter spc(){return p(' ');}
	public xwriter tab(){return p('\t');}
	public xwriter enter(){return p('\r');}
	public xwriter bell(){return p('\07');}
	public xwriter p(final CharSequence cs){return p(cs.toString());}
	public xwriter title(final String s){return script().xtitle(s).scriptEnd();}
	public xwriter css(final String cls,final String stl){return p(cls).p("{").p(stl).p("}");}
	public xwriter css(final a a,final String stl){return p("#").p(a.id()).p("{").p(stl).p("}");}
	public xwriter inputax(final a a,final String stylecls,final a ax,final String axp){
		tago("input").attr("value",a.toString()).attrdef(a).attr("type","text");
//		tago("x").attrdef(a).attr("contenteditable","true");
		if(stylecls!=null)
			attr("class",stylecls);
//		final String onfocus="$d(this+' onfocus');var r=document.createRange();r.selectNodeContents(this);r.collapse(false);var s=window.getSelection();s.removeAllRanges();s.addRange(r);";
//		attr("onfocus",onfocus);
		attr("onfocus","this.setSelectionRange(this.value.length,this.value.length)");
		final StringBuilder sb=new StringBuilder();
		sb.append((ax==null?a:ax).id());
		if(!b.isempty(axp))
			sb.append(" ").append(axp);
		final String sbs=sb.toString();
//		attr("oninput","this._changed=true;if(this.innerHTML=='<br>')this.innerHTML='';$x('"+sbs+"')");
		attr("oninput","ui.qpb(this.id);$x('"+sbs+"');return true;");
//		attr("onblur","if(!this._changed)return true;$x('"+sbs+"')");
//		return tagoe().p(a.toString()).tagEnd("x");
		return tagoe();
	}
	public xwriter inputax(final a a){return inputax(a,null,a.parent(),null);}

	public xwriter script(final String s){return script().p(s).scriptEnd();}
	public xwriter output(final a a){return tago("output").attr("id",a.id()).tagoe().tagEnd("output");}
	public xwriter spc(final int n){for(int i=0;i<n;i++)spc();return this;}
	public xwriter jsstr(final String s){try{new osjsstr(os).write(b.tobytes(s));}catch(final IOException e){throw new Error(e);}return this;}

	public xwriter xu(final a a){return p("$s('").p(a.id()).p("','").jsstr(a.toString()).pl("');");}
	public xwriter xu(final a a,final String s){return p("$s('").p(a.id()).p("','").jsstr(s).pl("');");}
	public xwriter xu(final a a,final path p)throws IOException{p.to(xub(a));return xube();}
	public xwriter xu(final String id,final String s){return p("$s('").p(id).p("','").jsstr(s).pl("');");}
	public xwriter xub(final a a){p("$s('").p(a.id()).p("','");return new xwriter(new osjsstr(os));}
	public xwriter xube(){return pl("');");}
	public xwriter xua(final a a)throws Throwable{a.to(xub(a));return xube();}
	public xwriter xinterval(final a a,final String ax,final int ms){return p("setInterval(\"$x('").p(a.id()).p(" ").p(ax).p("')\",").p(ms).pl(");");}
	private xwriter xhide(final a a,final boolean hide){
		//? bug style block  display:inherit
		return p("$('").p(a.id()).p("').style.display='").p(hide?"none":"inline").pl("';");
	}
	public xwriter xhide(final a a){return xhide(a,true);}
	public xwriter xshow(final a a){return xhide(a,false);}
	public xwriter xalert(final String s){return p("ui.alert('").jsstr(s).pl("');");}
	public xwriter xreload(){return pl("location.reload(true);");}
	public xwriter xfocus(final a a){return p("$f('").p(a.id()).pl("');");}
	public xwriter xtitle(final String s){return p("$t('").jsstr(b.isempty(s,"")).pl("');");}
	public xwriter xp(final a a,final String s){return p("$p('").p(a.id()).p("','").jsstr(s).pl("');");}
	public xwriter ul(final String cls){return tago("ul").attr("class",cls).tagoe();}
	public xwriter cssfont(final String name,final String url){
		return p("@font-face{font-family:").p(name).p(";src:url(").p(url).p(");}");
	}

	
//	public xwriter img(final String srcdim){
//		final String[]sa=srcdim.split("\\.");
//		if(sa.length>2){
//			final String[]da=sa[sa.length-2].split("x");
//			if(da.length>2)
//				p("<img width=").p(da[0]).p(" height=").p(da[1]).p(" src=").p(srcdim).p(">");
//			return this;
//		}
//		return p("<img src=").p(srcdim).p(">");
//	}
}
