ui={}
ui.is_dbg=false;
ui.axconwait=false;
$d=function(v){
	if(!ui.is_dbg)return;
	var w=ui._dbgw;
	if(!w||w.closed){
		w=window.open('','uidbg','width=400,height=640');
		if(!w){
			if(!ui.nodbgwinalrt){
				alert('cannot open debug window');
				ui.nodbgwinalrt=true;
			}
			window.status=v;
			return;
		}
		ui._dbgw=w;
		w.document.writeln('<pre>');
		window.focus();
	}
	var s=''+v;
	s=s.replace(/</g,'&lt;').replace(/>/g,'&gt;');
	w.document.writeln(s);
	w.scrollBy(0,9999);
}
$=function(eid){return document.getElementById(eid);}
$s=function(eid,txt){
	var e=$(eid);
	if(!e){
		$d(eid+' notfound');
		return;
	}
	if(e.nodeName=="INPUT"||e.nodeName=="TEXTAREA"||e.nodeName=="OUTPUT"){
		e.value=txt;
		$b(e);
	}else{
		e.innerHTML=txt;
		if(e.contentEditable=="true")
			$b(e);
	}
}
$o=function(eid,txt){$(eid).outerHTML=txt;}
$p=function(eid,txt){
	var e=$(eid);
	if(e.nodeName=="INPUT"||e.nodeName=="TEXTAREA"||e.nodeName=="OUTPUT"){
		e.value+=txt;
		$b(e);
	}else{
		e.innerHTML+=txt;
		if(e.contentEditable=="true")
			$b(e);
	}
}
$l=function(){if(ui.keys)document.onkeyup=ui.onkey;}
$a=function(eid,a,v){$(eid).setAttribute(a,v);}
$r=function(ev,ths,axpb){if(!ev)ev=window.event;$b(ths);if(ev.keyCode!=13)return true;$x(axpb);return false;}
$f=function(eid){var e=$(eid);if(!e)return;if(e.focus)e.focus();}
$t=function(s){document.title=s;}
ui.alert=function(msg){alert(msg);}
ui._clnfldvl=function(s){return s.replace(/\r\n/g,'\n').replace(/\r/g,'\n');}
ui._hashKey=function(event){
	var kc=(event.shiftKey?'s':'')+(event.ctrlKey?'c':'')+(event.altKey?'a':'')+(event.metaKey?'m':'')+String.fromCharCode(event.keyCode);
	$d(kc);
	return kc;
}
ui.keys=[];
ui.onkey=function(ev){
	if(!ev)ev=window.event;
	var cmd=ui.keys[ui._hashKey(ev)];
	if(cmd)eval(cmd);
}



ui._onreadystatechange=function(){
	$d(" * readState:"+this.readyState);
	switch(this.readyState){
	case 1:
		if(this._hasopened)break;this._hasopened=true;//? firefox quirkfix1
		$d(" * server connection established");
		this._t0=new Date().getMilliseconds();
		this.setRequestHeader('Content-Type','text/plain; charset=utf-8');
//		$d("~~~~~~~ ~~~~~~~ ~~~~~~~ ~~~~~~~ ")
		$d(this._pd);
//		$d("~~~~~~~ ~~~~~~~ ~~~~~~~ ~~~~~~~ ")
		ui.req._jscodeoffset=0;
		this.send(this._pd);
		break;
	case 2:
		$d(" * request received");
		break;
	case 3:
		$d(" * processing request  ("+this.status+")");
		var s=this.responseText.charAt(this.responseText.length-1);
		if(s!='\n'){
			$d(" * not eol "+(this.responseText.length-this._jscodeoffset));
			//$d(s);
			//$d(this.responseText);
			break;
		}
		var jscode=this.responseText.substring(this._jscodeoffset);
		$d(" * run "+jscode.length+" bytes");
		$d(jscode);
//		$d("~~~~~~~ ~~~~~~~ ~~~~~~~ ~~~~~~~ ")
		this._jscodeoffset+=jscode.length;
		eval(jscode);
		break;
	case 4:
		$d(" * request finished   ("+ui.req.status+")");
		this._hasopened=null;//? firefox quirkfix1
		this._pd=null;
		ui._pbls=[];
	
	
		var jscode=this.responseText.substring(this._jscodeoffset);
		if(jscode.length>0){
			$d(" * run "+jscode.length+" bytes");
			$d(jscode);
//			$d("~~~~~~~ ~~~~~~~ ~~~~~~~ ~~~~~~~ ")
			this._jscodeoffset+=jscode.length;
			eval(jscode);
		}
		this._dt=new Date().getMilliseconds()-this._t0;
		$d("~~~~~~~ ~~~~~~~ ~~~~~~~ ~~~~~~~ ")
		$d("done in "+this._dt+" ms");
		break;		
	}
}
ui._pbls=[];
ui.qpb=function(e){
	$d("ui.qpb("+e.id+")");
	if(ui.qpbhas(e.id))return;
	ui._pbls[e.id]=e.id;
	$d("   added");
}
$b=ui.qpb;
ui.qpbhas=function(id){return id in ui._pbls;}
ui._axc=1;
$x=function(pb){
	ui._axc++;
	$d("\n\najax req #"+ui._axc);
	var post='$='+pb+'\r';
	for(var id in ui._pbls){
		var e=$(id)
		$d(id+" "+(e?e.nodeName:""));
		post+=e.id+'=';			
		if(e.value!==undefined)
			post+=ui._clnfldvl(e.value);
		else{
			post+=ui._clnfldvl(e.innerHTML);
		}
		post+='\r';
	}
	$d("~~~~~~~ ~~~~~~~ ~~~~~~~ ~~~~~~~ ")
	if(!ui.req){
		ui.req=new XMLHttpRequest();
		ui.req.onreadystatechange=ui._onreadystatechange;
		$d(" * new connection");
	}else{
		$d(" * reusing connection");
		var count=0;
		while(ui.req.readyState==1||ui.req.readyState==2||ui.req.readyState==3){
			if(ui.axconwait){
				$d("  * busy, waiting");
				//setTimeout("goto bbt",1000);bbt:
				alert("connection busy. waiting.");//? 
				count++;
				if(count>3)
					throw "waiting cancelled";
			}else{
				$d("   * busy, cancelling");
				ui.req.abort();
				ui.req._hasopened=null;//? firefox quirkfix1
			}
		}	
		$d(" * has connection");
	}
	ui.req._pd=post;
	ui.req.open('post',location.href,true);
}
