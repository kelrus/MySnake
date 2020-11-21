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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val head = View(this)
        head.layoutParams = LinearLayout.LayoutParams(HeadSize,HeadSize)
        head.background = ContextCompat.getDrawable( this, R.drawable.circle)
        container.addView(head)
        Start();

        icArrowUp.setOnClickListener { Snake.SnakeMove = {move(Directions.Up, head)} }
        icArrowDown.setOnClickListener { Snake.SnakeMove = {move(Directions.Down, head)} }
        icArrowLeft.setOnClickListener { Snake.SnakeMove = {move(Directions.Left, head)} }
        icArrowRight.setOnClickListener { Snake.SnakeMove = {move(Directions.Right, head)} }
        icPause.setOnClickListener { isPlay=false }
        icPlay.setOnClickListener { isPlay=true }
    }

    fun move(directions: Directions, head:View){
        when (directions) {
            Directions.Up -> (head.layoutParams as FrameLayout.LayoutParams).topMargin -=HeadSize
            Directions.Down ->(head.layoutParams as FrameLayout.LayoutParams).topMargin +=HeadSize
            Directions.Left -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin -=HeadSize
            Directions.Right -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin +=HeadSize
        }
        runOnUiThread {
            GenerateFeed()
            container.removeView(head)
            container.addView(head)
        }
    }

    private fun GenerateFeed(){
        Thread(Runnable {
            val Feed = ImageView(this)
            Feed.layoutParams = FrameLayout.LayoutParams(HeadSize,HeadSize)
            Feed.setImageResource(R.drawable.ic_feed)
            (Feed.layoutParams as FrameLayout.LayoutParams).topMargin = (1..10).random() * HeadSize
            (Feed.layoutParams as FrameLayout.LayoutParams).leftMargin = (1..10).random() * HeadSize
            runOnUiThread{
                container.addView(Feed)
            }
        }).start()
    }
}

enum class Directions{
    Up,
    Right,
    Left,
    Down
}