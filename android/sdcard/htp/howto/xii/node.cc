#include<stdlib.h>
#include<stdio.h>
#include"node.h"
node::node(){}
node::~node(){
	if(prv&&nxt){
		prv->nxt=nxt;
		nxt->prv=prv;
	}else if(prv&&!nxt){
		prv->nxt=NULL;
	}else if(!prv&&nxt){
		nxt->prv=NULL;
	}
}
void node::print(FILE*file){fprintf(file,"node{%p}",this);}
node*node::getnxt()const{return nxt;}
node*node::getprv()const{return prv;}
//struct node*nodegetup(struct node*o){return o->up;}
//struct node*nodegetchlds(struct node*o){return o->chlds;}
void*node::getdata()const{return data;}
void node::setdata(void*data){this->data=data;}
void node::addafter(node*nxt){
	nxt->nxt=nxt;
	nxt->prv=this;
	nxt=nxt;
}
void node::addbefore(node*prv){
	prv->nxt=this;
	prv->prv=prv;
	prv=prv;
}
