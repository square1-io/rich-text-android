package io.square1.richtext;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.RichText;


public class MainActivity extends ActionBarActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // List<PostItem> items = parser.parse();
        final ArrayList<Object> obs = new ArrayList<>();

        String html = ReadFromfile("complete_set.html");

       final JSONArray output = new JSONArray();

        RichText.fromHtml(this, html, new RichText.RichTextCallback() {


            @Override
            public void onElementFound(RichText.TNodeType type, Object content, HashMap<String, Object> attributes) {
                Log.e("html[" + type + "]", String.valueOf(content));
                try {

                    JSONObject current = new JSONObject();


                    if(type == RichText.TNodeType.EText){
                       content = Html.toHtml((SpannableStringBuilder) content);
                    }

                     if(type == RichText.TNodeType.EEmbed){
                        EmbedUtils.TEmbedType embedType = (EmbedUtils.TEmbedType) attributes.get(RichText.EMBED_TYPE);
                        current.put(JSON_TYPE, String.valueOf(embedType));
                        attributes.remove(RichText.EMBED_TYPE);
                    }else {
                        current.put(JSON_TYPE, String.valueOf(type));
                    }

                    current.put(JSON_CONTENT, String.valueOf(content));
                    current.put(JSON_ATTRS, fromMap(attributes));

                    output.put(current);

                }catch (Exception ex){

                }

            }

            @Override
            public void onError(Exception exc) {

            }
        },true);

         Log.i("json", output.toString());

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
}
