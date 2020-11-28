package com.example.mysnake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.example.mysnake.Snake
import android.widget.FrameLayout


class MainActivity : AppCompatActivity() {

    val Feed by lazy { ImageView(this) }
    val head by lazy { View(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Feed.layoutParams = FrameLayout.LayoutParams(Snake.HeadSize,Snake.HeadSize)
        Feed.background = ContextCompat.getDrawable( this, R.drawable.ic_feed)
        container.addView(Feed)

        head.layoutParams = FrameLayout.LayoutParams(Snake.HeadSize,Snake.HeadSize)
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
            Snake.isPlay=false
            icChoosePlay.layoutParams = icPause.layoutParams
        }
        icPlay.setOnClickListener {
            Snake.isPlay=true
            icChoosePlay.layoutParams = icPlay.layoutParams
        }

        Snake.Start();
        GenerateFeed();
    }

    fun move(directions: Directions){
        when (directions) {
            Directions.Up -> (head.layoutParams as FrameLayout.LayoutParams).topMargin -=Snake.HeadSize
            Directions.Down ->(head.layoutParams as FrameLayout.LayoutParams).topMargin +=Snake.HeadSize
            Directions.Left -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin -=Snake.HeadSize
            Directions.Right -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin +=Snake.HeadSize
        }
        runOnUiThread {
            moveBody(head.top,head.left)
            EatSnake()
            container.removeView(head)
            container.addView(head)
        }
    }

    fun moveBody(headTop: Int, headLeft: Int){
       var prevBodyPart : PartOfBodySnake? = null
        for (i in 0 until Snake.bodySnake.size){
            val bodyPart = Snake.bodySnake[i]
            container.removeView(bodyPart.ImageBody)
            if(i == 0){
                prevBodyPart=bodyPart
                Snake.bodySnake[i] = PartOfBodySnake(headTop,headLeft,DrawPartOfBody(headTop,headLeft))
            }else{
                val currentBodyTale = Snake.bodySnake[i]
                prevBodyPart?.let {
                    Snake.bodySnake[i] = PartOfBodySnake(it.top,it.left,DrawPartOfBody(it.top,it.left))
                }
                prevBodyPart = currentBodyTale
            }
        }
    }

    private fun GenerateFeed(){
            (Feed.layoutParams as FrameLayout.LayoutParams).topMargin = (0..9).random() * Snake.HeadSize
            (Feed.layoutParams as FrameLayout.LayoutParams).leftMargin = (0..9).random() * Snake.HeadSize
            container.removeView(Feed)
            container.addView(Feed)
    }

    private fun EatSnake(){
        if(head.top==Feed.top && head.left==Feed.left)
        {
            GenerateFeed()
            Snake.AddPartBodySnake(head.top, head.left,DrawPartOfBody(head.top,head.left))
        }
    }

    private fun DrawPartOfBody(top:Int, left:Int):ImageView{
        val BodyImage = ImageView(this)
        BodyImage.background = ContextCompat.getDrawable( this, R.drawable.circle)
        BodyImage.layoutParams = FrameLayout.LayoutParams(Snake.HeadSize, Snake.HeadSize)
        (BodyImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (BodyImage.layoutParams as FrameLayout.LayoutParams).leftMargin=left

        container.addView(BodyImage)
        return BodyImage
    }
}

enum class Directions{
    Up,
    Right,
    Left,
    Down
}