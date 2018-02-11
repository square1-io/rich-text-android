/*
 * Copyright (c) 2016. Roberto  Prato <https://github.com/robertoprato>
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

import android.content.Context;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by roberto on 19/10/2016.
 *
 *
 * Keeps a pool of MedialPlayers
 *
 */

public class MediaPlayerPool {

    HashMap<String,RichMediaPlayer> mMediaPlayerHashMap;


    public MediaPlayerPool(){
        mMediaPlayerHashMap = new HashMap<>();
    }

    public RichMediaPlayer get(Context context, String videoPath){

        synchronized (this){

            RichMediaPlayer mediaPlayer = mMediaPlayerHashMap.get(videoPath);
            if(mediaPlayer == null){
                mediaPlayer = new RichMediaPlayer(context);
                mMediaPlayerHashMap.put(videoPath, mediaPlayer);
                mediaPlayer.setData(videoPath);
            }

            return mediaPlayer;
        }
    }

}
