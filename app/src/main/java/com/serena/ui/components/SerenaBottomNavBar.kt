package com.serena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serena.R

enum class SerenaTab {
    INICIO, DIARIO, CHAT, PERFIL
}

@Composable
fun SerenaBottomNavBar(
    currentTab: SerenaTab,
    onTabSelected: (SerenaTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.white))
            .padding(vertical = 10.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(
            label = "Inicio",
            iconRes = R.drawable.icon_email,
            isSelected = currentTab == SerenaTab.INICIO,
            onClick = { onTabSelected(SerenaTab.INICIO) }
        )
        NavItem(
            label = "Diario",
            iconRes = R.drawable.icon_candado,
            isSelected = currentTab == SerenaTab.DIARIO,
            onClick = { onTabSelected(SerenaTab.DIARIO) }
        )
        NavItem(
            label = "Chat",
            iconRes = R.drawable.icon_email,
            isSelected = currentTab == SerenaTab.CHAT,
            onClick = { onTabSelected(SerenaTab.CHAT) }
        )
        NavItem(
            label = "Perfil",
            iconRes = R.drawable.icon_email,
            isSelected = currentTab == SerenaTab.PERFIL,
            onClick = { onTabSelected(SerenaTab.PERFIL) }
        )
    }
}

@Composable
private fun NavItem(
    label: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = colorResource(R.color.serena_green_dark)
    val inactiveColor = colorResource(R.color.serena_text_gray)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else inactiveColor
        )
    }
}