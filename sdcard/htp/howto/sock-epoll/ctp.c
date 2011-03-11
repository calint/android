#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/epoll.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>
#include <unistd.h>
#define nclients 1024
#define port 8088

struct con{
	int fd;
}server;
void sigexit() {
    close(server.fd);
    signal(SIGINT,SIG_DFL);
    kill(getpid(),SIGINT);
    puts("stopped");
    exit(0);
}
int main(){
	signal(SIGINT,sigexit);
	struct epoll_event events[nclients];
	char ca_buf[1024];
	int ca_buf_len;
	char ca_msg[]="hello world!";
	puts(ca_msg);
	sprintf(ca_buf,"HTTP/1.1 200\r\nContent-Length: %d\r\n\r\n%s",strlen(ca_msg),ca_msg);
	ca_buf_len=strlen(ca_buf);

	struct sockaddr_in srv;
	bzero(&srv,sizeof(srv));
	srv.sin_family=AF_INET;
	srv.sin_addr.s_addr=INADDR_ANY;
	srv.sin_port=htons(port);
	if((server.fd=socket(AF_INET,SOCK_STREAM,0))==-1){perror("server socket");exit(1);}
	if(bind(server.fd,(struct sockaddr*)&srv,sizeof(srv))){perror("server socket bind");exit(1);}
	listen(server.fd,nclients);
	int epfd=epoll_create(nclients);
	if(!epfd){perror("epoll_create\n");exit(1);}
	struct epoll_event ev;
	ev.events=EPOLLIN;
	ev.data.ptr=&server;
	if(epoll_ctl(epfd,EPOLL_CTL_ADD,server.fd,&ev)<0){perror("server listen reg");exit(1);}
	while(1){
		int res=epoll_wait(epfd,events,nclients,-1);
		if(res==-1){perror("epoll_wait");exit(1);}
		int i;		
		for(i=0;i<res;i++){
			struct con*con=(struct con*)events[i].data.ptr;
			if(con->fd==server.fd){
				int clifd=accept(server.fd,NULL,NULL);
				if(clifd==-1){perror("accept");exit(1);}
				//printf("[%d] accept\n",clifd);
				int opts=fcntl(clifd,F_GETFL);
				if(opts<0){perror("fcntl(F_GETFL)\n");exit(1);}
				opts=(opts|O_NONBLOCK);
				if(fcntl(clifd,F_SETFL,opts)){perror("set nonblock");exit(1);}
				struct con*clientcon=calloc(1,sizeof(struct con));
				clientcon->fd=clifd;
				ev.data.ptr=clientcon;
				ev.events=EPOLLIN|EPOLLET;
				if(epoll_ctl(epfd,EPOLL_CTL_ADD,clifd,&ev)){perror("reg client");exit(1);}
				continue;
			}
			//printf("event %x\n",events[i].events);
			con=(struct con*)events[i].data.ptr;
			char buffer[1024];
			int n=recv(con->fd,buffer,1023,0);
			//printf("[%d] received %d B\n",con->fd,n);
			if(n==0){
				printf("[%d] closed\n",con->fd);
				struct con*con=(struct con*)events[i].data.ptr;
				if(epoll_ctl(epfd,EPOLL_CTL_DEL,con->fd,NULL)){perror("closed");exit(1);}
				free(con);
			}else if(n<0){//error
				epoll_ctl(epfd,EPOLL_CTL_DEL,con->fd,NULL);
				close(con->fd);
				free(con);
			}else{
				ssize_t nn=send(con->fd,ca_buf,ca_buf_len,0);
				//printf("[%d] sent  %d of %d B\n",con->fd,nn,ca_buf_len);
				if(nn!=ca_buf_len)
					printf("!! %d: sent %d of %d\n",con->fd,nn,ca_buf_len);					
				//epoll_ctl(epfd,EPOLL_CTL_DEL,con->fd,NULL);
				close(con->fd);
				free(con);
			}
		}
	}	
	return 0;
}
