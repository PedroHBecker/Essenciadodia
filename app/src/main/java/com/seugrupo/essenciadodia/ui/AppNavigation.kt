package com.seugrupo.essenciadodia.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.seugrupo.essenciadodia.ui.OcasiaoViewModel
import com.seugrupo.essenciadodia.ui.PerfumeViewModel
import com.seugrupo.essenciadodia.ui.SugestaoViewModel

private val CorPerfumes = Color(0xFFD32F2F)   // vermelho
private val CorOcasioes = Color(0xFF1976D2)   // azul
private val CorSugestao = Color(0xFFFFA000)   // dourado

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val perfumeViewModel: PerfumeViewModel = viewModel()
    val ocasiaoViewModel: OcasiaoViewModel = viewModel()
    val sugestaoViewModel: SugestaoViewModel = viewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    fun irPara(rota: String) {
        navController.navigate(rota) {
            popUpTo("perfumes") { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Item selecionado fica com a cor cheia; os outros ficam mais suaves
    fun corIcone(cor: Color, selecionado: Boolean): Color =
        if (selecionado) cor else cor.copy(alpha = 0.55f)

    val coresItem = NavigationBarItemDefaults.colors(indicatorColor = Color.White)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (rotaAtual == "perfumes" || rotaAtual == "ocasioes" || rotaAtual == "sugestao") {
                NavigationBar {
                    NavigationBarItem(
                        selected = rotaAtual == "perfumes",
                        onClick = { irPara("perfumes") },
                        icon = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = corIcone(CorPerfumes, rotaAtual == "perfumes")
                            )
                        },
                        label = { Text("Perfumes") },
                        colors = coresItem
                    )
                    NavigationBarItem(
                        selected = rotaAtual == "ocasioes",
                        onClick = { irPara("ocasioes") },
                        icon = {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                tint = corIcone(CorOcasioes, rotaAtual == "ocasioes")
                            )
                        },
                        label = { Text("Ocasiões") },
                        colors = coresItem
                    )
                    NavigationBarItem(
                        selected = rotaAtual == "sugestao",
                        onClick = { irPara("sugestao") },
                        icon = {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = corIcone(CorSugestao, rotaAtual == "sugestao")
                            )
                        },
                        label = { Text("Sugestão") },
                        colors = coresItem
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "perfumes",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("perfumes") {
                PerfumeListScreen(
                    viewModel = perfumeViewModel,
                    onNovo = { navController.navigate("perfume_form/0") },
                    onEditar = { id -> navController.navigate("perfume_form/$id") }
                )
            }
            composable(
                route = "perfume_form/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                PerfumeFormScreen(
                    viewModel = perfumeViewModel,
                    perfumeId = id,
                    onVoltar = { navController.popBackStack() }
                )
            }
            composable("ocasioes") {
                OcasiaoListScreen(
                    viewModel = ocasiaoViewModel,
                    onNova = { navController.navigate("ocasiao_form/0") },
                    onEditar = { id -> navController.navigate("ocasiao_form/$id") }
                )
            }
            composable(
                route = "ocasiao_form/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                OcasiaoFormScreen(
                    viewModel = ocasiaoViewModel,
                    ocasiaoId = id,
                    onVoltar = { navController.popBackStack() }
                )
            }
            composable("sugestao") {
                SugestaoScreen(viewModel = sugestaoViewModel)
            }
        }
    }
}