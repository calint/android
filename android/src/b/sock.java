package b;
public interface sock{
	enum op{write,read,close,cont,wait}
	op sockinit(final sockio _)throws Throwable;
	op read()throws Throwable;
	op write()throws Throwable;
}
