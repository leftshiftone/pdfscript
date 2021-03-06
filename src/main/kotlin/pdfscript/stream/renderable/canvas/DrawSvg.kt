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

package pdfscript.stream.renderable.canvas

import pdfscript.stream.Evaluation
import pdfscript.stream.configurable.Brush
import pdfscript.stream.configurable.Context
import pdfscript.stream.configurable.font.FontProvider
import pdfscript.stream.renderable.AbstractWritable
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL

class DrawSvg(private val supplier: () -> InputStream,
              private val x: Float,
              private val y: Float,
              private val s: Float,
              private val b: Brush) : AbstractWritable() {

    constructor(url: URL, x: Float, y: Float, s: Float, b: Brush) : this(url::openStream, x, y, s, b)
    constructor(bytes: ByteArray, x: Float, y: Float, s: Float, b: Brush)
            : this({ ByteArrayInputStream(bytes) }, x, y, s, b)

    override fun evaluate(context: Context, fontProvider: FontProvider): List<Evaluation> {
        // eager svg content loading
        val bytes = this.supplier().readBytes()

        return listOf(Evaluation({ 0f }, { 0f }) { stream, coordinates ->
            stream.drawSvg(ByteArrayInputStream(bytes), x, y, s)
        })
    }

}
