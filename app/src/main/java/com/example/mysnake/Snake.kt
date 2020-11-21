package com.example.mysnake

object Snake{

    var SnakeMove:() -> Unit = {}
    var isPlay = true;

    fun Start(){
        Thread(Runnable{
            while (true){
                Thread.sleep(500)
                if(isPlay){
                    SnakeMove()
                }

            }
        }).start()
    }
}