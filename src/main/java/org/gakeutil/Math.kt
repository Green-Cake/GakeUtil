package org.gakeutil

import org.gakeutil.util.Double2d
import org.gakeutil.util.math.IFraction
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

fun <T : Double2d<T>> min(a: T, b: T) = a.copy(min(a.f, b.f), min(a.s, b.s))

fun <T : Double2d<T>> max(a: T, b: T) = a.copy(max(a.f, b.f), max(a.s, b.s))

tailrec fun gcd(a: Int, b: Int): Int = if(b == 0) a else gcd(b, a % b)

infix fun Int.gcdWith(other: Int): Int = gcd(this, other)

fun lcm(a: Int, b: Int) = a * b / gcd(a, b)

infix fun Int.lcmWith(other: Int): Int = lcm(this, other)

tailrec fun factorial(n: BigInteger, count: Int = n.toInt()): BigInteger =
    if(count <= 2) n else factorial(n * (count.toBigInteger() - BigInteger.ONE), count-1)

fun fraction(denominator: Int, numerator: Int = 1, autoReduction: Boolean = true): IFraction = IFraction.new(denominator, numerator, autoReduction)

fun Int.toFraction(autoReduction: Boolean = true) = fraction(this, autoReduction = autoReduction)

inline fun <T : Any?> T.doIf(b: Boolean, block: T.()->T): T = if(b) this.block() else this