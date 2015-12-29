package io.square1.richtext.io.square1.richtext.sample;


import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import io.square1.richtext.R;
import io.square1.richtextlib.spans.RemoteBitmapSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.v2.content.OembedDocumentElement;
import io.square1.richtextlib.v2.content.RichDocument;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * Created by roberto on 22/12/2015.
 */
public class ContentAdapter extends BaseAdapter  {

    private RichDocument mDocument;
    private UrlBitmapDownloader mDownloader;

    public ContentAdapter(UrlBitmapDownloader downloader){
        super();
        mDownloader = downloader;
    }


    public void setDocument(RichDocument document){
        mDocument = document;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return  mDocument == null ? 0 : mDocument.getElements().size();
    }

    @Override
    public Object getItem(int position) {
        return mDocument.getElements().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
                view = (RichContentView) LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view,parent,false);
                view.setUrlBitmapDownloader(mDownloader);
                convertView = view;
            }
            view.setText( (RichTextDocumentElement) item);
        }
        else if(item instanceof OembedDocumentElement){

            if(convertView == null){
                TextView text = new TextView(parent.getContext());
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
        Object item = getItem(position);
        if(item instanceof RichTextDocumentElement) return 0;
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

}
