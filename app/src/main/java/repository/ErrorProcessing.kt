package repository

interface ErrorProcessing {
    fun onWebApiErrorProcessing(numError:Int, textErr:String)
}