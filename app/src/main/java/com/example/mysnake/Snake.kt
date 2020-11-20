package com.example.mysnake

object Snake{

    var SnakeMove:() -> Unit = {}

    fun Start(){
        Thread(Runnable{
            while (true){
                Thread.sleep(500)
                SnakeMove()
            }
        }).start()
    }
}