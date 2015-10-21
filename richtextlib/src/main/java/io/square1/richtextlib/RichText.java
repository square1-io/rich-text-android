package io.square1.richtextlib;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;

import android.net.Uri;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ParagraphStyle;
import android.util.Patterns;


import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Stack;
import java.util.regex.Matcher;


import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.parser.MarkupTag;
import io.square1.richtextlib.style.*;
import io.square1.richtextlib.util.NumberUtils;

/**
 * This class processes HTML strings into displayable styled text.
 * Not all HTML tags are supported.
 */
public class RichText {

    public static final String EMBED_TYPE = "EMBED_TYPE";
    public static final String IMAGE_WIDTH = "width";
    public static final String  IMAGE_HEIGHT = "height";

    public final static String NO_SPACE_CHAR = "\uFFFC";

    public enum TNodeType {
        EText,
        EEmbed,
        EImage
    }


    public static class DefaultStyle implements  Style {

        private static final float[] HEADER_SIZES = {
                1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f,
        };

        private Context mApplicationContext;
        private Bitmap mQuoteBitmap;
        private int mMaxImageWidth;

        public DefaultStyle(Context context){
            mApplicationContext = context.getApplicationContext();
            Resources resources = context.getResources();
            mQuoteBitmap = BitmapFactory.decodeResource(resources, R.drawable.quote);
            mMaxImageWidth = resources.getDisplayMetrics().widthPixels;
        }

        @Override
        public Context getApplicationContext(){
            return mApplicationContext;
        }
        @Override
        public Bitmap quoteBitmap() {
            return mQuoteBitmap;
        }

        @Override
        public int getQuoteBackgroundColor() {
            return Color.parseColor("#d3d3d3");
        }

        @Override
        public int headerColor() {
            return Color.BLACK;
        }

        @Override
        public int backgroundColor() {
            return Color.WHITE;
        }

        @Override
        public int maxImageWidth() {
            return mMaxImageWidth;
        }

        @Override
        public int maxImageHeight() {
            return Style.USE_DEFAULT;
        }

        @Override
        public float headerIncrease(int headerLevel) {
            return HEADER_SIZES[headerLevel];
        }


        @Override
        public float smallTextReduce(){
            return 0.8f;
        }

        @Override
        public boolean parseWordPressTags(){
            return true;
        }

        @Override
        public boolean treatAsHtml(){
            return true;
        }

    }




    private static class HtmlParser {
        private static final HTMLSchema schema = new HTMLSchema();
    }

    public interface RichTextCallback {

        void onElementFound(TNodeType type, Object content, HashMap<String, Object> attributes);
        void onError(Exception exc);
    }





    private RichText() { }


    /**
     * Returns displayable styled text from the provided HTML string.
     * Any &lt;img&gt; tags in the HTML will use the specified ImageGetter
     * to request a representation of the image (use null if you don't
     * want this) and the specified TagHandler to handle unknown tags
     * (specify null if you don't want this).
     *
     * <p>This uses TagSoup to handle real HTML, including all of the brokenness found in the wild.
     */


    public static void fromHtml(Context context,
                                String source,
                                RichTextCallback callback) {

        final Style defaultStyle = new DefaultStyle(context);
        fromHtmlImpl(context, source, defaultStyle, callback);

    }

    public static void fromHtml(Context context,
                                String source,
                                RichTextCallback callback,
                                Style style ) {

        fromHtmlImpl(context, source, style, callback);

    }

    final static String SOUND_CLOUD = "\\[soundcloud (.*?)/?\\]";
    final static String SOUND_CLOUD_REPLACEMENT = "<soundcloud $1 />";

    final static String INTERACTION = "\\[interaction (.*?)/?\\]";
    final static String INTERACTION_REPLACEMENT = "";


