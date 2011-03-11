#include<X11/Xlib.h>
#include<stdlib.h>
#include<stdio.h>
#include<unistd.h>
#include"frameless-cfg.h"
typedef int xdesk;
typedef struct {
	Window w;
	int rx,ry;
	unsigned int rw,rh;
	int gx,gy;
	unsigned int gw,gh;
	unsigned int border,depth;
	int vh;
	xdesk desk;
	int desk_x;
	char bits;
} xwin;
#define xwinsct 128
static xwin wins[xwinsct];
static FILE*flog;
static Display*dpy;
static Window root;
static xdesk dsk=0;
static int wincount=0;
static int scr=0;
static int scr_w=0;
static int scr_h=0;
static unsigned int key=0;
static int winslip=7;
static xwin*winfocused=NULL;
static int dragging=0;
static char
		*ix_evnames[LASTEvent]=
				{"unknown","unknown",//0
				"KeyPress","KeyRelease",//2
				"ButtonPress","ButtonRelease",//4
				"MotionNotify",//6
				"EnterNotify","LeaveNotify",//7 LeaveWindowMask LeaveWindowMask
				"FocusIn","FocusOut",//9 from XSetFocus
				"KeymapNotify",//11
				"Expose","GraphicsExpose","NoExpose",//12
				"VisibilityNotify","CreateNotify","DestroyNotify","UnmapNotify","MapNotify",//15
				"MapRequest","ReparentNotify","ConfigureNotify","ConfigureRequest","GravityNotify","ResizeRequest","CirculateNotify","CirculateRequest","PropertyNotify","SelectionClear","SelectionRequest","SelectionNotify","ColormapNotify","ClientMessage","MappingNotify","GenericEvent"};

