package a.android;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import b.a;
import b.bin;
import b.path;
import b.req;
import b.xwriter;
import c.a.activity;

public class takepicture extends a implements bin{
	static final long serialVersionUID=1L;
	public String contenttype(){return "text/plain";}
	public void to(final xwriter x)throws Throwable{
		activity.get().camera_takepicture(req.get().session().path("cam"+System.currentTimeMillis()+".jpg"));
	}
	public void more(final xwriter x){
		x.pl("getting camera").flush();
		final Camera cam=Camera.open();
		x.pl(cam.toString()).flush();
		x.pl("setting params").flush();
		final Camera.Parameters params=cam.getParameters();
		params.setPictureFormat(PixelFormat.JPEG); 
		cam.setParameters(params);
		
		final path pth=req.get().session().path("pic-"+new SimpleDateFormat("yyyyMMdd-hhmmss",Locale.US).format(new Date())+".jpg");
		final SurfaceView sv=new SurfaceView(activity.get());
		sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		sv.getHolder().addCallback(new SurfaceHolder.Callback(){
			public void surfaceCreated(final SurfaceHolder holder){
				System.out.println("cam surface created");
			}
			public void surfaceChanged(final SurfaceHolder holder,final int format,final int width,final int height){
				System.out.println("cam surface changed");
				try{
					cam.setPreviewDisplay(sv.getHolder());
				}catch(IOException e){
					e.printStackTrace();
				}
				cam.startPreview();
				cam.takePicture(
						new Camera.ShutterCallback(){public void onShutter(){
							System.out.println("cam shutter");
//							x.pl("shutter").flush();
						}},
						new Camera.PictureCallback(){public void onPictureTaken(byte[]data,Camera camera){
							System.out.println("cam raw");
//							x.pl("raw").flush();
						}},
						new Camera.PictureCallback(){public void onPictureTaken(byte[]data,Camera camera){
							System.out.println("cam jpg");
							x.pl("jpg").flush();
							try{
//								x.outputstream().write(data);
								pth.writeba(data);
								System.out.println("cam wrote jpg to "+pth.fullpath());
								x.pl("wrote "+pth.fullpath()).flush();
							}catch(Throwable t){throw new Error(t);}
							synchronized(cam){cam.notifyAll();}
						}});
			}
			public void surfaceDestroyed(final SurfaceHolder holder){
				System.out.println("surface destroyed");
				cam.stopPreview();
				cam.release();
			}
		});
	}
}
