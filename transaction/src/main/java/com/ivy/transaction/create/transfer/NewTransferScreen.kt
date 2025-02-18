package com.ivy.transaction.create.transfer

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ivy.core.domain.pure.dummy.dummyActual
import com.ivy.core.domain.pure.dummy.dummyValue
import com.ivy.core.domain.pure.format.dummyValueUi
import com.ivy.core.ui.data.account.dummyAccountUi
import com.ivy.core.ui.data.dummyCategoryUi
import com.ivy.core.ui.data.transaction.dummyTrnTimeActualUi
import com.ivy.design.l1_buildingBlocks.SpacerVer
import com.ivy.design.l2_components.modal.rememberIvyModal
import com.ivy.design.util.IvyPreview
import com.ivy.design.util.KeyboardController
import com.ivy.design.util.keyboardPadding
import com.ivy.design.util.keyboardShownState
import com.ivy.transaction.component.*

@Composable
fun BoxScope.NewTransferScreen() {
    val viewModel: NewTransferViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    UI(state = state, onEvent = viewModel::onEvent)
}

@Composable
private fun BoxScope.UI(
    state: NewTransferState,
    onEvent: (NewTransferEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item(key = "toolbar") {
            SpacerVer(height = 24.dp)
            TrnScreenToolbar(
                onClose = {
                    onEvent(NewTransferEvent.Close)
                },
                actions = {},
            )
        }
        item(key = "title") {
            SpacerVer(height = 24.dp)
            var titleFocused by remember { mutableStateOf(false) }
            val keyboardShown by keyboardShownState()
            TitleInput(
                modifier = Modifier.onFocusChanged {
                    titleFocused = it.isFocused || it.hasFocus
                },
                title = state.title,
                focus = state.titleFocus,
                onTitleChange = { onEvent(NewTransferEvent.TitleChange(it)) },
                onCta = { onEvent(NewTransferEvent.Add) }
            )
            TitleSuggestions(
                focused = titleFocused && keyboardShown,
                suggestions = state.titleSuggestions,
                onSuggestionClick = { onEvent(NewTransferEvent.TitleChange(it)) }
            )
        }
        item(key = "category") {
            SpacerVer(height = 12.dp)
            CategoryComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                category = state.category
            ) {
                state.categoryPickerModal.show()
            }
        }
        item(key = "description") {
            SpacerVer(height = 24.dp)
            DescriptionComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                description = state.description
            ) {
                state.descriptionModal.show()
            }
        }
        item(key = "trn_time") {
            SpacerVer(height = 12.dp)
            TrnTimeComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                trnTime = state.timeUi
            ) {
                state.timeModal.show()
            }
        }
        item(key = "last_item_spacer") {
            val keyboardShown by keyboardShownState()
            if (keyboardShown) {
                SpacerVer(height = keyboardPadding())
            }
            // To account for "Amount Account sheet" height
            SpacerVer(height = 480.dp)
        }
    }
}


// region Previews
@Preview
@Composable
private fun Preview() {
    IvyPreview {
        UI(
            state = NewTransferState(
                accountFrom = dummyAccountUi(),
                amountFromUi = dummyValueUi(),
                amountFrom = dummyValue(),
                accountTo = dummyAccountUi(),
                amountToUi = dummyValueUi(),
                amountTo = dummyValue(),
                category = dummyCategoryUi(),
                description = null,
                timeUi = dummyTrnTimeActualUi(),
                time = dummyActual(),
                title = null,

                titleFocus = FocusRequester(),
                titleSuggestions = listOf("Title 1", "Title 2"),
                categoryPickerModal = rememberIvyModal(),
                descriptionModal = rememberIvyModal(),
                timeModal = rememberIvyModal(),
                accountPickerModal = rememberIvyModal(),
                amountModal = rememberIvyModal(),
                keyboardController = KeyboardController()
            ),
            onEvent = {}
        )
    }
}
// endregion