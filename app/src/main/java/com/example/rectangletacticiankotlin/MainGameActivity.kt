package com.example.rectangletacticiankotlin

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainGameActivity : AppCompatActivity(), OnFragmentListener {

//    private lateinit var exceptionTV: TextView
//    private var isException = false
//    //
    var playerCount = 0
    var fieldWidth = 0
    var fieldHeight = 0
    //companion object {
        var playerNumber = 1
    //}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)
        supportActionBar?.hide() ?: throw NoSuchElementException("ActionBar is lack")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        supportFragmentManager.beginTransaction().apply {
            val startFragment = PlayerStartFragment()
            val startFragmentBundle = Bundle()
            startFragmentBundle.putInt("playerNumber", playerNumber)
            startFragment.arguments = startFragmentBundle
            replace(R.id.frame_container_main, startFragment)
            commit()
        }

        playerCount = intent.getIntExtra("playerCount", 2)
        fieldWidth = intent.getIntExtra("fieldWidth", 25)
        fieldHeight = intent.getIntExtra("fieldHeight", 35)

        Log.d("my", "playerCountMainGameAct: $playerCount")
        Log.d("my", "fieldWidthMainGameAct: $fieldWidth")
        Log.d("my", "fieldHeightMainGameAct: $fieldHeight")

        val mainGameFragment = MainGameFragment()
        val mainGameFragmentBundle = Bundle()
        mainGameFragmentBundle.putInt("playerCount", playerCount)
        mainGameFragmentBundle.putInt("fieldWidth", fieldWidth)
        mainGameFragmentBundle.putInt("fieldHeight", fieldHeight)
        mainGameFragmentBundle.putInt("playerNumber", playerNumber)
        mainGameFragment.arguments = mainGameFragmentBundle

//        val a = intent.getStringExtra("width")
//        val b = intent.getStringExtra("height")
//        Log.d("my2", "$a")
//        Log.d("my2", "$b")

//        exceptionTV = findViewById(R.id.exceptionTV)

        //exceptionTVNoRectangle()
    }

//    fun exceptionTVNoException() {
//        isException = true
//        exceptionTV.setBackgroundColor(Color.GREEN)
//        exceptionTV.text = getString(R.string.exceptionTV_good_text)
//    }
//
//    fun exceptionTVNoRectangle() {
//        isException = false
//        exceptionTV.setBackgroundColor(Color.BLUE)
//        exceptionTV.text = getString(R.string.exceptionTV_bad_NoRectangle_text)
//    }
//
//    fun exceptionTVOutOfBounds() {
//        isException = false
//        exceptionTV.setBackgroundColor(Color.RED)
//        exceptionTV.text = getString(R.string.exceptionTV_bad_OutOfBoundsException_text)
//    }
//
//    fun exceptionTVLocation() {
//        isException = false
//        exceptionTV.setBackgroundColor(Color.RED)
//        exceptionTV.text = getString(R.string.exceptionTV_bad_LocationException_text)
//    }

//    fun onClick(view: View) {
//        when(view.id) {
//            R.id.rotationButton -> {
//                fieldWidth += fieldHeight
//                fieldHeight = fieldWidth - fieldHeight
//                fieldWidth -= fieldHeight
//                //check players' rectangle
//            }
//            R.id.endPlayerTurnButton -> {
//                //remember players' coordinates & next turn
//            }
//        }
//    }

//    override fun onButtonSelected(buttonId: Int) {
//        when(buttonId) {
//            R.id.playerNotificationTV -> {}
//            R.id.generateSidesButton -> {}
//        }
//    }

    override fun onParamsSelected(params: Map<String, String>) {
        val mainGameFragment = MainGameFragment()
        mainGameFragment.apply {
            rectWidth = params["rectWidth"]?.toInt() ?: 0
            rectHeight = params["rectHeight"]?.toInt() ?: 0
            Log.d("my", "rectWidthMainGameAct: $rectWidth")
            Log.d("my", "rectHeightMainGameAct: $rectHeight")
        }
    }

    override fun onBackPressed() {
        //2 варианта контекста внутри этого метода:
        // 1. Либо спросить пользователя, точно ли он хочет выйти и предупредить о несохранности всех данных игры
        // 2. Либо оставить просто как заглушку, чтобы не могли выйти из игры кнопкой "назад".
        //  Так игра сохранится, елси не убрать её из открытых приложений, иначе не сохранится.
    }
}