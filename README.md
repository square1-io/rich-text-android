
# RichText Library

[ ![Download](https://api.bintray.com/packages/square1io/maven/richtext/images/download.svg) ](https://bintray.com/square1io/maven/richtext/_latestVersion)

###Features
- Display rich text using a fluent interface.
- Support Image loading from network.


####Sample Fluent Interface 
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
                .center()
                .bold()
                .sizeChange(1.5f)
                .color(Color.RED)
                .newLine()
                .append("It has survived not only five centuries,")
                .color(Color.GRAY)
                .sizeChange(2.5f)
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



<p align="center">
 <img src="resources/sample-text-rendered.gif?raw=true" />
</p>