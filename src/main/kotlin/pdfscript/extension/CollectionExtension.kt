package pdfscript.extension

fun Collection<Float>.sumOrDefault(default:Float):Float = if (this.isEmpty()) default else this.sum()
fun Collection<Float>.maxOrDefault(default:Float):Float = if (this.isEmpty()) default else this.max()!!
