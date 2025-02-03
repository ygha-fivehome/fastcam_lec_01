package com.ygha.mylibrary

class Cage {
    private val animals:MutableList<Animal> = mutableListOf()

    fun getFirst():Animal{
        return animals.first()
    }

    fun put(animal:Animal){
        this.animals.add(animal)
    }

    fun moveFrom(cage:Cage){
        this.animals.addAll(cage.animals)
    }
}

abstract class Animal(
    val anme:String)

abstract class Fish(name:String) : Animal(name)

class GoldFish(name: String):Fish(name)
class Carp(name: String):Fish(name)

fun main(){
    val cage = Cage()
    cage.put(Carp("Carp"))
    //val carp:Carp = cage.getFirst()
}