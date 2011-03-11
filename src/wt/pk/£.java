package wt.pk;
import htp.htp;
import htp.req;
import htp.wt;
import htp.xwriter;
public class £ extends wt{
	private static final long serialVersionUID=1;
	public wt q;
	public info info;
	public requires requires;
	public files files;
	@Override public void to(final xwriter x) throws Throwable{
		final req r=req.get();
		String qdec=r.querystr();
		final String[] qs=qdec.toLowerCase().replace(';',' ').replace('&',' ').trim().split("\\?");
		if(!(qs[0].length()==0))
			q.setValue(qs[0]);
		x.p("<pre>package “").inputText(q,null,this,"upd").focus(q).p("” ");
		int i=qs.length>1?Integer.parseInt(qs[1]):0;
		wt[] wta=new wt[]{files,requires,info};
		for(int j=0;j<wta.length;j++){
			wt wt=wta[j];
			if(i==j)
				x.p("  :: ").p(wt.getName());
			else
				x.p("  :: ").aBgn(r.pathstr()+"?"+htp.urlencode(qs[0])+"?"+j).p(wt.getName()).aEnd();
		}
		x.nl();
		if(q.toString().length()==0)
			return;
		x.wt(wta[i]).nl().nl();
	}
	public void ax_upd(xwriter x,String[] p) throws Throwable{
		x.p("location.href=location.pathname+'?"+htp.urlencode(q.toString())+"';");
	}
}
