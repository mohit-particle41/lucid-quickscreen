package com.lucidhearing.lucidquickscreen.utils.constants

import android.graphics.Color

object GraphConstants{
    const val LABEL_LEFT = "Left"
    const val LABEL_RIGHT = "Right"
    const val AXIS_LEFT = true
    const val AXIS_RIGHT = false
    const val AXIS_X = true
    const val DRAW_GRID_LINE = false
    const val DASHED_GRID_LINE_LENGTH = 3f
    const val DASHED_GRID_LINE_SPACE = 3f
    const val DASHED_GRID_LINE_PHASE = 0f
    const val ANIMATE_X_DURATION_MS = 2500
    const val ANIMATE_Y_DURATION_MS = 1500
    const val LEGEND = false
    const val DESCRIPTION = false
    const val TOUCH = false
    const val DRAG = false
    const val SCALE = false
    const val PINCH = false

    const val AXIS_X_MINIMUM = 1.0f
    const val AXIS_X_MAXIMUM = 5.0f
    const val AXIS_X_GRANULARITY = 1f
    const val AXIS_X_LABEL_COUNT = 5
    const val AXIS_X_TEXT_COLOR = Color.WHITE
    const val AXIS_X_TEXT_SIZE = 11f

    const val AXIS_LEFT_MINIMUM = -20.0f
    const val AXIS_LEFT_MAXIMUM = 20.0f
    const val AXIS_LEFT_GRANULARITY = 1f
    const val AXIS_LEFT_LABEL_COUNT = 5
    const val AXIS_LEFT_TEXT_COLOR = Color.WHITE
    const val AXIS_LEFT_TEXT_SIZE = 11f

    val DATA_LINE_COLOR_LEFT = Color.rgb(133,179,239)
    val DATA_LINE_COLOR_RIGHT = Color.rgb(230, 110, 110)
    const val DATA_LINE_VALUE_TEXT_COLOR = Color.GRAY
    const val DATA_LINE_SET_DRAW_VALUES = false
    const val DATA_LINE_WIDTH = 2f
    const val DATA_LINE_VALUE_HIGHLIGHT_COLOR_LEFT = Color.RED
    const val DATA_LINE_VALUE_HIGHLIGHT_COLOR_RIGHT = Color.BLUE
    const val DATA_LINE_HIGHLIGHT_ENABLED = false
    const val DATA_LINE_HIGHLIGHT_INDICATORS = false

}