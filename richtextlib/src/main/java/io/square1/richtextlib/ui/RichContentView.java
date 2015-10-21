package io.square1.richtextlib.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Size;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.Toast;

import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.R;
import io.square1.richtextlib.style.ClickableSpan;
import io.square1.richtextlib.style.P2ParcelableSpan;
import io.square1.richtextlib.style.Style;
import io.square1.richtextlib.style.URLSpan;
import io.square1.richtextlib.style.UnsupportedContentSpan;
import io.square1.richtextlib.style.UrlBitmapDownloader;
import io.square1.richtextlib.style.YouTubeSpan;

/**
 * Created by roberto on 20/09/15.
 */
public class RichContentView extends FrameLayout implements RichContentViewDisplay {



    private Style mInternalStyle = new Style() {

        private Bitmap mQuoteBitmap = null;
        private int mQuoteBackgroundColor = Style.NOT_SET;
        private int mTextColorHeader = Style.NOT_SET;

        @Override
        public Context getApplicationContext() {
            return RichContentView.this.getContext();
        }

        @Override
        public Bitmap quoteBitmap() {
            return mQuoteBitmap;
        }

        @Override
        public int getQuoteBackgroundColor() {
            return mQuoteBackgroundColor;
        }

        @Override
        public int headerColor() {
            return mTextColorHeader;
        }

        @Override
        public int backgroundColor() {
            return 0;
        }

        @Override
        public int maxImageWidth() {
            return 0;
        }

        @Override
        public int maxImageHeight() {
            return 0;
        }

        @Override
        public float headerIncrease(int headerLevel) {
            return 0;
        }

        @Override
        public float smallTextReduce() {
            return 0;
        }

        @Override
        public boolean parseWordPressTags() {
            return false;
        }

        @Override
        public boolean treatAsHtml() {
            return false;
        }
    };

    private UrlBitmapDownloader mBitmapManager;

    private RichTextDocumentElement mText;
    private P2ParcelableSpan[] mSpans;

    private boolean mAttachedToWindow;

    private TextPaint mTextPaint;
    private Layout mLayout;
    private float mSpacingMult = 1.0f;
    private float mSpacingAdd = 0.0f;

    private int mLastMeasuredWidth;
    private float mDefaultPixelSize;

    private Thread mThread;

    private OnSpanClickedObserver mOnSpanClickedObserver;



    public RichContentView(Context context) {
        super(context);
        init(context, null, -1, -1);

    }

