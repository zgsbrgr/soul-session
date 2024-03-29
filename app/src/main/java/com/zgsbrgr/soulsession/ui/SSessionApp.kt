/*
 * Copyright 2022 zgsbrgr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zgsbrgr.soulsession.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zgsbrgr.soulsession.R
import com.zgsbrgr.soulsession.core.network.util.NetworkMonitor
import com.zgsbrgr.soulsession.core.ui.theme.SoulSessionTheme
import com.zgsbrgr.soulsession.navigation.SoulSessionNavHost

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalLifecycleComposeApi::class
)
@Composable
fun SSessionApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    backDispatcher: OnBackPressedDispatcher,
    appState: SoulSessionAppState = rememberSoulSessionAppState(networkMonitor = networkMonitor)
) {
    SoulSessionTheme {
        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentDestination = navBackStackEntry?.destination

        val snackbarHostState = remember { SnackbarHostState() }

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                modifier = Modifier,
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { padding ->
                val isOffline by appState.isOffline.collectAsStateWithLifecycle()

                val notConnected = stringResource(R.string.not_connected)
                LaunchedEffect(isOffline) {
                    if (isOffline) snackbarHostState.showSnackbar(
                        message = notConnected,
                        duration = SnackbarDuration.Indefinite
                    )
                }
                Row(
                    Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                        )
                ) {

                    SoulSessionNavHost(
                        windowSizeClass = windowSizeClass,
                        navController = navController,
                        modifier = Modifier
                            .padding(padding)
                            .consumedWindowInsets(padding),
                        backDispatcher = backDispatcher
                    )
                }
            }
        }
    }
}
