package io.square1.richtext;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.ArrayList;

import io.square1.richtext.io.square1.richtext.sample.Utils;
import io.square1.richtextlib.spans.Style;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.DocumentElement;
import io.square1.richtextlib.v2.content.ImageDocumentElement;
import io.square1.richtextlib.v2.content.RichDocument;
import io.square1.richtextlib.v2.content.RichTextDocumentElement;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private Style EXTRACT_IMAGES_STYLE ;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        EXTRACT_IMAGES_STYLE =  new RichTextV2.DefaultStyle(getContext()){

            @Override
            public boolean extractImages(){
                return true;
            }
        };

    }

//    public void testPlainText() throws Exception {
//
//
//
//        final String HTML = " Some text is parsed here .\n some more text. Hello what is the content of this ? ";
//
//        RichDocument document = RichTextV2.fromHtml(getContext(), HTML);
//
//        assertEquals(document.getElements().size(), 1);
//
//        RichTextDocumentElement element = (RichTextDocumentElement) document.getElements().get(0);
//
//        assertEquals(HTML.equals(element.contentString()), true);
//
//    }

    public void testHtmlFile() throws Exception {

        String html = Utils.readFromfile(getContext(), "samples/test.html");
        RichDocument document = RichTextV2.fromHtml(getContext(), html);
        assertNotNull(document);
        ArrayList elements = document.getElements();
        assertTrue(elements.size() > 0);
    }

//    public void testHtmlP() throws Exception {
//
//        final String content = " content of  tag ";
//        final String HTML = "<p>"+content+"</p>";
//
//        RichDocument document = RichTextV2.fromHtml(getContext(), HTML);
//
//        assertEquals(document.getElements().size(), 1);
//
//        RichTextDocumentElement element = (RichTextDocumentElement) document.getElements().get(0);
//
//        assertEquals(content.equals(element.contentString()), true);
//
//    }

//    public void testHtmlH1() throws Exception {
//
//        final String content = " content of  tag ";
//        final String HTML = "<h1>"+content+"</h1>";
//
//        RichDocument document = RichTextV2.fromHtml(getContext(), HTML);
//
//        assertEquals(document.getElements().size(), 1);
//
//        RichTextDocumentElement element = (RichTextDocumentElement) document.getElements().get(0);
//
//        assertEquals(content.equals(element.contentString()), true);
//
//    }

    public void testHtmlIMG() throws Exception {

        final String IMAGE = "http://www.example.com/myimage.jpg";
        final String HTML = "<img src=\""+IMAGE+"\"></img>";

        RichDocument document = RichTextV2.fromHtml(getContext(), HTML, EXTRACT_IMAGES_STYLE);

        assertEquals(document.getElements().size(), 1);

        DocumentElement element = document.getElements().get(0);
        assertEquals(element instanceof ImageDocumentElement, true);

        ImageDocumentElement imageDocumentElement = (ImageDocumentElement)element;
        assertEquals(imageDocumentElement.getImageURL(), IMAGE);

    }
}




