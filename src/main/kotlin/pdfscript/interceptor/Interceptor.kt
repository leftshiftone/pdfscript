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

package pdfscript.interceptor

import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImage
import java.net.URL

open class Interceptor {

    open fun beginText() {
        // do nothing
    }

    open fun endText() {
        // do nothing
    }

    open fun showText(text:String) {
        // do nothing
    }

    open fun setFont(font: PDFont, size:Float) {
        // do nothing
    }

    open fun newLineAtOffset(x:Float, y:Float) {
        // do nothing
    }

    open fun drawImage(url: URL, x:Float, y:Float) {
        // do nothing
    }

    open fun drawImage(image: PDImage, x:Float, y:Float) {
        // do nothing
    }

    open fun close() {
        // do nothing
    }

    open fun setStrokingColor(colorStr: String) {
        // do nothing
    }

    open fun setNonStrokingColor(colorStr: String) {
        // do nothing
    }

    open fun drawLine(x1:Float, y1:Float, x2:Float, y2:Float) {
        // do nothing
    }

    open fun addRect(x:Float, y:Float, width:Float, height:Float) {
        // do nothing
    }

}
