package com.Kohquqe3.Vurnaf.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kohquqe3.Vurnaf.core.di.AppViewModelFactory
import com.Kohquqe3.Vurnaf.core.navigation.Screen
import com.Kohquqe3.Vurnaf.core.theme.BackgroundBase
import com.Kohquqe3.Vurnaf.presentation.game.GameScreen
import com.Kohquqe3.Vurnaf.presentation.gameover.GameOverScreen
import com.Kohquqe3.Vurnaf.presentation.levels.LevelSelectScreen
import com.Kohquqe3.Vurnaf.presentation.lore.LoreScreen
import com.Kohquqe3.Vurnaf.presentation.menu.MenuScreen
import com.Kohquqe3.Vurnaf.presentation.splash.SplashScreen
import com.Kohquqe3.Vurnaf.presentation.workshop.WorkshopScreen

@Composable
fun AppRoot(
    appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
) {
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val revision by appViewModel.revision.collectAsStateWithLifecycle()

    BackHandler(
        enabled = screen !is Screen.Splash && screen !is Screen.Menu && screen !is Screen.Game
    ) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBase)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(tween(durationMillis = 420)) togetherWith fadeOut(tween(durationMillis = 320))
            }
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.replace(Screen.Menu) }
                )

                is Screen.Menu -> MenuScreen(
                    revision = revision,
                    onPlay = { levelId, challenge ->
                        appViewModel.navigate(Screen.Game(levelId, challenge, revision))
                    },
                    onLevels = { appViewModel.navigate(Screen.LevelSelect) },
                    onWorkshop = { appViewModel.navigate(Screen.Workshop) },
                    onLore = { appViewModel.navigate(Screen.Lore) }
                )

                is Screen.Game -> GameScreen(
                    levelId = current.levelId,
                    challenge = current.challenge,
                    attempt = current.attempt,
                    onFinished = { outcome -> appViewModel.replace(Screen.GameOver(outcome)) },
                    onMenu = { appViewModel.openMenu() }
                )

                is Screen.GameOver -> GameOverScreen(
                    outcome = current.outcome,
                    revision = revision,
                    onPlayAgain = {
                        appViewModel.replace(
                            Screen.Game(current.outcome.levelId, current.outcome.challenge, revision)
                        )
                    },
                    onNextGate = {
                        val next = current.outcome.nextLevel ?: current.outcome.levelId
                        appViewModel.replace(Screen.Game(next, false, revision))
                    },
                    onWorkshop = { appViewModel.navigate(Screen.Workshop) },
                    onMenu = { appViewModel.openMenu() }
                )

                is Screen.LevelSelect -> LevelSelectScreen(
                    revision = revision,
                    onStartLevel = { levelId, challenge ->
                        appViewModel.navigate(Screen.Game(levelId, challenge, revision))
                    },
                    onMenu = { appViewModel.openMenu() }
                )

                is Screen.Workshop -> WorkshopScreen(
                    revision = revision,
                    onMenu = { appViewModel.openMenu() }
                )

                is Screen.Lore -> LoreScreen(
                    revision = revision,
                    onMenu = { appViewModel.openMenu() }
                )
            }
        }
    }
}
