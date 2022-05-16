package main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import common.main.Main

@Composable
fun BottomNavBar(selectedTab: Main.Tab, onClick: (Main.Tab) -> Unit) {
    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottomNavItem(Icons.Outlined.Home, "Home", Main.Tab.TABLE, selectedTab == Main.Tab.TABLE, onClick)
        BottomNavItem(Icons.Outlined.AccountCircle, "Account", Main.Tab.REGISTRATION, selectedTab == Main.Tab.REGISTRATION, onClick)
        BottomNavItem(Icons.Outlined.LocationOn, "Map", Main.Tab.MAP, selectedTab == Main.Tab.MAP, onClick)
        BottomNavItem(Icons.Outlined.Add, "New", Main.Tab.ADD_ENTITY, selectedTab == Main.Tab.ADD_ENTITY, onClick)
    }
}

@Composable
fun RowScope.BottomNavItem(icon: ImageVector, label: String, tab: Main.Tab, isSelected: Boolean, onClick: (Main.Tab) -> Unit) {
    BottomNavigationItem(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
        icon = {
            Icon(
                icon,
                label,
            )
        },
        label = { Text(text = label) },
        alwaysShowLabel = false,
//        selectedContentColor = pri,
//        unselectedContentColor = oceanBlue.copy(0.4f),
        selected = isSelected,
        onClick = {
            onClick(tab)
        }
    )
}