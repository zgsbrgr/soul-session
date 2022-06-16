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

package com.zgsbrgr.soulsession.feature.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zgsbrgr.soulsession.core.common.result.Result
import com.zgsbrgr.soulsession.core.common.result.asResult
import com.zgsbrgr.soulsession.core.data.repository.EpisodeRepository
import com.zgsbrgr.soulsession.core.model.data.Episode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val episodeRepository: EpisodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EpisodesUiState(loading = true))
    val uiState: StateFlow<EpisodesUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            val response: Flow<Result<List<Episode>>> =
                episodeRepository.getEpisodesStream().asResult()

            response.collect { responseResult ->
                _uiState.update {
                    when (responseResult) {
                        is Result.Success -> {
                            it.copy(
                                episodes = responseResult.data,
                                loading = false,
                                error = ""
                            )
                        }
                        is Result.Loading -> {
                            it.copy(
                                episodes = emptyList(),
                                loading = true,
                                error = ""
                            )
                        }
                        is Result.Error -> {
                            it.copy(
                                episodes = emptyList(),
                                loading = false,
                                error = "failed"
                            )
                        }
                    }
                }
            }
        }
    }
}

data class EpisodesUiState(
    val loading: Boolean = false,
    val episodes: List<Episode> = emptyList(),
    val error: String = ""
) {
    val initialLoad: Boolean
        get() = episodes.isEmpty() && error.isEmpty() && loading
}
