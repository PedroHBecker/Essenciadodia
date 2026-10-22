package com.seugrupo.essenciadodia.ui
import com.seugrupo.essenciadodia.ui.theme.corTopBar
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
import androidx.compose.ui.unit.dp
import com.seugrupo.essenciadodia.data.Ocasiao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcasiaoListScreen(
    viewModel: OcasiaoViewModel,
    onNova: () -> Unit,
    onEditar: (Int) -> Unit
) {
    val ocasioes by viewModel.ocasioes.collectAsState()
    var ocasiaoParaExcluir by remember { mutableStateOf<Ocasiao?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ocasiões") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNova) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar ocasião")
            }
        }
    ) { padding ->
        if (ocasioes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma ocasião cadastrada.\nToque no + para adicionar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ocasioes, key = { it.id }) { ocasiao ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onEditar(ocasiao.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(ocasiao.nome, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "Intensidade ideal: ${rotuloIntensidade(ocasiao.intensidadeIdeal)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(onClick = { ocasiaoParaExcluir = ocasiao }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }
                        }
                    }
                }
            }
        }
    }

    ocasiaoParaExcluir?.let { ocasiao ->
        AlertDialog(
            onDismissRequest = { ocasiaoParaExcluir = null },
            title = { Text("Excluir ocasião") },
            text = { Text("Deseja excluir \"${ocasiao.nome}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.excluir(ocasiao)
                    ocasiaoParaExcluir = null
                }) { Text("Excluir") }
            },
            dismissButton = {
                TextButton(onClick = { ocasiaoParaExcluir = null }) { Text("Cancelar") }
            }
        )
    }
}