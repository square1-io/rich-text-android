/*
 * Copyright (c) 2015. Roberto  Prato <https://github.com/robertoprato>
 *
 *  *
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

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