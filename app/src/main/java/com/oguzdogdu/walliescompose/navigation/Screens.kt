package com.oguzdogdu.walliescompose.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens {
    @Serializable
    data object AuthenticatedUserScreenRoute : Screens()

    @Serializable
    data object ChangeEmailScreenRoute : Screens()

    @Serializable
    data class ChangeNameAndSurnameScreenRoute(
        val name: String? = null,
        val surname: String? = null
    ) : Screens()

    @Serializable
    data object ChangePasswordScreenRoute : Screens()

    @Serializable
    data object CollectionScreenNavigationRoute : Screens()

    @Serializable
    data class CollectionDetailListScreenRoute(
        val collectionDetailListId: String? = null,
        val collectionDetailListTitle: String? = null,
    ) : Screens()

    @Serializable
    data class DetailScreenRoute(val photoId: String? = null) : Screens()

    @Serializable
    data object FavoritesScreenNavigationRoute : Screens()

    @Serializable
    data object HomeScreenNavigationRoute : Screens()

    @Serializable
    data object LatestScreenNavigationRoute : Screens()

    @Serializable
    data object LoginScreenNavigationRoute : Screens()

    @Serializable
    data object ForgotPasswordScreenNavigationRoute : Screens()

    @Serializable
    data object SignInWithEmailScreenNavigationRoute : Screens()

    @Serializable
    data object PopularScreenNavigationRoute : Screens()

    @Serializable
    data class ProfileDetailScreenNavigationRoute(val username: String? = null) : Screens()

    @Serializable
    data class SearchScreenNavigationRoute(val queryFromDetail: String? = null) : Screens()

    @Serializable
    data object SettingsScreenNavigationRoute : Screens()

    @Serializable
    data object SignUpScreenNavigationRoute : Screens()

    @Serializable
    data object SplashScreenRoute : Screens()

    @Serializable
    data object TopicsScreenNavigationRoute : Screens()

    @Serializable
    data class TopicDetailListScreenNavigationRoute(val topicId: String? = null)
}