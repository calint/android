package c.a;


public interface device{
	String get_host_ip_address();
	state state();
	location location();
	static final class location{
		public double longitude,latitude;
		public float accuracy_m;
	}
	static final class orientation{
		public float[]zxy=new float[16];
	}
	orientation orientation();
}
