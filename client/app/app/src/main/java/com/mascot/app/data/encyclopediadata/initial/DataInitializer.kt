package com.mascot.app.data.encyclopediadata.initial

import android.content.Context
import com.google.gson.Gson
import com.mascot.app.data.encyclopediadata.db.AppDatabase
import com.mascot.app.data.encyclopediadata.entity.MascotEntity
import com.mascot.app.data.encyclopediadata.entity.ZoneEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DataInitializer {

    fun initialize(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {

            val db = AppDatabase.getInstance(context)
            val dao = db.mascotDao()

            // JSON 읽기
            val jsonString = context.assets.open("mascot_data.json")
                .bufferedReader().use { it.readText() }

            val data = Gson().fromJson(jsonString, MascotData::class.java)

            // zone + mascot 저장
            data.zones.forEach { zone ->

                dao.insertZone(
                    ZoneEntity(
                        id = zone.id,
                        name = zone.name,
                        region = zone.region
                    )
                )

                val m = zone.mascot
                dao.insertMascot(
                    MascotEntity(
                        id = m.id,
                        zoneId = zone.id,
                        name = m.name,
                        imageUrl = m.imageUrl,
                        region = m.region,
                        description = m.description,
                        isCollected = m.isCollected
                    )
                )
            }
        }
    }
}
