/*
 * Copyright (c) 2015. Roberto  Prato <https://github.com/robertoprato>
 *
 *  *
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package io.square1.richtext.io.square1.richtext.sample;


import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import io.square1.richtext.R;
import io.square1.richtextlib.spans.RemoteBitmapSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.v2.content.ImageDocumentElement;
import io.square1.richtextlib.v2.content.OembedDocumentElement;
import io.square1.richtextlib.v2.content.RichDocument;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * Created by roberto on 22/12/2015.
 */
public class ContentAdapter extends BaseAdapter  {

    private RichDocument mDocument;
    private UrlBitmapDownloader mDownloader;


    public ContentAdapter( UrlBitmapDownloader downloader){
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
        else if(item instanceof ImageDocumentElement){

            if(convertView == null){
                ImageView img = new ImageView(parent.getContext());
                convertView = img;
            }

            String url = ((ImageDocumentElement)item).getImageURL();
            ImageView imageView = (ImageView)convertView;
            Glide.with(parent.getContext()).load(url).into(imageView);

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
