package com.ivy.core.ui.amount.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ivy.core.ui.amount.util.rememberDecimalSeparator
import com.ivy.design.l0_system.UI
import com.ivy.design.l0_system.color.rememberContrast
import com.ivy.design.l1_buildingBlocks.*
import com.ivy.design.l3_ivyComponents.button.ButtonFeeling
import com.ivy.design.l3_ivyComponents.button.ButtonVisibility
import com.ivy.design.l3_ivyComponents.button.toColor
import com.ivy.design.util.ComponentPreview
import com.ivy.design.util.thenWhen
import com.ivy.math.calculator.CalculatorOperator
import com.ivy.resources.R

// region Customize UI
private const val keypadOuterWeight = 1f
private const val keypadInnerWeight = 0.2f
private val keypadButtonBig = 90.dp
private val keypadButtonSmall = 82.dp
private val keyboardVerticalMargin = 12.dp
// endregion

@Suppress("unused")
@Composable
internal fun ColumnScope.Keyboard(
    calculatorVisible: Boolean,
    onCalculatorEvent: (CalculatorOperator) -> Unit,
    onNumberEvent: (Int) -> Unit,
    onDecimalSeparator: () -> Unit,
    onBackspace: () -> Unit,
    onCalculatorC: () -> Unit,
    onCalculatorEquals: () -> Unit,
) {
    val keypadBtnSize by animateDpAsState(
        targetValue = if (calculatorVisible)
            keypadButtonSmall else keypadButtonBig
    )
    CalculatorTopRow(
        calculatorVisible = calculatorVisible,
        keypadBtnSize = keypadBtnSize,
        onCalculatorEvent = onCalculatorEvent,
        onCalculatorC = onCalculatorC,
    )
    // margin is built-in in calculator's top row
    KeyboardRow {
        SpacerWeight(weight = keypadOuterWeight)
        KeypadButton(symbol = "7", size = keypadBtnSize, onClick = { onNumberEvent(7) })
        SpacerWeight(weight = keypadInnerWeight)
        KeypadButton(symbol = "8", size = keypadBtnSize, onClick = { onNumberEvent(8) })
        SpacerWeight(weight = keypadInnerWeight)
        KeypadButton(symbol = "9", size = keypadBtnSize, onClick = { onNumberEvent(9) })
        AnimatedCalculatorButton(
            calculatorVisible = calculatorVisible,
            symbol = "*",
            onClick = { onCalculatorEvent(CalculatorOperator.Multiply) }
        )
        SpacerWeight(weight = keypadOuterWeight)
    }
    SpacerVer(height = keyboardVerticalMargin)
    KeyboardRow {
        SpacerWeight(weight = keypadOuterWeight)
        KeypadButton(symbol = "4", size = keypadBtnSize, onClick = { onNumberEvent(4) })
        SpacerWeight(weight = keypadInnerWeight)
        KeypadButton(symbol = "5", size = keypadBtnSize, onClick = { onNumberEvent(5) })
        SpacerWeight(weight = keypadInnerWeight)
        KeypadButton(symbol = "6", size = keypadBtnSize, onClick = { onNumberEvent(6) })
        AnimatedCalculatorButton(
            calculatorVisible = calculatorVisible,
            symbol = "-",
            onClick = { onCalculatorEvent(CalculatorOperator.Minus) }
        )
        SpacerWeight(weight = keypadOuterWeight)
    }
    SpacerVer(height = keyboardVerticalMargin)
    KeyboardRow {
        SpacerWeight(weight = keypadOuterWeight)
        KeypadButton(symbol = "1", size = keypadBtnSize, onClick = { onNumberEvent(1) })
        SpacerWeight(weight = keypadInnerWeight)
        KeypadButton(symbol = "2", size = keypadBtnSize, onClick = { onNumberEvent(2) })
        SpacerWeight(weight = keypadInnerWeight)
        KeypadButton(symbol = "3", size = keypadBtnSize, onClick = { onNumberEvent(3) })
        AnimatedCalculatorButton(
            calculatorVisible = calculatorVisible,
            symbol = "+",
            onClick = { onCalculatorEvent(CalculatorOperator.Plus) }
        )
        SpacerWeight(weight = keypadOuterWeight)
    }
    SpacerVer(height = keyboardVerticalMargin)
    KeyboardRow {
        SpacerWeight(weight = keypadOuterWeight)
        KeypadButton(
            symbol = rememberDecimalSeparator().toString(),
            size = keypadBtnSize,
            onClick = onDecimalSeparator
        )
        SpacerWeight(weight = keypadInnerWeight)
        KeypadButton(symbol = "0", size = keypadBtnSize, onClick = { onNumberEvent(0) })
        SpacerWeight(weight = keypadInnerWeight)
        BackSpaceButton(
            size = keypadBtnSize,
            onClick = onBackspace,
            onLongClick = onCalculatorC
        )
        AnimatedCalculatorButton(
            calculatorVisible = calculatorVisible,
            symbol = "=",
            feeling = ButtonFeeling.Positive,
            onClick = onCalculatorEquals,
        )
        SpacerWeight(weight = keypadOuterWeight)
    }
}

