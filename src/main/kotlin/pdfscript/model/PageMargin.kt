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

class PageMargin(val top:Float, val bottom:Float, val left:Float, val right:Float, val header:Float, val footer:Float) {

    companion object {
        /** user space units per inch  */
        private val POINTS_PER_INCH = 72f
        /** user space units per millimeter  */
        private val POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH

        @JvmStatic
        fun standard() = PageMargin(25 * POINTS_PER_MM,
                25 * POINTS_PER_MM,
                25 * POINTS_PER_MM,
                25 * POINTS_PER_MM,
                12.5f * POINTS_PER_MM,
                12.5f * POINTS_PER_MM)
    }

}
