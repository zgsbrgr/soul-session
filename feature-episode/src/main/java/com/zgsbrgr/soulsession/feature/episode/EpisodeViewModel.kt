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

package com.zgsbrgr.soulsession.feature.episode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.soulsession.core.common.events.AppEventBus
import com.zgsbrgr.soulsession.core.common.result.Result
import com.zgsbrgr.soulsession.core.common.result.asResult
import com.zgsbrgr.soulsession.core.data.repository.EpisodeRepository
import com.zgsbrgr.soulsession.core.model.data.Episode
import com.zgsbrgr.soulsession.core.model.data.Topic
import com.zgsbrgr.soulsession.feature.episode.navigation.EpisodeDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val episodeRepository: EpisodeRepository,
    private val appEventBus: AppEventBus
) : ViewModel() {

    val selectedPodcast = appEventBus.selectedItem

    var favourited by mutableStateOf(false)

    private val episodeId: String = checkNotNull(
        savedStateHandle[EpisodeDestination.episodeIdArg]
    )

    private val episode: Flow<Result<Episode>> = episodeRepository.getEpisodeStream(
        id = episodeId
    ).asResult()

    private val _uiState = MutableStateFlow(EpisodeScreenUiState())
    val uiState: StateFlow<EpisodeScreenUiState> = _uiState.asStateFlow()

    init {
        episode.onEach { episodeResult ->
            val episodeState: EpisodeUiState =
                when (episodeResult) {
                    is Result.Success -> {
                        favourited = episodeResult.data.favorite
                        EpisodeUiState.Success(episodeResult.data)
                    }
                    is Result.Error -> EpisodeUiState.Error
                    is Result.Loading -> EpisodeUiState.Loading
                }
            _uiState.update {
                it.copy(
                    episodeState = episodeState
                )
            }
        }.launchIn(viewModelScope)
    }

    fun selectTopic(topic: Topic?) {
        appEventBus.postMessage(topic)
    }

    fun favorite() = viewModelScope.launch {
        episodeRepository.episodeFavorite(episodeId, !favourited)
    }
}

sealed interface EpisodeUiState {
    data class Success(val episode: Episode) : EpisodeUiState
    object Loading : EpisodeUiState
    object Error : EpisodeUiState
}

data class EpisodeScreenUiState(
    val episodeState: EpisodeUiState = EpisodeUiState.Loading
)
