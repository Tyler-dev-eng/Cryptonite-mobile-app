# Cryptonite — Codebase Tour

A structured reading order that follows the data flow from the outside in, then back out to the UI.

---

## Mental Model

```
MainActivity
  └── CryptoniteNavGraph (CoinList | CoinDetail)
        ├── CoinListViewModel
        │     └── GetCoinsUseCase   →  CoinRepository → CoinPaprikaApi
        │
        └── CoinDetailViewModel
              └── GetCoinUseCase    →  CoinRepository → CoinPaprikaApi
```

The ViewModels never touch Retrofit directly — they only talk to use cases, which only talk to the `CoinRepository` interface, which is injected by Hilt with its real implementation. That's the Clean Architecture boundary in practice.

Cryptonite is deliberately small: two screens, two use cases, one repository, no local cache. Every screen load is a fresh network call wrapped in a `Flow<Resource<T>>` that emits `Loading` then `Success`/`Error`. Once you've read one use case + one screen, you've effectively read the whole app — the second screen is the same shape with a different payload.

---

## Layer 1 — Domain

> The "what". Pure Kotlin — no Android or framework dependencies. Everything else depends on this layer; it depends on nothing.

| #  | File                                          | Purpose                                                                                                                                        |
|----|------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | `common/Resource.kt`                           | Generic `Success / Loading / Error` sealed class. Use cases emit it from a `Flow`; ViewModels map it to UI state. Read this first — it's referenced by both use cases and both ViewModels. |
| 2  | `domain/model/CoinDomainModel.kt`              | A single row from the coin list: `id`, `isActive`, `name`, `rank`, `symbol`. Note this is a strict subset of the fields on `CoinDetailDomainModel` — the list and detail screens intentionally use different, smaller models. |
| 3  | `domain/model/TeamMemberDomainModel.kt`        | A single team member: `id`, `name`, `position` (nullable). Used only inside `CoinDetailDomainModel`.                                              |
| 4  | `domain/model/CoinDetailDomainModel.kt`        | Full detail view of a coin: adds `description` and `tags` (flattened to a `List<String>` of names) and `team` on top of the list model's fields. |
| 5  | `domain/repository/CoinRepository.kt`          | The single domain contract for all coin data. Two suspend functions: `getCoins()` and `getCoinById(coinId)`. Read this to see the full capability surface before touching data-layer code. |
| 6  | `domain/use_case/get_coins/GetCoinsUseCase.kt` | Wraps `CoinRepository.getCoins()` in a `Flow`, emitting `Resource.Loading` then `Resource.Success`/`Resource.Error`. Catches `HttpException` and `IOException` explicitly and turns them into user-facing messages. |
| 7  | `domain/use_case/get_coin/GetCoinUseCase.kt`   | Same shape as `GetCoinsUseCase`, but for `CoinRepository.getCoinById(coinId)`. Read alongside #6 — they're near-identical, which is intentional: each use case has exactly one responsibility. |

---

## Layer 2 — Data

> The "how". Fulfills the contract defined in the domain layer.

