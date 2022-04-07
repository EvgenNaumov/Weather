package repository

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(2000L) // эмуляция сетевого запроса
        return Weather()// эмуляция ответа
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> =  getRussianCities()// эмуляция ответа


    override fun getWeatherFromLocalStorageWord(): List<Weather> = getWorldCities()

}