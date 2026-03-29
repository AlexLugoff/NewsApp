# NewsApp — Современный агрегатор новостей (RSS + Clean Architecture)

Современное Android-приложение для чтения новостей из различных RSS-источников, построенное на передовом стеке технологий 2025 года. Проект следует принципам Clean Architecture и ориентирован на Offline-first опыт.

## 🚀 Основные возможности
* **RSS Engine:** Загрузка и обработка новостей из гибко настраиваемых RSS-лент.
* **Source Management:** Управление источниками новостей (включение/выключение конкретных лент) через BottomSheet.
* **Offline First:** Полное кеширование статей в локальную базу данных Room.
* **Smart Cache:** Автоматическая фоновая очистка старых новостей.
* **Modern UI:** Полная поддержка Edge-to-Edge и динамических цветов (Material You).

## 🛠 Стек технологий (2025 Edition)
* **Core:** Kotlin 2.3.20 + Gradle 9.4.1
* **UI:** Jetpack Compose (Material 3 v1.4.0)
* **Architecture:** Clean Architecture + MVVM + MVI
* **DI:** Hilt (Dagger-Hilt v2.59.2)
* **Asynchronous:** Kotlin Coroutines & Flow (StateFlow, SharedFlow)
* **Database:** Room v2.8.4 (с поддержкой KSP)
* **Image Loading:** Coil 3.4.0 (OkHttp engine)
* **Parsing:** Prof18 RSS Parser v6.1.5 + Jsoup 1.22.1
* **Navigation:** Type-safe Compose Navigation v2.9.7
* **Testing:** MockK, Turbine, JUnit 4, Espresso

## 🏗 Модульная структура
Проект разделен на независимые слои для обеспечения масштабируемости и тестируемости:
* **`:app`** — Точка входа, DI граф и навигация.
* **`:core:model`** — Чистые Kotlin-модели (Shared).
* **`:core:domain`** — Бизнес-логика и Use Cases.
* **`:core:data`** — Репозитории и источники данных.
* **`:core:network`** — RSS парсинг и сетевые запросы.
* **`:core:database`** — Локальное хранилище (Room).
* **`:core:ui`** — Общие компоненты, темы и ресурсы.
* **`:feature:*`** — Изолированные экраны (NewsList, Details, Sources).

## 🎨 Дизайн и Темы
Приложение использует продвинутую систему тем:
* **Material You:** Автоматическая адаптация цветов под обои пользователя (Android 12+).
* **Surface Container:** Использование современных ролей поверхностей M3 для лучшего визуального разделения.
* **Edge-to-Edge:** Весь контент отображается "под" системными панелями для максимального погружения.

## 📸 Скриншоты
<p align="left">
  <img src="screenshots/Screenshot_New_App_1.png" width="250" />
  <img src="screenshots/Screenshot_New_App_2.png" width="250" />
  <img src="screenshots/Screenshot_New_App_3.png" width="250" />
</p>
