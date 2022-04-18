package repository

interface ErrorProcessing {
    fun onWebApiErrorProcessing(numErr:Int, textErr:String, showAllInfo:Boolean = false)
}