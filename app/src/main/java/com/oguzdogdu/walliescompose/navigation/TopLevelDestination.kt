/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oguzdogdu.walliescompose.navigation

import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.home.navigation.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.collections.navigation.CollectionScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.favorites.navigation.FavoritesScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.settings.navigation.SettingsScreenNavigationRoute

enum class TopLevelDestination(
    val icon: Int,
    val iconTextId: Int,
    val route: String,
    val titleTextId: Int,
) {
    WALLPAPERS(
        route = HomeScreenNavigationRoute,
        icon = R.drawable.wallpaper,
        iconTextId = R.string.wallpapers_title,
        titleTextId = R.string.wallpapers_title,
    ),
    COLLECTIONS(
        route = CollectionScreenNavigationRoute,
        icon = R.drawable.collections,
        iconTextId = R.string.collections_title,
        titleTextId = R.string.collections_title,
    ),
    FAVORITES(
        route = FavoritesScreenNavigationRoute,
        icon = R.drawable.favorite,
        iconTextId = R.string.favorites_title,
        titleTextId = R.string.favorites_title,
    ),
    SETTINGS(
        route = SettingsScreenNavigationRoute,
        icon = R.drawable.settings,
        iconTextId = R.string.settings_title,
        titleTextId = R.string.settings_title,
    ),
}
