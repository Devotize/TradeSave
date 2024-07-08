package com.tradesave.list.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, V> Flow<List<T>>.mapList(mapper: (T) -> V): Flow<List<V>> =
    map { it.map(mapper) }