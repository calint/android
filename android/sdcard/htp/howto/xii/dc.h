#ifndef DC_H
#define DC_H
class dc{
public:
	dc();
	~dc();
	void clrbw();
	void drwline(const int x0,const int y0,const int x1,const int y1);
	void drwhr();
	void drwstr(const char*s,const int len);
	void cr();
	void yinc(const int dy);
	int xget()const;
	void xset(const int x);
	int yget()const;
	void yset(const int y);
	int wget()const;
	int wscrget()const;
	void wset(const int width);
	void xlftset(const int x);
	void fontset(const char*name,const float size);
	void flush();
private:
	int scr;
	Display*dpy;
	Window win;
	GC gc;
	Colormap cmap;
	XftFont*font;
	XftDraw*draw;
	XftColor color;
	unsigned int dpy_width;
	unsigned int dpy_height;
	unsigned int width;
	int xlft;
	int ytop;
	int ddoty;
	int doty;
	int dotx;
	XRenderColor rendcol;
};
#endif
