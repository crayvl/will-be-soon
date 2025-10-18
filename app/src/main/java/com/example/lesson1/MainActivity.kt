package com.example.lesson1
import java.util.*
import java.util.concurrent.TimeUnit
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.collections.get

class MainActivity : AppCompatActivity() {

    // Переменные
    private lateinit var mainText: TextView
    private lateinit var textDD: TextView
    private lateinit var textHH: TextView
    private lateinit var textMM: TextView

    private lateinit var currentDate: Date
    private var numDays: Int = 0
    private var numHours: Int = 0
    private var numMinutes: Int = 0
    private var numSeconds: Int = 0

    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mainText = findViewById(R.id.mainText)
        textDD = findViewById(R.id.textDD)
        textHH = findViewById(R.id.textHH)
        textMM = findViewById(R.id.textMM)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        main()
    }
    private fun main() {
        getBetweenWithCalendar()

        textDD.text = numDays.toString()
        textHH.text = numHours.toString()
        textMM.text = numMinutes.toString()
        startRepeatingTask()
    }

    private fun getBetweenWithCalendar(){
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentSecond = calendar.get(Calendar.SECOND)
        currentDate = calendar.time

        calendar.set(2025, Calendar.DECEMBER, 26, 0,0)
        val targetDate = calendar.time

        val difference = targetDate.time - currentDate.time
        numDays = (TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)).toInt()
        numHours = 23 - currentHour
        numMinutes = 59 - currentMinute
        numSeconds = 59 - currentSecond
    }

    private fun startRepeatingTask() {
        job = lifecycleScope.launch {
            while (isActive) {
                updateTimes()
                delay(1000L) // Пауза 1 секунда
            }
        }
    }
    private fun updateTimes(){
        numSeconds--
        mainText.text = numSeconds.toString()
        if(numSeconds <= 0){
            numSeconds = 60
            numMinutes--
            updateUI()
            if(numMinutes <= -1){
                numMinutes = 59
                numHours--
                if(numHours <= -1){
                    numHours = 23
                    numDays--
                }
            }
        }
    }
    private fun updateUI()
    {
        mainText.text = numSeconds.toString()
        textMM.text = numMinutes.toString()
        textHH.text = numHours.toString()
        textDD.text = numDays.toString()
    }
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}