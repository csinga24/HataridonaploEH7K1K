package hu.bme.aut.android.hataridonaploeh7k1k.ui.habittracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitTrackerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is HABITTRACKER Fragment \n commming soon..."
    }
    val text: LiveData<String> = _text
}