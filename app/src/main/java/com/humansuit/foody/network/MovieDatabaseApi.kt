package com.humansuit.foody.network

import javax.inject.Inject

class MovieDatabaseApi @Inject constructor (private val movieDatabaseService: MovieDatabaseService) {

    suspend fun fetchMovies() {

    }

}