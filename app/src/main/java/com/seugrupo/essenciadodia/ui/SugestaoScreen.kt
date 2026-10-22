package com.seugrupo.essenciadodia.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun descricaoClima(temperatura: Double): String = when {
    temperatura < 12 -> "Frio"
    temperatura < 20 -> "Fresco"
    temperatura < 27 -> "Agradável"
    else -> "Quente"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SugestaoScreen(viewModel: SugestaoViewModel) {
    val perfumes by viewModel.perfumes.collectAsState()
    val ocasioes by viewModel.ocasioes.collectAsState()
    val temperaturaAtual by viewModel.temperatura.collectAsState()
    val carregando by viewModel.carregando.collectAsState()
    val erroAtual by viewModel.erro.collectAsState()
    val ocasiaoId by viewModel.ocasiaoSelecionadaId.collectAsState()

    val temperatura = temperaturaAtual
    val mensagemErro = erroAtual
    val ocasiao = ocasioes.find { it.id == ocasiaoId }

    Scaffold(
        topBar = { TopAppBar(title = { Text("🧴 Essência do Dia") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cartão do clima (dados vindos da API)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Curitiba agora",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    when {
                        carregando -> Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Buscando temperatura...")
                        }
                        mensagemErro != null -> {
                            Text(mensagemErro, color = MaterialTheme.colorScheme.error)
                            TextButton(onClick = { viewModel.carregarClima() }) {
                                Text("Tentar de novo")
                            }
                        }
                        temperatura != null -> Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                "%.1f°C".format(temperatura),
                                style = MaterialTheme.typography.displaySmall
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                descricaoClima(temperatura),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            // Escolha da ocasião
            Text("Para qual ocasião?", style = MaterialTheme.typography.titleMedium)
            if (ocasioes.isEmpty()) {
                Text("Cadastre ocasiões na aba Ocasiões.")
            } else {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ocasioes.forEach { o ->
                        FilterChip(
                            selected = o.id == ocasiaoId,
                            onClick = { viewModel.selecionarOcasiao(o.id) },
                            label = { Text(o.nome) }
                        )
                    }
                }
            }

            // Resultado
            when {
                perfumes.isEmpty() ->
                    Text("Cadastre perfumes na aba Perfumes para receber sugestões.")
                ocasiao == null ->
                    Text("Escolha uma ocasião acima.")
                temperatura == null ->
                    Text("Aguardando a temperatura para sugerir.")
                else -> {
                    val sugestoes = calcularSugestoes(perfumes, ocasiao, temperatura)
                    val melhor = sugestoes.first()

                    // Cartão de destaque
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Sugestão de hoje", style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                melhor.perfume.nome,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                "${melhor.perfume.marca} • ${melhor.perfume.familiaOlfativa}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = { melhor.compatibilidade / 100f },
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onPrimary,
                                trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text("Compatibilidade: ${melhor.compatibilidade}%")
                        }
                    }

                    if (sugestoes.size > 1) {
                        Text("Outras opções", style = MaterialTheme.typography.titleMedium)
                        sugestoes.drop(1).take(3).forEach { s ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                s.perfume.nome,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                s.perfume.marca,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                        Text(
                                            "${s.compatibilidade}%",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    LinearProgressIndicator(
                                        progress = { s.compatibilidade / 100f },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}