package com.example.foodplanner.data.sync

import com.example.foodplanner.data.local.MealDao
import com.example.foodplanner.data.local.MealEntity
import com.example.foodplanner.data.local.PlanDao
import com.example.foodplanner.data.local.PlannedMealEntity
import com.example.foodplanner.utils.UserProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SyncManager(
    private val mealDao: MealDao,
    private val planDao: PlanDao
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val uid get() = UserProvider.getCurrentUserId()


    fun backupFavorite(meal: MealEntity) {
        Log.d("SYNC", "backupFavorite called → uid=$uid, meal=${meal.idMeal}")
        if (uid == "guest") return
        firestore.collection("users").document(uid)
            .collection("favorites").document(meal.idMeal)
            .set(meal)
            .addOnSuccessListener { Log.d("SYNC", "backupFavorite SUCCESS ✔") }
            .addOnFailureListener { Log.e("SYNC", "backupFavorite FAILED", it) }
    }

    fun backupPlanMeal(meal: PlannedMealEntity) {
        Log.d("SYNC", "backupPlanMeal called → uid=$uid, date=${meal.date}")
        if (uid == "guest") return
        firestore.collection("users").document(uid)
            .collection("plan").document("${meal.date}_${meal.mealId}")
            .set(meal.copy(planId = 0))
            .addOnSuccessListener { Log.d("SYNC", "backupPlanMeal SUCCESS ✔") }
            .addOnFailureListener { Log.e("SYNC", "backupPlanMeal FAILED", it) }
    }

    fun removeFavoriteBackup(mealId: String) {
        if (uid == "guest") return
        firestore.collection("users").document(uid)
            .collection("favorites").document(mealId).delete()
    }



    fun removePlanMealBackup(date: String, mealId: String) {
        if (uid == "guest") return
        firestore.collection("users").document(uid)
            .collection("plan").document("${date}_$mealId").delete()
    }




    suspend fun restore(): Int = withContext(Dispatchers.IO) {
        val uid = UserProvider.getCurrentUserId()
        Log.d("SYNC", "restore start → uid=$uid")

        if (uid == "guest") {
            Log.w("SYNC", "restore SKIPPED")
            return@withContext 0
        }

        var count = 0

        val favSnap = firestore.collection("users").document(uid)
            .collection("favorites").get().await()
        Log.d("SYNC", "favorites from cloud = ${favSnap.size()}")

        for (doc in favSnap) {
            val meal = doc.toObject(MealEntity::class.java)
            Log.d("SYNC", "restoring favorite → ${meal.idMeal}")
            mealDao.insertMeal(meal.copy(userId = uid))
            count++
        }

        val planSnap = firestore.collection("users").document(uid)
            .collection("plan").get().await()
        Log.d("SYNC", "plan from cloud = ${planSnap.size()}")

        for (doc in planSnap) {
            val item = doc.toObject(PlannedMealEntity::class.java)
            planDao.addToPlan(item.copy(userId = uid, planId = 0))
            count++
        }

        Log.d("SYNC", "restore finished → count=$count")
        count
    }
}