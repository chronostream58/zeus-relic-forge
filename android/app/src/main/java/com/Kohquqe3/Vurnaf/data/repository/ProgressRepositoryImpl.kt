package com.Kohquqe3.Vurnaf.data.repository

import com.Kohquqe3.Vurnaf.data.local.ProgressPreferences
import com.Kohquqe3.Vurnaf.domain.model.PlayerProgress
import com.Kohquqe3.Vurnaf.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val preferences: ProgressPreferences) : ProgressRepository {

    override fun load(): PlayerProgress = preferences.read()

    override fun save(progress: PlayerProgress) = preferences.write(progress)
}
