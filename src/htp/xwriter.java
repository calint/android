package htp;
import java.io.IOException;
import java.io.OutputStream;
public final class xwriter{
	public static String encForValue(final String text){
		if(text==null)
			return "";
		String s=text;
		s=s.replaceAll("\"","&quot;");
		return s;
	}
	private OutputStream os;
	public xwriter(final OutputStream os){this.os=os;}
	public OutputStream outputStream(){return os;}
	public xwriter p(final String s){
		if(s==null)
			return this;
		try{
			os.write(htp.tobytes(s));
		}catch(Throwable e){
			throw new Error(e);
		}
		return this;
	}
	public xwriter nl(){return p("\n");}
	public xwriter p(final char n){return p(Character.toString(n));}
	public xwriter p(final int n){return p(Integer.toString(n));}
	public xwriter p(final float n){return p(Float.toString(n));}
	public xwriter p(final long n){return p(Long.toString(n));}
	public xwriter pl(final String string){return p(string).nl();}
	public xwriter tag(final String name){return p("<").p(name).p(">");}
	public xwriter tago(final String name){return p("<").p(name);}
	public xwriter attr(final String name,final int value){return p(" ").p(name).p("=").p(value);}
	public xwriter attr(final String name,final String value){return p(" ").p(name).p("=\"").p(xwriter.encForValue(value)).p("\"");}
	public xwriter attrdef(final wt w){String wid=w.wid();return attr("id",wid).attr("name",wid);}
	public xwriter tagoe(){return p(">");}
	public xwriter tagEnd(final String name){return p("</").p(name).p(">");}
	public xwriter aBgn(final String href){return tago("a").attr("href",href).tagoe();}
	public xwriter aEnd(){return tagEnd("a");}
	public xwriter a(final String href,final String txt){return aBgn(href).p(txt).aEnd();}
	public xwriter action_ax(final wt w,final String args,final String title){return tago("a").attr("href","javascript:ui.ax('"+w.wid()+" "+args+"')").tagoe().p(title).p("</a>");}
	public xwriter a_ax(final wt w,final String ax,final String html){return p("<a href=\"javascript:ui.ax('").p(w.wid()).p(" ").p(ax).p(" ").p(html).p("')\">");}
	public xwriter actionBgn_ax(final wt w,final String args){return tago("a").attrdef(w).attr("href","javascript:ui.ax('"+w.wid()+" "+args+"')").tagoe();}
	public xwriter actionEnd_ax(){return tagEnd("a");}
	public xwriter br(){return tag("br").nl();}
	public xwriter div(final String cls){return tago("div").attr("class",cls).tagoe();}
	public xwriter divEnd(){return tagEnd("div");}
	public xwriter focus(final wt wt){return script().x_focus(wt).scriptEnd();}
	public xwriter inputInt(final wt fld){return tago("input").attr("value",fld.toString()).attrdef(fld).attr("type","text").attr("class","nbr").attr("size",5).tagoe();}
	public xwriter inputLng(final wt fld){return inputInt(fld);}
	public xwriter inputText(final wt w,final String stylecls,final wt axonreturn,final String axp){
		tago("input").attr("value",w.toString()).attrdef(w).attr("type","text");
		if(stylecls!=null)
			attr("class",stylecls);
		if(axonreturn!=null)
			attr("onkeypress","return ui._cr(event,this,'"+axonreturn.wid()+" "+axp+"')");
		return tagoe();
	}
	public xwriter inputTextArea(final wt fld,final String cls){
		tago("textarea").attr("class",cls).attrdef(fld).attr("onkeypress","this._changed=true").tagoe();
//		fld.to(new ostxa(os));
		String s=fld.toString();
		s=s.replaceAll("\\<","&lt;");
		s=s.replaceAll("\\>","&gt;");
		return p(s).tagEnd("textarea");
	}
	public xwriter pre(){return tag("pre");}
	public xwriter pre(final String cls){return tago("pre").attr("class",cls).tagoe();}
	public xwriter preEnd(){return tagEnd("pre");}
	public xwriter script(){return tag("script");}
	public xwriter scriptEnd(){return tagEnd("script");}
	public xwriter span(final wt f){tago("span").attrdef(f).tagoe();try{f.to(new osltgt(os));}catch(Throwable e){throw new Error(e);};return spanEnd();}
	public xwriter spanHtml(final wt f){tago("span").attrdef(f).tagoe();try{f.to(os);}catch(Throwable e){throw new Error(e);}return spanEnd();}
	public xwriter spanEnd(){return tagEnd("span");}
	public xwriter table(){return tag("table");}
	public xwriter table(final String cls){return tago("table").attr("class",cls).tagoe();}
//	public xwriter table(final String attr,final String value){return tago("table").attr(attr,value).tagoe();}
	public xwriter tableEnd(){return tagEnd("table");}
	public xwriter style(){return tag("style");}
	public xwriter styleEnd(){return tagEnd("style");}
	public xwriter td(){return tag("td");}
	public xwriter td(final String cls){return tago("td").attr("class",cls).tagoe();}
	public xwriter tdEnd(){return tagEnd("td");}
	public xwriter th(){return tag("th");}
	public xwriter th(final String cls){return tago("th").attr("class",cls).tagoe();}
	public xwriter thEnd(){return tagEnd("th");}
	public xwriter tr(){return tag("tr");}
	public xwriter tr(final String cls){return tago("tr").attr("class",cls).tagoe();}
	public xwriter trEnd(){return tagEnd("tr");}
	public xwriter ul(){return tag("ul");}
	public xwriter ulEnd(){return tagEnd("ul");}
	public xwriter li(){return tag("li");}
	public xwriter li(final String cls){return tago("li").attr("class",cls).tagoe();}
	public xwriter wt(final wt e) throws Throwable{e.to(this);return this;}
	public xwriter x_alert(final String msg){p("ui.alert('");try{new osjsstr(os).write(htp.tobytes(msg));}catch (IOException e){throw new Error(e);}return p("');");}
	public xwriter x_reload(){return p("location.reload(true);");}
	public xwriter x_focus(final wt w){return p("ui.e_focus('").p(w.wid()).p("');");}
	public void flush()throws IOException{os.flush();}
	public String toString(){return os.toString();}
}
