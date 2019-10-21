package com.colaman.kyle.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.GsonUtils
import com.colaman.kyle.base.BaseActivity
import com.colaman.kyle.databinding.LayoutCommonWebviewBinding
import com.colaman.kyle.impl.IBackpressInterceptor
import com.google.gson.Gson
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


/**
 *
 *     author : kyle
 *     time   : 2019/10/21
 *     desc   : 通用配置的webview
 *
 */
class CommonWebView : FrameLayout {
    lateinit var binding: LayoutCommonWebviewBinding
    val webview by lazy {
        binding.webView
    }
    var url = ""

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        val view = LayoutInflater.from(context)
            .inflate(com.colaman.kyle.R.layout.layout_common_webview, null)
        binding = DataBindingUtil.bind<LayoutCommonWebviewBinding>(view)!!
        addView(view)
        initDefaultConfig()
        binding.refreshLayout.setOnRefreshListener {
            webview.loadUrl(url)

        }
    }

    /**
     * 加载网站
     * @param url String
     * @return CommonWebView
     */
    fun load(url: String): CommonWebView {
        this.url = url
        webview?.loadUrl(url)
        return this
    }

    /**
     * 绑定activty
     * @param activity BaseActivity<*>
     * @return CommonWebView
     */
    fun bindActivity(activity: BaseActivity<*>): CommonWebView {
        activity.addBackpressInterceptor(object : IBackpressInterceptor {
            override fun OnInterceptor(activity: BaseActivity<*>): Boolean {
                if (webview.canGoBack()) {
                    webview.goBack()
                    return true
                }
                return false
            }
        })
        return this
    }

    /**
     * 调用JS
     * @param builder JSBuilder js参数builder
     */
    fun callJs(builder: JSBuilder) {
        webview.evaluateJavascript("javascript:${builder.method}(${builder.params})") { value ->
            builder.callback?.invoke(value)
        }
    }

    /**
     * 设置一些默认的属性
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initDefaultConfig() {
        webview.settings?.run {
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
        webview.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                p0: WebView?,
                sslErrorHandler: SslErrorHandler?,
                p2: SslError?
            ) {
                //忽略SSL证书错误
                sslErrorHandler?.proceed()
            }
        }

        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webview: WebView?, progress: Int) {
                super.onProgressChanged(webview, progress)
                binding.progressBar.progress = progress
                binding.progressBar.visibility = if (progress != 100) View.VISIBLE else View.GONE
                if (progress == 100) {
                    binding.progressBar.visibility = View.GONE
                    if (binding.refreshLayout.isRefreshing) {
                        binding.refreshLayout.isRefreshing = false
                    }
                } else {
                    binding.progressBar.visibility = View.VISIBLE
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