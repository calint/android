#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
void serve(int clientsock);
void error(char *msg){
	perror(msg);
	exit(1);
}
int main(int argc,char**argv){
	int serversock,clientsock,pid;
	unsigned int portno,clilen;
	struct sockaddr_in serv_addr,cli_addr;
	serversock=socket(AF_INET,SOCK_STREAM,0);
	if(serversock<0)
		error("ERROR opening socket");
	bzero((char*)&serv_addr,sizeof(serv_addr));
	if(argc>1)
		portno=atoi(argv[1]);
	else
		portno=8083;
	serv_addr.sin_family=AF_INET;
	serv_addr.sin_addr.s_addr=INADDR_ANY;
	serv_addr.sin_port=htons(portno);
	if(bind(serversock,(struct sockaddr*)&serv_addr,sizeof(serv_addr))<0)
		error("ERROR on binding");
	listen(serversock,5);
	printf("public domain c server #1\n   port: %d\n",portno);
	clilen=sizeof(cli_addr);
	while(1){
		puts("accept");
		clientsock=accept(serversock,(struct sockaddr*)&cli_addr,&clilen);
		if(clientsock<0){
			puts("accept error");
			continue;
		}
		pid=fork();
		if(pid<0){
			puts("fork error");
			continue;
		}
		if(pid==0){
			puts("serve");
			close(serversock);
			serve(clientsock);
			exit(0);
		}else{
			close(clientsock);
		}
	}
}
