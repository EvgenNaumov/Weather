package viewmodel

import repository.Weather

sealed class AppState{
    object Loading:AppState()
    data class Success(val weatherData: Weather):AppState(){
        fun test(){}
    }
    data class Error(val error:Throwable):AppState()
}
