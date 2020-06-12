package com.kyle.colman.view

import com.kyle.colman.databinding.LayoutRefreshWebviewBinding

/**
 * Author   : kyle
 * Date     : 2020/6/12
 * Function : webview
 */

fun LayoutRefreshWebviewBinding.bindUrl(url: String) {
    refreshLayout.setOnRefreshListener {
        webView.load(url)
    }
    webView.progressCallback = { progresss ->
        if (progresss == 100) {
            refreshLayout.isRefreshing = false
        }
    }
    refreshLayout.isRefreshing = true
}