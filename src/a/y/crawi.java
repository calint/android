package a.y;
import b.a;
import b.xwriter;
public final class crawi extends a{
	static final long serialVersionUID=1;
	public void to(xwriter x)throws Throwable{
		x.p("znjr xi..");
		x.p("--00 00..");
		x.p("--10 00..  jump to subroutine specified by registers");
		x.p("--01 00..  return from subroutine after instruction");
		x.p("--11 00..  ");
		x.p("--00 10..  instruction then next in loop");
		x.p("--10 10..  jump to subroutine, after return next in loop");
		x.p("--01 10..  instruction then next in loop and if end of loop return from subroutine");
		x.p("--11 10..  ");
		x.p("--00 01..  ");
		x.p("--10 01..  jump to subroutine using immediate address");
		x.p("--01 01..  ");
		x.p("--11 01..  ");
		x.p("--00 11..  ");
		x.p("--10 11..  ");
		x.p("--01 11..  ");
		x.p("--11 11..  ");

		x.p("znjr xi.. criw oooo");
		x.p(" • clear destination register");
		x.p(" •  read ram address to latch");
		x.p(" •  operation latch and register to register");
		x.p(" •  write data register to ram address");
		x.p(" •  increase address register");
		x.p(" ");
}
}
