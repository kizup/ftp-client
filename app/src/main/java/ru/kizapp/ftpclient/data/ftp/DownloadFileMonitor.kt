package ru.kizapp.ftpclient.data.ftp

import com.jcraft.jsch.SftpProgressMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class DownloadFileMonitor @Inject constructor() : SftpProgressMonitor {

    private var cancelled = false
    private var loadedBytes = 0L
    private var maxBytes = 0L
    private var percent = 0

    private val loadingPercentFlow by lazy { MutableStateFlow(percent) }

    val percentFlow: Flow<Int>
        get() = loadingPercentFlow

    override fun init(op: Int, src: String?, dest: String?, max: Long) {
        cancelled = false
        maxBytes = max
    }

    override fun count(count: Long): Boolean {
        loadedBytes += count
        val percent = (loadedBytes.toFloat() / maxBytes) * 100
        if (percent > this.percent) {
            this.percent = percent.toInt()
            loadingPercentFlow.tryEmit(this.percent)
        }
        return !cancelled
    }

    override fun end() {
        loadedBytes = 0L
        maxBytes = 0L
        cancelled = false
        percent = 0
    }

    fun cancel() {
        loadedBytes = 0L
        cancelled = true
        maxBytes = 0L
        percent = 0
    }
}