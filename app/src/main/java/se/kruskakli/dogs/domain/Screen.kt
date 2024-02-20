package se.kruskakli.dogs.domain

sealed class Screen(val route: String) {
    object BreedScreen : Screen("main_screen")
    object SettingsScreen : Screen("settings_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }

}
