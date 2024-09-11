package com.practicum.playlistmaker.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,  //— задержка в миллисекундах
    coroutineScope: CoroutineScope, //— область видимости сопрограммы, в которой будут выполняться задачи;
    useLastParam: Boolean,  //— флаг, указывающий, должна ли функция использовать последний переданный параметр
    action: (T) -> Unit //— функция, вызов которой мы хотим "задебаунсить"
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}