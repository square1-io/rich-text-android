package io.square1.richtext;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.square1.richtextlib.RichText;
import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.RemoteBitmapSpan;
import io.square1.richtextlib.style.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichTextView;


public class MainActivity extends ActionBarActivity implements UrlBitmapDownloader, RichText.RichTextCallback {


    @Override
    public void onElementFound(RichText.TNodeType type, Object content, HashMap<String, Object> attributes) {
        if (type == RichText.TNodeType.EText){
            ((RichTextView) findViewById(R.id.textView)).setText((ParcelableSpannedBuilder) content);
             MainActivity2Activity.show(this, (ParcelableSpannedBuilder) content);
    }
}

    @Override
    public void onError(Exception exc) {

    }

    public  class SimpleTargetDrawable extends SimpleTarget<GlideDrawable> {

        private RemoteBitmapSpan mUrlBitmapSpan;

        SimpleTargetDrawable(RemoteBitmapSpan span){
            super();
            mUrlBitmapSpan = span;
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            mUrlBitmapSpan.updateBitmap(MainActivity.this, resource);

            resource.start();
        }
    };

    private final static String JSON_CONTENT = "content";
    private final static String JSON_TYPE = "type";
    private final static String JSON_ATTRS = "attrs";

    public JSONObject fromMap(HashMap<String,Object> params){

        JSONObject object = new JSONObject();
        if(params == null) return object;

        for(Map.Entry entry : params.entrySet()){
            try {
                object.put(String.valueOf(entry.getKey()), entry.getValue());
            }catch (Exception ex){}
        }

        return object;
    }

    public String ReadFromfile(String fileName) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Object> mItems = new ArrayList<>();
        final ArrayList<Object> obs = new ArrayList<>();
        String html = ReadFromfile("facebookvideo.html");
        final JSONArray output = new JSONArray();

//        RichText.fromHtml(this, html, new RichText.RichTextCallback() {
//
//            @Override
//            public void onElementFound(RichText.TNodeType type, Object content, HashMap<String, Object> attributes) {
//                Log.e("html[" + type + "]", String.valueOf(content));
//                try {
//
//                    mItems.add(content);
//
//                    JSONObject current = new JSONObject();
//
//
//                    if (type == RichText.TNodeType.EText) {
//                        ((RichTextView) findViewById(R.id.textView)).setText((SpannableStringBuilder)content);
//                        content = Html.toHtml((SpannableStringBuilder) content);
//                    }
//
//                    if (type == RichText.TNodeType.EEmbed) {
//                        EmbedUtils.TEmbedType embedType = (EmbedUtils.TEmbedType) attributes.get(RichText.EMBED_TYPE);
//                        current.put(JSON_TYPE, String.valueOf(embedType));
//                        attributes.remove(RichText.EMBED_TYPE);
//                    } else {
//                        current.put(JSON_TYPE, String.valueOf(type));
//                    }
//
//                    current.put(JSON_CONTENT, String.valueOf(content));
//                    current.put(JSON_ATTRS, fromMap(attributes));
//
//                    output.put(current);
//
//                } catch (Exception ex) {
//
//                }
//
//            }
//
//            @Override
//            public void onError(Exception exc) {
//
//            }
//        }, this, true);
//
//


        RichText.fromHtml(this,html,this,this);

        adapter = new BaseAdapter() {

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return 1;// mItems.size();
            }

            @Override
            public Object getItem(int position) {
                return mItems.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                Object item = getItem(position);
                RichTextView view = (RichTextView)convertView;
                if(convertView == null){
                    view = new RichTextView(MainActivity.this);
                    view.setDrawingCacheEnabled(false);
                    view.setRichTextContentChanged(new RichTextView.RichTextContentChanged() {
                        @Override
                        public void onContentChanged(RichTextView view) {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                if(item instanceof Spanned){
                    view.setText((Spanned)item);
                }else{
                   view.setText(String.valueOf(item));
                }
                return view;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };




        //((ListView) findViewById(R.id.list)).setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void downloadImage(RemoteBitmapSpan urlBitmapSpan, Uri image) {
        Glide.with(this).load(image).into(new SimpleTargetDrawable(urlBitmapSpan));
    }
}
