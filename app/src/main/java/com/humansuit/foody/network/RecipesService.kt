package com.humansuit.foody.network

import com.humansuit.foody.model.RecipesWrapper
import com.humansuit.foody.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesService {

    @GET("complexSearch?apiKey=${Constants.API_KEY}")
    suspend fun getRecipesByType(
        @Query("type") type: String
    ) : Response<RecipesWrapper>

}