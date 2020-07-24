package com.humansuit.foody.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.humansuit.foody.model.Recipe

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeList(recipeList: List<Recipe>)

    @Query("select * from recipe limit 10 offset :page")
    suspend fun getRecipeList(page: Int = 0) : List<Recipe>

}