package com.humansuit.foody.di.module

import android.app.Application
import androidx.room.Room
import com.humansuit.foody.database.RecipeDao
import com.humansuit.foody.database.RecipesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideRecipeDatabase(application: Application) : RecipesDatabase {
        return Room
            .databaseBuilder(application, RecipesDatabase::class.java, "recipes.db")
            .build()
    }


    @Provides
    @Singleton
    fun provideRecipeDao(recipesDatabase: RecipesDatabase) : RecipeDao {
        return recipesDatabase.recipeDao()
    }


}