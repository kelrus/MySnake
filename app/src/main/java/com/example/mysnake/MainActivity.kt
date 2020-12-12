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

    val feed by lazy { ImageView(this) }
    val head by lazy { ImageView(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container.layoutParams = LinearLayout.LayoutParams(Snake.HeadSize* Cells_Field,Snake.HeadSize* Cells_Field)

        feed.layoutParams = FrameLayout.LayoutParams(Snake.HeadSize,Snake.HeadSize)
        feed.setImageResource(R.drawable.feed)
        container.addView(feed)

        head.layoutParams = FrameLayout.LayoutParams(Snake.HeadSize,Snake.HeadSize)
        head.setImageResource(R.drawable.snake_head)
        container.addView(head)

        Snake.SnakeMove = {isCurrentInput(PlayerInput.Down,PlayerInput.Up)}
        icArrowUp.setOnClickListener {
            Snake.SnakeMove = {isCurrentInput(PlayerInput.Up,PlayerInput.Down) }
        }
        icArrowDown.setOnClickListener {
            Snake.SnakeMove = {isCurrentInput(PlayerInput.Down,PlayerInput.Up)}
        }
        icArrowLeft.setOnClickListener {
            Snake.SnakeMove = {isCurrentInput(PlayerInput.Left,PlayerInput.Right)}
        }
        icArrowRight.setOnClickListener {
            Snake.SnakeMove = {isCurrentInput(PlayerInput.Right,PlayerInput.Left)}
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

    fun isCurrentInput(choose:PlayerInput, opposite: PlayerInput){
        if (currentInput == opposite){
            MoveHead(currentInput)
        }
        else{
            currentInput=choose
            MoveHead(choose)
        }

    }

    fun MoveHead(playerInput: PlayerInput){
        when (playerInput) {
            PlayerInput.Up -> RotateHead(PlayerInput.Up, 180f)
            PlayerInput.Down ->RotateHead(PlayerInput.Down, 0f)
            PlayerInput.Left ->RotateHead(PlayerInput.Left, 90f)
            PlayerInput.Right ->RotateHead(PlayerInput.Right, 270f)
        }
        runOnUiThread {
            if(Snake.IsSnakeDead(head)){
                icPause.callOnClick()
                ShowEnd()
                return@runOnUiThread
            }
            MoveBody(head.top,head.left)
            EatSnake()
            container.removeView(head)
            container.addView(head)
        }
    }

    fun RotateHead(input: PlayerInput, rotate: Float){
        head.rotation = rotate
        when(input){
            PlayerInput.Up -> (head.layoutParams as FrameLayout.LayoutParams).topMargin -=Snake.HeadSize
            PlayerInput.Down ->(head.layoutParams as FrameLayout.LayoutParams).topMargin +=Snake.HeadSize
            PlayerInput.Left -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin -=Snake.HeadSize
            PlayerInput.Right -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin +=Snake.HeadSize
        }
    }

    fun MoveBody(headTop: Int, headLeft: Int){
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

    private fun ShowEnd(){
        AlertDialog.Builder(this)
                .setTitle("You lose")
                .setPositiveButton("Restart" ,{_,_ ->this.recreate()}).setCancelable(false)
                                                                      .create()
                                                                      .show()
    }

    private fun GenerateFeed(){
            var isFeedCoordinateEqSnake = true
            var feedTop = (0 until Cells_Field).random() * Snake.HeadSize
            var feedLeft = (0 until Cells_Field).random() * Snake.HeadSize
            while(isFeedCoordinateEqSnake){
                isFeedCoordinateEqSnake = false
                for (body in Snake.bodySnake){
                    if(body.top == feedTop && body.left == feedLeft){
                        isFeedCoordinateEqSnake = true
                        feedTop = (0 until Cells_Field).random() * Snake.HeadSize
                        feedLeft = (0 until Cells_Field).random() * Snake.HeadSize
                    }
                }
            }
            (feed.layoutParams as FrameLayout.LayoutParams).topMargin = feedTop
            (feed.layoutParams as FrameLayout.LayoutParams).leftMargin = feedLeft
            container.removeView(feed)
            container.addView(feed)
    }

    private fun EatSnake(){
        if(head.top==feed.top && head.left==feed.left)
        {
            GenerateFeed()
            Snake.AddPartBodySnake(head.top, head.left,DrawPartOfBody(head.top,head.left))
        }
    }

    private fun DrawPartOfBody(top:Int, left:Int):ImageView{
        val BodyImage = ImageView(this)
        BodyImage.setImageResource(R.drawable.snake_scale)
        BodyImage.layoutParams = FrameLayout.LayoutParams(Snake.HeadSize, Snake.HeadSize)
        (BodyImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (BodyImage.layoutParams as FrameLayout.LayoutParams).leftMargin=left

        container.addView(BodyImage)
        return BodyImage
    }
}