#ifndef NODE_H
#define NODE_H
class node{
public:
	node();
	~node();
	void print(FILE*file);
	node*getnxt()const;
	node*getprv()const;
	//struct node*nodegetup(struct node*o);
	//struct node*nodegetchlds(struct node*o);
	void*getdata()const;
	void setdata(void*data);
	void addafter(node*nxt);
	void addbefore(node*prv);
private:
	node*nxt;
	node*prv;
//	struct node*chlds;
	void*data;

};
#endif
