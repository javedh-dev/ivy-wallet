package com.ivy.menu

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ivy.design.l0_system.UI
import com.ivy.design.l1_buildingBlocks.ColumnRoot
import com.ivy.design.l1_buildingBlocks.SpacerVer
import com.ivy.design.l1_buildingBlocks.SpacerWeight
import com.ivy.design.l2_components.modal.CloseButton
import com.ivy.design.l3_ivyComponents.Feeling
import com.ivy.design.l3_ivyComponents.Visibility
import com.ivy.design.l3_ivyComponents.button.ButtonSize
import com.ivy.design.l3_ivyComponents.button.IvyButton
import com.ivy.design.util.IvyPreview
import com.ivy.design.util.hiltViewModelPreviewSafe

@Composable
fun HomeMoreMenu(
    visible: Boolean,
    onMenuClose: () -> Unit
) {
    val viewModel: HomeMoreMenuViewModel? = hiltViewModelPreviewSafe()

    AnimatedVisibility(
        modifier = Modifier.zIndex(10_000f),
        visible = visible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it },
    ) {
        BackHandler(enabled = visible) {
            onMenuClose()
        }

        ColumnRoot(
            modifier = Modifier
                .background(UI.colors.pure)
        ) {
            SpacerWeight(weight = 1f)
            IvyButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                size = ButtonSize.Big,
                visibility = Visibility.Medium,
                feeling = Feeling.Positive,
                text = "Categories",
                icon = null
            ) {
                viewModel?.onEvent(MoreMenuEvent.CategoriesClick)
            }
            SpacerVer(height = 16.dp)
            IvyButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                size = ButtonSize.Big,
                visibility = Visibility.Medium,
                feeling = Feeling.Positive,
                text = "Settings",
                icon = null
            ) {
                viewModel?.onEvent(MoreMenuEvent.SettingsClick)
            }
            SpacerVer(height = 16.dp)
            SpacerWeight(weight = 1f)
            CloseButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onMenuClose
            )
            SpacerVer(height = 48.dp)
        }
    }
}


@Preview
@Composable
private fun HomeMoreMenuPreview() {
    IvyPreview {
        HomeMoreMenu(
            visible = true,
            onMenuClose = {}
        )
    }
}
