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
import com.oguzdogdu.walliescompose.navigation.utils.IconResource
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons

enum class TopLevelDestination(
    val icon: IconResource,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    WALLPAPERS(
        icon = IconResource.fromDrawableResource(WalliesIcons.WallpapersIcon),
        iconTextId = R.string.wallpapers_title,
        titleTextId = R.string.wallpapers_title,
    ),
    COLLECTIONS(
        icon = IconResource.fromDrawableResource(WalliesIcons.CollectionsIcon),
        iconTextId = R.string.collections_title,
        titleTextId = R.string.collections_title,
    ),
    FAVORITES(
        icon = IconResource.fromDrawableResource(WalliesIcons.FavoritesIcon),
        iconTextId = R.string.favorites_title,
        titleTextId = R.string.favorites_title,
    ),
    SETTINGS(
        icon = IconResource.fromDrawableResource(WalliesIcons.SettingsIcon),
        iconTextId = R.string.settings_title,
        titleTextId = R.string.settings_title,
    ),
}
