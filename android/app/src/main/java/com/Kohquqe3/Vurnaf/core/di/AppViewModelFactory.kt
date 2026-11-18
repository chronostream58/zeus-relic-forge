package com.Kohquqe3.Vurnaf.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Kohquqe3.Vurnaf.domain.usecase.ApplyHintUseCase
import com.Kohquqe3.Vurnaf.domain.usecase.AwardRewardsUseCase
import com.Kohquqe3.Vurnaf.domain.usecase.ForgeToolUseCase
import com.Kohquqe3.Vurnaf.domain.usecase.GenerateBoardUseCase
import com.Kohquqe3.Vurnaf.domain.usecase.RotateRingUseCase
import com.Kohquqe3.Vurnaf.presentation.app.AppViewModel
import com.Kohquqe3.Vurnaf.presentation.game.GameViewModel
import com.Kohquqe3.Vurnaf.presentation.gameover.GameOverViewModel
import com.Kohquqe3.Vurnaf.presentation.levels.LevelSelectViewModel
import com.Kohquqe3.Vurnaf.presentation.lore.LoreViewModel
import com.Kohquqe3.Vurnaf.presentation.menu.MenuViewModel
import com.Kohquqe3.Vurnaf.presentation.splash.SplashViewModel
import com.Kohquqe3.Vurnaf.presentation.workshop.WorkshopViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    private val generateBoard = GenerateBoardUseCase()
    private val rotateRing = RotateRingUseCase()
    private val applyHint = ApplyHintUseCase()
    private val forgeTool = ForgeToolUseCase()
    private val awardRewards = AwardRewardsUseCase()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(ServiceLocator.progress)
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    ServiceLocator.progress,
                    ServiceLocator.catalog,
                    generateBoard,
                    rotateRing,
                    applyHint,
                    awardRewards
                )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(ServiceLocator.progress)
            modelClass.isAssignableFrom(LevelSelectViewModel::class.java) ->
                LevelSelectViewModel(ServiceLocator.progress, ServiceLocator.catalog)
            modelClass.isAssignableFrom(WorkshopViewModel::class.java) ->
                WorkshopViewModel(ServiceLocator.progress, ServiceLocator.catalog, forgeTool)
            modelClass.isAssignableFrom(LoreViewModel::class.java) ->
                LoreViewModel(ServiceLocator.progress, ServiceLocator.catalog)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