    public RichContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1, -1);
    }

    public RichContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, -1);
    }

    @Override
    public void addSubView(View view) {
        addView(view);
    }

    public void setText(RichTextDocumentElement builder){
        if(mText != builder) {
            mText = builder;
            mSpans = mText.getSpans();
            mLayout = null;
            for(P2ParcelableSpan span : mSpans){
                span.onSpannedSetToView(this);
            }

            requestLayout();
        }
    }


    private void setRawTextSize(float size) {

        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);
            performLayout();
        }

    }

    @Override
    public void performLayout(){
        if (mLayout != null) {
            mLayout = null;
            requestLayout();
            invalidate();
        }
    }

    @Override
    public void setUrlBitmapDownloader(UrlBitmapDownloader downloader) {
        mBitmapManager = downloader;
    }

    @Override
    public void setRichTextContentChanged(RichTextContentChanged richTextContentChanged) {

    }

    public void setOnSpanClickedObserver( OnSpanClickedObserver observer){
        mOnSpanClickedObserver = observer;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RichContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        setWillNotDraw(false);

        mText = new RichTextDocumentElement();
        mSpans = mText.getSpans();

        final Resources res = getResources();
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.linkColor = Color.BLUE;
        mTextPaint.density = res.getDisplayMetrics().density;
        mLastMeasuredWidth = 0;

        mDefaultPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                15,
                res.getDisplayMetrics());

        setRawTextSize(mDefaultPixelSize);

        parseCustomAttributes(context, attrs);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);


        if(mLayout == null || (mLastMeasuredWidth != widthSize)){
            mLastMeasuredWidth = widthSize;
            mLayout = makeLayout(widthSize - getPaddingLeft() - getPaddingRight());
        }

        if (mLayout != null) {
            setMeasuredDimension(getPaddingLeft() +
                            getPaddingRight() + mLayout.getWidth(),
                    getPaddingTop() + getPaddingBottom() + mLayout.getHeight());

            setMeasuredDimension(widthSize, mLayout.getHeight());
        }

    }


    private Layout makeLayout(int width){

        StaticLayout result = new StaticLayout(mText,
                mTextPaint,
                width,
                Layout.Alignment.ALIGN_NORMAL,
                mSpacingMult,
                mSpacingAdd,
                true);

        return result;
    }


    @Override
    public void onDraw(Canvas canvas){
        canvas.save();
        if (mLayout != null) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
            mLayout.draw(canvas);
        }
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        // If action has finished
        if(mLayout != null && (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN)) {

            // Locate the area that was pressed
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= getPaddingLeft();
            y -= getPaddingTop();
            x += getScrollX();
            y += getScrollY();

            // Locate the URL text
            int line = mLayout.getLineForVertical(y);
            int off = mLayout.getOffsetForHorizontal(line, x);

            Animatable[] animatables =  mText.getSpans(off, off, Animatable.class);
            if(animatables.length > 0) return false;

            if (animatables.length != 0 && action == MotionEvent.ACTION_UP) {
                if(animatables[0].isRunning()){
                    animatables[0].stop();
                }else{
                    animatables[0].start();
                }
            }
            // Find the URL that was pressed
            ClickableSpan[] link = mText.getSpans(off, off, ClickableSpan.class);
            // If we've found a URL
            if (link.length != 0 && action == MotionEvent.ACTION_UP) {
                onSpansClicked(link);
            }

            return true;
        }

        return super.onTouchEvent(event);
    }



    @Override
    public UrlBitmapDownloader getDownloader() {
        return mBitmapManager;
    }

    @Override
    public boolean viewAttachedToWindow() {
        return mAttachedToWindow;
    }


    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();

        mAttachedToWindow = true;

        for (P2ParcelableSpan span : mSpans) {
                span.onAttachedToView(this);
        }

    }

    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
        for(P2ParcelableSpan span : mSpans){
            span.onDetachedFromView(this);
        }
    }


    public LayoutParams generateDefaultLayoutParams(Point position,int width, int height ){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
        params.leftMargin = position.x;
        params.topMargin = position.y;

        return params;
    }

    public void onSpansClicked(ClickableSpan[] spans) {

        if(spans == null) return;

        for(ClickableSpan span : spans){

            //if handled externally lets just continue
            if(mOnSpanClickedObserver != null &&
                   mOnSpanClickedObserver.onSpanClicked(span) == true)
                continue;

            if(span instanceof YouTubeSpan){

                String id = ((YouTubeSpan)span).getYoutubeId();

                this.getContext().
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + id)));
            }
            else if(span instanceof URLSpan){

                String url = ((URLSpan)span).getURL();
                try {
                    this.getContext().
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }catch (Exception exc){
                    Toast.makeText(getContext(), R.string.error_opening_message, Toast.LENGTH_LONG).show();
                }

            }else if (span instanceof UnsupportedContentSpan){
                String url = ((UnsupportedContentSpan)span).getURL();
                FallbackWebDialog dialog = new FallbackWebDialog(getContext(),url);
                dialog.setCancelable(true);
                dialog.show();
            }
        }

    }


    private void parseCustomAttributes(Context ctx, AttributeSet attrs) {

        if(attrs == null){
            return;
        }


        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.io_square1_richtextlib_ui_RichContentView);

        String customFont = a.getString(R.styleable.io_square1_richtextlib_ui_RichContentView_fontFamily);
        setFontFamily(customFont);

        if(a.hasValue(R.styleable.io_square1_richtextlib_ui_RichContentView_textSize)) {
           int textSize =  a.getDimensionPixelSize(R.styleable.io_square1_richtextlib_ui_RichContentView_textSize, (int)mDefaultPixelSize);
           setRawTextSize(textSize);
        }

        if(a.hasValue(R.styleable.io_square1_richtextlib_ui_RichContentView_textColor)) {
            int color =  a.getColor(R.styleable.io_square1_richtextlib_ui_RichContentView_textColor, Color.BLACK);
            mTextPaint.setColor(color);
        }


        if(a.hasValue(R.styleable.io_square1_richtextlib_ui_RichContentView_textColorLink)) {
            int color =  a.getColor(R.styleable.io_square1_richtextlib_ui_RichContentView_textColorLink,Color.BLUE);
            mTextPaint.linkColor = color;
        }

       // a.getColor(R.stryleable)



        a.recycle();
    }

    public boolean setFontFamily(String customFont) {

        if(TextUtils.isEmpty(customFont)){
            return false;
        }

        try {

            Typeface tf  = Typeface.createFromAsset(getContext().getAssets(), customFont);
            mTextPaint.setTypeface(tf);

        } catch (Exception e) {

            return false;

        }

        return true;
    }

    public Point getSpanOrigin(Object span) {

        Rect parentTextViewRect = new Rect();


        double startOffsetOfClickedText = mText.getSpanStart(span);
        double endOffsetOfClickedText = mText.getSpanEnd(span);
        double startXCoordinatesOfClickedText = mLayout.getPrimaryHorizontal((int) startOffsetOfClickedText);
        double endXCoordinatesOfClickedText = mLayout.getPrimaryHorizontal((int) endOffsetOfClickedText);


        // Get the rectangle of the clicked text
        int currentLineStartOffset = mLayout.getLineForOffset((int) startOffsetOfClickedText);
        int currentLineEndOffset = mLayout.getLineForOffset((int) endOffsetOfClickedText);
        boolean keywordIsInMultiLine = currentLineStartOffset != currentLineEndOffset;
        mLayout.getLineBounds(currentLineStartOffset, parentTextViewRect);


        // Update the rectangle position to his real position on screen
        int[] parentTextViewLocation = {0, 0};
        getLocationOnScreen(parentTextViewLocation);

        double parentTextViewTopAndBottomOffset = (
                parentTextViewLocation[1] -
                        getScrollY() +
                        getPaddingTop()
        );
        parentTextViewRect.top += parentTextViewTopAndBottomOffset;
        parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;

        parentTextViewRect.left += (
                parentTextViewLocation[0] +
                        startXCoordinatesOfClickedText +
                        getPaddingLeft() -
                        getScrollX()
        );

        parentTextViewRect.right = (int) (
                parentTextViewRect.left +
                        endXCoordinatesOfClickedText -
                        startXCoordinatesOfClickedText
        );

        int x = (parentTextViewRect.left + parentTextViewRect.right) / 2;
        int y = parentTextViewRect.bottom;
        if (keywordIsInMultiLine) {
            x = parentTextViewRect.left;
        }

        return  new Point(x,y);
    }
}
