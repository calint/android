#include<signal.h>
#include<locale.h>
#include<sys/time.h>
#include<X11/cursorfont.h>
#include<X11/Xft/Xft.h>
#include<X11/keysymdef.h>
#include"node.h"
#include"dc.h"
static int scr;
static Display*dpy;
static Window win;
static int win_w=150;
static int win_h=400;
static GC gc;
static Colormap cmap;
static int counter=0;
static XftFont*font;
static XftDraw*draw;
static XftColor color;
static const char title[]="«·xii·» ĸœđ ¹²³ ¼ ⅝";
static char bbuf[1024];
static Atom wm_delete_window;
static int dragging=0;
static node*firstnode=NULL;
static void on_draw(){
	sprintf(bbuf,"%d hello%s %s",counter,(counter==1?"":"s"),title);
	XClearWindow(dpy,win);
	XftDrawStringUtf8(draw,&color,font,10,20,(FcChar8*)bbuf,strlen(bbuf));
	int i=0;
	node*nd;
	for(nd=firstnode;nd!=NULL;nd=nd->getnxt()){
		bbuf[i++]=(char)((int)nd->getdata());
	}
	bbuf[i]=0;
	XftDrawStringUtf8(draw,&color,font,11,40,(FcChar8*)bbuf,strlen(bbuf));
	XFlush(dpy);
}
static void on_tick(const int s){
	printf("%d\n",s);
	counter++;
	on_draw();
}
int main(int argc,char**args){
	while(argc--)puts(*args++);
	setlocale(LC_ALL,"");
	dpy=XOpenDisplay(NULL);
	if(!dpy){fprintf(stderr,"!!! could not open display\n");return 1;}
	wm_delete_window=XInternAtom(dpy,"WM_DELETE_WINDOW",False);
	scr=DefaultScreen(dpy);
	cmap=DefaultColormap(dpy,scr);
	win=XCreateSimpleWindow(dpy,DefaultRootWindow(dpy),0,0,win_w,win_h,0,BlackPixel(dpy,scr),BlackPixel(dpy,scr));
	XStoreName(dpy,win,title);
	gc=XCreateGC(dpy,win,0,NULL);
	cmap=DefaultColormap(dpy,scr);
	font=XftFontOpen(dpy,scr,XFT_FAMILY,XftTypeString,"mono-8",XFT_SIZE,XftTypeDouble,7.0,NULL);
	draw=XftDrawCreate(dpy,win,DefaultVisual(dpy,scr),cmap);
	XRenderColor xrendcolwhite={0xffff,0xffff,0xffff,0xffff};
	XftColorAllocValue(dpy,DefaultVisual(dpy,scr),cmap,&xrendcolwhite,&color);
	Cursor cursor=XCreateFontCursor(dpy,XC_heart);
	XDefineCursor(dpy,win,cursor);
	XSelectInput(dpy,win,StructureNotifyMask|ExposureMask|KeyPressMask|KeyReleaseMask|ButtonPressMask|ButtonReleaseMask|EnterWindowMask|LeaveWindowMask|FocusChangeMask|PointerMotionMask|DestroyNotify);
	XMapWindow(dpy,win);

	firstnode=new node();

	struct sigaction sa;
	memset(&sa,0,sizeof(sa));
	sa.sa_handler=&on_tick;
	sigaction(SIGALRM,&sa,NULL);
	struct itimerval timer;
	timer.it_value.tv_sec=1;
	timer.it_value.tv_usec=0;
	timer.it_interval.tv_sec=1;
	timer.it_interval.tv_usec=0;
	setitimer(ITIMER_REAL,&timer,NULL);

	int go=1;
	while(go){
		XEvent ev;
		XNextEvent(dpy,&ev);
		switch(ev.type){
		case ClientMessage:
			if((unsigned int)ev.xclient.data.l[0]==wm_delete_window){
				printf("ClientMessage: Quit\n");
				go=0;
	        }else{
	        	printf("ClientMessage: unknown\n");
	        }
			break;
		case EnterNotify:
			printf("EnterNotify\n");
			break;
		case LeaveNotify:
			printf("LeaveNotify\n");
			break;
		case FocusIn:
			printf("FocusIn %d\n",ev.xfocus.detail);
			break;
		case Expose:
			printf("Expose %d,%d+%dx%d  %d\n",ev.xexpose.x,ev.xexpose.y,ev.xexpose.width,ev.xexpose.height,ev.xexpose.count);
			on_draw();
			break;
		case FocusOut:
			printf("FocusOut %d\n",ev.xfocus.detail);
			break;
		case MapNotify:
			printf("MapNotify\n");
			break;
		case UnmapNotify:
			printf("UnmapNotify\n");
			break;
		case VisibilityNotify:
			printf("VisibilityNotify\n");
			break;
		case ConfigureNotify:
			printf("ConfigureNotify  %d,%d+%dx%d %d\n",ev.xconfigure.x,ev.xconfigure.y,ev.xconfigure.width,ev.xconfigure.height,ev.xconfigure.border_width);
			break;
		case ButtonPress:
			printf("ButtonPress %d\n",ev.xbutton.button);
			if(ev.xbutton.button==1){
				dragging=1;
			}
			break;
		case ButtonRelease:
			printf("ButtonRelease %d\n",ev.xbutton.button);
			if(ev.xbutton.button==1){
				dragging=0;
			}
			break;
		case DestroyNotify:
			printf("DestroyNotify\n");
			go=0;
			break;
		case KeyPress:{
			printf("KeyPress %d\n",ev.xkey.keycode);
//			XEvent*nev=calloc(1,sizeof(XEvent));
//			memcpy(nev,&ev,sizeof(XEvent));
//			nodesetdata(nd,nev);
			KeySym keysim;
			XComposeStatus composestatus;
			char buf[32];
			XLookupString(&ev.xkey,buf,32,&keysim,&composestatus);
			if(keysim==XK_BackSpace){
				node*nd=firstnode->getnxt();
				if(nd){
					delete firstnode;
					firstnode=nd;
					on_draw();
					break;
				}
			}
			node*nd=new node();
			nd->setdata((void*)((int)buf[0]));
			firstnode->addbefore(nd);
			firstnode=nd;
			on_draw();
			break;}
		case KeyRelease:
			printf("KeyRelease %d\n",ev.xkey.keycode);
			break;
		case MotionNotify:
			printf("MotionNotify %d,%d\n",ev.xmotion.x,ev.xmotion.y);
			if(dragging){
				XSetForeground(dpy,gc,WhitePixel(dpy,scr));
				XDrawLine(dpy,win,gc,ev.xmotion.x,ev.xmotion.y,ev.xmotion.x,ev.xmotion.y);
			}
			break;
		case KeymapNotify:
			printf("KeymapNotify %s\n",ev.xkeymap.key_vector);
			break;
		default:
			printf("Unknown event: %d\n",ev.type);
			break;
		}
	}
	XCloseDisplay(dpy);
	puts("XCloseDisplay");
	return 0;
}
