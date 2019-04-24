package pdfscript.stream.writable.decorator

import pdfscript.PdfScriptStream

abstract class AbstractDecoratable {

    abstract fun evaluate(stream: PdfScriptStream)

}
