package com.stephenelf.geminigeneratedapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class annotated with @HiltAndroidApp to trigger Hilt's code generation.
 * This class is the entry point for Hilt and the parent container for all dependencies.
 */
@HiltAndroidApp
class GymsApplication : Application()