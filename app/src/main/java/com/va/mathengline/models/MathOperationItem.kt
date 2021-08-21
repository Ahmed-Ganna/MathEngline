package com.va.mathengline.models

import com.va.mathengline.ui.MainActivity
import java.time.Duration

class MathOperationItem(val id:String, val operator: MainActivity.MathOperator, val delaySec:Long, val parameters : List<Int> , var result : Int? = null)
