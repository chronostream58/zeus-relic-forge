package com.Kohquqe3.Vurnaf.presentation.app

import androidx.lifecycle.ViewModel
import com.Kohquqe3.Vurnaf.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _revision = MutableStateFlow(0)
    val revision: StateFlow<Int> = _revision.asStateFlow()

    private val backStack = ArrayDeque<Screen>()

    private fun show(target: Screen) {
        _screen.value = target
        _revision.value = _revision.value + 1
    }

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) backStack.addLast(current)
        show(target)
    }

    fun replace(target: Screen) {
        show(target)
    }

    fun openMenu() {
        backStack.clear()
        show(Screen.Menu)
    }

    fun back(): Boolean {
        val current = _screen.value
        if (current is Screen.Splash) return false
        if (current is Screen.GameOver) {
            openMenu()
            return true
        }
        val previous = backStack.removeLastOrNull() ?: return false
        show(previous)
        return true
    }
}
