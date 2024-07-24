package com.andreeailie.tracker_data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.andreeailie.tracker_data.local.GroceryDao
import com.andreeailie.tracker_data.local.TrackerDao
import com.andreeailie.tracker_data.mapper.toGrocery
import com.andreeailie.tracker_data.mapper.toGroceryEntity
import com.andreeailie.tracker_data.mapper.toTrackableFood
import com.andreeailie.tracker_data.mapper.toTrackedFood
import com.andreeailie.tracker_data.mapper.toTrackedFoodEntity
import com.andreeailie.tracker_data.remote.CustomFoodApi
import com.andreeailie.tracker_data.remote.dto.NutrientRequest
import com.andreeailie.tracker_data.remote.dto.SearchFoodRequest
import com.andreeailie.tracker_data.remote.dto.SearchGroceryRequest
import com.andreeailie.tracker_domain.model.Grocery
import com.andreeailie.tracker_domain.model.TrackableFood
import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val trackerDao: TrackerDao,
    private val groceryDao: GroceryDao,
    private val api: CustomFoodApi
) : TrackerRepository {
    override suspend fun searchFood(
        query: String
    ): Result<List<TrackableFood>> {
        return try {
            Log.d("TrackerRepositoryImpl", "searchFood")
            val request = SearchFoodRequest(query = query)
            Log.d("TrackerRepositoryImpl", "request: $request")
            val searchDto = api.searchFood(
                request
            )
            Log.d("TrackerRepositoryImpl", "searchDto: $searchDto")
            val result = searchDto.products.mapNotNull { it.toTrackableFood() }
            Log.d("TrackerRepositoryImpl", "result: $result")
            Result.success(
                result
            )
        } catch (e: Exception) {
            e.printStackTrace()
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
            Result.success(
                searchedProduct.toTrackableFood()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun insertTrackedFood(food: TrackedFood) {
        trackerDao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun deleteTrackedFood(food: TrackedFood) {
        trackerDao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return trackerDao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { it.toTrackedFood() }
        }
    }

    override suspend fun searchGrocery(
        query: String
    ): Result<List<Grocery>> {
        return try {
            val request = SearchGroceryRequest(query = query)
            val searchDto = api.searchGrocery(
                request
            )
            val result = searchDto.groceries.mapNotNull { it.toGrocery() }
            Result.success(
                result
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getGroceries(): Flow<List<Grocery>> {
        return groceryDao.getGroceries().map { entities ->
            entities.map { it.toGrocery() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun insertGrocery(grocery: Grocery) {
        groceryDao.insertGrocery(grocery.toGroceryEntity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun deleteGrocery(grocery: Grocery) {
        groceryDao.deleteGrocery(grocery.toGroceryEntity())
    }
}