    private static void fromHtmlImpl(Context context,
                                 String source,
                                 Style style,
                                 RichTextCallback callback)  {

        HtmlToSpannedConverter converter = null;
        try {

            XMLReader reader = new Parser();
            reader.setProperty(Parser.schemaProperty, HtmlParser.schema);

            if(style.parseWordPressTags() == true) {

                //soundcloud
                //source = source.replaceAll("\\[/soundcloud\\]","");

                source = source.replaceAll(SOUND_CLOUD,
                        SOUND_CLOUD_REPLACEMENT);

                source = source.replaceAll(INTERACTION,
                        INTERACTION_REPLACEMENT);

               // Matcher m = pattern.matcher(source);
              //  while (m.find() == true) {
              //      m.groupCount();
              //  }
            }

            converter = new HtmlToSpannedConverter(source, reader, style, callback);
            converter.convert();

        } catch (Exception e) {
            callback.onError(e);
            e.printStackTrace();
        }

    }


static class HtmlToSpannedConverter implements ContentHandler, EmbedUtils.ParseLinkCallback{



    private Stack<MarkupTag> mStack = new Stack<>();

    private boolean mInsideTweet  = false;
    private boolean mInsideFacebookVideo = false;
    private boolean mInsideInstagram = false;

    private MarkupTag mLastOpened;
    private MarkupTag mLastClosed;

    private String mSource;
    private XMLReader mReader;
    private StringBuilder mAccumulatedText;
    private RichTextDocumentElement mSpannableStringBuilder;
    private RichText.RichTextCallback mCallback;
    private Style mStyle;

    public HtmlToSpannedConverter(String source,
                                  XMLReader reader,
                                  Style style,
                                  RichText.RichTextCallback callback) {
        mStyle = style;
        mCallback = callback;
       // mDownloader = dowloader;
        mSource = String.format("<root>%s</root>" , source);
        mReader = reader;
        mAccumulatedText = new StringBuilder();
        mSpannableStringBuilder = new RichTextDocumentElement();

    }


    public void convert() throws Exception{


        mReader.setContentHandler(this);
      //  try {
            mReader.parse(new InputSource(new StringReader(mSource)));
//      //  } catch (IOException e) {
//            // We are reading from a string. There should not be IO problems.
//            throw new RuntimeException(e);
//        } catch (SAXException e) {
//            // TagSoup doesn't throw parse exceptions.
//            throw new RuntimeException(e);
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }

        if(mSpannableStringBuilder.length() > 0){
            fixFlags(mSpannableStringBuilder);
            mCallback.onElementFound(RichText.TNodeType.EText, mSpannableStringBuilder, null);
        }

    }

    private static void fixFlags(RichTextDocumentElement builder){

        // Fix flags and range for paragraph-type markup.
        Object[] obj = builder.getSpans(0, builder.length(), ParagraphStyle.class);
        for (int i = 0; i < obj.length; i++) {
            int start = builder.getSpanStart(obj[i]);
            int end = builder.getSpanEnd(obj[i]);

            // If the last line of the range is blank, back off by one.
            if (end - 2 >= 0) {
                if (builder.charAt(end - 1) == '\n' &&
                        builder.charAt(end - 2) == '\n') {
                    end--;
                }
            }

            if (end == start) {
                builder.removeSpan(obj[i]);
            } else {
                builder.setSpan(obj[i], start, end, Spannable.SPAN_PARAGRAPH);
            }
        }

    }

    private void handleStartTag( String tag, Attributes attributes) {

        mSpannableStringBuilder = processAccumulatedTextContent();
        mLastOpened = new MarkupTag(tag, attributes);
        mStack.push(mLastOpened);

        applyStartTag(mSpannableStringBuilder,mLastOpened,attributes);
    }

    private MarkupTag getCurrent(){
        try {
            return mStack.peek();
        }catch (Exception e){}

        return null;
    }

    boolean storeContent(MarkupTag tag){

        return mInsideTweet == false &&
                mInsideFacebookVideo == false &&
                (tag != null && tag.tag.equalsIgnoreCase("script") == false);

    }

