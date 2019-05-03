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

import java.util.*

class Coordinates(val xInit:Float, val yInit:Float, val width:Float = 0f, val height:Float = 0f) {

    constructor(origin:Coordinates, width:Float, height:Float) : this(origin.x, origin.y, width, height) {
        this.origin = Optional.of(origin)
    }

    var origin:Optional<Coordinates> = Optional.empty()

    var x:Float = this.xInit;
    var y:Float = this.yInit;

    fun moveX(x:Float) {
        this.x += x
    }

    fun moveY(y:Float) {
        this.y += y
    }
}
