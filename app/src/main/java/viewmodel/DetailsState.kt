package viewmodel

import repository.Weather

sealed class DetailsState {
    object Loading: DetailsState()
    data class Success(val weatherData:Weather):DetailsState()
    data class Error(val error:Throwable):DetailsState()
}