    private void applyStartTag(RichTextDocumentElement spannable, MarkupTag internalTag, Attributes attributes) {

        final String tag = internalTag.tag;




        if (tag.equalsIgnoreCase("root")) {
            handleStartRoot(spannable);
        } else if (tag.equalsIgnoreCase("br")) {
            // We don't need to handle this. TagSoup will ensure that there's a </br> for each <br>
            // so we can safely emite the linebreaks when we handle the close tag.
        } else if (tag.equalsIgnoreCase("p") && mInsideTweet == false) {

            internalTag.closeOnEnd = false;
            internalTag.duplicateOnStart = false;
            handleP(spannable);

        } else if (tag.equalsIgnoreCase("ul")) {
            handleP(spannable);
        }else  if(tag.equalsIgnoreCase("li")){
                startBullet();
        } else if (tag.equalsIgnoreCase("div")) {
            startDiv(spannable, attributes);
        } else if (tag.equalsIgnoreCase("strong")) {
            start(spannable, new Bold());
        } else if (tag.equalsIgnoreCase("b")) {
            start(spannable, new Bold());
        } else if (tag.equalsIgnoreCase("em")) {
            start(spannable, new Italic());
        } else if (tag.equalsIgnoreCase("cite")) {
            start(spannable, new Italic());
        } else if (tag.equalsIgnoreCase("dfn")) {
            start(spannable, new Italic());
        } else if (tag.equalsIgnoreCase("i")) {
            start(spannable, new Italic());
        } else if (tag.equalsIgnoreCase("big")) {
            start(spannable, new Big());
        } else if (tag.equalsIgnoreCase("small")) {
            start(spannable, new Small());
        } else if (tag.equalsIgnoreCase("font")) {
            startFont(spannable, attributes);
        } else if (tag.equalsIgnoreCase("blockquote")) {

            if(mInsideFacebookVideo == true) return;

            String elementClass = attributes.getValue("class");

            if(Blockquote.CLASS_INSTAGRAM.equalsIgnoreCase(elementClass)){
                mInsideInstagram = true;
            }
            else if(Blockquote.CLASS_TWEET.equalsIgnoreCase(elementClass)){
                mInsideTweet = true;
                internalTag.closeOnEnd = false;
                internalTag.duplicateOnStart = false;
             //   start(spannable, new Blockquote(Blockquote.CLASS_TWEET));
            }else {
                start(spannable, new Blockquote(elementClass));
                handleP(spannable);
            }

        } else if (tag.equalsIgnoreCase("tt")) {
            start(spannable, new Monospace());
        } else if (tag.equalsIgnoreCase("a")) {
            if(mInsideInstagram == true){
                startInstagramA(spannable,attributes);
            }else {
                startA(spannable, mInsideTweet, attributes);
            }
        } else if (tag.equalsIgnoreCase("u")) {
            start(spannable, new Underline());
        } else if (tag.equalsIgnoreCase("sup")) {
            start(spannable, new Super());
        }else  if(tag.equalsIgnoreCase("iframe")){
            startIFrame(spannable, attributes);
        } else  if(tag.equalsIgnoreCase("soundcloud")){
          startSoundCloud(spannable, attributes);
        } else if (tag.equalsIgnoreCase("sub")) {
            start(spannable, new Sub());
        } else if (tag.length() == 2 &&
                   Character.toLowerCase(tag.charAt(0)) == 'h' &&
                   tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
            handleP(spannable);
            start(spannable, new Header(tag.charAt(1) - '1'));
        } else if (tag.equalsIgnoreCase("img")) {
            startImg(attributes);
        }
//        } else if (mDefault != null) {
//            mDefault.handleTag(true, tag, spannable, mReader);
//        }
    }

