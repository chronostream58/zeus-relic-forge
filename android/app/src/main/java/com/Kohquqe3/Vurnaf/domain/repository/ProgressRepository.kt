package com.Kohquqe3.Vurnaf.domain.repository

import com.Kohquqe3.Vurnaf.domain.model.PlayerProgress

interface ProgressRepository {
    fun load(): PlayerProgress
    fun save(progress: PlayerProgress)
}
