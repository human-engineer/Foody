package com.humansuit.foody.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.humansuit.foody.model.Recipe


@Database(entities = [Recipe::class], version = 1, exportSchema = true)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

}