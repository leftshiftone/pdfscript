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

package pdfscript.model

import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.common.PDRectangle.*

class PageFormat(val rectangle:PDRectangle) {

    fun page():PDPage = PDPage(rectangle)

    companion object {
        @JvmStatic
        fun dinA0() = PageFormat(A0)
        @JvmStatic
        fun dinA1() = PageFormat(A1)
        @JvmStatic
        fun dinA2() = PageFormat(A2)
        @JvmStatic
        fun dinA3() = PageFormat(A3)
        @JvmStatic
        fun dinA4() = PageFormat(A4)
        @JvmStatic
        fun dinA5() = PageFormat(A5)
        @JvmStatic
        fun dinA6() = PageFormat(A6)
    }

    fun height() = rectangle.height
    fun width() = rectangle.width

}
