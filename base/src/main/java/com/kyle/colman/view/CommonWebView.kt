package com.kyle.colaman.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.widget.FrameLayout
import com.blankj.utilcode.util.GsonUtils
import com.kyle.colman.R
import com.kyle.colman.view.KActivity
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.layout_common_webview.view.*


/**
 *
 *     author : kyle
 *     time   : 2019/10/21
 *     desc   : 通用配置的webview
 *
 */
class CommonWebView : FrameLayout {

    var url = ""

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_common_webview, null)
        addView(view)
        initDefaultConfig()
        refresh_layout.setOnRefreshListener {
            web_view.loadUrl(url)
        }
    }

    /**
     * 加载网站
     * @param url String
     * @return CommonWebView
     */
    fun load(url: String): CommonWebView {
        this.url = url
        web_view?.loadUrl(url)
        return this
    }

    /**
     * 绑定activty
     * @param activity BaseActivity<*>
     * @return CommonWebView
     */
    fun bindActivity(activity: KActivity<*>): CommonWebView {
        activity.addBackpressInterceptor({ activity ->
            if (web_view.canGoBack()) {
                web_view.goBack()
                true
            }
            false
        })
        return this
    }

    /**
     * 调用JS
     * @param builder JSBuilder js参数builder
     */
    fun callJs(builder: JSBuilder) {
        web_view.evaluateJavascript("javascript:${builder.method}(${builder.params})") { value ->
            builder.callback?.invoke(value)
        }
    }

    /**
     * 设置一些默认的属性
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initDefaultConfig() {
        web_view.settings?.run {
            javaScriptEnabled = true;                    //支持Javascript 与js交互
            javaScriptCanOpenWindowsAutomatically = true;//支持通过JS打开新窗口
            allowFileAccess = true;                      //设置可以访问文件
            this.setSupportZoom(true);                          //支持缩放
            builtInZoomControls = true;                  //设置内置的缩放控件
            useWideViewPort = true;                      //自适应屏幕
            setSupportMultipleWindows(true);               //多窗口
            defaultTextEncodingName = "utf-8";            //设置编码格式
            setAppCacheEnabled(true);
            domStorageEnabled = true;
            this.setAppCacheMaxSize(Long.MAX_VALUE);
            cacheMode = WebSettings.LOAD_NO_CACHE;       //缓存模式
        }
        web_view.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                p0: WebView?,
                sslErrorHandler: SslErrorHandler?,
                p2: com.tencent.smtt.export.external.interfaces.SslError?
            ) {
                //忽略SSL证书错误
                sslErrorHandler?.proceed()
            }
        }

        web_view.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(p0: com.tencent.smtt.sdk.WebView?, progress: Int) {
                super.onProgressChanged(p0, progress)
                progress_bar.progress = progress
                progress_bar.visibility = if (progress != 100) View.VISIBLE else View.GONE
                if (progress == 100) {
                    progress_bar.visibility = View.GONE
                    if (refresh_layout.isRefreshing) {
                        refresh_layout.isRefreshing = false
                    }
                } else {
                    progress_bar.visibility = View.VISIBLE
                }
            }
        }
    }


    /**
     * 用于构建JS语句
     * @property method String JS方法名
     * @property params String JS参数
     * @property callback Function1<String, Unit?>?  Js回调
     */
    class JSBuilder {
        var method = ""
        var params = ""
        var callback: ((String) -> Unit?)? = null

        /**
         * 添加方法名
         * @param methodName String
         */
        fun method(methodName: String): JSBuilder {
            method = methodName
            return this
        }

        /**
         * 添加参数
         * @param value Any
         */
        fun addParam(value: Any): JSBuilder {
            if (params.isNotBlank()) {
                params += ","
            }
            when (value) {
                is String -> params += "'$value'"
                is Number -> params += "$value"
                else -> params += "'${GsonUtils.toJson(value)}'"
            }
            return this
        }

        /**
         * 设置调用js之后的结果回调，由js返回
         * @param callback Function1<String, Unit>
         */
        fun callback(callback: (String) -> Unit): JSBuilder {
            this.callback = callback
            return this
        }
    }
}