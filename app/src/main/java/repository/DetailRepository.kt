package repository

interface DetailRepository {
 fun getWeatherDetails(city: City):WeatherDTO
}