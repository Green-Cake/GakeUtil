package org.gakeutil.util.math

import org.gakeutil.gcd

interface IFraction {

    companion object {

        fun new(denominator: Int, numerator: Int, autoReduction: Boolean): IFraction =
            if(autoReduction)
                Fraction(denominator, numerator)
            else
                FractionWithoutReduction(denominator, numerator)

    }

    val denominator: Int
    val numerator: Int

    val isReductionEnabled get() = this !is FractionWithoutReduction

    fun copy(d: Int = denominator, n: Int = numerator): IFraction

    private open class Fraction(override val denominator: Int, override val numerator: Int) : Number(), IFraction {

        override fun copy(d: Int, n: Int): IFraction =
            Fraction(d, n).apply { reduction() }

        fun reduction(): Fraction {

            val gcd = gcd(denominator, numerator)

            return Fraction(denominator / gcd, numerator / gcd)

        }

        operator fun plus(other: Fraction) = copy((denominator * other.numerator) + (other.denominator * numerator), numerator * other.numerator)

        operator fun minus(other: Fraction) = copy((denominator * other.numerator) - (other.denominator * numerator), numerator * other.numerator)

        operator fun times(other: Fraction) = copy(denominator * other.denominator, numerator * other.numerator)

        operator fun div(other: Fraction) = copy(denominator * other.numerator, numerator * other.denominator)

        operator fun plus(other: Int) = plus(Fraction(other, 1))

        operator fun minus(other: Int) = minus(Fraction(other, 1))

        operator fun times(other: Int) = times(Fraction(other, 1))

        operator fun div(other: Int) = div(Fraction(other, 1))

        override fun toString() = "$denominator / $numerator"

        override fun toByte() = toInt().toByte()

        override fun toChar() = toInt().toChar()

        override fun toDouble() = denominator.toDouble() / numerator

        override fun toFloat() = toDouble().toFloat()

        override fun toInt() = denominator / numerator

        override fun toLong() = toInt().toLong()

        override fun toShort() = toInt().toShort()

    }

    private class FractionWithoutReduction(override val denominator: Int, override val numerator: Int) : Fraction(denominator, numerator) {

        override fun copy(d: Int, n: Int): IFraction =
            FractionWithoutReduction(d, n)

    }

}
