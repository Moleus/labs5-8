package main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
fun NavBar(selectedTab: Main.Tab, onClick: (Main.Tab) -> Unit) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        backgroundColor = MaterialTheme.colors.primarySurface
    ) {
        NavItem(Icons.Outlined.Home, "Home", Main.Tab.TABLE, selectedTab == Main.Tab.TABLE, onClick)
        NavItem(Icons.Outlined.AccountCircle, "Account", Main.Tab.REGISTRATION, selectedTab == Main.Tab.REGISTRATION, onClick)
        NavItem(Icons.Outlined.LocationOn, "Map", Main.Tab.MAP, selectedTab == Main.Tab.MAP, onClick)
        NavItem(Icons.Outlined.Add, "New", Main.Tab.ADD_ENTITY, selectedTab == Main.Tab.ADD_ENTITY, onClick)
    }
}

@Composable
fun ColumnScope.NavItem(icon: ImageVector, label: String, tab: Main.Tab, isSelected: Boolean, onClick: (Main.Tab) -> Unit) {
    NavigationRailItem(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
        icon = {
            Icon(
                icon,
                label,
            )
        },
        label = { Text(text = label) },
        alwaysShowLabel = false,
        selectedContentColor = LocalContentColor.current,
        selected = isSelected,
        onClick = {
            onClick(tab)
        }
    )
}