[![CircleCI branch](https://img.shields.io/circleci/project/github/leftshiftone/pdfscript/master.svg?style=flat-square)](https://circleci.com/gh/leftshiftone/pdfscript)
[![GitHub tag (latest SemVer)](https://img.shields.io/github/tag/leftshiftone/pdfscript.svg?style=flat-square)](https://github.com/leftshiftone/pdfscript/tags)
[![Bintray](https://img.shields.io/badge/dynamic/json.svg?label=bintray&query=name&style=flat-square&url=https%3A%2F%2Fapi.bintray.com%2Fpackages%2Fleftshiftone%2Fpdfscript%2Fone.leftshift.pdfscript.pdfscript%2Fversions%2F_latest)](https://bintray.com/leftshiftone/pdfscript/one.leftshift.pdfscript.pdfscript/_latestVersion)

# PDFScript

PDFScript is an open source software library for script based PDF generation
using a rendering evaluation graph. The graph nodes are used to evaluate the
bounding boxes of each renderable before it gets rendered. The evaluation 
graphs enables **PDFScript** to auto-adjust the renderables within the boundaries of a page.

Available in [jcenter](https://bintray.com/leftshiftone/pdfscript/one.leftshift.pdfscript.pdfscript/_latestVersion). Can be included like `compile 'one.leftshift.pdfscript:pdfscript:0.25.0'`.

## Quickstart

A PDF script is initiated by calling one of the static methods e.g. dinA4.
The static method call opens a new PDFScriptStream, which is used to create
an evaluation graph. By calling **execute** on the script stream, the PDF
pages gets rendered and the PDF is returned as a byte array.

```
dinA4 { text("Hello World") }.execute()
``` 

A PDFScriptStream automatically takes care of the boundaries of a PDF page format (e.g. dinA4).
If a row or column overflows the available space, the text automatically wraps to the available space.

For example the following script creates an evaluation graph which leads to multiple lines.
```
dinA4 { 
    text("Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.") 
}
``` 

## Header and Footer

By using the **withHeader** and **withFooter** function, it is possible to create a header and/or footer content for each page.
PDFScript automatically takes care of the page format boundary adjustments. 

```
dinA4 { 
    withHeader {
        text("Header Text")
    }
    withFooter {
        text("Footer Text")
    }
}
``` 

## Table

PDFScript provides a flexible way of table rendering. By default, PDFScript auto adjusts each
column by giving each column the same amount of space.

```
dinA4 { 
    table {
        row {
            col { text("Column 1") }
            col { text("Column 2") }
        }
        row {
            col { text("Column 3") }
            col { text("Column 4") }
        }
    } 
}
``` 

In order to overwrite the default behaviour, it is possible to set the ratio of each column at the row level.
A ratio setting e.g. `ratio(40, 60)` means, that the first column has a width of 40% while the second column
has a width of 60%. 

```
dinA4 { 
    table {
        row({ratio(40, 60)}) {
            col { text("Column 1") }
            col { text("Column 2") }
        }
        row({ratio(60, 40)}) {
            col { text("Column 3") }
            col { text("Column 4") }
        }
    } 
}
``` 

## Image
PDFScript renders an jpg or png image by calling **image** with an image source, width and height argument.
The image source can be a url, a byte array or a supplier of an input stream. The supplier handling is necessary
because of the possibility of multiple image render executions. For example an image within a header or footer
is rendered for each page. 

```
image("https://example.com/image.jpg", 150, 100)
image(getByteArray(), 150, 100)
image({getInputStream()}, 150, 100)
```

## Svg
Analogous to the **image** call the **svg** method lets define a svg renderable. The given svg image
gets encoded to a png image on the fly.

```
svg("https://example.com/image.svg", 150, 100)
svg(getByteArray(), 150, 100)
svg({getInputStream()}, 150, 100)
```

## Tabulator
By calling the **tab** renderable a tabulator gets rendered. By default, the tabulator space
is set to 100 points. The tabulator space value can be set by specifying the amount at the **tab** call.  

```
text("A")
tab()
text("B")
tab(200)
```

## Subscript and Superscript
Subscript and superscript renderables are elements that are set slightly below or above the normal line of text. 
They are smaller than the standard text and appear either below the baseline (subscript) or above the baseline (superscript).

```
superscript("A")
text("B")
subscript("A")
```

## Paragraph
A paragraph groups multiple renderables and separates them from other elements by a newline before and after the paragraph.
```
paragraph {
   text("A")
   text("B")
   text("C")
}
paragraph {
   text("D")
   text("E")
   text("F")
}
```

## Bold
By using the bold renderable a text can be styled in a bold manner. The specific font is selected automatically.
When using a non standard font the bold style font have to be registered.
```
val font1 = loadFont(document, "/SpecialFont-Regular.ttf")
val font2 = loadFont(document, "/SpecialFont-Bold.ttf")

val fontProvider = FontProvider()
fontProvider.addFont(font1)
fontProvider.addFont(font2)

dinA4({ font(font1) }, fontProvider) {
   text("A")
   bold("B")
   text("C")
}
```

## Canvas
A canvas supports to draw elements on an absolute position without adjusting the current position of the element flow.
So by the use of a canvas it is possible to draw elements in a free way onto the pdf.

Each function within the *withCanvas* block accepts the x and y coordinates on the pdf. While the x coordinate starts
on the left of the page, the y coordinate starts on the bottom. A negative x value can be used to start on the right
and a negative y value can be used to start from the top.

```
dinA4 {
   withCanvas {
      drawCircle(0, -100, 5)
      drawLine(0, -200, 10, -200)
      drawRect(0, -300, 10, 10)
      drawSvg("/image.svg", -400, 0, 0.25)
      drawImage("/image.jpg", 100, 200, -400, -200)
      drawText("text", -500, 0, 0.25)
   }
}
```

## Page and Pages
Within a **text** renderable it is possible to render the current page number as well as the total amount of pages.
To do this, simple add either the **{{page}}** and/or **{pages}}** expression within the text string.
Alternatively the variables can be added by the expressions **#PAGE** and **#PAGES**.

```
text("Page {{page}} of {{pages}}")
text("Page #PAGE of #PAGES")
```

## Styling
PDFScript allows the styling of each renderable by applying a **styler** function as the first parameter.

```
paragraph({paddingTop(5); paddingBottom(5)}) {
    text("B")
}
```

| name          | description                                                    | values         
|---------------|----------------------------------------------------------------|----------------
| font          | the font to use                                                | [font qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/font.properties "Font Qualifiers")
| fontName      | the name of the font to use                                    | [font qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/font.properties "Font Qualifiers")
| fontSize      | the size of the font to use                                    | number
| foreground    | the color which is used to render text and line elements       | hex code or [color qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/color.properties "Color Qualifiers")
| background    | the color which is used to render element backgrounds          | hex code or [color qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/color.properties "Color Qualifiers")
| border        | determines if and how the borders should be rendered           | "none", hex code or [color qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/color.properties "Color Qualifiers")
| borderBottom  | determines if and how the bottom border should be rendered     | "none", hex code or [color qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/color.properties "Color Qualifiers")
| borderTop     | determines if and how the top border should be rendered        | "none", hex code or [color qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/color.properties "Color Qualifiers")
| borderLeft    | determines if and how the left border should be rendered       | "none", hex code or [color qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/color.properties "Color Qualifiers")
| borderRight   | determines if and how the right border should be rendered      | "none", hex code or [color qualifier](https://github.com/leftshiftone/pdfscript/blob/master/src/main/resources/color.properties "Color Qualifiers")
| paddingTop    | determines the top padding                                     | number
| paddingBottom | determines the bottom padding                                  | number
| align         | indicates the text alignment                                   | "left", "center", "right"
| ratio         | the ratio of the row columns                                   | number varargs

## Font
PDFScript offers a convenient way to embed a font in a pdf document.

```
private fun loadFont(document: PDDocument, path:String): PDFont {
   val fontStream = TextTest::class.java.getResourceAsStream(path)
   return PDType0Font.load(document, fontStream)
}

val document = PDDocument()
val font = loadFont(document, "/customFont.ttf")

val interceptor = RawCommandsInterceptor()
dinA4({ font(font) }) { text("č") }.execute(interceptor, document)
```

Sometimes it is necessary to have a fallback font, if glyphs were used which are not supported by the current font.
For this case, a PDFontResolver can be used in order to build a font chain.

```
private fun loadFont(document: PDDocument, path:String): PDFont {
   val fontStream = TextTest::class.java.getResourceAsStream(path)
   return PDType0Font.load(document, fontStream)
}

val document = PDDocument()
val font1 = loadFont(document, "/customFont1.ttf")
val font2 = loadFont(document, "/customFont2.ttf")

val interceptor = RawCommandsInterceptor()
dinA4({ font(PDFontResolver(font1, font2)) }) { text("\u0627") }.execute(interceptor, document)
```

## Interceptor
The PDFScript execute method accepts an interceptor instance which can be used to hook into the events of the
PDF generation.
```
val interceptor = RawCommandsInterceptor()
dinA4 { text("Hello World") }.execute(interceptor)
print(interceptor.commands)
```

## Unit Testing
PDFScript supports pixel perfect PDF rendering unit tests by using the **RawCommandsInterceptor**.
The **RawCommandsInterceptor** collects the raw PDF instructions so that a unit test simple asserts the
actual raw commands with the expected commands. (collected from a previous pdf rendering run)

```
@Test
fun `create a superscript text`() {
    val interceptor = RawCommandsInterceptor()
    dinA4 {
        text("text")
        superscript("superscript")
    }.execute(interceptor)

    val expected = listOf(
            "setFont[Helvetica, 12.0]",
            "beginText:[]",
            "newLineAtOffset:[70.86614, 758.7796]",
            "showText:[text ]",
            "endText:[]",
            "setFont[Helvetica, 7.2000003]",
            "beginText:[]",
            "newLineAtOffset:[93.54614, 764.2796]",
            "showText:[superscript]",
            "endText:[]",
            "setFont[Helvetica, 12.0]"
    )
    assertEquals(expected, interceptor.commands)
}
```

## Native PDF reader/writer
PDFScript is supporting the native handling of  PDF elements by using one of the native reader/writer classes.
The PdfTextReader for example extracts all text elements together with the corresponding bounding boxes from a pdf file
while the PdfTextWriter creates a new pdf file by a list of pdfText bounding boxes.

```
val elements = PdfTextReader().read("/pdf/result.pdf")
val pdfBytes = PdfTextWriter().write(elements)
```

## Development

### Release
Releases are triggered locally. Just a tag will be pushed and CI pipelines take care of the rest.

#### Major
Run `./gradlew final -x bintrayUpload -Prelease.scope=major` locally.

#### Minor
Run `./gradlew final -x bintrayUpload -Prelease.scope=minor` locally.

#### Patch
Run `./gradlew final -x bintrayUpload -Prelease.scope=patch` locally.

#### Upcoming Features

* Footnotes support
* Endnotes support
