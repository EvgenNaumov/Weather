package viewmodel

import repository.Weather

sealed class AppState{
    object Loading:AppState()
    data class Success(val weatherData: List<Weather>):AppState(){
        fun test(){}
    }
    data class Error(val error:Throwable):AppState()
}
