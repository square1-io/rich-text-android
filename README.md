
# RichText Library

[ ![Download](https://api.bintray.com/packages/square1io/maven/richtext/images/download.svg) ](https://bintray.com/square1io/maven/richtext/_latestVersion)

Features
--------
- Display rich text using a fluent interface.
- Support Image loading from network.
- Parse HTML, including images and video tag into displayable content.

Download
--------

Use Gradle:

```gradle
repositories {
   jcenter()
}

dependencies {
  compile 'io.square1:richtext:x.x.x'
}
```

Setup a RichContentView
--------
Add a RichContentView to an xml layout, wrap around a ScrollView to enable content scrolling.

```xml 
 <ScrollView
     android:layout_width="match_parent"
     android:layout_height="match_parent">
     <io.square1.richtextlib.ui.RichContentView
         android:padding="10dip"
         android:id="@+id/richTextView"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:text="@string/hello_blank_fragment" />
 </ScrollView>
```
Enable Image download 
--------
Supply an instance of a class that implements UrlImageDownloader instance and can download images
from the network: 

```java
    contentView.setUrlBitmapDownloader(new UrlBitmapDownloader() {
      @Override
      public void downloadImage(final RemoteBitmapSpan urlBitmapSpan, Uri image) {
        Glide.with(activity)
            .load(image)
            .into(new BaseTarget<Drawable>() {
              @Override
              public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                urlBitmapSpan.updateBitmap(activity, resource);
              }

              @Override
              public void getSize(@NonNull SizeReadyCallback cb) {
                cb.onSizeReady(urlBitmapSpan.getPossibleSize().width(), urlBitmapSpan.getPossibleSize().height());
              }

              @Override
              public void removeCallback(@NonNull SizeReadyCallback cb) {

              }
            });

      }

    });
```

Enable click events  
--------
Supply an instance of a clicked observer to receive on click events on parts of the content: 

```java
        contentView.setOnSpanClickedObserver(new RichContentViewDisplay.OnSpanClickedObserver() {
            @Override
            public boolean onSpanClicked(ClickableSpan span) {
                String action = span.getAction();
                action = TextUtils.isEmpty(action) ? " no action" : action;
                Toast.makeText(getContext(), action, Toast.LENGTH_LONG).show();
                return true;
            }
        });
```
Parse and display of HTML  
--------
```java
 String html = "<p><i>This text is italic</i></p>";
 RichTextDocumentElement element = RichTextV2.textFromHtml(context, html);
 contentView.setText(element);
```
Sample Fluent Interface to create formatted text 
--------
```java
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
                .video("http://html5demos.com/assets/dizzy.mp4")
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
```

![sample](resources/sample-text-rendered.gif?raw=true)


License 
--------

RichText is licensed under the Apache v2 license:

Copyright 2017 www.square1.io

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

