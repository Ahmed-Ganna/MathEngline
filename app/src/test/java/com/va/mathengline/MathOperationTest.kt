package com.va.mathengline

import com.va.mathengline.models.MathOperationItem
import com.va.mathengline.utils.MathOperator
import com.va.mathengline.utils.MathUtils
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


/**
 * Unit tests for the MathOperation logic.
 */
@RunWith(MockitoJUnitRunner::class)
class MathOperationTest {


    @Test
    fun mathOperation_AddOperation_ReturnsAdd() {

        val item =  Mockito.mock(MathOperationItem::class.java)

        Mockito.`when`(item.getParameters()).thenReturn(listOf(2,5,80,40,80,90))
        Mockito.`when`(item.getOperator()).thenReturn(MathOperator.ADD)

        assertEquals(297, MathUtils().doOperation(item))
    }

    @Test
    fun mathOperation_SubOperation_ReturnsSub() {

        val item =  Mockito.mock(MathOperationItem::class.java)

        Mockito.`when`(item.getParameters()).thenReturn(listOf(100,20,30,10))
        Mockito.`when`(item.getOperator()).thenReturn(MathOperator.SUB)

        assertEquals(40, MathUtils().doOperation(item))
    }

    @Test
    fun mathOperation_MulOperation_ReturnsMul() {

        val item =  Mockito.mock(MathOperationItem::class.java)

        Mockito.`when`(item.getParameters()).thenReturn(listOf(20,10,30))
        Mockito.`when`(item.getOperator()).thenReturn(MathOperator.MUL)

        assertEquals(6000, MathUtils().doOperation(item))
    }

    @Test
    fun mathOperation_DivOperation_ReturnsDiv() {

        val item =  Mockito.mock(MathOperationItem::class.java)

        Mockito.`when`(item.getParameters()).thenReturn(listOf(100,2,2))
        Mockito.`when`(item.getOperator()).thenReturn(MathOperator.DIV)

        assertEquals(25, MathUtils().doOperation(item))
    }


}