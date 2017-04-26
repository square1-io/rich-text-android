
package io.square1.richtextlib.ui.iframe;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class WrapContentWebView extends WebView {

    private static final long UPDATE_INTERVAL = 100; // ms

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
        addJavascriptInterface(new JavascriptInterface(), "java");
        postDelayed(scheduleRunnable, UPDATE_INTERVAL);
    }

    private Runnable scheduleRunnable = new Runnable() {
        @Override
        public void run() {
            loadUrl("javascript:window.java.onNewHeight(document.body.offsetHeight);");
            postDelayed(scheduleRunnable, UPDATE_INTERVAL);
        }
    };

    private class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void onNewHeight(String bodyOffsetHeight) {
            if (bodyOffsetHeight == null) {
                return;
            }

            final int newHeight =
                    (int) (Integer.parseInt(bodyOffsetHeight) *
                            getScaleY() *
                            getResources().getDisplayMetrics().density);

            if (getLayoutParams().height != newHeight) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
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