
package io.square1.richtextlib.ui.web;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import io.square1.richtextlib.util.NumberUtils;

public class WrapContentWebView extends WebView {

    private static final long DEFAULT_UPDATE_INTERVAL = 100; // ms

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
        settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            // older android version, disable hardware acceleration
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mMainLoopHandler = new Handler(Looper.getMainLooper());
        addJavascriptInterface(new JavascriptInterface(), "java");
    }

    public void setEnableResize(boolean enableResize) {

        mEnableResize = enableResize;
    }

    public void resize() {

        if (mEnableResize) {

            postDelayed(scheduleRunnable, DEFAULT_UPDATE_INTERVAL);
        }
    }

    private Runnable scheduleRunnable = new Runnable() {

        @Override
        public void run() {

            loadUrl("javascript:window.java.onNewHeight(document.body.offsetHeight, document.body.scrollHeight,document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);");
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

            final int newHeight = (int) (NumberUtils.max(NumberUtils.parseInt(bodyOffsetHeight),
                    NumberUtils.parseInt(bodyScrollHeight),
                    NumberUtils.parseInt(documentElementClientHeight),
                    NumberUtils.parseInt(documentElementScrollHeight),
                    NumberUtils.parseInt(documentElementOffsetHeight)) *
                    getScaleY() *
                    getResources().getDisplayMetrics().density);

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