package view.weatherlist

import repository.Weather

interface onItemListClickListener {
    fun onItemViewClick(weather:Weather)
}