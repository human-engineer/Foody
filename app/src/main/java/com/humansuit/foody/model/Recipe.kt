package com.humansuit.foody.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey val id : Int,
    val title : String,
    val image : String,
    val imageType : String,
    var recipeType : String?
)