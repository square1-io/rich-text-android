package io.square1.richtextlib.ui.video;

import android.media.MediaPlayer;

class InternalMediaPlayer extends MediaPlayer {



    private  OnCompletionListener mOnCompletionListener;
    private  OnSeekCompleteListener mSeekListener;

    public OnCompletionListener getOnCompletionListener() {
        return mOnCompletionListener;
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener completionListener) {
        mOnCompletionListener = completionListener;
        super.setOnCompletionListener(completionListener);
    }


    @Override
    public void setOnSeekCompleteListener(OnSeekCompleteListener listener){
        mSeekListener = listener;
        super.setOnSeekCompleteListener(mSeekListener);
    }

    public OnSeekCompleteListener getSeekListener(){
            return mSeekListener;
        }

    }