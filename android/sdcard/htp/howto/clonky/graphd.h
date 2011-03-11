#ifndef GRAPHD_H
#define GRAPHD_H
#include"dc.h"
struct graphd;
struct graphd*graphdnew(const int nvalues);
void graphddel(struct graphd*this);
void graphdprint(const struct graphd*this);
void graphddraw(const struct graphd*this,struct dc*dc,const int height,const long long maxvalue);
void graphdaddvalue(struct graphd*this,const long long value);
#endif
