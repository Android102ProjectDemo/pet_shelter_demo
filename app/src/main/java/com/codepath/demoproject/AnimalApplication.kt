package com.codepath.demoproject

import android.app.Application

class AnimalApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}