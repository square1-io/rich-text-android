package io.square1.richtext;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


import io.square1.richtextlib.v2.content.OembedDocumentElement;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.spans.RemoteBitmapSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentView;


import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichDocument;


public class MainActivity extends ActionBarActivity implements UrlBitmapDownloader {


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

        String html = ReadFromfile("joe.html");
        final RichDocument document =  RichTextV2.fromHtml(this, html);

        RichTextDocumentElement element = (RichTextDocumentElement) document.getElements().get(0);
        char c = element.charAt(element.length() - 1);
        element.trim(1);
        ((RichContentView)findViewById(R.id.fixed)).setText(element);

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
                return  document.getElements().size();
            }

            @Override
            public Object getItem(int position) {
                return document.getElements().get(position);
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
                return getViewV2(position,convertView,parent);
            }



            public View getViewV2(int position, View convertView, ViewGroup parent) {

                Object item = getItem(position);

                if(item instanceof RichTextDocumentElement){

                    RichContentView view = (RichContentView)convertView;
                    if(convertView == null){
                        view = (RichContentView)LayoutInflater.from(MainActivity.this).inflate(R.layout.text_view,parent,false);
                        view.setUrlBitmapDownloader(MainActivity.this);
                        convertView = view;
                    }
                    view.setText( (RichTextDocumentElement) item);
                }
                else if(item instanceof OembedDocumentElement){

                    if(convertView == null){
                        TextView text = new TextView(MainActivity.this);
                        text.setBackgroundColor(Color.RED);
                        convertView = text;
                    }
                    OembedDocumentElement oembedElement = (OembedDocumentElement)item;
                    ((TextView)convertView).setText(oembedElement.getType() + " " + oembedElement.getContent());
                }

                return convertView;
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

        ((ListView) findViewById(R.id.list)).setAdapter(adapter);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity2Activity.show(MainActivity.this, document);
            }
        });
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
