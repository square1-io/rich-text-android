

package io.square1.richtext.io.square1.richtext.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import io.square1.richtext.R;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.v2.content.RichDocument;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;

public class Main2Activity extends ActionBarActivity {

    public static void showDocument(RichDocument document , Activity context){
        Intent intent = new Intent(context, Main2Activity.class);
        intent.putExtra("doc", document);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RichContentView richContentView = (io.square1.richtextlib.ui.RichContentView)findViewById(R.id.content);
        RichDocument richDocument = getIntent().getParcelableExtra("doc");
        richContentView.setText((RichTextDocumentElement) richDocument.getElements().get(0));
    }
}
