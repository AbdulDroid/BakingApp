package com.example.floating.bakingapp.model


/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

class Baker {
    var id: Long = 0
    lateinit var name: String
    lateinit var ingredients: List<Ingredients>
    lateinit var steps: List<Steps>
    lateinit var servings: String
    lateinit var image: String

    override fun toString(): String {
        return "Baker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", servings='" + servings + '\'' +
                ", image='" + image + '\'' +
                '}'
    }
}
