package com.example.arexperimentation

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun computeBinomialCoeffFor3() {
        assertEquals(1, getBinomialCoeff(3, 0))
        assertEquals(3, getBinomialCoeff(3, 1))
        assertEquals(3, getBinomialCoeff(3, 2))
        assertEquals(1, getBinomialCoeff(3, 3))
    }

    @Test
    fun computeBinomialCoeffFor4() {
        assertEquals(1, getBinomialCoeff(4, 0))
        assertEquals(4, getBinomialCoeff(4, 1))
        assertEquals(6, getBinomialCoeff(4, 2))
        assertEquals(4, getBinomialCoeff(4, 3))
        assertEquals(1, getBinomialCoeff(4, 4))
    }
}
