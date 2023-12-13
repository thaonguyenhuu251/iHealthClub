package com.htnguyen.ihealthclub

class Test {
    val list = mutableListOf<TestTT>()
    val listPro = mutableListOf<TestTT>()

    fun testt(){
        list.add(TestTT(1,"A"))
        list.add(TestTT(1,"B"))
        list.add(TestTT(1,"C"))
        list.add(TestTT(2,"A"))
        list.add(TestTT(2,"B"))
        list.add(TestTT(2,"C"))
        list.add(TestTT(2,"D"))
        list.add(TestTT(3,"A"))
        list.add(TestTT(3,"B"))
        list.add(TestTT(4,"B"))
        list.add(TestTT(4,"D"))
    }

    fun check(a: Int, b:Int):Int{
        if(a==b)
            return 0
        else
            return 1
    }
    fun addtest(){
        for(i in list){
            for(j in listPro){
                if(listPro != null){
                    if(check(i.id,j.id)==1){
                        listPro.add(TestTT(i.id,i.name))
                    }

                }
                else
                    listPro.add(TestTT(i.id,i.name))
            }

        }

        for(j in listPro){
            print("${j.id}  ${j.name}")
        }
    }

    fun main(){
        testt()
        addtest()
    }

}


data class TestTT(
    val id: Int,
    val name: String
)