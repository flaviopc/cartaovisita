package br.com.f16.businesscard

import android.app.Application
import br.com.f16.businesscard.data.AppDatabase
import br.com.f16.businesscard.data.BusinessCardRepository

class App: Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { BusinessCardRepository(database.businessDAO()) }
}