    private void handleEndTag(RichTextDocumentElement spannable, String tag) {

        spannable = processAccumulatedTextContent();
        MarkupTag current = mStack.pop();
        applyEndTag(spannable, tag);
        mLastClosed = current;
    }
    private void applyEndTag(RichTextDocumentElement spannable, String tag) {

        if (tag.equalsIgnoreCase("root")) {
            handleEndRoot(spannable);
        }
        else if (tag.equalsIgnoreCase("ul")) {
         handleP(spannable);
        }
        else  if(tag.equalsIgnoreCase("li")){
            endBullet();
        }
    else if (tag.equalsIgnoreCase("br")) {
            handleBr(spannable);
        } else if (tag.equalsIgnoreCase("p") && mInsideTweet == false) {
            handleP(spannable);
        } else if (tag.equalsIgnoreCase("div")) {
            mInsideFacebookVideo = false;
           // handleP(spannable);
        } else if (tag.equalsIgnoreCase("strong")) {
            end(spannable, Bold.class, new StyleSpan(Typeface.BOLD));
        } else if (tag.equalsIgnoreCase("b")) {
            end(spannable, Bold.class, new StyleSpan(Typeface.BOLD));
        } else if (tag.equalsIgnoreCase("em")) {
            end(spannable, Italic.class, new StyleSpan(Typeface.ITALIC));
        } else if (tag.equalsIgnoreCase("cite")) {
            end(spannable, Italic.class, new StyleSpan(Typeface.ITALIC));
        } else if (tag.equalsIgnoreCase("dfn")) {
            end(spannable, Italic.class, new StyleSpan(Typeface.ITALIC));
        } else if (tag.equalsIgnoreCase("i")) {
            end(spannable, Italic.class, new StyleSpan(Typeface.ITALIC));
        } else if (tag.equalsIgnoreCase("big")) {
            end(spannable, Big.class, new RelativeSizeSpan(1.25f));
        } else if (tag.equalsIgnoreCase("small")) {
            end(spannable, Small.class, new RelativeSizeSpan(0.8f));
        } else if (tag.equalsIgnoreCase("font")) {
            endFont(spannable);
        } else if (tag.equalsIgnoreCase("blockquote")) {
            mInsideInstagram = false;
            mInsideTweet = false;
            if(mInsideFacebookVideo == false) {
                endQuote(spannable);
            }
        } else if (tag.equalsIgnoreCase("tt")) {
            end(spannable, Monospace.class,
                    new TypefaceSpan("monospace"));
        } else if (tag.equalsIgnoreCase("a")) {
            endA(mStack,spannable);
        } else if (tag.equalsIgnoreCase("u")) {
            end(spannable, Underline.class, new UnderlineSpan());
        } else if (tag.equalsIgnoreCase("sup")) {
            end(spannable, Super.class, new SuperscriptSpan());
        } else if (tag.equalsIgnoreCase("sub")) {
            end(spannable, Sub.class, new SubscriptSpan());
        }else  if(tag.equalsIgnoreCase("iframe")){
            endIFrame(spannable);
        } else if (tag.length() == 2 &&
                Character.toLowerCase(tag.charAt(0)) == 'h' &&
                tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
            handleP(spannable);
            endHeader(spannable);
        }
//        else if (mDefault != null) {
//            mDefault.handleTag(false, tag, spannable, mReader);
//        }
    }


    private  void handleStartRoot(RichTextDocumentElement spannable){
       // start(spannable, new Background(mStyle.backgroundColor()));
    }

    private  void handleEndRoot(RichTextDocumentElement text){

//        int len = text.length();
//        Background obj = (Background)getLast(text, Background.class);
//        int where = text.getSpanStart(obj);
//        text.removeSpan(obj);
//
//        BackgroundColorSpan repl = new BackgroundColorSpan(obj != null ? obj.mColor : 0);
//
//        if (where != len) {
//            text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
    }

    private static void ensureAtLeastOneNewLine(RichTextDocumentElement text){

        ensureAtLeastThoseNewLines(text,1);

//        int len = text.length();
//
//        if (len >= 1 && text.charAt(len - 1) != '\n'){
//            text.append("\n");
//        }

    }

    private static void ensureAtLeastThoseNewLines(RichTextDocumentElement text, int newLines){

        int len = text.length();

        int currentNewLines = 0;

        while(len > 0){
            if( text.charAt(len - 1) == '\n'){
                currentNewLines ++;
            }else{
                break;
            }
            len --;
        }

        newLines = newLines - currentNewLines;


        for(int index = 0; index < newLines; index ++){
            text.append('\n');
        }


    }

    private  void handleP(RichTextDocumentElement text) {

        if(mInsideTweet == true) return;

        int len = text.length();

        if (len >= 1 && text.charAt(len - 1) == '\n') {
            if (len >= 2 && text.charAt(len - 2) == '\n') {
                return;
            }

            text.append("\n");
            return;
        }

        if (len != 0 &&
                mLastClosed != null &&
                "iframe".equalsIgnoreCase(mLastClosed.tag) == false) {

            text.append("\n\n");
        }
    }

    private static void handleBr(RichTextDocumentElement text) {
        text.append("\n");
    }

