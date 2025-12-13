package com.mascot.app.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mascot.app.data.model.QuestItem

@Composable
fun QuestItemCard(
    quest: QuestItem,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(quest.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = quest.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = quest.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
