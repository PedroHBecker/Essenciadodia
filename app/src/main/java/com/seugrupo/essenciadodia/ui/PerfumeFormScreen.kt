
package com.seugrupo.essenciadodia.ui
import com.seugrupo.essenciadodia.ui.theme.corTopBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.seugrupo.essenciadodia.data.Perfume

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfumeFormScreen(
    viewModel: PerfumeViewModel,
    perfumeId: Int,
    onVoltar: () -> Unit
) {
    // rememberSaveable mantém o que foi digitado ao girar a tela
    var nome by rememberSaveable { mutableStateOf("") }
    var marca by rememberSaveable { mutableStateOf("") }
    var familia by rememberSaveable { mutableStateOf("") }
    var intensidade by rememberSaveable { mutableIntStateOf(2) }
    var tempMin by rememberSaveable { mutableStateOf("15") }
    var tempMax by rememberSaveable { mutableStateOf("30") }
    var tentouSalvar by rememberSaveable { mutableStateOf(false) }
    var carregado by rememberSaveable { mutableStateOf(perfumeId == 0) }

    // Edição: carrega os dados do perfume uma única vez
    LaunchedEffect(perfumeId) {
        if (!carregado) {
            viewModel.buscar(perfumeId)?.let { p ->
                nome = p.nome
                marca = p.marca
                familia = p.familiaOlfativa
                intensidade = p.intensidade
                tempMin = p.tempMin.toString()
                tempMax = p.tempMax.toString()
            }
            carregado = true
        }
    }

    val min = tempMin.toIntOrNull()
    val max = tempMax.toIntOrNull()
    val faixaInvalida = min == null || max == null || min > max

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (perfumeId == 0) "Novo perfume" else "Editar perfume") },
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
                label = { Text("Nome do perfume") },
                isError = tentouSalvar && nome.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                isError = tentouSalvar && marca.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = familia,
                onValueChange = { familia = it },
                label = { Text("Família olfativa (ex.: amadeirado, cítrico)") },
                isError = tentouSalvar && familia.isBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Intensidade", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..3).forEach { nivel ->
                    FilterChip(
                        selected = intensidade == nivel,
                        onClick = { intensidade = nivel },
                        label = { Text(rotuloIntensidade(nivel)) }
                    )
                }
            }

            Text("Faixa de temperatura ideal (°C)", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = tempMin,
                    onValueChange = { tempMin = it.filter(Char::isDigit).take(2) },
                    label = { Text("Mínima") },
                    isError = tentouSalvar && faixaInvalida,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = tempMax,
                    onValueChange = { tempMax = it.filter(Char::isDigit).take(2) },
                    label = { Text("Máxima") },
                    isError = tentouSalvar && faixaInvalida,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            if (tentouSalvar && faixaInvalida) {
                Text(
                    "A temperatura mínima deve ser menor ou igual à máxima.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    tentouSalvar = true
                    if (nome.isNotBlank() && marca.isNotBlank() && familia.isNotBlank() &&
                        min != null && max != null && min <= max
                    ) {
                        viewModel.salvar(
                            Perfume(
                                id = perfumeId,
                                nome = nome.trim(),
                                marca = marca.trim(),
                                familiaOlfativa = familia.trim(),
                                intensidade = intensidade,
                                tempMin = min,
                                tempMax = max
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