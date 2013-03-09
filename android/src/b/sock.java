package b;

import java.util.Map;

public interface sock{
	enum op{write,read,close,cont,wait}
	op sockinit(final Map<String,String>hdrs,final sockio _)throws Throwable;
	op read()throws Throwable;
	op write()throws Throwable;
}
