package com.sdapps.wellbeing.wellness

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.sdapps.wellbeing.LoginActivity.Companion.CURRENT_DATE
import com.sdapps.wellbeing.R
import com.sdapps.wellbeing.mindfullness.data.MenuBO
import com.sdapps.wellbeing.mindfullness.ui.MindfulnessActivity
import com.sdapps.wellbeing.mindfullness.ui.MindfulnessActivity.Companion.BREATHE
import com.sdapps.wellbeing.mindfullness.ui.MindfulnessActivity.Companion.CHALLENGE
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WellnessActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityWellnessBinding
    private var countDownTimer: CountDownTimer? = null
    private var timerInSeconds: Long = 0

    private var moduleCode: String? = BREATHE
    private var menuBO: MenuBO? = null

    private var activityCompletedCount = 0

    private lateinit var activityTrackerSP: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor

    private lateinit var timerText: TextView
    private var pastAnsweredQuestions: Int = 0

    private var currentPhase = INHALE
    private lateinit var todaysDate : LocalDate
    private var dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy")

    companion object {
        const val activityTracker = "activity_tracker"
        const val INHALE = "INHALE"
        const val EXHALE = "EXHALE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleBundles()
        if (!moduleCode.isNullOrEmpty()) {
            when (moduleCode) {
                BREATHE -> {
                    setContentView(R.layout.activity_wellness)
                    init()
                }

                CHALLENGE -> {
                    setContentView(R.layout.activity_challenge)
                    initChallengeLayout()
                }

                else -> throw IllegalArgumentException("Unable to find View!")
            }
        } else {
            setContentView(R.layout.activity_wellness)
        }
        activityTrackerSP =
            applicationContext.getSharedPreferences(activityTracker, Context.MODE_PRIVATE)
        spEditor = activityTrackerSP.edit()
    }

    private fun initChallengeLayout() {
        prepareChallengeList()
    }

    private fun prepareChallengeList() {
        val questionsList = arrayListOf(
            TaskBO(1, "Compliment a stranger."),
            TaskBO(2, "Help someone carry something heavy."),
            TaskBO(3, "Write a thank you note to someone."),
            TaskBO(4, "Buy coffee for the person in line behind you."),
            TaskBO(5, "Volunteer for a local charity.")
        )

        val qtnsAdapter = QuestionsAdapter(questionsList)

        findViewById<RecyclerView>(R.id.questionRecyclerView).apply {
            layoutManager =  LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            adapter = qtnsAdapter
        }

        findViewById<Button>(R.id.save).setOnClickListener {
            todaysDate = LocalDate.now()
            val formattedDate = dateFormatter.format(todaysDate)

            if(checkIfQuestionsAnsweredAlready()){
                spEditor.putInt(moduleCode, pastAnsweredQuestions + qtnsAdapter.getTotalCount())
                spEditor.putString(CURRENT_DATE,formattedDate.toString())
            }else {
                Log.d("DHANUSH", "new Achievement count : ${qtnsAdapter.getTotalCount()}")
                spEditor.putString(CURRENT_DATE,formattedDate.toString())
                spEditor.putInt(moduleCode,qtnsAdapter.getTotalCount())
            }
            spEditor.apply()
            finish()
        }



    }

    private fun checkIfQuestionsAnsweredAlready(): Boolean{
        val sp = getSharedPreferences(activityTracker, Context.MODE_PRIVATE)
        pastAnsweredQuestions = sp.getInt(moduleCode,0)
        Log.d("DHANUSH", "Past Data -> ${sp.getInt(moduleCode,0)}")
        return pastAnsweredQuestions > 0

    }


    private fun checkForPreviousAchievements(): Int {
        val sharedPreferences = getSharedPreferences(activityTracker, Context.MODE_PRIVATE)
        val spCounter = sharedPreferences.getInt(moduleCode, 0)
        Log.d("DHANUSH", "Past Data -> $spCounter")
        return spCounter
    }

    private fun init() {
        timerText = findViewById(R.id.timerText)
        activityCompletedCount = checkForPreviousAchievements()
        startCount()

    }

    private fun handleBundles() {
        if (intent != null) {
            menuBO = intent?.getParcelableExtra("key")
            moduleCode = intent?.getStringExtra(MindfulnessActivity.Companion.MODULE_CODE)
        }
    }

    private fun startCount() {
        if (menuBO!!.isTime) {
            timerText.visibility = View.VISIBLE
            timerInSeconds = (menuBO!!.attr).toLong()
            countDownTimer = object : CountDownTimer(timerInSeconds * 1000, 5000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerInSeconds = millisUntilFinished / 1000
                    timerText.text = currentPhase

                    when(currentPhase) {
                        INHALE -> {
                            currentPhase = EXHALE
                        }
                        EXHALE -> {
                            currentPhase = INHALE
                        }
                    }
                }

                override fun onFinish() {
                    timerText.visibility = View.GONE
                    findViewById<LottieAnimationView>(R.id.lottieAnimationView).apply {
                        visibility = View.VISIBLE
                        setAnimation(R.raw.completed_animation)
                        playAnimation()
                    }
                    findViewById<TextView>(R.id.activityCompleted).visibility = View.VISIBLE
                    activityCompletedCount += 1
                    sendToSp(activityCompletedCount)
                    Log.d("DHANUSH", "Activity completed : $activityCompletedCount")

                    findViewById<Button>(R.id.finishBtn).apply {
                        visibility = View.VISIBLE
                        setOnClickListener { finish() }
                    }
                }
            }.start()
        }
    }

    private fun sendToSp(counter: Int) {
        if (!moduleCode.isNullOrEmpty()) {
            spEditor.putInt(moduleCode, counter);
            spEditor.apply()

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}