// region Calculator
@Composable
private fun CalculatorTopRow(
    calculatorVisible: Boolean,
    keypadBtnSize: Dp,
    onCalculatorEvent: (CalculatorOperator) -> Unit,
    onCalculatorC: () -> Unit,
) {
    AnimatedVisibility(
        visible = calculatorVisible,
        enter = expandHorizontally() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        KeyboardRow(
            modifier = Modifier.padding(bottom = keyboardVerticalMargin)
        ) {
            SpacerWeight(weight = keypadOuterWeight)
            KeypadButton(
                symbol = "C",
                size = keypadBtnSize,
                visibility = ButtonVisibility.High,
                feeling = ButtonFeeling.Negative,
                onClick = onCalculatorC,
            )
            SpacerWeight(weight = keypadInnerWeight)
            KeypadButton(
                symbol = "( )",
                visibility = ButtonVisibility.High,
                feeling = ButtonFeeling.Positive,
                size = keypadBtnSize,
                onClick = { onCalculatorEvent(CalculatorOperator.Brackets) }
            )
            SpacerWeight(weight = keypadInnerWeight)
            KeypadButton(
                symbol = "%",
                visibility = ButtonVisibility.High,
                feeling = ButtonFeeling.Positive,
                size = keypadBtnSize,
                onClick = { onCalculatorEvent(CalculatorOperator.Percent) }
            )
            SpacerWeight(weight = keypadInnerWeight)
            KeypadButton(
                symbol = "/",
                size = keypadBtnSize,
                visibility = ButtonVisibility.High,
                feeling = ButtonFeeling.Positive,
                onClick = { onCalculatorEvent(CalculatorOperator.Divide) }
            )
            SpacerWeight(weight = keypadOuterWeight)
        }
    }
}

@Composable
private fun RowScope.AnimatedCalculatorButton(
    calculatorVisible: Boolean,
    symbol: String,
    modifier: Modifier = Modifier,
    feeling: ButtonFeeling = ButtonFeeling.Positive,
    onClick: () -> Unit
) {
    if (calculatorVisible) {
        SpacerWeight(weight = keypadInnerWeight)
    }
    AnimatedVisibility(
        visible = calculatorVisible,
        enter = expandHorizontally() + fadeIn(),
        exit = shrinkHorizontally() + fadeOut(),
    ) {
        KeypadButton(
            modifier = modifier,
            feeling = feeling,
            visibility = ButtonVisibility.High,
            size = keypadButtonSmall,
            symbol = symbol,
            onClick = onClick
        )
    }
}
// endregion

@Composable
private fun KeyboardRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

// region Keypad Buttons
@Composable
private fun KeypadButton(
    symbol: String,
    size: Dp,
    modifier: Modifier = Modifier,
    visibility: ButtonVisibility = ButtonVisibility.Medium,
    feeling: ButtonFeeling = ButtonFeeling.Positive,
    onClick: () -> Unit
) {
    KeypadButtonBox(
        modifier = modifier,
        feeling = feeling,
        visibility = visibility,
        size = size,
        onClick = onClick
    ) {
        B1Second(
            text = symbol,
            color = when (visibility) {
                ButtonVisibility.Focused,
                ButtonVisibility.High ->
                    rememberContrast(feeling.toColor())
                ButtonVisibility.Medium,
                ButtonVisibility.Low -> UI.colorsInverted.pure
            },
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BackSpaceButton(
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    KeypadButtonBox(
        modifier = modifier,
        feeling = ButtonFeeling.Negative,
        size = size,
        onClick = onClick,
        onLongClick = onLongClick
    ) {
        IconRes(
            icon = R.drawable.outline_backspace_24,
            tint = UI.colorsInverted.pure,
        )
    }
}

@Composable
private fun KeypadButtonBox(
    feeling: ButtonFeeling,
    size: Dp,
    modifier: Modifier = Modifier,
    visibility: ButtonVisibility = ButtonVisibility.Medium,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(UI.shapes.circle)
            .hapticClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(all = 4.dp)
            .thenWhen {
                when (visibility) {
                    ButtonVisibility.Focused,
                    ButtonVisibility.High -> background(
                        color = feeling.toColor(),
                        shape = UI.shapes.circle
                    )
                    ButtonVisibility.Low,
                    ButtonVisibility.Medium -> border(
                        width = 1.dp,
                        color = feeling.toColor(),
                        shape = UI.shapes.circle
                    )
                }
            },
        contentAlignment = Alignment.Center,
        content = content
    )
}
// endregion


// region Previews
@Preview
@Composable
private fun Preview() {
    ComponentPreview {
        Column {
            Keyboard(
                calculatorVisible = false,
                onCalculatorEvent = {},
                onNumberEvent = {},
                onDecimalSeparator = {},
                onBackspace = {},
                onCalculatorC = {},
                onCalculatorEquals = {}
            )
        }
    }
}

@Preview
@Composable
private fun Preview_calculator_visible() {
    ComponentPreview {
        Column {
            Keyboard(
                calculatorVisible = false,
                onCalculatorEvent = {},
                onNumberEvent = {},
                onDecimalSeparator = {},
                onBackspace = {},
                onCalculatorC = {},
                onCalculatorEquals = {}
            )
        }
    }
}
// endregion