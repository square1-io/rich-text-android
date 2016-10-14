
package io.square1.richtextlib.ui.video;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import io.square1.richtextlib.R;


public class FullScreenVideoFragment extends DialogFragment implements TextureView.SurfaceTextureListener, View.OnTouchListener {

    private FrameLayout mMainVideoContainer;
    private FrameLayout mAspectRatioContainer;

    private RichVideoView mInitialView;
    private VideoControls mVideoControls;
    private TextureView mFullScreenView;
    //setup the progress bar
    private ProgressBar mLoadingProgress;

    public FullScreenVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.internal_richtext_video_display, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
        mMainVideoContainer = (FrameLayout)view;
        mAspectRatioContainer = (FrameLayout)view.findViewById(R.id.internal_aspect_ratio_view);
        //setup the progress bar
        mLoadingProgress = (ProgressBar) view.findViewById(R.id.internal_progress);
        mLoadingProgress.setIndeterminate(true);

        mVideoControls = new VideoControls(getActivity(), mInitialView,(FrameLayout)getView());
        mFullScreenView = (TextureView) view.findViewById(R.id.internal_texture_view);
        mFullScreenView.setSurfaceTextureListener(this);
        if(mFullScreenView.isAvailable() == true){
            SurfaceTexture surfaceTexture = mFullScreenView.getSurfaceTexture();
            onSurfaceTextureAvailable(surfaceTexture,mFullScreenView.getWidth(),mFullScreenView.getHeight() );
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        //pass the media back to the inline video
       // mFullScreenView.handover(mInitialView);

    }

    void presentVideoFullScreen(Activity activity, RichVideoView initialView){
        mInitialView = initialView;
        show(activity.getFragmentManager(), "FullScreenVideoFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void updateSurface(SurfaceTexture texture){
        mInitialView.setSurface(texture);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mLoadingProgress.setVisibility(View.GONE);

        RichMediaPlayer mp = mInitialView.getMediaPlayer();

        RichVideoView.adjustAspectRatio(mMainVideoContainer, mAspectRatioContainer,
                mp.getVideoWidth(),
                mp.getVideoHeight());

        RichVideoView.adjustAspectRatio(mFullScreenView,
                mp.getVideoWidth(),
                mp.getVideoHeight());

        updateSurface(surface);

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        //updateSurface(surface);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
      //  updateSurface(surface);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mVideoControls.showControls();
        return false;
    }
}
