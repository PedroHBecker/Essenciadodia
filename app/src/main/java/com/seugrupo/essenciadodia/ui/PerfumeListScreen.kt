package com.seugrupo.essenciadodia.ui
import com.seugrupo.essenciadodia.ui.PerfumeViewModel
import com.seugrupo.essenciadodia.ui.theme.corTopBar

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.seugrupo.essenciadodia.data.Perfume

fun rotuloIntensidade(valor: Int): String = when (valor) {
    1 -> "Leve"
    2 -> "Moderada"
    else -> "Forte"
}

// Monta o texto do inventário e abre o menu de compartilhamento do Android
fun compartilharInventario(context: Context, perfumes: List<Perfume>) {
    val texto = buildString {
        appendLine("🧴 Minha coleção de perfumes (${perfumes.size})")
        appendLine()
        perfumes.forEachIndexed { i, p ->
            appendLine("${i + 1}. ${p.nome} - ${p.marca}")
            appendLine("   ${p.familiaOlfativa} • ${rotuloIntensidade(p.intensidade)} • ${p.tempMin}°C a ${p.tempMax}°C")
        }
        appendLine()
        append("Enviado pelo app Essência do Dia")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Minha coleção de perfumes")
        putExtra(Intent.EXTRA_TEXT, texto)
    }
    context.startActivity(Intent.createChooser(intent, "Compartilhar coleção"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfumeListScreen(
    viewModel: PerfumeViewModel,
    onNovo: () -> Unit,
    onEditar: (Int) -> Unit
) {
    val perfumes by viewModel.perfumes.collectAsState()
    var perfumeParaExcluir by remember { mutableStateOf<Perfume?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus perfumes") },
                actions = {
                    if (perfumes.isNotEmpty()) {
                        TextButton(onClick = { compartilharInventario(context, perfumes) }) {
                            Text("Compartilhar")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovo) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar perfume")
            }
        }
    ) { padding ->
        if (perfumes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum perfume cadastrado.\nToque no + para adicionar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(perfumes, key = { it.id }) { perfume ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onEditar(perfume.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(perfume.nome, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "${perfume.marca} • ${perfume.familiaOlfativa}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "${rotuloIntensidade(perfume.intensidade)} • ${perfume.tempMin}°C a ${perfume.tempMax}°C",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(onClick = { perfumeParaExcluir = perfume }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }
                        }
                    }
                }
            }
        }
    }

    perfumeParaExcluir?.let { perfume ->
        AlertDialog(
            onDismissRequest = { perfumeParaExcluir = null },
            title = { Text("Excluir perfume") },
            text = { Text("Deseja excluir \"${perfume.nome}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.excluir(perfume)
                    perfumeParaExcluir = null
                }) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { perfumeParaExcluir = null }) { Text("Cancelar") }
            }
        )
    }
}