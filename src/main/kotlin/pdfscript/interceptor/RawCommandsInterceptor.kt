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

class RawCommandsInterceptor : Interceptor() {

    val commands = ArrayList<String>()

    override fun beginText() {
        commands.add("beginText:[]")
    }

    override fun endText() {
        commands.add("endText:[]")
    }

    override fun showText(text: String) {
        commands.add("showText:[$text]")
    }

    override fun setFont(font: PDFont, size: Float) {
        commands.add("setFont[${font.fontDescriptor.fontName}, $size]")
    }

    override fun newLineAtOffset(x: Float, y: Float) {
        commands.add("newLineAtOffset:[$x, $y]")
    }

    override fun drawImage(url: URL, x:Float, y:Float) {
        commands.add("drawImage(${url.path}, $x, $y]")
    }

    override fun drawImage(image: PDImage, x: Float, y: Float) {
        commands.add("drawImage($x, $y)")
    }

    override fun setStrokingColor(colorStr: String) {
        commands.add("setStrokingColor($colorStr)")
    }

    override fun setNonStrokingColor(colorStr: String) {
        commands.add("setNonStrokingColor($colorStr)")
    }

    override fun drawLine(x1:Float, y1:Float, x2:Float, y2:Float) {
        commands.add("drawLine($x1, $y1, $x2, $y2)")
    }

    override fun drawRect(x: Float, y: Float, width: Float, height: Float) {
        commands.add("drawRect($x, $y, $width, $height)")
    }

    override fun drawCircle(x: Float, y: Float, r: Float) {
        commands.add("drawCircle($x, $y, $r)")
    }

    override fun drawSvg(x:Float, y:Float, s: Float) {
        commands.add("drawSvg($x, $y, $s)")
    }

}