    private static Object getLast(Spanned text, Class kind) {
        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            return objs[objs.length - 1];
        }
    }

    private static void start(RichTextDocumentElement text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);
    }

    private static void end(RichTextDocumentElement text, Class kind,
                            Object repl) {
        int len = text.length();
        Object obj = getLast(text, kind);
        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        if (where != len) {
            text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private  void endQuote(RichTextDocumentElement text) {



        Blockquote obj = (Blockquote)getLast(text, Blockquote.class);

        if(obj == null) return;
        boolean isTweet = Blockquote.CLASS_TWEET.equalsIgnoreCase(obj.getElementClass());

        if(isTweet == false){
            handleP(text);
        }
        int len = text.length();
        int where = text.getSpanStart(obj);
        text.removeSpan(obj);

        if( isTweet == false) {

            if (where != len) {

                StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
                text.setSpan(styleSpan, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                QuoteSpan quoteSpan = new QuoteSpan(mStyle.getQuoteBackgroundColor(), mStyle.quoteBitmap());
                text.setSpan(quoteSpan, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }else {/// ooops we are inside one of those tweets type of cells !
            //look for the twitter status

        }
    }


    private  void startDiv(RichTextDocumentElement text, Attributes attributes) {

        String elementClass = attributes.getValue("", "class");

        if("pb_feed".equalsIgnoreCase(elementClass)){
            String dataGame  = attributes.getValue("", "data-game");

            if(TextUtils.isEmpty(dataGame) == true) return;

            int where = text.length();
            String message = " TAKE  THE  QUIZ HERE!" ;

            text.append(message);
            String url = Uri.parse("http://www.playbuzz.com")
                    .buildUpon()
                    .encodedPath(dataGame)
                    .build()
                    .toString();


            text.setSpan( new StyleSpan(Typeface.BOLD), where, where + message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            UnsupportedContentSpan span = new UnsupportedContentSpan(url);
            text.setSpan(span, where,where + message.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        else if("fb-video".equalsIgnoreCase(elementClass)){
            String url = attributes.getValue("","data-href");
            if(TextUtils.isEmpty(url) == false){

                mInsideFacebookVideo = true;

                int where = text.length();
                String message = " See Facebook Video Here " ;
                text.append(message);
                UnsupportedContentSpan span = new UnsupportedContentSpan(url);
                text.setSpan(span, where,where + message.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
    private  void startImg(Attributes attributes) {

        //buildNewSpannable();
        ensureAtLeastThoseNewLines(mSpannableStringBuilder,1);
        String src = attributes.getValue("", "src");


//        BitmapSpan imageDrawable = new BitmapSpan( Uri.parse(src),
//                mStyle,
//                Integer.valueOf(attributes.getValue("width")),
//                Integer.valueOf(attributes.getValue("height")));

        int maxSize = mStyle.maxImageWidth();
        UrlBitmapSpan imageDrawable = new UrlBitmapSpan(Uri.parse(src),
                NumberUtils.parseImageDimension(attributes.getValue("width"),maxSize),
                NumberUtils.parseImageDimension(attributes.getValue("height"),0),
                        mStyle.maxImageWidth() );

        int len = mSpannableStringBuilder.length();
        mSpannableStringBuilder.append("\uFFFC");

        mSpannableStringBuilder.setSpan(imageDrawable,
                len,
                mSpannableStringBuilder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //HashMap<String,Object> attrs = new HashMap<>();
       // attrs.put(RichText.IMAGE_WIDTH, attributes.getValue("width"));
       // attrs.put(RichText.IMAGE_HEIGHT, attributes.getValue("height"));
       // mCallback.onElementFound(RichText.TNodeType.EImage, src, attrs);

    }

    //Bullet

    private  void startBullet() {

        ensureAtLeastThoseNewLines(mSpannableStringBuilder, 1);
        mSpannableStringBuilder.append("\t•  ");
       // int len = mSpannableStringBuilder.length();
       // mSpannableStringBuilder.setSpan(new Bullet(), len, len, Spannable.SPAN_MARK_MARK);



    }

    private  void endBullet() {

//        int len = mSpannableStringBuilder.length();
//        Object obj = getLast(mSpannableStringBuilder, Bullet.class);
//        int where = mSpannableStringBuilder.getSpanStart(obj);
//
//        mSpannableStringBuilder.removeSpan(obj);
//
//        if (where != len) {
//
//            BulletSpan bulletSpan = new BulletSpan();
//            mSpannableStringBuilder.setSpan(bulletSpan,
//                    where,
//                    len,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        }

    }

    private void buildNewSpannable(){

        RichTextDocumentElement newSpannable = new RichTextDocumentElement("");
        //skip the current image tag
        ListIterator<MarkupTag> tags = mStack.listIterator(mStack.size() - 1);

        while (tags.hasPrevious() == true){

            MarkupTag tag = tags.previous();

            if(tag.closeOnEnd == true)
                applyEndTag(mSpannableStringBuilder, tag.tag);

            if (tag.duplicateOnStart == true)
                applyStartTag(newSpannable, tag, tag.attributes);

        }

        if(mSpannableStringBuilder.length() > 0 ) {

            fixFlags(mSpannableStringBuilder);

            mCallback.onElementFound(RichText.TNodeType.EText,
                    mSpannableStringBuilder,
                    null);
        }

        mSpannableStringBuilder = newSpannable;

    }

    private static void startFont(RichTextDocumentElement text,
                                  Attributes attributes) {
        String color = attributes.getValue("", "color");
        String face = attributes.getValue("", "face");

        int len = text.length();
        text.setSpan(new Font(color, face), len, len, Spannable.SPAN_MARK_MARK);
    }

    private static void endFont(RichTextDocumentElement text) {
        int len = text.length();
        Object obj = getLast(text, Font.class);
        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        if (where != len) {
            Font f = (Font) obj;

            if (!TextUtils.isEmpty(f.mColor)) {
                if (f.mColor.startsWith("@")) {
                    Resources res = Resources.getSystem();
                    String name = f.mColor.substring(1);
                    int colorRes = res.getIdentifier(name, "color", "android");
                    if (colorRes != 0) {
                        ColorStateList colors = res.getColorStateList(colorRes);
                        text.setSpan(new TextAppearanceSpan(null, 0, 0, colors, null),
                                where, len,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    int c = Color.parseColor(f.mColor);
                    if (c != -1) {
                        text.setSpan(new ForegroundColorSpan(c | 0xFF000000),
                                where, len,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            if (f.mFace != null) {
                text.setSpan(new TypefaceSpan(f.mFace), where, len,
                             Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private  void startIFrame(RichTextDocumentElement text, Attributes attributes) {
        String href = attributes.getValue("", "src");
        if( EmbedUtils.parseLink(mStack.peek(), href, this) == false) {
            makeUnsupported(href,null,text);
        }
    }

    private void makeUnsupported(String link,String text,RichTextDocumentElement builder){
        //clean the link:
        if(link.indexOf("//") == 0){
            link = "http:" + link;
        }
        if(TextUtils.isEmpty(text) == true){
            text = link;
        }
        int len = builder.length();
        builder.append(text);
        builder.setSpan(new UnsupportedContentSpan(link), len, len + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void makeLink(String link,String text,RichTextDocumentElement builder){
        //clean the link:
        if(link.indexOf("//") == 0){
            link = "http:" + link;
        }
        final int maxLength = 20;
        if(TextUtils.isEmpty(text) == true){
            if(link.length() > maxLength){
                text = link.substring(0,maxLength - 1);
                text = text.trim();
                text = text + "...";
            }else {
                text = link;
            }
        }
        int len = builder.length();
        builder.append(text);
        builder.setSpan(new URLSpan(link), len, len + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void makeYoutube(String youtubeId,RichTextDocumentElement builder){

        ensureAtLeastOneNewLine(builder);
        int len = builder.length();
        builder.append(NO_SPACE_CHAR);
        builder.setSpan(new YouTubeSpan(youtubeId, mStyle.maxImageWidth()),
                len, builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    }


    private  void endIFrame(RichTextDocumentElement text) {
        endA(mStack, text);
    }


    private  void startSoundCloud(RichTextDocumentElement text, Attributes attributes) {

        buildNewSpannable();
        String src = attributes.getValue("", "url");
        src = EmbedUtils.getSoundCloudStream(src,"1f6456941b1176c22d44fb16ec2015a2");
        // mSpannableStringBuilder.append("\uFFFC");
        if(TextUtils.isEmpty(src) == false) {
            HashMap<String, Object> attrs = new HashMap<>();
            attrs.put(RichText.EMBED_TYPE, EmbedUtils.TEmbedType.ESoundCloud);
            mCallback.onElementFound(RichText.TNodeType.EEmbed, src, attrs);
        }
    }

    private  void endSoundCloud(RichTextDocumentElement text) {
        endA(mStack, text);
    }

    private  void startInstagramA(RichTextDocumentElement text,  Attributes attributes) {
        String href = attributes.getValue("", "href");
        //if( LinksUtils.parseLink( mStack.peek(), href,this) == false) {
        int len = text.length();
        Href h = new Href(href);

        mLastOpened.closeOnEnd = false;
        mLastOpened.duplicateOnStart = false;

        if( h.type == EmbedUtils.TEmbedType.EInstagram ) {
            // now here we are inside a tweet
            onLinkParsed(this,h.mHref,h.type);
        }else  {
            text.setSpan(h, len, len, Spannable.SPAN_MARK_MARK);
        }
        // }
    }

    private  void startA(RichTextDocumentElement text, boolean insideTweet, Attributes attributes) {
        String href = attributes.getValue("", "href");
        //if( LinksUtils.parseLink( mStack.peek(), href,this) == false) {
            int len = text.length();
        Href h = new Href(href);

        mLastOpened.closeOnEnd = !insideTweet;
        mLastOpened.duplicateOnStart = !insideTweet;

        if( h.type == EmbedUtils.TEmbedType.ETwitter && insideTweet) {
            // now here we are inside a tweet
            onLinkParsed(this,h.mHref,h.type);
        }else if (insideTweet == false) {
            text.setSpan(h, len, len, Spannable.SPAN_MARK_MARK);
        }
       // }
    }

    private static void endA(Stack<MarkupTag> stack, RichTextDocumentElement text) {
        int len = text.length();
        Object obj = getLast(text, Href.class);
        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        if (where != len) {

            if(obj == null){
                return;
            }

            Href h = (Href) obj;

            if (h.mHref != null) {

               if(isWithinTag( stack, "noscript") != null){
                    text.setSpan(new UnsupportedContentSpan(h.mHref), where, len,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    text.setSpan(new URLSpan(h.mHref), where, len,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    private static MarkupTag isWithinTag(Stack<MarkupTag> stack, String tag){

         for(MarkupTag t : stack){
             if(t.tag.equalsIgnoreCase(tag) == true)
                 return t;
         }

        return null;
    }

    private  void endHeader(RichTextDocumentElement text) {
        int len = text.length();
        Object obj = getLast(text, Header.class);

        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        // Back off not to change only the text, not the blank line.
        while (len > where && text.charAt(len - 1) == '\n') {
            len--;
        }

        if (where != len) {
            Header h = (Header) obj;

            int foreground = mStyle.headerColor();

            text.setSpan(new ForegroundColorSpan(foreground),
                    where,
                    len,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            text.setSpan(new RelativeSizeSpan(mStyle.headerIncrease(h.mLevel)),
                         where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new StyleSpan(Typeface.BOLD),
                         where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        handleStartTag(localName, attributes);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        handleEndTag(mSpannableStringBuilder, localName);
    }

    public void characters(char ch[], final int start, final int length) throws SAXException {

        if(storeContent(getCurrent()) == false){
            return;
        }

        /*
         * Ignore whitespace that immediately follows other whitespace;
         * newlines count as spaces.
         */

        if(mStyle.treatAsHtml() == true) {


            for (int i = 0; i < length; i++) {
                char c = ch[i + start];

                if (c == ' ' || c == '\n') {

                    char pred;
                    int len = mAccumulatedText.length();

                    //no text yet in the accumulated buffer
                    if (len == 0) {

                        len = mSpannableStringBuilder.length();

                        if (len == 0) {
                            pred = '\n';
                        } else {
                            pred = mSpannableStringBuilder.charAt(len - 1);
                        }
                    } else {
                        pred = mAccumulatedText.charAt(len - 1);
                    }

                    if (pred != ' ' && pred != '\n') {
                        mAccumulatedText.append(' ');
                    }
                } else {
                    mAccumulatedText.append(c);
                }
            }
        }else {
            mAccumulatedText.append(ch, start, length);
        }


    }

    public RichTextDocumentElement processAccumulatedTextContent()  {

        if(storeContent(getCurrent()) == false ||
                mAccumulatedText.length() == 0){

            return mSpannableStringBuilder;
        }

        Matcher m = Patterns.WEB_URL.matcher(mAccumulatedText);

        while (m.find()) {
            //link found
            int matchStart = m.start();
            int matchEnd = m.end();

            //any text in between ?
            StringBuffer buffer = new StringBuffer();
            m.appendReplacement(buffer, "");
            mSpannableStringBuilder.append(buffer);

            CharSequence link = mAccumulatedText.subSequence( matchStart, matchEnd);
           // if( EmbedUtils.parseLink(mAccumulatedText, String.valueOf(link), this) == false){
           if( EmbedUtils.parseLink(mAccumulatedText, String.valueOf(link), new EmbedUtils.ParseLinkCallback() {
                @Override
                public void onLinkParsed(Object callingObject, String result, EmbedUtils.TEmbedType type) {
                    if(type == EmbedUtils.TEmbedType.EYoutube){
                        makeYoutube(result, mSpannableStringBuilder);
                    }
                }
            }) == false){

               if(TextUtils.isEmpty(link) == false) {
                   makeLink(link.toString(),null,mSpannableStringBuilder);
                   //int where = mSpannableStringBuilder.length();
                   // mSpannableStringBuilder.append(link);
                   // mSpannableStringBuilder.setSpan(new URLSpan(link.toString()), where, where + link.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
               }
               // }

           }


        }
        StringBuffer buffer = new StringBuffer();
        m.appendTail(buffer);
        mSpannableStringBuilder.append(buffer);
        mAccumulatedText.setLength(0);

        return mSpannableStringBuilder;
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void skippedEntity(String name) throws SAXException {
    }

    @Override
    public void onLinkParsed(Object callingObject, String result, EmbedUtils.TEmbedType type) {

        if(type == EmbedUtils.TEmbedType.EYoutube){
            makeYoutube(result, mSpannableStringBuilder);
            return;
        }
        //we have to remove embeds from quotes tags
        buildNewSpannable();
        HashMap<String,Object> attrs = new HashMap<>();
        attrs.put(RichText.EMBED_TYPE,type);
        mCallback.onElementFound(RichText.TNodeType.EEmbed, result, attrs);

    }

    //those are just markers classes to tag specific index in the string
    private static class Bold { }
    private static class Italic { }
    private static class Underline { }
    private static class Big { }
    private static class Small { }
    private static class Monospace { }
    private static class Bullet { }
    private static class Blockquote {

        public static final String CLASS_TWEET = "twitter-tweet";
        public static final String CLASS_INSTAGRAM = "instagram-media";

        private String mClass;

        Blockquote(String elementClass){
            mClass = elementClass;
        }

        public String getElementClass(){
            return mClass;
        }
    }
    private static class Super { }
    private static class Sub { }

    private static class Background {

        public  int mColor;
        public Background(int color){
            mColor = color;
        }
    }

    private static class Font {
        public String mColor;
        public String mFace;

        public Font(String color, String face) {
            mColor = color;
            mFace = face;
        }
    }

    private static class Href {

        public String mHref;
        public EmbedUtils.TEmbedType type;

        public Href(String href) {
            mHref = href;
           if( EmbedUtils.parseLink(this, href, new EmbedUtils.ParseLinkCallback() {
                @Override
                public void onLinkParsed(Object callingObject, String result, EmbedUtils.TEmbedType type) {
                    Href.this.type = type;
                    Href.this.mHref = result;

                }
            }) == false){
               type = EmbedUtils.TEmbedType.EUnsupported;
           }
        }
    }

    private static class Header {
        private int mLevel;

        public Header(int level) {
            mLevel = level;
        }
    }
}


}

