#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
static const int serve_buf_size=1024;
static char serve_buf[1024];
static int serve_buf_len=0;
static int serve_buf_ix=0;
static int serve_wrote=0;
static int serve_state=0;
//static int serve_connection_keep_alive=0;
static int serve_parse(){
	char*p=serve_buf;
	while(serve_buf_ix<serve_buf_len){
		char c=*p;
		//		printf("%x  %c   %d\n",c,c,serve_state);
		serve_buf_ix++;
		p++;
		switch(serve_state){
		case 0://method
			if(c==' '){
				serve_state=1;
			}
			break;
		case 1://uri
			if(c==' ')
				serve_state=2;
			break;
		case 2://protocol
			if(c=='\n')
				serve_state=3;
			break;
		case 3://header key
			if(c=='\n')
				serve_state=5;
			else if(c==':')
				serve_state=4;
			break;
		case 4://header value
			if(c=='\n')
				serve_state=3;
			break;
		case 5://content
			//			puts("done parse,write");
			return 0;
		}
	}
	if(serve_state==5){
		//		puts("done parse,write");
		return 0;
	}
	return 1;
}
static void serve_read(int sock){
	serve_buf_len=read(sock,serve_buf,serve_buf_size);
	if(serve_buf_len<0){
		perror("serve_read error");
		exit(1);
	}
	serve_buf_ix=0;
}
static const char*serve_hello="HTTP/1.1 200\r\n\r\nI got your message\0";
void serve(int clientsock){
	serve_state=0;
	while(serve_parse())
		serve_read(clientsock);
	serve_wrote=write(clientsock,serve_hello,strlen(serve_hello));
	if(serve_wrote<0){
		perror("serve write error");
	}
	puts("close");
	close(clientsock);
}

