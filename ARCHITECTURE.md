# Architecture

Cryptonite follows **Clean Architecture** with three layers: `data`, `domain`, and `presentation`. Each layer has a strict dependency direction — `presentation` depends on `domain`, `data` depends on `domain`, and `domain` depends on nothing.

```
app/src/main/java/com/tylerdev/cryptonite/
├── CryptoniteApp.kt
├── common/
├── data/
│   ├── remote/
│   │   ├── dto/
│   │   └── mapper/
│   └── repository/
├── di/
├── domain/
│   ├── model/
│   ├── repository/
│   └── use_case/
│       ├── get_coin/
│       └── get_coins/
└── presentation/
    ├── navigation/
    ├── screens/
    │   ├── coin_detail/
    │   │   ├── components/
    │   │   ├── state/
    │   │   └── viewModel/
    │   └── coin_list/
    │       ├── components/
    │       ├── state/
    │       └── viewModel/
    └── ui/
        └── theme/
```

---

## Root

| File               | Purpose                                                                                                                          |
|--------------------|------------------------------------------------------------------------------------------------------------------------------------|
| `CryptoniteApp.kt` | `Application` subclass annotated with `@HiltAndroidApp`. Required for Hilt to generate the component graph from modules in `di/`. |

---

## `common/`

Shared, layer-agnostic utility types used across `data`, `domain`, and `presentation`.

