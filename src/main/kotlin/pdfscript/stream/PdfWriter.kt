/*
 * Copyright 2019 Leftshift One
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

import pdfscript.stream.configurable.Context
import pdfscript.stream.configurable.font.FontProvider
import pdfscript.stream.renderable.*
import java.io.InputStream
import java.net.URL
import java.util.*

class PdfWriter(private val context: Context, private val fontProvider: FontProvider) {

    val evaluations = ArrayList<Evaluation>()

    fun paragraph(style: Context.() -> Unit, config: PdfWriter.() -> Unit) {
        evaluations.addAll(Paragraph(config, style, fontProvider).evaluate(context))
    }

    fun paragraph(config: PdfWriter.() -> Unit) {
        evaluations.addAll(Paragraph(config, {}, fontProvider).evaluate(context))
    }

    fun table(config: Table.TableWriter.() -> Unit) {
        evaluations.addAll(Table(config, {}, fontProvider).evaluate(context))
    }

    fun table(style: Context.() -> Unit, config: Table.TableWriter.() -> Unit) {
        evaluations.addAll(Table(config, style, fontProvider).evaluate(context))
    }

    fun table(style: Context.() -> Unit, ratio: List<Number>, config: Table.TableWriter.() -> Unit) {
        evaluations.addAll(Table(config, style, fontProvider).evaluate(context))
    }

    fun text(text: String) = evaluations.addAll(Text(text, {}, fontProvider).evaluate(context))
    fun text(style: Context.() -> Unit = {}, text: String) = evaluations.addAll(Text(text, style, fontProvider).evaluate(context))

    @Deprecated("use paragraph() instead")
    fun linebreak() = evaluations.addAll(LineBreak().evaluate(context))

    fun tab() {
        evaluations.addAll(Tabulator().evaluate(context))
    }

    fun tab(tabSize: Number) {
        evaluations.addAll(Tabulator(Optional.ofNullable(tabSize.toFloat())).evaluate(context))
    }

    fun image(image: String, width: Number, height: Number) {
        evaluations.addAll(Image(URL(image), width.toFloat(), height.toFloat()).evaluate(context))
    }

    fun image(image: () -> InputStream, width: Number, height: Number) {
        evaluations.addAll(Image(image, width.toFloat(), height.toFloat()).evaluate(context))
    }

    fun image(image: ByteArray, width: Number, height: Number) {
        evaluations.addAll(Image(image, width.toFloat(), height.toFloat()).evaluate(context))
    }

    fun svg(image: String, width: Number, height: Number) {
        evaluations.addAll(Svg(URL(image), width.toFloat(), height.toFloat()).evaluate(context))
    }

    fun svg(image: () -> InputStream, width: Number, height: Number) {
        evaluations.addAll(Svg(image, width.toFloat(), height.toFloat()).evaluate(context))
    }

    fun svg(image: ByteArray, width: Number, height: Number) {
        evaluations.addAll(Svg(image, width.toFloat(), height.toFloat()).evaluate(context))
    }

    fun superscript(text: String) = evaluations.addAll(Superscript(text, {}, fontProvider).evaluate(context))
    fun superscript(style: Context.() -> Unit = {}, text: String) = evaluations.addAll(Superscript(text, style, fontProvider).evaluate(context))

    fun subscript(text: String) = evaluations.addAll(Subscript(text, {}, fontProvider).evaluate(context))
    fun subscript(style: Context.() -> Unit = {}, text: String) = evaluations.addAll(Subscript(text, style, fontProvider).evaluate(context))

    fun withContext(configurer: Context.() -> Unit) = this.context.apply(configurer)

}
