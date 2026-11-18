package com.Kohquqe3.Vurnaf.core.di

import android.content.Context
import com.Kohquqe3.Vurnaf.data.local.ProgressPreferences
import com.Kohquqe3.Vurnaf.data.repository.CatalogRepositoryImpl
import com.Kohquqe3.Vurnaf.data.repository.ProgressRepositoryImpl
import com.Kohquqe3.Vurnaf.domain.repository.CatalogRepository
import com.Kohquqe3.Vurnaf.domain.repository.ProgressRepository

object ServiceLocator {

    private var progressRepository: ProgressRepository? = null
    private val catalogRepository: CatalogRepository = CatalogRepositoryImpl()

    fun init(context: Context) {
        if (progressRepository == null) {
            progressRepository = ProgressRepositoryImpl(ProgressPreferences(context.applicationContext))
        }
    }

    val progress: ProgressRepository
        get() = requireNotNull(progressRepository)

    val catalog: CatalogRepository
        get() = catalogRepository
}
