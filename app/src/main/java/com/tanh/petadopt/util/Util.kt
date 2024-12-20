package com.tanh.petadopt.util

import com.tanh.petadopt.R

object Util {

    const val DEFAULT_WEB_CLIENT_ID = "856323978983-aa7m25o3rhbq38rfhdcoj6dl4fo6igeo.apps.googleusercontent.com"
    const val HOME = "Home"
    const val LOG_IN = "login"
    const val DETAIL = "detail"

    const val ANIMALS_COLLECTION = "pets"
    const val USERS_COLLECTION = "users"
    const val PREFERENCES_COLLECTION = "preferences"

    val categories = listOf(
        "Dogs" to R.drawable.dog to false,
        "Cats" to R.drawable.cat to false,
        "Birds" to R.drawable.bird to false,
        "Fish" to R.drawable.fish to false
    )

}

enum class Category {
    Dogs, Cats, Fish, Birds
}

