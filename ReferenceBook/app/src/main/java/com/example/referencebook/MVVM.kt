package com.example.referencebook

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MVVM: ViewModel() {
    private val _newsFeed = MutableStateFlow(
        listOf(
            Data(R.drawable.jaki, "Пришли холода и хозяйка собаки Жаки утеплила питомца", 0),
            Data(R.drawable.fatcat, "Худеющий кот Крошик пытался устроить побег и застрял в полке", 0),
            Data(R.drawable.kasha, "Собака по кличке Каша научилась команде \"хрюкни\"", 0),
            Data(R.drawable.itcat, "Японская IT-компания взяла на работу десять кошек", 0),
            Data(R.drawable.katysha, "Студенты подарили панде Катюше кубики на 1 сентября", 0),
            Data(R.drawable.pingvin, "Пингвин-долгожитель прожил вдвое больше обычного и оставил 230 потомков", 0),
            Data(R.drawable.dog, "Пёс предотвратил ограбление и был официально принят на работу охранником", 0),
            Data(R.drawable.chicken, "Две беспризорные курицы были «арестованы» полицией", 0),
            Data(R.drawable.gadalka, "Кот, живший в доме престарелых, предсказал более ста смертей", 0),
            Data(R.drawable.bear, "Людям запретили делать селфи с «депрессивным» медведем", 0),
        )
    )
    private val _currentNews = MutableStateFlow(_newsFeed.value.take(4))
    val currentNews: StateFlow<List<Data>> = _currentNews

    init {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                try {
                    delay(5000)
                    updateRandomNews()
                } catch (e: Exception) {
                    println(e)
                }
            }
        }
    }

    fun getLikes(i: Int) {
        val updatedCurrentNews = _currentNews.value.toMutableList()
        val updatedItem = updatedCurrentNews[i].copy(likes = updatedCurrentNews[i].likes + 1)
        updatedCurrentNews[i] = updatedItem
        _currentNews.value = updatedCurrentNews
        val indexInFeed = _newsFeed.value.indexOfFirst { it.title == updatedItem.title }

        if (indexInFeed != -1) {
            val updatedNewsFeed = _newsFeed.value.toMutableList()
            updatedNewsFeed[indexInFeed] = updatedItem
            _newsFeed.value = updatedNewsFeed
        }
    }

    private fun updateRandomNews() {
        val currentNewsList = _currentNews.value.toMutableList()
        val availableNews = _newsFeed.value.filter { it !in currentNewsList }
        if (availableNews.isNotEmpty()) {
            val randomIndex = Random.nextInt(0, 4)
            val randomNews = availableNews.random()
            currentNewsList[randomIndex] = randomNews
            _currentNews.value = currentNewsList
        }
    }
}