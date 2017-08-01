/*
 * Copyright (c) 2017. Roberto  Prato <https://github.com/robertoprato>
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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.square1.richtext.R;
import io.square1.richtextlib.spans.ClickableSpan;
import io.square1.richtextlib.spans.RemoteBitmapSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.RichContentViewDisplay;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextBuilderExampleFragment extends Fragment {


    public TextBuilderExampleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_builder_example, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RichContentView contentView = (RichContentView)view.findViewById(R.id.richTextView);

        contentView.setOnSpanClickedObserver(new RichContentViewDisplay.OnSpanClickedObserver() {
            @Override
            public boolean onSpanClicked(ClickableSpan span) {
                String action = span.getAction();
                action = TextUtils.isEmpty(action) ? " no action" : action;
                Toast.makeText(getContext(), action, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        contentView.setUrlBitmapDownloader(new UrlBitmapDownloader() {

            @Override
            public void downloadImage(RemoteBitmapSpan urlBitmapSpan, Uri image) {
                    Glide.with(getActivity())
                            .load(image)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(new GlideTarget(getActivity(),urlBitmapSpan));
                }

        });

        String paragraph = getResources().getString(R.string.sample_text);
        RichTextDocumentElement element = new RichTextDocumentElement
                .TextBuilder("What is Lorem Ipsum")
                .bold()
                .color(Color.BLUE)
                .underline(true)
                .sizeChange(1.5f)
                .center()
                .newLine()
                .image("https://netdna.webdesignerdepot.com/uploads/2013/07/icons-animation.gif",10,10)
                .click("You have clicked on the image at the top!")
                .newLine()
                .append("Click the Image above")
                .font("fonts/SourceCodePro-Bold.ttf")
                .center()
                .bold()
                .sizeChange(1.5f)
                .color(Color.RED)
                .newLine()
                .append(paragraph)
                .left()
                .image("http://random-ize.com/lorem-ipsum-generators/lorem-ipsum/lorem-ipsum.jpg")
                .click("You have clicked on the  image in the middle of the text")
                .append("Click the lorem ipsum image")
                .font("fonts/GreatVibes-Regular.otf")
                .center()
                .bold()
                .sizeChange(1.5f)
                .color(Color.RED)
                .newLine()
                .append("It has survived not only five centuries,")
                .color(Color.GRAY)
                .sizeChange(2.0f)
                .center()
                .append("but also the leap into electronic typesetting,")
                .strikethrough(true)
                .append("remaining essentially unchanged.")
                .click("Hello you have clicked on the text")
                .bold()
                .italic()
                .build();


        contentView.setText(element);

    }
}