| #  | File                                       | Purpose                                                                                                                                                  |
|----|---------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| 8  | `data/remote/dto/CoinDto.kt`               | DTO for a row from the `v1/coins` endpoint. Fields: `id`, `isActive`, `isNew`, `name`, `rank`, `symbol`, `type`.                                            |
| 9  | `data/remote/dto/Tag.kt`                   | A category tag: `id`, `name`, `coinCounter`, `icoCounter`.                                                                                                 |
| 10 | `data/remote/dto/TeamMember.kt`            | A single team member DTO: `id`, `name`, `position`.                                                                                                        |
| 11 | `data/remote/dto/Whitepaper.kt`            | `link` and `thumbnail` for a coin's whitepaper. Not yet surfaced in the UI.                                                                                |
| 12 | `data/remote/dto/Stats.kt`                 | Repo/community stats (`contributors`, `followers`, `stars`, `subscribers`) attached to an extended link. Not yet surfaced in the UI.                       |
| 13 | `data/remote/dto/Links.kt`                 | Social/reference links: `explorer`, `facebook`, `reddit`, `sourceCode`, `website`, `youtube`. Not yet surfaced in the UI.                                  |
| 14 | `data/remote/dto/LinksExtended.kt`         | A single extended link entry (`type`, `url`, optional `Stats`). Not yet surfaced in the UI.                                                                |
| 15 | `data/remote/dto/CoinDetailDto.kt`         | DTO for the `v1/coins/{coinId}` endpoint. Nearly every field beyond `id`, `name`, `symbol` is nullable — CoinPaprika omits fields inconsistently across coins, so read the mapper (#17) to see how nulls are defaulted. |
| 16 | `data/remote/CoinPaprikaApi.kt`            | Retrofit interface with two endpoints: `GET v1/coins` and `GET v1/coins/{coinId}`. Exposes `BASE_URL` as a companion constant, consumed by `AppModule`.    |
| 17 | `data/remote/mapper/CoinMapper.kt`         | `CoinDto → CoinDomainModel`. A direct field-for-field copy — the simplest mapper in the app, good as a baseline before reading #18.                       |
| 18 | `data/remote/mapper/CoinDetailMapper.kt`   | `CoinDetailDto → CoinDetailDomainModel`. Defaults nullable DTO fields (`description.orEmpty()`, `rank ?: 0`, `isActive ?: false`), flattens `tags` to names, and maps `team` via a private `TeamMember.toTeamMemberDomainModel()` helper. |
| 19 | `data/repository/CoinRepositoryImpl.kt`    | Implements `CoinRepository`. Both functions are one-liners: call the API, map the result. No caching layer — every screen load is a fresh network round-trip. This is the file to extend first if local persistence is ever added. |

---

## Layer 3 — DI

> The wiring. Shows how Hilt connects interfaces to their implementations.

| #  | File                    | Purpose                                                                                                                                    |
|----|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| 20 | `di/AppModule.kt`       | Provides the singleton `Moshi` instance (with `KotlinJsonAdapterFactory`), a default `OkHttpClient`, and the Retrofit-backed `CoinPaprikaApi` built from `CoinPaprikaApi.BASE_URL`. |
| 21 | `di/RepositoryModule.kt`| Single `@Binds` entry: `CoinRepositoryImpl` → `CoinRepository`. If more repositories are added, they go here.                                |

---

## Layer 4 — Presentation: Coin List Screen

> The app's landing screen. Simplest screen in the app — no search, no tabs, no favorites.

| #  | File                                                              | Purpose                                                                                                                                     |
|----|---------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| 22 | `presentation/screens/coin_list/state/CoinListUiState.kt`         | Sealed class: `Loading`, `Success(coins: List<CoinDomainModel>)`, `Error(message: String)`. Read this before the ViewModel — it defines the shape everything maps into. |
| 23 | `presentation/screens/coin_list/viewModel/CoinListViewModel.kt`   | Injects `GetCoinsUseCase`. Fetches on `init` via `viewModelScope.launch`, collecting the use case's `Flow` and mapping each `Resource` to `CoinListUiState`. No retry mechanism yet — a failed load requires recomposition (e.g. process death) to retry. |
| 24 | `presentation/screens/coin_list/components/CoinListItem.kt`       | A single clickable row: `"{rank}. {name} ({symbol})"` on the leading side, active/inactive status text (colored primary/error) on the trailing side. |
| 25 | `presentation/screens/coin_list/CoinListScreen.kt`                 | `when` over `CoinListUiState`: `CircularProgressIndicator` (loading), centered error text (error), or a `LazyColumn` of `CoinListItem` keyed by coin id (success). Takes `onCoinClick` as a parameter rather than owning navigation — the nav graph supplies the lambda. |

---

## Layer 5 — Presentation: Coin Detail Screen

> Reads the `coinId` navigation argument out of `SavedStateHandle` rather than a screen parameter.

| #  | File                                                                  | Purpose                                                                                                                                          |
|----|-------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| 26 | `presentation/screens/coin_detail/state/CoinDetailUiState.kt`         | Sealed class: `Loading`, `Success(coin: CoinDetailDomainModel)`, `Error(message: String)`. Same shape as the list screen's state — read #22 first if you haven't already. |
| 27 | `presentation/screens/coin_detail/viewModel/CoinDetailViewModel.kt`   | Injects `GetCoinUseCase` and `SavedStateHandle`. The key line is `savedStateHandle.toRoute<Screen.CoinDetail>().coinId` — this is how the screen gets its argument without the composable itself taking a `coinId` parameter. Fetches on `init`, same collect-and-map pattern as `CoinListViewModel`. |
| 28 | `presentation/screens/coin_detail/components/CoinTag.kt`              | A single rounded, outlined chip displaying a tag name in the primary color. Used inside a `FlowRow` so tags wrap naturally.                      |
| 29 | `presentation/screens/coin_detail/components/TeamListItem.kt`         | A single team member row: name (`titleLarge`) followed by an optional italicized position (`bodyMedium`).                                        |
| 30 | `presentation/screens/coin_detail/CoinDetailScreen.kt`                 | Loading/error states match the list screen. On success, a `LazyColumn` with a single `item {}` block containing: header (rank, name, symbol, active status), description text, a `FlowRow` of `CoinTag`s, then a team member list (only rendered if `team.isNotEmpty()`) separated by `HorizontalDivider`s. |

---

## Layer 6 — Navigation, Theme, and Bootstrap

| #  | File                                        | Purpose                                                                                                                                                  |
|----|-----------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 31 | `presentation/navigation/Screen.kt`         | Two `@Serializable` route types: `CoinList` (data object) and `CoinDetail(coinId: String)` (data class). Type-safe navigation — no string routes anywhere in the app. |
| 32 | `presentation/navigation/NavGraph.kt`       | `NavHost` wiring the two routes. `CoinList`'s `onCoinClick` lambda navigates to `CoinDetail(coinId = coin.id)`.                                            |
| 33 | `presentation/ui/theme/Color.kt`            | Crypto-themed brand palette: electric teal, slate blue, and amber accents, each with container/on-container variants, defined separately for dark and light themes. |
| 34 | `presentation/ui/theme/Theme.kt`            | `CryptoniteTheme` builds full Material 3 `lightColorScheme`/`darkColorScheme` from the brand palette, switching on `isSystemInDarkTheme()`.                 |
| 35 | `presentation/ui/theme/Type.kt`             | Typography scale. Currently overrides only `bodyLarge`; the rest of the Material 3 defaults are commented out as a starting point for future customization. |
| 36 | `presentation/MainActivity.kt`              | Single activity. Wraps `CryptoniteNavGraph` in a `Scaffold` under `CryptoniteTheme`, with edge-to-edge enabled. No bottom nav bar — Cryptonite is a two-screen stack, not a tabbed app. |
| 37 | `CryptoniteApp.kt`                          | `@HiltAndroidApp` application class — just the bootstrap needed for Hilt to generate its component graph.                                                    |

---

## Known gaps worth knowing before you extend this codebase

- **No local caching.** `CoinRepositoryImpl` hits the network on every call. If offline support or a Room cache is added, `data/repository/CoinRepositoryImpl.kt` and `di/AppModule.kt` are the files to touch first (see Stonks' `StockDao`/`StockDatabase` pattern for a reference shape).
- **`res/values/colors.xml` and `themes.xml` are stock template values**, unrelated to the actual brand palette in `presentation/ui/theme/Color.kt`. The Compose theme is the real source of truth.
- **`common/Constants.kt` is an empty placeholder** — no shared constants exist yet (e.g. the API key currently has no home outside `AppModule`/`BuildConfig`).
