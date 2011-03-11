#ifndef DC_H
#define DC_H
struct dc;
struct dc*dcnew();
void dcdel(struct dc*this);
void dcclrbw(struct dc*this);
void dcdrwline(struct dc*this,const int x0,const int y0,const int x1,const int y1);
void dcdrwhr(struct dc*this);
void dcdrwstr(struct dc*this,const char*s,const int len);
void dccr(struct dc*this);
void dcyinc(struct dc*this,const int dy);
void dcflush(const struct dc*this);
int dcxget(const struct dc*this);
void dcxset(struct dc*this,const int x);
int dcyget(const struct dc*this);
void dcyset(struct dc*this,const int px);
int dcwget(const struct dc*this);
int dcwscrget(const struct dc*this);
void dcwset(struct dc*this,const int width);
void dcxlftset(struct dc*this,const int x);
#endif
