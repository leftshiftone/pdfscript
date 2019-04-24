# PDFScript

PDFScript is an open source software library for script based PDF generation
using a rendering evaluation graph. The graph nodes are used to evaluate the
bounding boxes of each renderable before it gets rendered. The evaluation 
graphs enables **PDFScript** to auto-adjust the renderables within the boundaries 
of a page.

## Quickstart

A PDF script is initiated by calling one of the static methods e.g. dinA4.
The static method call opens a new PDFScriptStream, which is used to create
an evaluation graph. By calling **execute** on the script stream, the PDF
pages gets rendered.

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
PDFScript renders an image by calling **image** with an url, width and height argument. 

```
image("https://example.com/image.jpg", 150, 100)
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

## Superscript
TODO

## Paragraph
TODO

## Styling
TODO

## Interceptor
TODO

## Unit Testing
TODO
