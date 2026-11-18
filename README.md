# 🌸 Essência do Dia

Aplicativo Android que sugere **qual perfume usar hoje** com base na **temperatura atual** e na **ocasião** (trabalho, festa, encontro...). Você cadastra sua coleção de perfumes e suas ocasiões, e o app consulta o clima em tempo real para indicar as opções mais compatíveis.

## ✨ Funcionalidades

- **Coleção de perfumes (CRUD):** cadastrar, editar, listar e excluir perfumes com marca, família olfativa, intensidade (leve, moderada ou forte) e faixa de temperatura ideal
- **Ocasiões (CRUD):** cadastrar ocasiões com a intensidade ideal para cada uma
- **Sugestão do dia:** busca a temperatura atual de Curitiba na API **Open-Meteo** e calcula uma nota de compatibilidade (0 a 100) para cada perfume, ordenando do mais ao menos indicado
- **Compartilhar coleção:** envia a lista de perfumes por qualquer app (WhatsApp, e-mail etc.) usando o menu de compartilhamento do Android
- **Dados salvos no aparelho:** tudo fica guardado localmente com Room, funcionando mesmo depois de fechar o app

## 🧠 Como a sugestão funciona

Cada perfume começa com 100 pontos e perde pontos quando:
- a temperatura atual está **fora da faixa ideal** do perfume (−5 pontos por grau de diferença)
- a intensidade do perfume é **diferente da ideal** para a ocasião (−25 pontos por nível de diferença)

## 🛠️ Tecnologias

- **Kotlin** + **Jetpack Compose** (Material 3)
- Arquitetura **MVVM** com `ViewModel` e `StateFlow`
- **Room** (SQLite) para persistência local
- **Retrofit** + **Gson** para consumir a API REST do Open-Meteo
- **Coroutines** e **Flow**
- **Navigation Compose** com barra de navegação inferior

## 📁 Estrutura

```
app/src/main/java/com/seugrupo/essenciadodia/
├── data/   # Entidades (Perfume, Ocasiao), DAOs, banco Room e API de clima
└── ui/     # Telas em Compose, ViewModels e navegação
```

## ▶️ Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/PedroHBecker/Essenciadodia.git
   ```
2. Abra a pasta no **Android Studio**
3. Aguarde a sincronização do Gradle
4. Rode em um emulador ou celular com **Android 8.0 (API 26)** ou superior

> É preciso ter conexão com a internet para a sugestão do dia buscar a temperatura.

## 👥 Equipe

Projeto desenvolvido para a disciplina de **Desenvolvimento de Aplicativos Móveis** do curso de Análise e Desenvolvimento de Sistemas da Universidade Positivo, por:

- Pedro Henrique Becker
- Othon Freitas
- Walter Potma
