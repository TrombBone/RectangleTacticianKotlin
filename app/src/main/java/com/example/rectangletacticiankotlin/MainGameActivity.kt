package com.example.rectangletacticiankotlin

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rectangletacticiankotlin.MainGameFragment.Companion.playersResults

class MainGameActivity : AppCompatActivity(), OnFragmentListener {

    private var playerCount = 0
    private var fieldWidth = 0
    private var fieldHeight = 0
    private var playerNumber = 1

    private var rectWidth = 0
    private var rectHeight = 0

    var isEndGame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)
        supportActionBar?.hide() ?: throw NoSuchElementException("ActionBar is lack")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        nextTurn()

        playerCount = intent.getIntExtra("playerCount", 2)
        fieldWidth = intent.getIntExtra("fieldWidth", 25)
        fieldHeight = intent.getIntExtra("fieldHeight", 35)

//        Log.d("my", "playerCount_MainGameActivity: $playerCount")
//        Log.d("my", "fieldWidth_MainGameActivity: $fieldWidth")
//        Log.d("my", "fieldHeight_MainGameActivity: $fieldHeight")
    }

    private fun nextTurn() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            PlayerStartFragment().apply {
                Bundle().also {
                    it.putInt("playerNumber", playerNumber)
                    arguments = it
                }
                replace(R.id.frame_container_main, this)
            }
            commit()
        }
    }

    override fun onButtonSelected(buttonId: Int) {
        when(buttonId) {
            R.id.playerNotificationTV -> supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.frame_container_main, DiceFragment())
                commit()
            }
            R.id.generateSidesButton -> supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                MainGameFragment().apply {
                    Bundle().also {
                        it.putInt("playerCount", playerCount)
                        it.putInt("fieldWidth", fieldWidth)
                        it.putInt("fieldHeight", fieldHeight)
                        it.putInt("playerNumber", playerNumber)
                        it.putInt("rectWidth", rectWidth)
                        it.putInt("rectHeight", rectHeight)
                        arguments = it
//                        Log.d("my", "arguments_MainGameActivityForShare: $arguments")
                    }
                    replace(R.id.frame_container_main, this)
                }
                commit()
            }
            R.id.nextTurnButton -> {
                if (playerNumber == playerCount) playerNumber = 1 else playerNumber++
//                Log.d("my", "playerNumber_nextTurn: $playerNumber")
                nextTurn()
            }
            R.id.mySurfaceView -> {
                FinishDialogFragment().apply {
                    Bundle().also {
                        it.putInt("player1", playersResults[1]!!)
                        it.putInt("player2", playersResults[2]!!)
                        if (playerCount == 4) {
                            it.putInt("player3", playersResults[3]!!)
                            it.putInt("player4", playersResults[4]!!)
                        }
                        arguments = it
                    }
                    show(supportFragmentManager, "finishDialog")
                }
            }
        }
    }

    override fun onParamsSelected(params: Map<String, String>) {
        rectWidth = params["rectWidth"]?.toInt() ?: 0
        rectHeight = params["rectHeight"]?.toInt() ?: 0
//        Log.d("my", "rectWidthFromDiceActivity: $rectWidth")
//        Log.d("my", "rectHeightFromDiceActivity: $rectHeight")
    }

    override fun onBackPressed() {
        //Есть 2 возможных варианта контекста внутри этого метода:
        // 1. Спросить пользователя, точно ли он хочет выйти и предупредить о несохранности всех данных игры.
        // 2.(используется) Оставить как заглушку, чтобы не могли выйти из игры кнопкой "назад".
        //  Так игра сохранится до удаления её из открытых приложений.
    }
}