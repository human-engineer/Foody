package com.humansuit.foody.di.module

import com.humansuit.foody.database.RecipeDao
import com.humansuit.foody.network.RecipeApi
import com.humansuit.foody.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped


@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {


    @Provides
    @ActivityRetainedScoped
    fun provideRecipesRepository(
        recipeApi: RecipeApi,
        recipeDao: RecipeDao
    ) : RecipeRepository {
        return RecipeRepository(recipeApi, recipeDao)
    }


}