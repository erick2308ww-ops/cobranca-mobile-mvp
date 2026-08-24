# Cesta Básica Alves

Aplicativo Android (Kotlin + Jetpack Compose) para a gestão de vendas e cobrança de cestas básicas.
Funciona 100% offline, com os dados armazenados localmente no dispositivo (Room/SQLite).

## Funcionalidades

- **Início**: resumo com total a receber, total recebido, parcelas e clientes em atraso.
- **Clientes**: cadastro, edição e exclusão de clientes (nome, telefone, endereço).
- **Cestas**: catálogo de cestas básicas com descrição dos itens e preço.
- **Vendas**: registro de venda de uma cesta para um cliente, parcelada, gerando as parcelas automaticamente (vencimento mensal a partir da data escolhida).
- **Cobrança**: lista de parcelas com filtro (todas / pendentes / atrasadas / pagas) e ação para marcar parcela como paga (ou desfazer).

## Stack técnica

- Kotlin + Jetpack Compose + Material 3
- Navigation Compose
- Room (persistência local)
- kotlinx-datetime

## Como abrir e rodar

1. Abra a pasta do projeto no Android Studio (Koala ou mais recente).
2. Deixe o Android Studio baixar o Gradle/AGP e o Android SDK necessários na primeira sincronização.
3. Rode a configuração `app` em um emulador ou dispositivo físico (mínimo Android 7.0 / API 24).

Também é possível compilar via linha de comando, desde que o `ANDROID_HOME` esteja configurado:

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

> Este projeto foi criado neste ambiente sem acesso ao Android SDK nem ao repositório Maven do
> Google, então o build completo (`assembleDebug`) não pôde ser executado aqui. Recomenda-se
> compilar e testar no Android Studio antes do primeiro uso.

## Estrutura

```
app/src/main/java/com/alves/cestabasica/
├── data/local/          # Entidades Room, DAOs, banco de dados
├── data/repository/     # Repositório com as regras de negócio
└── ui/
    ├── navigation/       # NavHost e barra de navegação inferior
    ├── screens/          # Telas (dashboard, clientes, cestas, vendas, cobranca)
    └── theme/            # Tema Material 3
```
