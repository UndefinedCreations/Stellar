package com.undefined.stellar

import java.util.concurrent.Executor

interface ExecutorService {
    fun setExecutor(executor: Executor)
    fun execute(runnable: Runnable)
}