package io.square1.richtext;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import io.square1.richtextlib.ParcelableSpannedBuilder;
import io.square1.richtextlib.style.RemoteBitmapSpan;
import io.square1.richtextlib.style.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichTextView;
import io.square1.richtextlib.ui.RichTextViewV2;


public class MainActivity2Activity extends ActionBarActivity implements UrlBitmapDownloader {

    public static void show(Context c, ParcelableSpannedBuilder store){
        Intent start = new Intent();
        start.setClass(c,MainActivity2Activity.class);
        start.putExtra("d", (Parcelable) store);
        c.startActivity(start);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        ParcelableSpannedBuilder store = getIntent().getParcelableExtra("d");
       // RichTextViewV2.class.cast(findViewById(R.id.textView)).setUrlBitmapDownloader(this);
        RichTextViewV2.class.cast(findViewById(R.id.textView)).setText(store);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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

    public  class SimpleTargetDrawable extends SimpleTarget<GlideDrawable> {

        private RemoteBitmapSpan mUrlBitmapSpan;

        SimpleTargetDrawable(RemoteBitmapSpan span){
            super();
            mUrlBitmapSpan = span;
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            mUrlBitmapSpan.updateBitmap(MainActivity2Activity.this, resource);

            resource.start();
        }
    };

}
