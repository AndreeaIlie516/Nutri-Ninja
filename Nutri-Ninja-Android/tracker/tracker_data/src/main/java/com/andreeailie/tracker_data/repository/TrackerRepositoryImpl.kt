package com.andreeailie.tracker_data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.andreeailie.tracker_data.local.TrackerDao
import com.andreeailie.tracker_data.mapper.toTrackableFood
import com.andreeailie.tracker_data.mapper.toTrackedFood
import com.andreeailie.tracker_data.mapper.toTrackedFoodEntity
import com.andreeailie.tracker_data.remote.CustomFoodApi
import com.andreeailie.tracker_data.remote.dto.NutrientRequest
import com.andreeailie.tracker_data.remote.dto.SearchFoodRequest
import com.andreeailie.tracker_domain.model.TrackableFood
import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val dao: TrackerDao,
    private val api: CustomFoodApi
): TrackerRepository {
    override suspend fun searchFood(
        query: String
    ): Result<List<TrackableFood>> {
        Log.d("TrackerRepository", "searchFood called")
        return try {
            Log.d("TrackerRepository", "Try to get searchDto")
            val request = SearchFoodRequest(query = query)
            Log.d("TrackerRepository", "request: $request")
            val searchDto = api.searchFood(
                request
            )
            Log.d("TrackerRepository", "searchDto: $searchDto")
            Log.d("TrackerRepository", "products: ${searchDto.products}")
            val result = searchDto.products.mapNotNull { it.toTrackableFood() }
            Log.d("TrackerRepository", "result: $result")
            Result.success(
                result
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Log.e("TrackerRepository", "Exception during searchFood", e)
            Result.failure(e)
        }
    }

    override suspend fun getNutrients(
        foodName: String,
        quantity: Int,
        unit: String
    ): Result<TrackableFood?> {
        return try {
            val searchedProduct = api.getNutrients(
                NutrientRequest(
                    foodName = foodName,
                    quantity = quantity,
                    unit = unit
                )
            )
            if( searchedProduct.toTrackableFood() != null) {

            }
            Result.success(
                searchedProduct.toTrackableFood()
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { it.toTrackedFood() }
        }
    }
}