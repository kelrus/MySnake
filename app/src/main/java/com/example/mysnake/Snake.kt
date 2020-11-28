package com.example.mysnake

import android.widget.ImageView
import android.view.View


object Snake{

    const val HeadSize = 100

    var SnakeMove:() -> Unit = {}
    var isPlay = true
    val bodySnake = mutableListOf<PartOfBodySnake>()
    private val thread: Thread

    fun AddPartBodySnake(top:Int, left:Int, PartBody:ImageView){
        bodySnake.add(PartOfBodySnake(top,left,PartBody))
    }

    fun IsSnakeDead(head:View): Boolean{
        for (body in bodySnake){
            if(body.left == head.left && body.top == head.top){
                return true
            }
        }
        if(head.top<0 || head.left<0 || head.top>= HeadSize*10 || head.left>= HeadSize*10){
            return true
        }
        return false
    }

    init {
        thread = Thread(Runnable{
            while (true){
                Thread.sleep(350)
                if(isPlay){
                    SnakeMove()
                }

            }
        })
        thread.start()
    }

    fun Start(){
        bodySnake.clear();
    }
}