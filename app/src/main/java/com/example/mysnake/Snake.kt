package com.example.mysnake

import android.widget.ImageView


object Snake{

    const val HeadSize = 100

    var SnakeMove:() -> Unit = {}
    var isPlay = true
    val bodySnake = mutableListOf<PartOfBodySnake>()

    fun AddPartBodySnake(top:Int, left:Int, PartBody:ImageView){
        bodySnake.add(PartOfBodySnake(top,left,PartBody))
    }


    fun Start(){
        Thread(Runnable{
            while (true){
                Thread.sleep(350)
                if(isPlay){
                    SnakeMove()
                }

            }
        }).start()
    }
}