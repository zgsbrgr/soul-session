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

package com.zgsbrgr.soulsession.navigation

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.zgsbrgr.soulsession.feature.episode.navigation.EpisodeDestination
import com.zgsbrgr.soulsession.feature.episode.navigation.episodeGraph
import com.zgsbrgr.soulsession.feature.episodes.navigation.EpisodesDestination
import com.zgsbrgr.soulsession.feature.episodes.navigation.episodesGraph
import com.zgsbrgr.soulsession.feature.player.PlayerScreen
import com.zgsbrgr.soulsession.feature.player.SmallPlayerScreen

@Composable
fun SoulSessionNavHost(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = EpisodesDestination.route,
    backDispatcher: OnBackPressedDispatcher
) {

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            episodesGraph(
                navigateToEpisode =
                { navController.navigate("${EpisodeDestination.route}/$it") },
                nestedGraphs = {
                    episodeGraph(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            )
        }

        SmallPlayerScreen(
            modifier = Modifier.align(Alignment.BottomCenter),
            onPlaybackStopped = { }
        )
        PlayerScreen(
            modifier = modifier,
            backDispatcher = backDispatcher
        )
    }
}
