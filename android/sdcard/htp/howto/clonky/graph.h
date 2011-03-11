#ifndef GRAPH_H
#define GRAPH_H
#include"dc.h"
struct graph;
struct graph*graphnew(const int nvalues);
void graphdel(struct graph*this);
void graphprint(const struct graph*this);
void graphdraw(const struct graph*this,struct dc*dc,const int ysclshft);
void graphdraw2(const struct graph*this,struct dc*dc,const int height,const long long maxvalue);
void graphaddvalue(struct graph*this,const long long value);
#endif
