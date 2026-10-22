package com.seugrupo.essenciadodia.ui
import com.seugrupo.essenciadodia.ui.theme.corTopBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seugrupo.essenciadodia.data.Ocasiao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcasiaoFormScreen(
    viewModel: OcasiaoViewModel,
    ocasiaoId: Int,
    onVoltar: () -> Unit
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var intensidade by rememberSaveable { mutableIntStateOf(2) }
    var tentouSalvar by rememberSaveable { mutableStateOf(false) }
    var carregado by rememberSaveable { mutableStateOf(ocasiaoId == 0) }

    // Edição: carrega os dados da ocasião uma única vez
    LaunchedEffect(ocasiaoId) {
        if (!carregado) {
            viewModel.buscar(ocasiaoId)?.let { o ->
                nome = o.nome
                intensidade = o.intensidadeIdeal
            }
            carregado = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (ocasiaoId == 0) "Nova ocasião" else "Editar ocasião") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da ocasião (ex.: Trabalho, Balada)") },
                isError = tentouSalvar && nome.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Intensidade ideal do perfume", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..3).forEach { nivel ->
                    FilterChip(
                        selected = intensidade == nivel,
                        onClick = { intensidade = nivel },
                        label = { Text(rotuloIntensidade(nivel)) }
                    )
                }
            }

            Button(
                onClick = {
                    tentouSalvar = true
                    if (nome.isNotBlank()) {
                        viewModel.salvar(
                            Ocasiao(
                                id = ocasiaoId,
                                nome = nome.trim(),
                                intensidadeIdeal = intensidade
                            )
                        )
                        onVoltar()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}