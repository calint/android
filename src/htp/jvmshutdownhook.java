package htp;
final class jvmshutdownhook extends Thread{
	@Override public void run(){
		thdwatch._stop=true;
		try{session.all_save();}catch(Throwable e){throw new Error(e);}
	}
}