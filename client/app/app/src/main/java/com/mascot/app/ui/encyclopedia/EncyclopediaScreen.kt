@file:OptIn(ExperimentalLayoutApi::class)

package com.mascot.app.ui.encyclopedia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mascot.app.data.encyclopediadata.db.AppDatabase
import com.mascot.app.data.encyclopediadata.entity.MascotEntity
import com.mascot.app.data.encyclopediadata.entity.ZoneEntity

@Composable
fun EncyclopediaScreen() {

    val context = LocalContext.current
    val dao = AppDatabase.getInstance(context).mascotDao()

    // ✅ Room Flow 구독 (자동 변경 핵심)
    val zones by dao.observeZones()
        .collectAsState(initial = emptyList())

    val mascotList by dao.observeMascots()
        .collectAsState(initial = emptyList())

    // zoneId 기준으로 mascot 매핑
    val mascots = remember(mascotList) {
        mascotList.associateBy { it.zoneId }
    }

    var selectedMascot by remember { mutableStateOf<MascotEntity?>(null) }

    // 화면 가로폭 계산
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 16.dp * 2
    val spacing = 16.dp
    val availableWidth = screenWidth - horizontalPadding - (spacing * 2)
    val cardWidth = availableWidth / 3

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1620))
            .padding(16.dp)
    ) {

        item {
            Text(
                "지역 마스코트 도감",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        zones.groupBy { it.region }.forEach { (regionName, regionZones) ->

            item {
                RegionHeader(title = regionName)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                FlowRow(
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    regionZones.forEach { zone ->
                        mascots[zone.id]?.let { mascot ->
                            MascotCard(zone, mascot, cardWidth) {
                                selectedMascot = mascot
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    selectedMascot?.let { mascot ->
        MascotDetailDialog(mascot) { selectedMascot = null }
    }
}

@Composable
fun RegionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF222933))
            .padding(12.dp)
    ) {
        Text(text = title, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun MascotCard(
    zone: ZoneEntity,
    mascot: MascotEntity,
    cardWidth: Dp,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val imageId = remember {
        context.resources.getIdentifier(
            mascot.imageUrl,
            "drawable",
            context.packageName
        )
    }

    val silhouetteId = remember {
        context.resources.getIdentifier(
            "${mascot.imageUrl}_silhouette",
            "drawable",
            context.packageName
        )
    }

    val finalImageId =
        if (mascot.isCollected && imageId != 0) imageId
        else if (!mascot.isCollected && silhouetteId != 0) silhouetteId
        else 0

    Column(
        modifier = Modifier
            .width(cardWidth)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (finalImageId != 0) {
                Image(
                    painter = painterResource(id = finalImageId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.75f),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(mascot.name, color = Color.White, fontWeight = FontWeight.Bold)
        Text(mascot.region, color = Color.Gray)
    }
}

@Composable
fun MascotDetailDialog(
    mascot: MascotEntity,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            modifier = Modifier.padding(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(mascot.name, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Text("지역: ${mascot.region}")
                Spacer(Modifier.height(12.dp))
                Text(mascot.description)
                Spacer(Modifier.height(24.dp))
                Button(onClick = onDismiss) { Text("닫기") }
            }
        }
    }
}
