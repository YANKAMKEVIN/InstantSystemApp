# 📰 News App

A modern Android application that displays the latest top headlines using the [NewsAPI.org](https://newsapi.org/docs).  
Developed with Jetpack Compose and Kotlin using a Clean Architecture approach.

## ✨ Features

- 🔍 Search news articles dynamically
- 🧭 Browse top headlines by categories
- 🔖 Bookmark articles and read them later in the "Lectures" tab
- 📴 Top headlines available offline: the last list received is shown, with the date it was received
- 📱 Adaptive layout with master-detail (tablet-friendly)
- 🌙 Modern UI with Material 3
- ✅ Unit and UI tests (ViewModel, UseCases, Composables)

---

## 🔑 Setup: NewsAPI key

The app needs a free [NewsAPI.org](https://newsapi.org/register) API key. It is read at build time from
`local.properties` (at the root of the project, never committed):

```properties
NEWS_API_KEY=your_api_key_here
```

Then rebuild the app. Without this key the app still compiles, but every screen shows
"Clé API NewsAPI manquante…" ("NewsAPI key missing…" in English) instead of articles.

---

## 🧠 Architecture

The app uses **Clean MVVM Architecture** with the following layers:

- **UI Layer**: Jetpack Compose + Hilt-powered ViewModels  
- **Domain Layer**: Use cases + repository interfaces (business rules)  
- **Data Layer**: Retrofit API, Paging3 integration, Room cache of the top headlines (`RemoteMediator`)

This ensures:
- Separation of concerns
- Easy testability
- Scalability
- Maintainability

> All business logic is handled by UseCases, and UI does not depend on data sources.

---

## 🛠️ Tech Stack

| Layer        | Tools / Libraries                             | Why |
|--------------|-----------------------------------------------|-----|
| UI           | Jetpack Compose, Material 3                   | Modern, declarative UI |
| Navigation   | `NavigableListDetailPaneScaffold` (Material 3 adaptive) | Adaptive layouts |
| DI           | Hilt                                          | Clean dependency management |
| Networking   | Retrofit + OkHttp                             | Standard, robust API handling |
| Paging       | Paging 3                                      | Efficient pagination |
| Offline cache| Room (+ `room-paging`, `RemoteMediator`)      | Top headlines readable offline |
| Image Loading| Coil                                          | Fast and lightweight |
| Testing      | JUnit, Turbine, Mockk, Compose UI testing     | Full test coverage support |

---

## 🧪 Tests

| Layer     | Covered | Details |
|-----------|---------|---------|
| `data`    | ✅      | Retrofit, data source, error mapping |
| `domain`  | ✅      | UseCase logic |
| `ui`      | ✅      | ViewModels, key Composables (LoaderScreen, ErrorStateScreen, SearchScreen, etc.) |

---

## 🧩 Choices & Tradeoffs

- **Jetpack Compose**: To build modern UIs with less boilerplate.
- **Material 3 Adaptive Navigation**: To demonstrate ability to build tablet-friendly master-detail views.
- **Offline cache for the top headlines only**: Room stores the last list received for each category of the
  "À la une" screen (read through a `RemoteMediator`). Offline, that list stays on screen with the date it was
  received. Home and search results are not cached. The Room schema is exported to `app/schemas` for future
  migrations (version 1: no database existed before, so there is no migration yet).
- **Bookmarks without Room**: Bookmarked articles are stored on the device in SharedPreferences (JSON via Gson, already
  used by Retrofit), to avoid a database for a short list.
- **Recent searches in SharedPreferences**: the 5 latest searches are kept between launches, stored like the
  bookmarks (JSON via Gson).
- **Single Activity architecture**: Recommended pattern with Compose Navigation.

---

## 🚧 Known limitations / improvements

- 🌐 Only the top headlines are cached offline (not the home feed nor search results)
- 🌍 The home feed follows the device language (English when NewsAPI does not support it); top headlines stay on
  the "us" country on purpose
- 🧪 No end-to-end (instrumented) tests, only UI + unit tests

---

## Screenshots

<p align="center">
  <img src="images/screenshot-home.png" alt="InstantSystem app screenshot" width="240" />
</p>

<img src="https://github.com/user-attachments/assets/d253f991-0646-4767-8328-eb390f6f42c3" alt="news_app_3" width="240" />
<img src="https://github.com/user-attachments/assets/95229f9a-4169-41c8-902b-cbc168565a96" alt="news_app_2" width="240" />
<img src="https://github.com/user-attachments/assets/a10122dd-4589-4974-a6c3-8b16fb66e5ac" alt="news_app_1" width="240" />
<img src="https://github.com/user-attachments/assets/b88c9828-2b86-45b4-99cf-980e1c6d4aba" alt="news_app_4" width="240" />

## 💬 Final thoughts

This project is a concise but solid example of a real-world news application. I prioritized maintainable structure, modern Android practices, and UI responsiveness.

⏱️ **Time spent: ~20h**

Please feel free to reach out if you want me to walk through any section of the code!

Thanks for the opportunity 🙌  
— Kevin Hermann
