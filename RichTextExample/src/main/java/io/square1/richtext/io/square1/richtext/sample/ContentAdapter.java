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


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.square1.richtext.R;
import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.audio.AudioPlayer;
import io.square1.richtextlib.ui.audio.AudioPlayerHolder;
import io.square1.richtextlib.ui.web.WebContentHolder;
import io.square1.richtextlib.ui.video.RichVideoView;
import io.square1.richtextlib.v2.content.WebDocumentElement;
import io.square1.richtextlib.v2.content.ImageDocumentElement;
import io.square1.richtextlib.v2.content.OembedDocumentElement;
import io.square1.richtextlib.v2.content.RichDocument;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;
import io.square1.richtextlib.v2.content.VideoDocumentElement;

/**
 * Created by roberto on 22/12/2015.
 */
public class ContentAdapter extends BaseAdapter  {

    private RichDocument mDocument;
    private UrlBitmapDownloader mDownloader;

    private ArrayList<AudioPlayerHolder> mAudioHolders;
    private AudioPlayer mAudioPlayer;

    public ContentAdapter(UrlBitmapDownloader downloader, Context context){
        super();
        mAudioPlayer = new AudioPlayer(context);
        mDownloader = downloader;
        mAudioHolders = new ArrayList<>();
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
        final int type = getItemViewType(position);

        if(TYPE_WEB == type){
            return  getWebItemView((WebDocumentElement)item, convertView, parent);
        }

        if(TYPE_TEXT == type){
            return  getTextItemView((RichTextDocumentElement) item, convertView, parent);
        }

        if(TYPE_IMAGE == type){
            return  getImageItemView((ImageDocumentElement) item, convertView, parent);
        }

        if(TYPE_AUDIO == type){
            return getAudioEmbedView((OembedDocumentElement)item, convertView, parent);
        }

        if(TYPE_EMBED == type){
            return getEmbedView((OembedDocumentElement)item, convertView, parent);
        }

        //if(TYPE_VIDEO == type){
            return getVideoEmbedView((VideoDocumentElement)item, convertView, parent);
       // }

    }

    private View getImageItemView(ImageDocumentElement item, View convertView, ViewGroup parent){

        if(convertView == null){
            ImageView img = new ImageView(parent.getContext());
            convertView = img;
        }

        String url = item.getImageURL();
        ImageView imageView = (ImageView)convertView;
        Glide.with(parent.getContext()).load(url).into(imageView);

        return convertView;
    }

    private View getTextItemView(RichTextDocumentElement item, View convertView, ViewGroup parent){

        RichContentView view = (RichContentView)convertView;
        if(convertView == null){
            view = (RichContentView) LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view,parent,false);
            view.setUrlBitmapDownloader(mDownloader);
            convertView = view;
        }
        view.setText(item);

        return convertView;
    }

    private View getWebItemView(WebDocumentElement item, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.rich_text_embed_layout_webview,parent,false);
            new WebContentHolder(convertView);
        }
        WebContentHolder holder = (WebContentHolder) convertView.getTag();
        holder.setWebContent(item);
        return convertView;
    }

    private View getAudioEmbedView(OembedDocumentElement element, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rich_text_embed_layout_media_player,
                            parent, false);
            AudioPlayerHolder holder = new AudioPlayerHolder(convertView,mAudioPlayer);
            convertView.setTag(holder);
            mAudioHolders.add(holder);
        }

        ((AudioPlayerHolder)convertView.getTag()).setAudioFile(element.getContent());
        return convertView;

    }

    private View getVideoEmbedView(VideoDocumentElement element, View convertView, ViewGroup parent){

        if(convertView == null){
            RichVideoView text = new RichVideoView(parent.getContext());
            convertView = text;
        }

        ((RichVideoView)convertView).setData(element.getContent());

        return convertView;

    }



    private View getEmbedView(OembedDocumentElement element, View convertView, ViewGroup parent){


        if(convertView == null){
            TextView text = new TextView(parent.getContext());
            text.setBackgroundColor(Color.RED);
            convertView = text;
        }

        ((TextView)convertView).setText(element.getType() + " -> " + element.getContent());
        return convertView;

    }

    private static final int  TYPE_TEXT = 0;
    private static final int  TYPE_AUDIO = 1;
    private static final int  TYPE_EMBED = 2;
    private static final int  TYPE_VIDEO = 3;
    private static final int  TYPE_WEB = 4;
    private static final int  TYPE_IMAGE = 5;

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);

        if(item instanceof RichTextDocumentElement) return TYPE_TEXT;

        if(item instanceof OembedDocumentElement){
            EmbedUtils.TEmbedType embedType = ((OembedDocumentElement)item).getType();
            switch (embedType){
                case EAudio: return TYPE_AUDIO;
                default: return TYPE_EMBED;
            }
        }
        if(item instanceof VideoDocumentElement) return TYPE_VIDEO;
        if(item instanceof WebDocumentElement) return TYPE_WEB;
        return TYPE_IMAGE;
    }


    public void destroy(){
        mAudioPlayer.onDestroy();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

}
