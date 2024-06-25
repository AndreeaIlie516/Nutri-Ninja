package com.andreeailie.tracker_domain.di

import com.andreeailie.core.domain.preferences.Preferences
import com.andreeailie.tracker_domain.repository.TrackerRepository
import com.andreeailie.tracker_domain.use_case.AddGrocery
import com.andreeailie.tracker_domain.use_case.CalculateMealNutrients
import com.andreeailie.tracker_domain.use_case.DeleteGrocery
import com.andreeailie.tracker_domain.use_case.DeleteTrackedFood
import com.andreeailie.tracker_domain.use_case.GetFoodsForDate
import com.andreeailie.tracker_domain.use_case.GetGroceries
import com.andreeailie.tracker_domain.use_case.GroceryUseCases
import com.andreeailie.tracker_domain.use_case.SearchFood
import com.andreeailie.tracker_domain.use_case.SearchGrocery
import com.andreeailie.tracker_domain.use_case.ToggleGroceryStatus
import com.andreeailie.tracker_domain.use_case.TrackFood
import com.andreeailie.tracker_domain.use_case.TrackerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @ViewModelScoped
    @Provides
    fun provideTrackerUseCases(
        repository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsForDate = GetFoodsForDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGroceriesUseCases(
        repository: TrackerRepository,
    ): GroceryUseCases {
        return GroceryUseCases(
            addGrocery = AddGrocery(repository),
            deleteGrocery = DeleteGrocery(repository),
            searchGrocery = SearchGrocery(repository),
            getGroceries = GetGroceries(repository),
            toggleGroceryStatus = ToggleGroceryStatus(repository),
        )
    }
}