package com.example.rectangletacticiankotlin

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainGameActivity : AppCompatActivity(), OnFragmentListener {

    var allData = MyAppData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)
        supportActionBar?.hide() ?: throw NoSuchElementException("ActionBar is lack")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        nextTurn()

        allData.playerCount = intent.getIntExtra(TAG_PLAYER_COUNT, PLAYER_COUNT_DEFAULT)
        allData.fieldWidth = intent.getIntExtra(TAG_FIELD_WIDTH, FIELD_WIDTH_DEFAULT)
        allData.fieldHeight = intent.getIntExtra(TAG_FIELD_HEIGHT, FIELD_HEIGHT_DEFAULT)

//        Log.d("my", "playerCount_MainGameActivity: $playerCount")
//        Log.d("my", "fieldWidth_MainGameActivity: $fieldWidth")
//        Log.d("my", "fieldHeight_MainGameActivity: $fieldHeight")
    }

    private fun nextTurn() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            PlayerStartFragment(this@MainGameActivity as OnFragmentListener, allData.playerNumber).apply {
                replace(R.id.frame_container_main, this)
            }
            commit()
        }
    }

    override fun onButtonSelected(buttonId: Int) {
        when(buttonId) {
            R.id.playerNotificationTV -> supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.frame_container_main, DiceFragment(this@MainGameActivity as OnFragmentListener, allData))
                commit()
            }
            R.id.generateSidesButton -> supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                MainGameFragment(this@MainGameActivity as OnFragmentListener, allData).apply {
                    replace(R.id.frame_container_main, this)
                }
                commit()
            }
            R.id.nextTurnButton -> {
                allData.nextPlayer()
                nextTurn()
            }
            R.id.mySurfaceView -> {
                FinishDialogFragment().apply {
                    Bundle().also {
                        allData.apply {
                            it.putInt("player1", totalPlayersArea()[1]!!)
                            it.putInt("player2", totalPlayersArea()[2]!!)
                            if (playerCount == 4) {
                                it.putInt("player3", totalPlayersArea()[3]!!)
                                it.putInt("player4", totalPlayersArea()[4]!!)
                            }
                        }
                        arguments = it
                    }
                    show(supportFragmentManager, "finishDialog")
                }
            }
        }
    }

    override fun onBackPressed() {
        //Есть 2 возможных варианта контекста внутри этого метода:
        // 1. Спросить пользователя, точно ли он хочет выйти и предупредить о несохранности всех данных игры.
        // 2.(используется) Оставить как заглушку, чтобы не могли выйти из игры кнопкой "назад".
        //  Так игра сохранится до удаления её из открытых приложений.
    }
}