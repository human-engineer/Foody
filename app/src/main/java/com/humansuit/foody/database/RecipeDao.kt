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

    @Query("select * from recipe where recipeType = 'popular' limit 10 offset :page")
    suspend fun getPopularRecipeList(page: Int = 0) : List<Recipe>


    @Query("select * from recipe where recipeType = :type limit 10 offset :page")
    suspend fun getRecipeListByType(page: Int = 0, type: String) : List<Recipe>
}