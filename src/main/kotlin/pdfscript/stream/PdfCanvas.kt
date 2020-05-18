/*
 * Copyright 2020 Leftshift One
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pdfscript.stream

import pdfscript.stream.configurable.Brush
import pdfscript.stream.configurable.Context
import pdfscript.stream.configurable.font.FontProvider
import pdfscript.stream.renderable.canvas.*
import java.io.InputStream
import java.net.URL
import java.util.*

class PdfCanvas(private val context: Context, private val fontProvider: FontProvider) {

    val evaluations = ArrayList<Evaluation>()

    fun withContext(configurer: Context.() -> Unit) = this.context.apply(configurer)

    fun drawText(text: String, x:Number, y:Number, brush: Brush.() -> Unit = {}) {
        val a = if (x.toFloat() < 0) context.format.width() + x.toFloat() else x.toFloat()
        val b = if (y.toFloat() <= 0) context.format.height() + y.toFloat() else y.toFloat()

        evaluations.addAll(DrawText(text, a, b, brush).evaluate(context, fontProvider))
    }

    fun drawLine(x1:Number, y1:Number, x2:Number, y2:Number, brush: Brush.() -> Unit = {}) {
        val a = if (x1.toFloat() < 0) context.format.width() + x1.toFloat() else x1.toFloat()
        val b = if (y1.toFloat() <= 0) context.format.height() + y1.toFloat() else y1.toFloat()
        val c = if (x2.toFloat() < 0) context.format.width() + x2.toFloat() else x2.toFloat()
        val d = if (y2.toFloat() <= 0) context.format.height() + y2.toFloat() else y2.toFloat()

        evaluations.addAll(DrawLine(a, b, c, d, brush).evaluate(context, fontProvider))
    }

    fun drawRect(x:Number, y:Number, w:Number, h:Number, brush: Brush.() -> Unit = {}) {
        val x2 = if (x.toFloat() < 0) context.format.width() + x.toFloat() else x.toFloat()
        val y2 = if (y.toFloat() <= 0) context.format.height() + y.toFloat() else y.toFloat()

        evaluations.addAll(DrawRect(x2, y2, w.toFloat(), h.toFloat(), brush).evaluate(context, fontProvider))
    }

    fun drawCircle(x:Number, y:Number, r:Number, brush: Brush.() -> Unit = {}) {
        val x2 = if (x.toFloat() < 0) context.format.width() + x.toFloat() else x.toFloat()
        val y2 = if (y.toFloat() <= 0) context.format.height() + y.toFloat() else y.toFloat()

        evaluations.addAll(DrawCircle(x2, y2, r.toFloat(), brush).evaluate(context, fontProvider))
    }

    fun drawSvg(image: () -> InputStream, x:Number, y:Number, s:Number, brush: Brush.() -> Unit = {}) {
        val x2 = if (x.toFloat() < 0) context.format.width() + x.toFloat() else x.toFloat()
        val y2 = if (y.toFloat() <= 0) context.format.height() + y.toFloat() else y.toFloat()

        evaluations.addAll(DrawSvg(image, x2, y2, s.toFloat(), brush).evaluate(context, fontProvider))
    }

    fun drawSvg(image: URL, x:Number, y:Number, s:Number, brush: Brush.() -> Unit = {}) {
        val x2 = if (x.toFloat() < 0) context.format.width() + x.toFloat() else x.toFloat()
        val y2 = if (y.toFloat() <= 0) context.format.height() + y.toFloat() else y.toFloat()

        evaluations.addAll(DrawSvg(image, x2, y2, s.toFloat(), brush).evaluate(context, fontProvider))
    }

    fun drawSvg(image: ByteArray, x:Number, y:Number, s:Number, brush: Brush.() -> Unit = {}) {
        val x2 = if (x.toFloat() < 0) context.format.width() + x.toFloat() else x.toFloat()
        val y2 = if (y.toFloat() <= 0) context.format.height() + y.toFloat() else y.toFloat()

        evaluations.addAll(DrawSvg(image, x2, y2, s.toFloat(), brush).evaluate(context, fontProvider))
    }

}