| File            | Purpose                                                                                                                                                                                                       |
|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Resource.kt`    | Sealed class wrapping async operation results into `Success<T>`, `Loading<T>`, and `Error<T>`. Use cases emit `Resource` from a `Flow`; ViewModels map each variant to the appropriate UI state field.        |
| `Constants.kt`   | Empty placeholder object reserved for shared constants (e.g. API keys, timeouts) as the project grows.                                                                                                        |

---

## `data/`

Responsible for all I/O. Nothing in this layer leaks into `domain` or `presentation` — remote DTOs are mapped into domain models before crossing the boundary.

### `data/remote/`

Retrofit interface and Moshi DTOs for the [CoinPaprika](https://coinpaprika.com/api/) API.

| File               | Purpose                                                                                                                                     |
|--------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `CoinPaprikaApi.kt`| Retrofit interface declaring `GET v1/coins` (full coin list) and `GET v1/coins/{coinId}` (single coin detail). Exposes `BASE_URL` as a companion constant. |

#### `data/remote/dto/`

| File                  | Purpose                                                                                                                                                                                                                             |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `CoinDto.kt`          | Moshi DTO for a row from the coin-list endpoint. Fields: `id`, `isActive`, `isNew`, `name`, `rank`, `symbol`, `type`.                                                                                                                |
| `CoinDetailDto.kt`    | Moshi DTO for the coin-detail endpoint. Nearly every field beyond `id`, `name`, and `symbol` is nullable since CoinPaprika omits fields inconsistently across coins. Nests `Links`, `LinksExtended`, `Tag`, `TeamMember`, `Whitepaper`. |
| `Links.kt`            | Social/reference links for a coin: `explorer`, `facebook`, `reddit`, `sourceCode`, `website`, `youtube` (each a list of URLs).                                                                                                       |
| `LinksExtended.kt`    | A single extended link entry with an optional `Stats` payload, plus `type` and `url`.                                                                                                                                                |
| `Stats.kt`            | Repo/community stats attached to an extended link: `contributors`, `followers`, `stars`, `subscribers`.                                                                                                                              |
| `Tag.kt`              | A category tag: `id`, `name`, `coinCounter`, `icoCounter`.                                                                                                                                                                            |
| `TeamMember.kt`       | A single team member: `id`, `name`, `position`.                                                                                                                                                                                       |
| `Whitepaper.kt`       | `link` and `thumbnail` for the coin's whitepaper.                                                                                                                                                                                     |

All DTO constructor parameters use `@param:Json` to explicitly scope the Moshi annotation to the parameter, avoiding the Kotlin annotation-target ambiguity warning.

#### `data/remote/mapper/`

Extension functions that translate DTOs into domain models. Keeping this logic here prevents it leaking into repositories or DTOs.

| File                  | Purpose                                                                                                                                                              |
|-----------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `CoinMapper.kt`       | `CoinDto → CoinDomainModel`.                                                                                                                                          |
| `CoinDetailMapper.kt` | `CoinDetailDto → CoinDetailDomainModel`, defaulting nullable DTO fields (`description`, `rank`, `isActive`) and flattening `tags` to names and `team` to `TeamMemberDomainModel` via a private helper. |

### `data/repository/`

| File                    | Purpose                                                                                                                                          |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| `CoinRepositoryImpl.kt` | Implements `CoinRepository`. `getCoins` and `getCoinById` are single-shot suspend calls that fetch from `CoinPaprikaApi` and map the result to domain models. No caching layer yet — every call hits the network. |

---

## `di/`

Hilt modules that wire the dependency graph. All modules install into `SingletonComponent`.

| File                  | Purpose                                                                                                                                             |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| `AppModule.kt`        | Provides the singleton `Moshi` instance (with `KotlinJsonAdapterFactory`), the Retrofit-backed `CoinPaprikaApi`, and a default `OkHttpClient`.       |
| `RepositoryModule.kt` | Binds `CoinRepositoryImpl` as the singleton implementation of `CoinRepository`.                                                                       |

**What goes here as the project grows:** a `DatabaseModule` if local caching (Room) is introduced; additional `@Binds` entries for new repository implementations.

---

## `domain/`

Pure Kotlin — no Android framework imports. `data` and `presentation` both depend on this layer; it depends on neither.

### `domain/model/`

| File                       | Purpose                                                                                                          |
|-----------------------------|--------------------------------------------------------------------------------------------------------------------|
| `CoinDomainModel`           | A single row from the coin list: `id`, `isActive`, `name`, `rank`, `symbol`.                                       |
| `CoinDetailDomainModel`     | Full detail view of a coin: `coinId`, `name`, `description`, `symbol`, `rank`, `isActive`, `tags` (flattened to names), `team`. |
| `TeamMemberDomainModel`     | A single team member: `id`, `name`, `position` (nullable).                                                        |

### `domain/repository/`

| File                | Purpose                                                                                                              |
|---------------------|--------------------------------------------------------------------------------------------------------------------|
| `CoinRepository.kt` | The single domain contract for coin data. Declares `getCoins()` and `getCoinById(coinId)`, both `suspend` functions. |

### `domain/use_case/`

Each use case has a single responsibility and is invoked via `operator fun invoke()`. Both wrap the repository call in a `Flow`, emitting `Resource.Loading` then `Resource.Success`/`Resource.Error`, catching `HttpException` and `IOException` explicitly.

| File                  | Purpose                                                                                     |
|-----------------------|-----------------------------------------------------------------------------------------------|
| `GetCoinsUseCase`     | Fetches the full coin list via `CoinRepository.getCoins`.                                     |
| `GetCoinUseCase`      | Fetches a single coin's detail via `CoinRepository.getCoinById(coinId)`.                      |

---

## `presentation/`

Jetpack Compose UI layer. ViewModels hold `StateFlow<ScreenUiState>` and delegate all business logic to use cases. No Retrofit types appear here.

### `presentation/MainActivity.kt`

Single-activity entry point. Wraps `CryptoniteNavGraph` in a `Scaffold` under `CryptoniteTheme`, with edge-to-edge enabled.

### `presentation/navigation/`

| File           | Purpose                                                                                                                                          |
|----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| `Screen.kt`    | Type-safe navigation routes: `CoinList` (data object) and `CoinDetail(coinId: String)` (data class). Both annotated `@Serializable`.                |
| `NavGraph.kt`  | `NavHost` wiring the two routes. `CoinList` passes an `onCoinClick` lambda that navigates to `CoinDetail` with the clicked coin's `id`.               |

### `presentation/screens/coin_list/`

| File                   | Purpose                                                                                                                              |
|------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| `state/CoinListUiState.kt`      | Sealed class: `Loading`, `Success(coins: List<CoinDomainModel>)`, `Error(message: String)`.                                 |
| `viewModel/CoinListViewModel.kt`| Injects `GetCoinsUseCase`. Fetches the coin list on `init` and maps each `Resource` emission to `CoinListUiState`.                     |
| `CoinListScreen.kt`             | Renders `CircularProgressIndicator` (loading), centered error text (error), or a `LazyColumn` of `CoinListItem` rows keyed by coin id (success). |
| `components/CoinListItem.kt`    | A single clickable row: `"{rank}. {name} ({symbol})"` on the leading side, active/inactive status text (colored primary/error) on the trailing side. |

### `presentation/screens/coin_detail/`

| File                     | Purpose                                                                                                                                                                                                                                    |
|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `state/CoinDetailUiState.kt`      | Sealed class: `Loading`, `Success(coin: CoinDetailDomainModel)`, `Error(message: String)`.                                                                                                                                       |
| `viewModel/CoinDetailViewModel.kt`| Injects `GetCoinUseCase` and `SavedStateHandle`. Reads `coinId` from the `Screen.CoinDetail` route via `savedStateHandle.toRoute()`, then fetches details on `init` and maps each `Resource` emission to `CoinDetailUiState`. |
| `CoinDetailScreen.kt`             | Renders loading/error states, or on success a `LazyColumn` with a header (rank, name, symbol, active status), description, a `FlowRow` of `CoinTag`s, and a team member list (only shown if non-empty) separated by dividers. |
| `components/CoinTag.kt`           | A single rounded, outlined chip displaying a tag name in the primary color.                                                                                                                                                       |
| `components/TeamListItem.kt`      | A single team member row: name (`titleLarge`) followed by an optional italicized position (`bodyMedium`).                                                                                                                        |

### `presentation/ui/theme/`

| File       | Purpose                                                                                                                                                                                     |
|------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Color.kt` | Crypto-themed brand palette: electric teal, slate blue, and amber accents with dedicated container/on-container variants for both a dark theme (near-black surfaces) and a light theme (deepened hues for contrast on white). |
| `Theme.kt` | `CryptoniteTheme` composable. Builds full Material 3 `lightColorScheme` and `darkColorScheme` from the brand palette, switching on `isSystemInDarkTheme()`.                                 |
| `Type.kt`  | Typography scale. Currently overrides only `bodyLarge`; other Material 3 defaults are commented out as a starting point for future customization.                                          |

---

## `res/`

### `res/values/`

`colors.xml` and `themes.xml` still hold the stock Android Studio project-template values (`purple_200`, `teal_700`, `Theme.Cryptonite` extending `Theme.Material.Light.NoActionBar`) — they are not yet wired to the brand palette in `presentation/ui/theme/Color.kt`. The Compose `CryptoniteTheme` is the actual source of truth for in-app theming.

### `res/mipmap-*/`

Launcher icons at each screen density.

### `res/xml/`

Android backup and data-extraction configuration.

### `res/drawable/`

App drawable resources.
