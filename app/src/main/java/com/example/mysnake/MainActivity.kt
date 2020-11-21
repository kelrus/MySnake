package com.example.mysnake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mysnake.Snake.Start
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.example.mysnake.Snake.isPlay
import android.widget.FrameLayout

const val HeadSize = 100

class MainActivity : AppCompatActivity() {

    val Feed by lazy { ImageView(this) }
    val head by lazy { View(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Feed.layoutParams = FrameLayout.LayoutParams(HeadSize,HeadSize)
        Feed.background = ContextCompat.getDrawable( this, R.drawable.ic_feed)
        container.addView(Feed)

        head.layoutParams = FrameLayout.LayoutParams(HeadSize,HeadSize)
        head.background = ContextCompat.getDrawable( this, R.drawable.circle)
        container.addView(head)

        icArrowUp.setOnClickListener {
            Snake.SnakeMove = {move(Directions.Up) }
            icChooseMove.layoutParams = icArrowUp.layoutParams
        }
        icArrowDown.setOnClickListener {
            Snake.SnakeMove = {move(Directions.Down)}
            icChooseMove.layoutParams = icArrowDown.layoutParams
        }
        icArrowLeft.setOnClickListener {
            Snake.SnakeMove = {move(Directions.Left)}
            icChooseMove.layoutParams = icArrowLeft.layoutParams
        }
        icArrowRight.setOnClickListener {
            Snake.SnakeMove = {move(Directions.Right)}
            icChooseMove.layoutParams = icArrowRight.layoutParams
        }
        icPause.setOnClickListener {
            isPlay=false
            icChoosePlay.layoutParams = icPause.layoutParams
        }
        icPlay.setOnClickListener {
            isPlay=true
            icChoosePlay.layoutParams = icPlay.layoutParams
        }

        Start();
    }

    fun move(directions: Directions){
        when (directions) {
            Directions.Up -> (head.layoutParams as FrameLayout.LayoutParams).topMargin -=HeadSize
            Directions.Down ->(head.layoutParams as FrameLayout.LayoutParams).topMargin +=HeadSize
            Directions.Left -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin -=HeadSize
            Directions.Right -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin +=HeadSize
        }
        runOnUiThread {
            EatSnake()
            container.removeView(head)
            container.addView(head)
        }
    }

    private fun GenerateFeed(){
        Thread(Runnable {
            (Feed.layoutParams as FrameLayout.LayoutParams).topMargin = (1..9).random() * HeadSize
            (Feed.layoutParams as FrameLayout.LayoutParams).leftMargin = (1..9).random() * HeadSize
            runOnUiThread{
                container.addView(Feed)
            }

        }).start()
    }

    private fun EatSnake(){
        if(head.top==Feed.top && head.left==Feed.left)
        {
            container.removeView(Feed)
            GenerateFeed()
        }
    }
}

enum class Directions{
    Up,
    Right,
    Left,
    Down
}