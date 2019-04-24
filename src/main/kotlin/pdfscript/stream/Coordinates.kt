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
