package pdfscript.stream

import pdfscript.PdfScriptStream

open class Evaluation(val width: (EvaluationBase) -> Float,
                      val height:(EvaluationBase) -> Float,
                      val executionGraph:(PdfScriptStream, Coordinates) -> Unit) {

    fun execute(stream: PdfScriptStream, coordinates: Coordinates) = this.executionGraph(stream, coordinates)

    data class EvaluationBase(val available:Float, val accumulated:Float)

}
