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

package pdfscript.stream.renderable

import pdfscript.stream.Evaluation
import pdfscript.stream.configurable.Context
import pdfscript.stream.configurable.font.FontProvider
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL

class Image (private val supplier: () -> InputStream, private val width: Float, private val height: Float) : AbstractWritable() {

    constructor(url: URL, width: Float, height: Float) : this(url::openStream, width, height)
    constructor(bytes: ByteArray, width: Float, height: Float) : this({ByteArrayInputStream(bytes)}, width, height)

    override fun evaluate(context: Context, fontProvider: FontProvider): List<Evaluation> {
        return listOf(Evaluation({width}, {height}) { stream, coordinates ->
            coordinates.moveY(-height)
            stream.drawImage(this.supplier(), width.toInt(), height.toInt(), coordinates.x, coordinates.y)
            coordinates.moveY(height)

            coordinates.moveX(width)
        })
    }

}
