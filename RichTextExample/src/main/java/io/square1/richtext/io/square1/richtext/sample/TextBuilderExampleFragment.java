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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.square1.richtext.R;
import io.square1.richtextlib.ui.RichContentView;
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

        String paragraph = getResources().getString(R.string.sample_text);
        RichTextDocumentElement element = new RichTextDocumentElement
                .TextBuilder("What is Lorem Ipsum")
                .bold()
                .foreground(Color.BLUE)
                .underline(true)
                .sizeChange(1.5f)
                .center()
                .newLine()
                .paragraph(paragraph)
                .left()
                .append("It has survived not only five centuries,")
                .strikethrough(true)
                .foreground(Color.GRAY)
                .sizeChange(1.5f)
                .append("but also the leap into electronic typesetting,")
                .append("remaining essentially unchanged.")
                .bold()
                .italic()
                .build();

        contentView.setText(element);

    }
}