static xwin*xwinget(Window w){
//	if(w==root){
//		fprintf(flog," ? winget root\n");
//		fflush(flog);
//	}
//	char*name;
//	if(!XFetchName(dpy,w,&name)){
//		fprintf(flog,"xwinget: can not fetch name on  %p\n",(void*)w);
//		fflush(flog);
//		return NULL;
//	}else{
//		fprintf(flog,"xwinget %s\n",name);
//		fflush(flog);
//	}

	int n;
	xwin*xw=NULL;
	int firstavail=-1;
	for(n=0;n<xwinsct;n++){
		if(wins[n].bits&1){
			if(wins[n].w==w){
				return &wins[n];
			}
		}else{
			if(firstavail==-1){
				firstavail=n;
			}
		}
	}
	if(firstavail==-1){
		fprintf(flog,"!!! no free windows\n");
		fflush(flog);
		sleep(5);
		return xwinget(w);
	}
	xw=&wins[firstavail];
	xw->bits|=1;//allocated
	wincount++;
	xw->w=w;
	xw->vh=0;
	xw->desk=dsk;
	return xw;
}
static void xwinfocus(xwin*this){
	XWindowAttributes xwinattr;
	if(!XGetWindowAttributes(dpy,this->w,&xwinattr)){
		fprintf(flog,"focustry   can not retrieve attributes for %p   skipped\n",(void*)this);
		fflush(flog);
		return;
	}
	if(!xwinattr.root==root)
		return;
	if(winfocused){
		XSetWindowBorder(dpy,winfocused->w,0x00000000);
	}
	XSetInputFocus(dpy,this->w,RevertToParent,CurrentTime);
	XSetWindowBorder(dpy,this->w,0x00008000);
	winfocused=this;
}
static void xwinraise(xwin*this){
	XRaiseWindow(dpy,this->w);
}
static void focusfirstondesk(){
	int k;
	for(k=0;k<xwinsct;k++){
		xwin*w=&wins[k];
		if((w->bits&1)&&(w->desk==dsk)){
			xwinraise(w);
			xwinfocus(w);
			//			fprintf(flog,"focusfirstondesk found %d\n",k);
			//			fflush(flog);
			return;
		}
	}
	winfocused=NULL;
	//	fprintf(flog,"focusfirstondesk found no windows\n");
	//	fflush(flog);
}
static void xwinfree(Window w){
	fprintf(flog,"xwinfree  %x\n",(int)w);
	xwin*xw=xwinget(w);
	fprintf(flog,"xwinfree  fond  xwin %p\n",(void*)xw);
	xw->bits&=0xfe;//free
	wincount--;
	if(winfocused==xw){
		winfocused=NULL;
//		focusfirstondesk();
	}
}
static void xwingeom(xwin*this){
	Window wsink;
	XGetGeometry(dpy,this->w,(Window*)&wsink,&this->gx,&this->gy,&this->gw,&this->gh,&this->border,&this->depth);
}
static void xwingeomset(xwin*this,int x,int y,int w,int h){
	XMoveResizeWindow(dpy,this->w,x,y,w,h);
}
static void xwingeomset2(xwin*this){
	XMoveResizeWindow(dpy,this->w,this->gx,this->gy,this->gw,this->gh);
}
//static void xwinlower(xwin*this){
//	XLowerWindow(dpy,this->w);
//}
static void xwingeomcenter(xwin*this){
	xwingeom(this);
	int nx=(scr_w-this->gw)>>1;
	int ny=(scr_h-this->gh)>>1;
	xwingeomset(this,nx,ny,this->gw,this->gh);
}
static void xwingeomwider(xwin*this){
	xwingeom(this);
	int nw=((this->gw<<2)+this->gw)>>2;
	int nx=this->gx-((nw-this->gw)>>1);
	xwingeomset(this,nx,this->gy,nw,this->gh);
}
static void xwingeomthinner(xwin*this){
	xwingeom(this);
	int nw=((this->gw<<1)+this->gw)>>2;
	int nx=this->gx-((nw-this->gw)>>1);
	xwingeomset(this,nx,this->gy,nw,this->gh);
}
static void xwinclose(xwin*this){
	XEvent ke;
	ke.type=ClientMessage;
	ke.xclient.window=this->w;
	ke.xclient.message_type=XInternAtom(dpy,"WM_PROTOCOLS",True);
	ke.xclient.format=32;
	ke.xclient.data.l[0]=XInternAtom(dpy,"WM_DELETE_WINDOW",True);
	ke.xclient.data.l[1]=CurrentTime;
	XSendEvent(dpy,this->w,False,NoEventMask,&ke);
}
static void xwintogglefullscreen(xwin*this){
	if(this->vh&3){
		xwingeomset(this,this->gx,this->gy,this->gw,this->gh);
		this->vh=0;
	}else{
		xwingeom(this);
		xwingeomset(this,0,0,scr_w,scr_h);
		this->vh=3;
	}
}
static void xwintogglefullheight(xwin*this){
	if(this->vh&1){
		int gy=this->gy;
		int gh=this->gh;
		xwingeom(this);
		xwingeomset(this,this->gx,gy,this->gw,gh);
	}else{
		xwingeom(this);
		xwingeomset(this,this->gx,0,this->gw,scr_h);
	}
	this->vh^=1;
}
static void xwintogglefullwidth(xwin*this){
	if(this->vh&2){
		int gx=this->gx;
		int gw=this->gw;
		xwingeom(this);
		xwingeomset(this,gx,this->gy,gw,this->gh);
	}else{
		xwingeom(this);
		xwingeomset(this,0,this->gy,scr_w,this->gh);
	}
	this->vh^=2;
}
static void xwinhide(xwin*this){
	xwingeom(this);
	this->desk_x=this->gx;
	int slip=rand()%winslip;
	this->gx=(scr_w-13+slip);
	xwingeomset2(this);
}
static void xwinshow(xwin*this){
	this->gx=this->desk_x;
	xwingeomset2(this);
}
static void xwinbump(xwin*this,int r){
	xwingeom(this);
	this->gx+=(rand()%r)-(r>>1);
	this->gy+=(rand()%r)-(r>>1);
	xwingeomset2(this);
}
static int _xwinix(xwin*this){
	if(this==NULL)
		return -1;
	int k;
	for(k=0;k<xwinsct;k++)
		if(this==&wins[k])
			return k;
	return -1;
}
static int _focustry(int k){
	xwin*w=&wins[k];
	if((w->bits&1)&&(w->desk==dsk)){
		if(w->w==root){
			fprintf(flog,"focustry on window %p   skipped, isroot\n",(void*)w);
			fflush(flog);
			return 0;
		}
		char*name;
		XWindowAttributes xwinattr;
		if(!XGetWindowAttributes(dpy,w->w,&xwinattr)){
			fprintf(flog,"focustry   can not retrieve attributes for %p   skipped\n",(void*)w);
			fflush(flog);
		}
		if(!XFetchName(dpy,w->w,&name)){
			fprintf(flog,"focustry on window %p   skipped, no name\n",(void*)w);
			fflush(flog);
			return 0;
		}
		//		fprintf(flog,"focustry found %d   %s\n",k,name);
		//		fflush(flog);
		xwinraise(w);
		xwinfocus(w);
		return 1;
	}
	return 0;
}
static void focusnext(){//?
	int k0=_xwinix(winfocused);
	int k=k0;
	while(++k<xwinsct){
		if(_focustry(k)){
			return;
		}
	}
	k=0;
	while(k<=k0){
		if(_focustry(k)){
			return;
		}
		k++;
	}
	focusfirstondesk();
}
static void focusprev(){
	int k0=_xwinix(winfocused);
	int k=k0;
	while(--k>=0){
		if(_focustry(k)){
			return;
		}
	}
	k=xwinsct;
	while(--k>=0){
		if(_focustry(k)){
			return;
		}
	}
	focusfirstondesk();
}
static void togglefullscreen(){
	if(!winfocused)
		return;
	xwintogglefullscreen(winfocused);
}
static void togglefullheight(){
	if(!winfocused)
		return;
	xwintogglefullheight(winfocused);
}
static void togglefullwidth(){
	if(!winfocused)
		return;
	xwintogglefullwidth(winfocused);
}
static void deskshow(int dsk,int dskprv){
	int n;
	for(n=0;n<xwinsct;n++){
		xwin*xw=&wins[n];
		if(!(xw->bits&1))
			continue;
		if(xw->w==0)
			continue;
		if(xw->w==root)
			continue;
		if(xw->desk==dskprv)
			xwinhide(xw);
		if(xw->desk==dsk)
			xwinshow(xw);
	}
}
static void mixermastervoldown(){
	system(bin_voldown);
}
static void mixermastervolup(){
	system(bin_volup);
}
static void desksave(int dsk,FILE*f){
	int n=0;
	fprintf(flog,"desktop %d\n",dsk);
	fflush(flog);
	for(n=0;n<xwinsct;n++){
		xwin*w=&wins[n];
		if(!(w->bits&1))
			continue;
		xwingeom(w);
		char**argv;
		int argc;
		XGetCommand(dpy,w->w,&argv,&argc);
		if(argc>0)
			while(argc--)
				fprintf(flog,"%s ",*argv++);
		else
			fprintf(flog,"%x",(unsigned int)w->w);
		fprintf(f,"   %x %dx%d+%d+%d\n",(unsigned int)w->w,w->rw,w->rh,w->rx,w->ry);
		fflush(f);
	}
}
static int errorhandler(Display*d,XErrorEvent*e){
	char buffer_return[1024]="";
	int length=1024;
	XGetErrorText(d,e->error_code,buffer_return,length);
	fprintf(flog,"!!! x11 error from %x: %d\n",(unsigned int)e->resourceid,e->error_code);
	fprintf(flog,"%s\n",buffer_return);
	fflush(flog);
	return 0;
}
int main(int argc,char**args){
	while(argc--)
		puts(*args++);
	_Xdebug=1;
	puts("window manager frameless");
	XSetErrorHandler(errorhandler);
	int n;
	//	flog=stdout;
	flog=fopen(logfile,"a");
	if(!flog)
		exit(1);

	dpy=XOpenDisplay(NULL);
	if(!dpy)
		exit(2);

	scr=DefaultScreen(dpy);
	scr_w=DisplayWidth(dpy,scr);
	scr_h=DisplayHeight(dpy,scr);
	fprintf(flog,"%dx%d\n",scr_w,scr_h);
	fflush(flog);

	for(n=0;n<xwinsct;n++)
		wins[n].bits=0;

	root=DefaultRootWindow(dpy);
	xwinget(root);

	//	Window winrt,winpt;
	//	Window*lschld;
	//	unsigned int lsnchld;
	//	XQueryTree(dpy,root,&winrt,&winpt,&lschld,&lsnchld);
	//	while(lsnchld--){
	//		char*wname;
	//		XFetchName(dpy,*lschld,&wname);
	//		if(wname)
	//			puts(wname);
	//		xwin*xw=xwinget(*lschld++);
	//		xwintogglefullscreen(xw);
	//		xwinfocus(xw);
	//	}
	//	XFlush(dpy);

	XGrabKey(dpy,AnyKey,Mod4Mask,root,True,GrabModeAsync,GrabModeAsync);
	XGrabKey(dpy,AnyKey,Mod4Mask+ShiftMask,root,True,GrabModeAsync,GrabModeAsync);
	XGrabKey(dpy,122,0,root,True,GrabModeAsync,GrabModeAsync);//voldown
	XGrabKey(dpy,123,0,root,True,GrabModeAsync,GrabModeAsync);//volup
	XGrabKey(dpy,107,0,root,True,GrabModeAsync,GrabModeAsync);//print
	XSelectInput(dpy,root,SubstructureNotifyMask);
	srand(0);
	XEvent ev;
	while(!XNextEvent(dpy,&ev)){
		xwin*xw;
		switch(ev.type){
		default:
			fprintf(flog,"%s   %p %s  unhandled\n",ix_evnames[ev.type],(void*)ev.xany.window,ev.xany.window==root?"*":"");
			fflush(flog);
			break;
		case ClientMessage:
		case ReparentNotify:
		case CreateNotify:
		case DestroyNotify:
		case ConfigureNotify:
		case MapRequest:
//			fprintf(flog,"unhandled  %s\n",ix_evnames[ev.type]);
			break;
		case MapNotify:
			fprintf(flog,"mapnotify   %p\n",(void*)ev.xmap.window);
			if(ev.xmap.window==root||ev.xmap.window==0||ev.xmap.override_redirect)
				break;
			xw=xwinget(ev.xmap.window);
			xwingeomcenter(xw);
			xwinfocus(xw);
			XGrabButton(dpy,AnyButton,Mod4Mask,xw->w,True,ButtonPressMask,GrabModeAsync,GrabModeAsync,None,None);
			XSelectInput(dpy,xw->w,EnterWindowMask);
			break;
		case UnmapNotify:
			fprintf(flog,"unmapnotify\n");
			if(ev.xmap.window==root||ev.xmap.window==0||ev.xmap.override_redirect){
				fprintf(flog,"did not unmap window %d",(int)ev.xmap.window);
				fflush(flog);
				break;
			}
			xwinfree(ev.xmap.window);
//			xwinfree(ev.xmap.event);
			break;
		case EnterNotify:
			if(dragging)
				break;
			fprintf(flog,"enter notify: win=%p  root=%p   subwin=%p\n",(void*)ev.xcrossing.window,(void*)ev.xcrossing.root,(void*)ev.xcrossing.subwindow);
			xw=xwinget(ev.xcrossing.window);
			xwinfocus(xw);
			break;
		case KeyPress:
			key=ev.xkey.keycode;
			xw=xwinget(ev.xkey.subwindow);
			switch(key){
			case 53://x
				system(bin_sticky);
				break;
			case 54://c
				system(bin_console);
				break;
			case 107://sysrq prntscr
				system(bin_screenshot);
				break;
			case 70: {//f4
				fprintf(flog,"forking\n");
				fflush(flog);
				pid_t pid=fork();
				fprintf(flog,"forked %d\n",pid);
				fflush(flog);
				if(pid==0){//child
					//					int r=execl("/usr/bin/scrot","-s","scr--%Y-%m-%d---%H-%M-%S.jpg","-e","mkdir -p ~/img/&&mv $f ~/img/&&feh ~/img/$f",NULL);
					//					fprintf(flog,"screenshot rect: %d\n",r);
					//					fflush(flog);
					//					exit(r);
					int r=execlp("scrot","-s",NULL);
					fprintf(flog," after exec:  %d\n",r);
					fflush(flog);
					//exit(r);
					return r;
				}else if(pid<0){//could not fork
					fprintf(flog,"could not fork\n");
					fflush(flog);
				}
				//#define bin_screenshot_area "scrot -s 'scr--%Y-%m-%d---%H-%M-%S.jpg' -e 'mkdir -p ~/img/&&mv $f ~/img/&&feh ~/img/$f'"
				break;
			}
			case 24://q
				system(bin_binmenu);
				break;
			case 31://i
				system(bin_browser);
				break;
			case 58://m
				system(bin_media);
				break;
			case 41://f
				system(bin_files);
				break;
			case 55://v
				system(bin_mixer);
				break;
			case 26://e
				system(bin_editor);
				break;
			case 9://esc
				if(winfocused)
					xwinclose(winfocused);
				break;
			case 39://s
				if(winfocused)
					xwingeomcenter(winfocused);
				break;
			case 25://w
				if(winfocused){
					if(ev.xkey.state&ShiftMask)
						xwingeomthinner(winfocused);
					else
						xwingeomwider(winfocused);
				}
				break;
			case 56://border
				if(winfocused)
					xwinbump(winfocused,59);
				break;
			case 12://3
				togglefullscreen();
				break;
			case 13://4
				togglefullheight();
				break;
			case 14://5
				togglefullwidth();
				break;
			case 15://6
				xwinbump(winfocused,200);
				break;
			case 16://7
				system(bin_ide);
				break;
			case 113://left
				focusprev();
				break;
			case 114://right
				focusnext();
				break;
			case 119://del
				XKillClient(dpy,ev.xkey.subwindow);
				break;
			case 118://insert
			case 127://pause
				desksave(dsk,flog);
				break;
			case 74://volume down F8
				mixermastervoldown();
				break;
			case 75://volume up F9
				mixermastervolup();
				break;
			case 96://F12
				XSetCloseDownMode(dpy,RetainPermanent);
				XCloseDisplay(dpy);
				break;

				int dskprv;
			case 38://a
			case 111://up
				dskprv=dsk;
				dsk++;
				if(ev.xkey.state&ShiftMask)
					if(winfocused){
						winfocused->desk=dsk;
						xwingeom(winfocused);
						winfocused->desk_x=winfocused->gx;
						xwinraise(winfocused);
					}
				deskshow(dsk,dskprv);
				break;
			case 40://d			
			case 116://down
				dskprv=dsk;
				dsk--;
				if(ev.xkey.state&ShiftMask)
					if(winfocused){
						winfocused->desk=dsk;
						xwingeom(winfocused);
						winfocused->desk_x=winfocused->gx;
						xwinraise(winfocused);
					}
				deskshow(dsk,dskprv);
				break;
			}
			break;
		case KeyRelease:
			if(key==ev.xkey.keycode)
				key=0;
			break;

			XButtonEvent buttonevstart;
		case ButtonPress:
			dragging=1;
			xw=xwinget(ev.xbutton.window);
			xwinfocus(xw);
			XGrabPointer(dpy,xw->w,True,PointerMotionMask|ButtonReleaseMask,GrabModeAsync,GrabModeAsync,None,None,CurrentTime);
			xwinraise(xw);
			xwingeom(xw);
			buttonevstart=ev.xbutton;
			break;
		case MotionNotify:
			while(XCheckTypedEvent(dpy,MotionNotify,&ev))
				;
			int xdiff=ev.xbutton.x_root-buttonevstart.x_root;
			int ydiff=ev.xbutton.y_root-buttonevstart.y_root;
			int nx=xw->gx+xdiff;
			int nw=xw->gw+xdiff;
			int ny=xw->gy+ydiff;
			int nh=xw->gh+ydiff;
			if(xw->vh&2){
				nx=0;
				nw=scr_w;
			}
			if(xw->vh&1){
				ny=0;
				nh=scr_h;
			}
			switch(key){
			default:
				xwingeomset(xw,nx,ny,xw->gw,xw->gh);
				break;
			case 27://r
				if(nw<0)
					nw=0;
				if(nh<0)
					nh=0;
				xwingeomset(xw,xw->gx,xw->gy,nw,nh);
				break;
			}
			break;
		case ButtonRelease:
			dragging=0;
			xw=xwinget(ev.xbutton.window);
			xw->desk=dsk;
			XUngrabPointer(dpy,CurrentTime);
			break;
		}
	}
	return 0;
}
