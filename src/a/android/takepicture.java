package a.android;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import b.a;
import b.bin;
import b.xwriter;
import c.a.activity;

public class takepicture extends a implements bin{
	static final long serialVersionUID=1L;
	public String contenttype(){return "text/plain";}
	public void to(final xwriter x)throws Throwable{
		x.pl("getting camera").flush();
		final Camera cam=Camera.open();
		try{
			x.pl(cam.toString()).flush();
			x.pl("setting params").flush();
			final Camera.Parameters params=cam.getParameters();
			params.setPictureFormat(PixelFormat.JPEG); 
			cam.setParameters(params);
			final SurfaceHolder sh=activity.inst.surfaceView().getHolder();
//			sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			x.p("set preview ").pl(sh.toString()).flush();
			cam.setPreviewDisplay(sh);
			x.pl("start preview").flush();
			cam.startPreview();
			x.pl("take picture").flush();
			cam.takePicture(
				new Camera.ShutterCallback(){public void onShutter(){
					System.out.println("shutter");}},
				new Camera.PictureCallback(){public void onPictureTaken(byte[]data,Camera camera){System.out.println("raw");}},
				new Camera.PictureCallback(){public void onPictureTaken(byte[]data,Camera camera){System.out.println("jpg");}});
			x.pl("stop preview").flush();
			cam.stopPreview();
		}finally{
			cam.release();
		}
		x.pl("done");
	}
}
