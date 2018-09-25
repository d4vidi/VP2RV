package com.d4vidi.vp2rv.core

sealed class RVPageScrollState {
    class Idle: RVPageScrollState()
    class Dragging: RVPageScrollState()
    class Settling: RVPageScrollState()
}