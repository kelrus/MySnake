package com.example.mysnake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mysnake.Snake.Start
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.mysnake.Snake.SnakeMove
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val head = View(this)
        head.layoutParams = LinearLayout.LayoutParams(100,100)
        head.background = ContextCompat.getDrawable( this, R.drawable.circle)
        container.addView(head)
        Start();

        icArrowUp.setOnClickListener { Snake.SnakeMove = {move(Directions.Up, head)} }
        icArrowDown.setOnClickListener { Snake.SnakeMove = {move(Directions.Down, head)} }
        icArrowLeft.setOnClickListener { Snake.SnakeMove = {move(Directions.Left, head)} }
        icArrowRight.setOnClickListener { Snake.SnakeMove = {move(Directions.Right, head)} }
    }

    fun move(directions: Directions, head:View){
        when (directions) {
            Directions.Up -> (head.layoutParams as LinearLayout.LayoutParams).topMargin -=100
            Directions.Down ->(head.layoutParams as LinearLayout.LayoutParams).topMargin +=100
            Directions.Left -> (head.layoutParams as LinearLayout.LayoutParams).leftMargin -=100
            Directions.Right -> (head.layoutParams as LinearLayout.LayoutParams).leftMargin +=100
        }
        runOnUiThread {
            container.removeView(head)
            container.addView(head)
        }
    }
}

enum class Directions{
    Up,
    Right,
    Left,
    Down
}