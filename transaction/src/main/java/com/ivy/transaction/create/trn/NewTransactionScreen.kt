package com.ivy.transaction.create.trn

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ivy.core.domain.pure.dummy.dummyValue
import com.ivy.core.domain.pure.format.dummyValueUi
import com.ivy.core.ui.category.pick.CategoryPickerModal
import com.ivy.core.ui.data.account.dummyAccountUi
import com.ivy.core.ui.data.dummyCategoryUi
import com.ivy.core.ui.data.transaction.dummyTrnTimeActualUi
import com.ivy.core.ui.data.transaction.dummyTrnTimeDueUi
import com.ivy.core.ui.transaction.feeling
import com.ivy.core.ui.transaction.humanText
import com.ivy.core.ui.transaction.icon
import com.ivy.data.transaction.TransactionType
import com.ivy.data.transaction.dummyTrnTimeActual
import com.ivy.data.transaction.dummyTrnTimeDue
import com.ivy.design.l1_buildingBlocks.SpacerVer
import com.ivy.design.l2_components.modal.rememberIvyModal
import com.ivy.design.l3_ivyComponents.Visibility
import com.ivy.design.l3_ivyComponents.button.ButtonSize
import com.ivy.design.l3_ivyComponents.button.IvyButton
import com.ivy.design.util.IvyPreview
import com.ivy.design.util.KeyboardController
import com.ivy.design.util.keyboardPadding
import com.ivy.design.util.keyboardShownState
import com.ivy.navigation.destinations.transaction.NewTransaction
import com.ivy.resources.R
import com.ivy.transaction.component.*
import com.ivy.transaction.modal.DescriptionModal
import com.ivy.transaction.modal.TrnTimeModal
import com.ivy.transaction.modal.TrnTypeModal

@Composable
fun BoxScope.NewTransactionScreen(arg: NewTransaction.Arg) {
    val viewModel: NewTransactionViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    state.keyboardController.wire()

    LaunchedEffect(Unit) {
        viewModel.onEvent(NewTrnEvent.Initial(arg))
    }

    UI(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun BoxScope.UI(
    state: NewTrnState,
    onEvent: (NewTrnEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item(key = "toolbar") {
            SpacerVer(height = 24.dp)
            NewTrnScreenToolbar(
                onClose = {
                    onEvent(NewTrnEvent.Close)
                },
                trnType = state.trnType,
                onChangeTrnType = {
                    state.trnTypeModal.show()
                }
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
                onTitleChange = { onEvent(NewTrnEvent.TitleChange(it)) },
                onCta = { onEvent(NewTrnEvent.Add) }
            )
            TitleSuggestions(
                focused = titleFocused && keyboardShown,
                suggestions = state.titleSuggestions,
                onSuggestionClick = { onEvent(NewTrnEvent.TitleChange(it)) }
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

    AmountAccountSheet(
        amountUi = state.amountUi,
        amount = state.amount,
        amountBaseCurrency = state.amountBaseCurrency,
        account = state.account,
        ctaText = stringResource(R.string.add),
        ctaIcon = R.drawable.ic_round_add_24,
        accountPickerModal = state.accountPickerModal,
        amountModal = state.amountModal,
        onAccountChange = {
            onEvent(NewTrnEvent.AccountChange(it))
        },
        onAmountEnter = {
            onEvent(NewTrnEvent.AmountChange(it))
        },
        onCtaClick = {
            onEvent(NewTrnEvent.Add)
        }
    )

    Modals(state = state, onEvent = onEvent)
}

@Composable
private fun BoxScope.Modals(
    state: NewTrnState,
    onEvent: (NewTrnEvent) -> Unit
) {
    CategoryPickerModal(
        modal = state.categoryPickerModal,
        selected = state.category,
        trnType = state.trnType,
        onPick = {
            onEvent(NewTrnEvent.CategoryChange(it))
        }
    )

    DescriptionModal(
        modal = state.descriptionModal,
        initialDescription = state.description,
        onDescriptionChange = {
            onEvent(NewTrnEvent.DescriptionChange(it))
        }
    )

    TrnTypeModal(
        modal = state.trnTypeModal,
        trnType = state.trnType,
        onTransactionTypeChange = {
            onEvent(NewTrnEvent.TrnTypeChange(it))
        }
    )

    TrnTimeModal(
        modal = state.timeModal,
        trnTime = state.time,
        onTrnTimeChange = {
            onEvent(NewTrnEvent.TrnTimeChange(it))
        }
    )
}

@Composable
private fun NewTrnScreenToolbar(
    onClose: () -> Unit,
    trnType: TransactionType,
    onChangeTrnType: () -> Unit,
) {
    TrnScreenToolbar(
        onClose = onClose,
        actions = {
            IvyButton(
                size = ButtonSize.Small,
                visibility = Visibility.Medium,
                feeling = trnType.feeling(),
                text = trnType.humanText(),
                icon = trnType.icon(),
                onClick = onChangeTrnType,
            )
        }
    )
}


// region Preview
@Preview
@Composable
private fun Preview_Empty() {
    IvyPreview {
        UI(
            state = NewTrnState(
                trnType = TransactionType.Income,
                category = null,
                description = null,
                amountUi = dummyValueUi(),
                amount = dummyValue(),
                amountBaseCurrency = null,
                account = dummyAccountUi(),
                title = null,

                titleSuggestions = emptyList(),

                titleFocus = remember { FocusRequester() },
                keyboardController = KeyboardController(),
                timeUi = dummyTrnTimeActualUi(),
                time = dummyTrnTimeActual(),
                trnTypeModal = rememberIvyModal(),
                categoryPickerModal = rememberIvyModal(),
                accountPickerModal = rememberIvyModal(),
                descriptionModal = rememberIvyModal(),
                timeModal = rememberIvyModal(),
                amountModal = rememberIvyModal(),
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun Preview_Filled() {
    IvyPreview {
        UI(
            state = NewTrnState(
                trnType = TransactionType.Expense,
                title = "Tabu Shisha",
                category = dummyCategoryUi(),
                description = "Lorem ipsum blablablabla okay good test\n1\n2\n",
                amountUi = dummyValueUi(amount = "23.99"),
                amount = dummyValue(amount = 23.99),
                amountBaseCurrency = dummyValueUi(amount = "48.23", currency = "BGN"),
                account = dummyAccountUi(),

                titleSuggestions = emptyList(),

                titleFocus = remember { FocusRequester() },
                keyboardController = KeyboardController(),
                timeUi = dummyTrnTimeDueUi(),
                time = dummyTrnTimeDue(),
                trnTypeModal = rememberIvyModal(),
                categoryPickerModal = rememberIvyModal(),
                accountPickerModal = rememberIvyModal(),
                descriptionModal = rememberIvyModal(),
                timeModal = rememberIvyModal(),
                amountModal = rememberIvyModal(),
            ),
            onEvent = {}
        )
    }
}
// endregion