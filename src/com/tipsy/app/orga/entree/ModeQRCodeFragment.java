package com.tipsy.app.orga.entree;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.android.result.ResultHandlerFactory;
import com.tipsy.app.R;

/**
 * Created by tech on 12/02/14.
 */
public class ModeQRCodeFragment extends Fragment implements SurfaceHolder.Callback {

    /*
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_scan_qrcode, container, false);
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i("xZing", "contents: " + contents + " format: " + format);
            }
            else if(resultCode == getActivity().RESULT_CANCELED) {
                Log.i("xZing", "Cancelled");
            }
        }

    }
    */

        private CameraManager cameraManager=null;
        private CaptureActivityHandler handler=null;
        private Result savedResultToShow=null;
        private ViewfinderView viewfinderView=null;
        private boolean hasSurface=false;
        ProgressDialog progressDialog=null;

        FrameLayout root=null;

        @Override
        public void onStop() {
        super.onStop();

        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //restoreLanguage(null);
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getActivity().getApplication());
        viewfinderView = (ViewfinderView) getActivity().findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);


        handler = null;

        SurfaceView surfaceView = (SurfaceView) getActivity().findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface)
        {
            initCamera(surfaceHolder);
        }
        else
        {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }


    @Override
    public void onPause()
    {
        if (handler != null)
        {
            handler.quitSynchronously();
            handler = null;
        }

        cameraManager.closeDriver();
        if (!hasSurface)
        {
            SurfaceView surfaceView = (SurfaceView) getActivity().findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result)
    {
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        hasSurface = false;
        cameraManager.stopPreview();
        cameraManager.closeDriver();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }
/*
    public void handleDecode(Result rawResult, Bitmap barcode)
    {

        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);

        if (barcode == null)
        {
            handleDecodeInternally(rawResult, resultHandler, null);
        }
        else
        {
            handleDecodeInternally(rawResult, resultHandler, barcode);
        }
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode)
    {
        CharSequence displayContents = resultHandler.getDisplayContents(); //here is readed code. Do ehatever you want

        cameraManager.stopPreview();
    }

    private void initCamera(SurfaceHolder surfaceHolder)
    {
        try
        {
            cameraManager.openDriver(surfaceHolder);
            /*if (handler == null)
            {
                handler = new CaptureActivityHandler(this, null, null, cameraManager);
            }*/
            decodeOrStoreSavedBitmap(null, null);
        } catch (Exception e)
        {
        }
    }

    public void drawViewfinder()
    {
        viewfinderView.drawViewfinder();
    }
}


