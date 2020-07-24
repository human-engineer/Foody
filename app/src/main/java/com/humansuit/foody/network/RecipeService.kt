package com.humansuit.foody.network

import com.humansuit.foody.model.PopularRecipes
import com.humansuit.foody.model.Recipe
import com.humansuit.foody.model.RecipesWrapper
import com.humansuit.foody.utils.Constants
import com.humansuit.foody.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {

    @GET("complexSearch?apiKey=$API_KEY")
    suspend fun getRecipesByType(
        @Query("type") type: String
    ) : Response<RecipesWrapper>


    @GET("random?apiKey=$API_KEY")
    suspend fun getPopularRecipes(
        @Query("number") number: Int
    ) : Response<PopularRecipes>

}