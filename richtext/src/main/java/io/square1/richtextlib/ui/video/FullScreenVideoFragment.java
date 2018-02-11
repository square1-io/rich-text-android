
package io.square1.richtextlib.ui.video;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
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

    private ViewTreeObserver.OnGlobalLayoutListener mLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            layoutCompleted();
        }
    };

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
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
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
        //mAspectRatioContainer.setRotation(90);

         ViewTreeObserver vto = mMainVideoContainer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(mLayoutListener);

        //setup the progress bar
        mLoadingProgress = (ProgressBar) view.findViewById(R.id.internal_progress);
        mLoadingProgress.setIndeterminate(true);

        mVideoControls = new VideoControls(getActivity(),
                mInitialView,
                mAspectRatioContainer ,
                true);

        mVideoControls.setFullScreenButtonVisible(true);

        mVideoControls.setOverrideFullScreenListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        mFullScreenView = (TextureView) view.findViewById(R.id.internal_texture_view);
        mFullScreenView.setSurfaceTextureListener(this);
        if(mFullScreenView.isAvailable() == true){
            SurfaceTexture surfaceTexture = mFullScreenView.getSurfaceTexture();
            onSurfaceTextureAvailable(surfaceTexture,mFullScreenView.getWidth(),mFullScreenView.getHeight() );
        }
    }



    public void layoutCompleted(){

        ViewTreeObserver observer = mMainVideoContainer.getViewTreeObserver();
        observer.removeGlobalOnLayoutListener(mLayoutListener);

        float currentRotation = mAspectRatioContainer.getRotation();
        if(currentRotation != 90.0f){
            mAspectRatioContainer.setRotation(90.0f);
        }

        int w = mAspectRatioContainer.getWidth();
        int h = mAspectRatioContainer.getHeight();

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mAspectRatioContainer.getLayoutParams();

        if(lp.height != h || lp.width != w) {
            lp.gravity = Gravity.CENTER;
           // mAspectRatioContainer.setTranslationX((w - h) / 2);
           // mAspectRatioContainer.setTranslationY((h - w) / 2);

            observer.addOnGlobalLayoutListener(mLayoutListener);
            lp.height = w;
            lp.width = h;
            mAspectRatioContainer.setLayoutParams(lp);
            mMainVideoContainer.requestLayout();
        }

    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        mInitialView.toggleFullScreen(false);
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
        Window window = dialog.getWindow();

       WindowManager.LayoutParams attrs = window.getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            window.setAttributes(attrs);

            if (android.os.Build.VERSION.SDK_INT >= 14) {
                //noinspection all
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }

        return dialog;
    }

    private void updateSurface(SurfaceTexture texture){
        mInitialView.setSurface(texture);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mLoadingProgress.setVisibility(View.GONE);

        RichMediaPlayer mp = mInitialView.getMediaPlayer();
//
//        RichVideoView.adjustAspectRatio(mMainVideoContainer, mAspectRatioContainer,mp.getVideoHeight(),
//                mp.getVideoWidth());
//
        RichVideoView.adjustAspectRatio(mFullScreenView, mp.getVideoWidth(), mp.getVideoHeight());

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
