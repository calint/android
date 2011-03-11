#include<locale.h>
#include<X11/Xft/Xft.h>
#include"dc.h"
dc::dc(){
	setlocale(LC_ALL,"");
	dpy=XOpenDisplay(NULL);
	if(!dpy){
		fprintf(stderr,"!!! could not open display\n");
		return;
	}
	scr=DefaultScreen(dpy);
	dpy_width=DisplayWidth(dpy,scr);
	dpy_height=DisplayHeight(dpy,scr);
	width=dpy_width;
	xlft=0;
	ytop=0;
	dotx=0;
	doty=0;
	ddoty=10;
	win=RootWindow(dpy,scr);
	gc=XCreateGC(dpy,win,0,NULL);
	cmap=DefaultColormap(dpy,scr);
	font=XftFontOpen(dpy,scr,XFT_FAMILY,XftTypeString,"mono-7",XFT_SIZE,XftTypeDouble,7.0,NULL);
	draw=XftDrawCreate(dpy,win,DefaultVisual(dpy,scr),cmap);
	XRenderColor xrendcolwhite={0xffff,0xffff,0xffff,0xffff};
	rendcol=xrendcolwhite;
	XftColorAllocValue(dpy,DefaultVisual(dpy,scr),cmap,&rendcol,&color);
}
dc::~dc(){}
void dc::clrbw(){
	XSetForeground(dpy,gc,BlackPixel(dpy,scr));
	XFillRectangle(dpy,win,gc,xlft,ytop,width,dpy_height);
	XSetForeground(dpy,gc,WhitePixel(dpy,scr));
}
void dc::drwline(const int x0,const int y0,const int x1,const int y1){XDrawLine(dpy,win,gc,xlft+x0,ytop+y0,xlft+x1,ytop+y1);}
void dc::cr(){doty+=ddoty;}
void dc::drwstr(const char*s,const int len){XftDrawStringUtf8(draw,&color,font,xlft+dotx,ytop+doty,(FcChar8*)s,len);}
void dc::drwhr(){doty+=3;XDrawLine(dpy,win,gc,xlft,doty,xlft+width,doty);}
void dc::yinc(const int dy){doty+=dy;}
void dc::flush(){XFlush(dpy);}
int dc::xget()const{return dotx;}
void dc::xset(const int x){dotx=x;}
void dc::xlftset(const int x){xlft=x;}
int dc::yget()const{return doty;}
void dc::yset(const int y){doty=y;}
int dc::wget()const{return width;}
void dc::wset(const int width){this->width=width;}
int dc::wscrget()const{return dpy_width;}
