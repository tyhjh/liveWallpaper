package test

/**
 * Created by Tyhj on 2017/6/1.
 */

fun sum1(a: Int, b: Int) = a + b

fun sum2(a: Int, b: Int): Int {
    return a + b
}

fun main(args :Array<String>){
    println(sum1(2,3))
    println("这是什么情况$")
}

