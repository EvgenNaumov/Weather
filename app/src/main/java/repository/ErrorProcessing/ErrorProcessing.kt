package repository.ErrorProcessing

import repository.DetailsRepositoryRetrofit2Impl

interface ErrorProcessing {
    fun onWebApiErrorProcessing(numErr:Int, textErr:String = "")
    fun onWebApiErrorRetrofit(numErr:Int, lat:Double, lon:Double, callBackAPI: DetailsRepositoryRetrofit2Impl.CallBackAPI)
}