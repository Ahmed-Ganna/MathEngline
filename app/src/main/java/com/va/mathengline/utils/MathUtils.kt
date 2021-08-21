package com.va.mathengline.utils

import com.va.mathengline.models.MathOperationItem

class MathUtils {

    fun doOperation(operation: MathOperationItem) =  operation.getParameters().reduce { accumulator, element ->
        when(operation.getOperator()){
            MathOperator.ADD -> accumulator + element
            MathOperator.SUB -> accumulator - element
            MathOperator.MUL -> accumulator * element
            MathOperator.DIV -> accumulator / element
        }
    }
    
}