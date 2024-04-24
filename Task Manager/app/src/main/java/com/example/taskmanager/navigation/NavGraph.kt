package com.example.taskmanager.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskmanager.ui.home.HomeDestination
import com.example.taskmanager.ui.home.HomeScreen
import com.example.taskmanager.ui.routine.TaskEntryDestination
import com.example.taskmanager.ui.routine.TaskEntryScreen
import com.example.taskmanager.ui.view.TaskScreen
import com.example.taskmanager.ui.view.TaskScreenDestination

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
    ) {
        composable(
            route = HomeDestination.route
        ) {
            HomeScreen(
                navigateToRoutineEntry = {navController.navigate("task entry")},
                navigateToTaskScreen = {navController.navigate("${TaskScreenDestination.route}/${it}")}
            )
        }
        composable(
            route = TaskEntryDestination.route
        ) {
            TaskEntryScreen(
                navigateBack = { navController.popBackStack() },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable (
            route = TaskScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskScreenDestination.taskIdArg) {
                type = NavType.LongType
            })
        ) {
            TaskScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )

}