package wt.oo;

import htp.wt;
import htp.xwriter;

public class task extends oo{
	static final long serialVersionUID=1;
	public projectsel projsel;
	public projectmilestonesel projectmilestonesel;
	public wt seq;
	public taskfrom from;
	public taskto to;
	public taskinform inform;
	public taskstate state;
	public tasktype type;
	public taskbin taskbin;
	public tasklog tasklog;
	public timebook timebook;
	public tasklink tasklink;
	@Override public void to(xwriter x) throws Throwable{
		super.to(x);
		x.p("project").p(": ").wt(projsel).br();
		x.p("seq").p(": ").inputText(seq,"word",this,"save");
	}
}
