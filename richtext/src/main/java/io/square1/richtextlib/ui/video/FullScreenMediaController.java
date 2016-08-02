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


import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;



import java.lang.reflect.Field;

import io.square1.richtextlib.R;

/**
 * Custom MediaController that fixes issue with controls appearing offset on pre 4.3 devices
 * and shows how to add additional functionality such as fullscreen button
 */
public class FullScreenMediaController extends MediaController {

    public  interface OnMediaControllerInteractionListener {
        void onRequestFullScreen();
    }

    Context mContext;
    private OnMediaControllerInteractionListener mListener;

    public FullScreenMediaController(Context context) {
        super(context);
        mContext = context;
    }

    void setListener(OnMediaControllerInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.RIGHT|Gravity.TOP;


        ImageButton fullscreenButton = new ImageButton(getContext());
        fullscreenButton.setImageResource(R.drawable.fullscreen_enter);

        fullscreenButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            if(mListener != null) {
                mListener.onRequestFullScreen();
            }
            }
        });

        addView(fullscreenButton, frameParams);
    }

    @Override
    public void show(int timeout) {
        super.show(timeout);
        // fix pre Android 4.3 strange positioning when used in Fragments
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                Field field1 = MediaController.class.getDeclaredField("mAnchor");
                field1.setAccessible(true);
                View mAnchor = (View)field1.get(this);

                Field field2 = MediaController.class.getDeclaredField("mDecor");
                field2.setAccessible(true);
                View mDecor = (View)field2.get(this);

                Field field3 = MediaController.class.getDeclaredField("mDecorLayoutParams");
                field3.setAccessible(true);
                WindowManager.LayoutParams mDecorLayoutParams = (WindowManager.LayoutParams)field3.get(this);

                Field field4 = MediaController.class.getDeclaredField("mWindowManager");
                field4.setAccessible(true);
                WindowManager mWindowManager = (WindowManager)field4.get(this);

                // NOTE: this appears in its own Window so co-ordinates are screen co-ordinates
                int [] anchorPos = new int[2];
                mAnchor.getLocationOnScreen(anchorPos);

                // we need to know the size of the controller so we can properly position it
                // within its space
                mDecor.measure(MeasureSpec.makeMeasureSpec(mAnchor.getWidth(), MeasureSpec.AT_MOST),
                        MeasureSpec.makeMeasureSpec(mAnchor.getHeight(), MeasureSpec.AT_MOST));

                mDecor.setPadding(0,0,0,0);

                WindowManager.LayoutParams p = mDecorLayoutParams;
                p.verticalMargin = 0;
                p.horizontalMargin = 0;
                p.width = mAnchor.getWidth();
                p.gravity = Gravity.LEFT|Gravity.TOP;
                p.x = anchorPos[0];// + (mAnchor.getWidth() - p.width) / 2;
                p.y = anchorPos[1] + mAnchor.getHeight() - mDecor.getMeasuredHeight();
                mWindowManager.updateViewLayout(mDecor, mDecorLayoutParams);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}