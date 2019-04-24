package pdfscript.stream.renderable.decorator

import pdfscript.PdfScriptStream

abstract class AbstractDecoratable {

    abstract fun evaluate(stream: PdfScriptStream)

}
