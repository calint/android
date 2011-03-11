ui=function(){}
document.ui=ui;
ui.is_dbg=false;
ui.dbg=function(v){
	if(!ui.is_dbg)
		return;
	var w=ui._dbgw;
	if(!w||w.closed){
		w=window.open('','uidbg','width=400,height=640');
		window.focus();
		ui._dbgw=w;
		w.document.writeln('<pre>');
	}
	var s=''+v;
	s=s.replace(/</g,'&lt;').replace(/>/g,'&gt;');
	w.document.writeln(s);
	w.scroll(0,123456);
}
//ui.otos=function(o){if(!o)return 'null';var s=o+'\n';for(var name in o)s+=name+'='+o[name]+'\n';return s;}
//ui.oitos=function(o){if(!o)return 'null';var s=o+'\n';for(var name in o)s+=name+'\n';return s;}
//ui.pb=function(v){f=document.forms[0];f._pb.value=v;f.submit();}
ui.e=function(eid){return document.getElementById(eid);}
ui.e_seth=function(eid,txt){ui.e(eid).innerHTML=txt;}
ui.e_setv=function(eid,txt){ui.e(eid).value=txt;}
ui.e_seta=function(eid,a,v){ui.e(eid).setAttribute(a,v);}
ui.e_focus=function(eid){ui.e(eid).focus();}
//ui._itc=function(){document._itc=true;}
//ui._itcr=function(){if(document._itc){document._itc=null;return true;}return false;}
ui._cr=function(ev,ths,axpb){if(!ev)ev=window.event;ths._changed=true;if(ev.keyCode!=13)return true;ui.ax(axpb);return false;}
//ui._tvs=function(oid,wid){ui._itc();var e=ui.e(wid);v=e.value;var i=v.indexOf(oid+' ');if(i==-1){e.value=v+oid+' ';return true;}e.value=v.substring(0,i)+v.substring(i+(oid+' ').length);return false;}
//ui._tvr=function(cls,oid){if(ui._itcr())return;location.href='/web.edit+'+cls+'+'+oid;}
ui.focus=function(wid){var e=ui.e(wid);if(!e)return;if(e.focus)e.focus();}
//ui.select=function(wid){var e=ui.e(wid);if(!e)return;if(e.select)e.select();}
//ui.focusSelect=function(wid){var e=ui.e(wid);if(!e)return;if(!e.select)return;e.select();if(!e.focus)return;e.focus();}
ui.alert=function(msg){alert(msg);}
//ui.eval=function(js){return eval(js);}
ui._clnfldvl=function(s){return s.replace(/\r\n/g,'\n').replace(/\r/g,'\n');}
ui._onreadystatechange=function(){
	ui.dbg("onreadystate: "+ui.req.readyState);
	switch(ui.req.readyState){
	case 1:
		if(ui.req._hasopened)  //browerserquirkfix
			break;             //
		ui.req._hasopened=true;//
		ui.dbg("1. opened");
		ui.req.setRequestHeader('Content-Type','text/plain; charset=utf-8');
		ui.dbg(ui.req._pd);
		ui.req.send(ui.req._pd);
		break;
	case 2:
		//ui.dbg("2. response headers received:"+ui.req.status);
		break;
	case 3:
		ui.dbg("3. data received. status:"+ui.req.status);
		var jscode=ui.req.responseText.substring(ui.req._jscodeoffset);
		ui.req._jscodeoffset+=jscode.length;
		ui.dbg(jscode);
		eval(jscode);
		break;
	case 4:
		ui.dbg("4. finished receive. status:"+ui.req.status);
		ui.dbg("-- -- -- -- -- -- -- -- -- -- --");
		delete ui.req._pd;
		delete ui.req._hasopened;//browerserquirkfix
		break;		
	}
}
ui.ax=function(pb) {
	ui.dbg("-- -- -- -- -- -- -- -- -- -- --");
	var post='_ax='+pb+'\r';
	var frm=document.forms[0];
	for(var n=0;n<frm.elements.length;n++){
		var e=frm.elements[n];
		if(!e._changed){
			ui.dbg(e.id+" not changed");
			continue;
		}
		post+=e.id+'=';			
		if(e.value)
			post+=ui._clnfldvl(e.value);
		post+='\r';
		e._changed=null;
	}
	if(!ui.req){
		ui.req=new XMLHttpRequest();
		ui.dbg(" * connection created");
		ui.req.onreadystatechange=ui._onreadystatechange;
	}else{
		ui.dbg(" * connection found");
		if(ui.req.readyState==1||ui.req.readyState==2||ui.req.readyState==3){
			ui.dbg(" * in use, cancelling");
			ui.req.abort();
//			ui.alert("server is processing");
//			return;
		}	
	}
	ui.req._pd=post;
	ui.req._jscodeoffset=0;
	ui.req.open('post',location.href,true);
}
ui._hashKey=function(event){
	var kc=(event.shiftKey?'s':'')+(event.ctrlKey?'c':'')+(event.altKey?'a':'')+(event.metaKey?'m':'')+String.fromCharCode(event.keyCode);
	ui.dbg(kc);
	return kc;
}
ui.onkey=function(ev){
	if(!ev)
		ev=window.event;
	var cmd=ui.keys[ui._hashKey(ev)];
	if(cmd)
		eval(cmd);
}
ui.onload=function(){
	ui.dbg("onload body");
	if(ui.keys){
		ui.dbg("found key hook");
		document.onkeyup=ui.onkey;
	}
}
