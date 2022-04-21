package view.main

import repository.Weather

interface onItemListClickListener {
    fun onItemViewClick(weather:Weather)
}