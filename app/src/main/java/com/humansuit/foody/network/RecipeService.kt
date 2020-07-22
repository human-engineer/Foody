package com.humansuit.foody.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {

    @GET("complexSearch?apiKey=723aa747c8b14ae0853f6f96e40567d6")
    suspend fun getRecipesByType(
        @Query("type") type: String
    ) : Response<ResponseBody>

}