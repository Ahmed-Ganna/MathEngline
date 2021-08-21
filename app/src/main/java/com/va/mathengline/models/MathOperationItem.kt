package com.va.mathengline.models

import com.va.mathengline.utils.MathOperator

open class MathOperationItem(val id:String,private val operator: MathOperator, val delaySec:Long,private val parameters : List<Int>, var result : Int? = null){

    open fun getParameters() = parameters

    open fun getOperator() = operator
}
