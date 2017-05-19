
package io.square1.richtextlib.ui.web;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import io.square1.richtextlib.util.NumberUtils;

public class WrapContentWebView extends WebView {

    private static final long UPDATE_INTERVAL = 100; // ms

    private Handler mMainLoopHandler;
    private boolean mEnableResize;

    public WrapContentWebView(Context context) {
        super(context);
        init();
    }

    public WrapContentWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WrapContentWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {

        mEnableResize = false;
        setScrollContainer(false);
        clearCache(true);
        clearHistory();

        WebSettings settings = getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        mMainLoopHandler = new Handler(Looper.getMainLooper());
        addJavascriptInterface(new JavascriptInterface(), "java");
    }

    public void setEnableResize(boolean enableResize){

        if(mEnableResize == enableResize){
            return;
        }
        mEnableResize = enableResize;

        if(mEnableResize == true){
            postDelayed(scheduleRunnable, UPDATE_INTERVAL);
        }else {
            removeCallbacks(scheduleRunnable);
        }
    }

    private Runnable scheduleRunnable = new Runnable() {
        @Override
        public void run() {
            loadUrl("javascript:window.java.onNewHeight(document.body.offsetHeight, document.body.scrollHeight,document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
            postDelayed(scheduleRunnable, UPDATE_INTERVAL);
        }
    };

    private class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void onNewHeight(String bodyOffsetHeight,
                                String bodyScrollHeight,
                                String documentElementClientHeight,
                                String documentElementScrollHeight,
                                String documentElementOffsetHeight) {

            if (bodyOffsetHeight == null) {
                return;
            }

            final int newHeight = (int)(NumberUtils.max(NumberUtils.parseInt(bodyOffsetHeight),
            NumberUtils.parseInt(bodyScrollHeight),
            NumberUtils.parseInt(documentElementClientHeight),
            NumberUtils.parseInt(documentElementScrollHeight),
            NumberUtils.parseInt(documentElementOffsetHeight)) *
                    getScaleY() *
                    getResources().getDisplayMetrics().density) ;



            if (getLayoutParams().height != newHeight) {
                mMainLoopHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getLayoutParams().height = newHeight;
                        requestLayout();
                    }
                });
            }
        }
    }
}