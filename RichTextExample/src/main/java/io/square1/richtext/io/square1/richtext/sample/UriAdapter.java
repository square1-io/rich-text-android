/*
 * Copyright (c) 2016. Roberto  Prato <https://github.com/robertoprato>
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

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by roberto on 01/10/2016.
 */

public class UriAdapter extends BaseAdapter {

    private ArrayList<Uri> mUri;

    public UriAdapter(ArrayList list){
        mUri = list;
    }

    @Override
    public int getCount() {
        return mUri.size();
    }

    @Override
    public Uri getItem(int position) {
        return mUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_activated_1, parent, false);
        }

        TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
        Uri current = mUri.get(position);
        textView.setText(current.getLastPathSegment());
        return convertView;
    }
}
