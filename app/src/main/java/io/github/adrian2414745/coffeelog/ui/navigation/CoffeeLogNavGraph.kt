package io.github.adrian2414745.coffeelog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.adrian2414745.coffeelog.ui.addcoffee.AddCoffeeScreen
import io.github.adrian2414745.coffeelog.ui.dashboard.DashboardScreen
import io.github.adrian2414745.coffeelog.ui.editcoffee.EditCoffeeScreen
import io.github.adrian2414745.coffeelog.ui.history.HistoryScreen
import io.github.adrian2414745.coffeelog.ui.brewedit.BrewEditScreen
import io.github.adrian2414745.coffeelog.ui.settings.SettingsScreen

@Composable
fun CoffeeLogNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Dashboard) {
        composable<Dashboard> {
            DashboardScreen(
                onCoffeeClick = { coffeeId -> navController.navigate(History(coffeeId)) },
                onAddCoffee = { navController.navigate(AddCoffee) },
                onSettings = { navController.navigate(Settings) },
            )
        }
        composable<History> { backStackEntry ->
            val route = backStackEntry.toRoute<History>()
            HistoryScreen(
                coffeeId = route.coffeeId,
                onBack = { navController.popBackStack() },
                onAddBrew = { navController.navigate(BrewEdit(route.coffeeId)) },
                onEditBrew = { brewId -> navController.navigate(BrewEdit(route.coffeeId, brewId)) },
                onEditCoffee = { navController.navigate(EditCoffee(route.coffeeId)) },
            )
        }
        composable<BrewEdit> { backStackEntry ->
            val route = backStackEntry.toRoute<BrewEdit>()
            BrewEditScreen(
                coffeeId = route.coffeeId,
                brewId = route.brewId,
                onDone = { navController.popBackStack() },
            )
        }
        composable<AddCoffee> {
            AddCoffeeScreen(onDone = { navController.popBackStack() })
        }
        composable<EditCoffee> {
            EditCoffeeScreen(onDone = { navController.popBackStack() })
        }
        composable<Settings> {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
