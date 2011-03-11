#include"tmr.h"
#include"dc.h"
#include"graph.h"
#include"graphd.h"
#include"main-cfg.h"
#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<unistd.h>
#include<ctype.h>
#include<dirent.h>
#include<time.h>
static char*keysheet[]={
	"ĸey",
	"+c               console",
	"+f                 files",
	"+e                editor",
	"+m                 media",
	"+v                 mixer",
	"+i              internet",
	"+x                sticky",
	"+q              binaries",
	"+prtsc          snapshot",
	"",
	"đesktop",
	"+up                   up",
	"+down               down",
	"+shift+up        move-up",
	"+shift+down    move-down",
	"",
	"window",
	"+esc               close",
	"+b                  bump",
	"+s                center",
	"+w                 wider",
	"+W               thinner",
	"+r                resize",
	"+3            fullscreen",
	"+4           full height",
	"+5            full width",
	"+6   i-am-bored-surprise",
	"...                  ...",
	NULL};
static struct dc*dc;
static struct graph*graphcpu;
static struct graph*graphbat;
static struct graph*graphmem;
static struct graphd*graphwifi;
static unsigned int counter=0;
static unsigned int cpu_total_last=0;
static unsigned int cpu_usage_last=0;
static char bbuf[1024];
static char line[256];
static void sysvaluestr(char*path,char*value,int size){
	FILE*file=fopen(path,"r");
	if(!file){
		*value=0;
		return;
	}
	char fmt[16];
	sprintf(fmt,"%%%ds\\n",size);
	fscanf(file,fmt,value);
	char*p=value;
	while(*p){
		*p=tolower(*p);
		p++;
	}
	fclose(file);
}
static void sysvalueint(char*path,int*num){
	FILE*file=fopen(path,"r");
	if(!file){
		*num=0;
		return;
	}
	fscanf(file,"%d\n",num);
	fclose(file);
}
static void sysvaluelng(char*path,long long*num){
	FILE*file=fopen(path,"r");
	if(!file){
		*num=0;
		return;
	}
	fscanf(file,"%llu\n",num);
	fclose(file);
}
static void strcompactspaces(char *s){
	char*d=s;
	int last_char_was_space=0;
	while(*s){
		*d=*s;
		d++;
		if(*s==' ')
			last_char_was_space=1;
		else
			last_char_was_space=0;
		s++;
		if(last_char_was_space){
			while(*s==' ')
				s++;
			last_char_was_space=0;
		}
	}
	*d=0;
}
static void qdir(char*path,void f(char*)){
	DIR*dir;
	struct dirent *dirent;
	dir=opendir(path);
	if(dir==NULL)
		return;
	while((dirent=readdir(dir))!=NULL){
		f(dirent->d_name);
	}
	closedir(dir);
}
static void netdir(char*filename){
	if(!filename)
		return;
	if(*filename=='.')
		return;
	char path[256];

	*path=0;
	strcat(path,"/sys/class/net/");
	strcat(path,filename);
	strcat(path,"/operstate");
	char operstate[64];
	sysvaluestr(path,operstate,64);

	*path=0;
	strcat(path,"/sys/class/net/");
	strcat(path,filename);
	strcat(path,"/statistics/rx_bytes");
	long long rx;
	sysvaluelng(path,&rx);

	*path=0;
	strcat(path,"/sys/class/net/");
	strcat(path,filename);
	strcat(path,"/statistics/tx_bytes");
	long long tx;
	sysvaluelng(path,&tx);

	if(!strcmp(operstate,"up")){
		sprintf(bbuf,"%s %s %lld/%lld KB",filename,operstate,tx>>10,rx>>10);
	}else if(!strcmp(operstate,"dormant")){
		sprintf(bbuf,"%s %s",filename,operstate);
	}else if(!strcmp(operstate,"down")){
		sprintf(bbuf,"%s %s",filename,operstate);
	}else if(!strcmp(operstate,"unknown")){
		sprintf(bbuf,"%s %s %lld/%lld KB",filename,operstate,tx>>10,rx>>10);
	}
	dccr(dc);
	dcdrwstr(dc,bbuf,strlen(bbuf));
}
static FILE*file;
static void _rendlid(){
	file=fopen("/proc/acpi/button/lid/LID/state","r");
	if(file){
		fgets(line,sizeof(line),file);
		strcompactspaces(line);
		sprintf(bbuf,"lid %s",line);
		dccr(dc);
		dcdrwstr(dc,bbuf,strlen(bbuf)-1);
		fclose(file);
	}
}
static void _rendtherm(){
	file=fopen("/proc/acpi/thermal_zone/THM/temperature","r");
	if(file){
		fgets(line,sizeof(line),file);
		strcompactspaces(line);
		sprintf(bbuf,"%s",line);
		dccr(dc);
		dcdrwstr(dc,bbuf,strlen(bbuf)-1);
		fclose(file);
	}
}
static void _rendhr(){
	dcdrwhr(dc);
}
static void _rendbattery(){
	int charge_full_design;
	sysvalueint("/sys/class/power_supply/BAT0/charge_full_design",&charge_full_design);
	int charge_now;
	sysvalueint("/sys/class/power_supply/BAT0/charge_now",&charge_now);
	char state[32];
	sysvaluestr("/sys/class/power_supply/BAT0/status",state,sizeof(state));

	dcyinc(dc,default_graph_height);
	graphaddvalue(graphbat,charge_now);
	graphdraw2(graphbat,dc,default_graph_height,charge_full_design);
	dccr(dc);
	sprintf(bbuf,"%s  %d/%d mAh",state,charge_now/1000,charge_full_design/1000);
	dcdrwstr(dc,bbuf,strlen(bbuf));
}
static void _rendcpuload(){
	file=fopen("/proc/stat","r");
	if(file){
		// user: normal processes executing in user mode
		// nice: niced processes executing in user mode
		// system: processes executing in kernel mode
		// idle: twiddling thumbs
		// iowait: waiting for I/O to complete
		// irq: servicing interrupts
		// softirq: servicing softirqs
		int user,nice,system,idle,iowait,irq,softirq;
		fscanf(file,"%s %d %d %d %d %d %d %d\n",bbuf,&user,&nice,&system,&idle,&iowait,&irq,&softirq);
		int total=(user+nice+system+idle+iowait+irq+softirq);
		int usage=total-idle;
		long long dtotal=total-cpu_total_last;
		cpu_total_last=total;
		int dusage=usage-cpu_usage_last;
		cpu_usage_last=usage;
		int usagepercent=dusage*100/dtotal;
		graphaddvalue(graphcpu,usagepercent);
		dcyinc(dc,dyhr);
		dcyinc(dc,default_graph_height);
		graphdraw2(graphcpu,dc,default_graph_height,100);
		fclose(file);
	}
}
static void _rendhelloclonky(){
	sprintf(bbuf,"%d hello%sclonky",counter,counter!=1?"s ":" ");
	dccr(dc);
	dcdrwstr(dc,bbuf,strlen(bbuf));
}
static void _rendmeminfo(){
	file=fopen("/proc/meminfo","r");
	if(file){
		char name[64],unit[32];
		long long memtotal,memfree,memcached;
		fgets(bbuf,sizeof(bbuf),file);
		sscanf(bbuf,"%s %llu %s",name,&memtotal,unit);
		strcompactspaces(bbuf);
		fgets(bbuf,sizeof(bbuf),file);
		sscanf(bbuf,"%s %llu %s",name,&memfree,unit);
		strcompactspaces(bbuf);
		fgets(bbuf,sizeof(bbuf),file);
		fgets(bbuf,sizeof(bbuf),file);
		sscanf(bbuf,"%s %llu %s",name,&memcached,unit);
		strcompactspaces(bbuf);
		int proc=(memtotal-(memfree+memcached))*100/memtotal;
		graphaddvalue(graphmem,proc);
		dcyinc(dc,dyhr);
		dcyinc(dc,default_graph_height);
//		dcyinc(dc,100>>2+2);
		graphdraw(graphmem,dc,2);
		long long free=memfree+memcached;
		if(free>>10!=0){
			free>>=10;
			memtotal>>=10;
			strcpy(unit,"MB");
		}
		sprintf(bbuf,"freemem %llu of %llu %s",free,memtotal,unit);
		dccr(dc);
		dcdrwstr(dc,bbuf,strlen(bbuf));
		fclose(file);
	}
}
static void _rendnet(){
	qdir("/sys/class/net",netdir);
}
static void _rendwifitraffic(){
	dcyinc(dc,default_graph_height+dyhr);
	sprintf(bbuf,"/sys/class/net/%s/statistics/tx_bytes",wifieth);
	long long wifi_tx;
	sysvaluelng(bbuf,&wifi_tx);
	sprintf(bbuf,"/sys/class/net/%s/statistics/rx_bytes",wifieth);
	long long wifi_rx;
	sysvaluelng(bbuf,&wifi_rx);
	graphdaddvalue(graphwifi,wifi_tx+wifi_rx);
	graphddraw(graphwifi,dc,default_graph_height,wifigraphmax);
}
static void pl(const char*str){
	dccr(dc);
	dcdrwstr(dc,str,strlen(str));
}
static void _rendcheetsheet(){
	char**strptr=keysheet;
	while(*strptr){
		dccr(dc);
		dcdrwstr(dc,*strptr,strlen(*strptr));
		strptr++;
	}
}
static void _renddf(){
	char buf[1024];
	int buflen=1024;
	int r=system("df -h>file");
	if(r)
		return;
	FILE*f=fopen("file","r");
	while(1){
		if(!fgets(buf,buflen,f))
			break;
		strcompactspaces(buf);
		if(!strncmp(buf,"none ",5))
			continue;
		pl(buf);
	}
	fclose(f);
}
static void _renddatetime(){
	const time_t t=time(NULL);
	const struct tm *local=localtime(&t);
	sprintf(bbuf,"%s",asctime(local));
	dccr(dc);
	dcdrwstr(dc,bbuf,strlen(bbuf));
}
static void on_draw(){
	counter++;
	dcyset(dc,ytop);
	dcclrbw(dc);
#include"clonky_draw.ci"
	dcflush(dc);
}
int main(){
	if(!(dc=dcnew()))return 1;
	dcwset(dc,width);
	if(align==1)
		dcxlftset(dc,dcwscrget(dc)-width);
	graphcpu=graphnew(width);
	graphbat=graphnew(width);
	graphmem=graphnew(width);
	graphwifi=graphdnew(width);
	tmr(1,&on_draw);
	while(1)
		sleep(60*60*24);
}
