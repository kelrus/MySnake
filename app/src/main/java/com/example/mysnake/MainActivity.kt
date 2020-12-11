package com.example.mysnake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.example.mysnake.Snake
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog

const val Cells_Field = 10

class MainActivity : AppCompatActivity() {

    val Feed by lazy { ImageView(this) }
    val head by lazy { View(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container.layoutParams = LinearLayout.LayoutParams(Snake.HeadSize* Cells_Field,Snake.HeadSize* Cells_Field)

        Feed.layoutParams = FrameLayout.LayoutParams(Snake.HeadSize,Snake.HeadSize)
        Feed.background = ContextCompat.getDrawable( this, R.drawable.ic_feed)
        container.addView(Feed)

        head.layoutParams = FrameLayout.LayoutParams(Snake.HeadSize,Snake.HeadSize)
        head.background = ContextCompat.getDrawable( this, R.drawable.circle)
        container.addView(head)

        icArrowUp.setOnClickListener {
            Snake.SnakeMove = {move(PlayerInput.Up) }
            icChooseMove.layoutParams = icArrowUp.layoutParams
        }
        icArrowDown.setOnClickListener {
            Snake.SnakeMove = {move(PlayerInput.Down)}
            icChooseMove.layoutParams = icArrowDown.layoutParams
        }
        icArrowLeft.setOnClickListener {
            Snake.SnakeMove = {move(PlayerInput.Left)}
            icChooseMove.layoutParams = icArrowLeft.layoutParams
        }
        icArrowRight.setOnClickListener {
            Snake.SnakeMove = {move(PlayerInput.Right)}
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
        icPause.callOnClick()
        GenerateFeed();
    }

    fun move(playerInput: PlayerInput){
        when (playerInput) {
            PlayerInput.Up -> (head.layoutParams as FrameLayout.LayoutParams).topMargin -=Snake.HeadSize
            PlayerInput.Down ->(head.layoutParams as FrameLayout.LayoutParams).topMargin +=Snake.HeadSize
            PlayerInput.Left -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin -=Snake.HeadSize
            PlayerInput.Right -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin +=Snake.HeadSize
        }
        runOnUiThread {
            if(Snake.IsSnakeDead(head)){
                icPause.callOnClick()
                showEnd()
                return@runOnUiThread
            }
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

    private fun showEnd(){
        AlertDialog.Builder(this)
                .setTitle("You lose")
                .setPositiveButton("Restart" ,{_,_ ->this.recreate()}).setCancelable(false)
                                                                      .create()
                                                                      .show()
    }

    private fun GenerateFeed(){
            (Feed.layoutParams as FrameLayout.LayoutParams).topMargin = (0 until Cells_Field).random() * Snake.HeadSize
            (Feed.layoutParams as FrameLayout.LayoutParams).leftMargin = (0 until Cells_Field).random() * Snake.HeadSize
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