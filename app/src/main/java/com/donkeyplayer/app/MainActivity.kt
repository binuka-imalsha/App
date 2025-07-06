package com.donkeyplayer.app

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.donkeyplayer.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private var fileUploadCallback: ValueCallback<Array<Uri>>? = null
    private val donkeyToUrl = "https://donkey.to"
    
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = WebChromeClient.FileChooserParams.parseResult(result.resultCode, data)
            fileUploadCallback?.onReceiveValue(results)
        } else {
            fileUploadCallback?.onReceiveValue(null)
        }
        fileUploadCallback = null
    }
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            setupWebView()
        } else {
            Toast.makeText(this, "Storage permission is required for file uploads", Toast.LENGTH_LONG).show()
            setupWebView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        checkPermissions()
        setupSwipeRefresh()
        setupToolbar()
    }
    
    private fun checkPermissions() {
        val permissionsNeeded = mutableListOf<String>()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_VIDEO)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        
        if (permissionsNeeded.isNotEmpty()) {
            permissionLauncher.launch(permissionsNeeded.toTypedArray())
        } else {
            setupWebView()
        }
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            }
        }
        
        binding.refreshButton.setOnClickListener {
            binding.webView.reload()
        }
        
        binding.homeButton.setOnClickListener {
            binding.webView.loadUrl(donkeyToUrl)
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.webView.reload()
        }
        
        // Material 3 colors
        binding.swipeRefresh.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webSettings = binding.webView.settings
        
        // Enable JavaScript and other web features
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.setSupportZoom(true)
        webSettings.defaultTextEncodingName = "utf-8"
        
        // Enable mixed content for HTTPS sites loading HTTP content
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        
        // Improve performance
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.setAppCacheEnabled(true)
        webSettings.databaseEnabled = true
        
        // User agent for better compatibility
        webSettings.userAgentString = "Mozilla/5.0 (Linux; Android ${Build.VERSION.RELEASE}; ${Build.MODEL}) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
        
        binding.webView.webViewClient = CustomWebViewClient()
        binding.webView.webChromeClient = CustomWebChromeClient()
        
        // Load the website
        binding.webView.loadUrl(donkeyToUrl)
    }
    
    private inner class CustomWebViewClient : WebViewClient() {
        
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url = request?.url?.toString() ?: return false
            
            // Allow navigation within donkey.to domain
            if (url.contains("donkey.to")) {
                return false
            }
            
            // Block known ad domains
            if (isAdUrl(url)) {
                return true
            }
            
            return false
        }
        
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.loadingProgress.visibility = android.view.View.VISIBLE
            binding.swipeRefresh.isRefreshing = true
        }
        
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.loadingProgress.visibility = android.view.View.GONE
            binding.swipeRefresh.isRefreshing = false
            
            // Inject CSS to hide ads and improve UI
            injectAdBlockingCSS()
            injectMobileOptimizations()
        }
        
        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            val url = request?.url?.toString() ?: return null
            
            // Block ad requests
            if (isAdUrl(url)) {
                return WebResourceResponse("text/plain", "UTF-8", null)
            }
            
            return super.shouldInterceptRequest(view, request)
        }
        
        private fun isAdUrl(url: String): Boolean {
            val adDomains = listOf(
                "doubleclick.net",
                "googleadservices.com",
                "googlesyndication.com",
                "googletagmanager.com",
                "google-analytics.com",
                "facebook.com/tr",
                "ads.yahoo.com",
                "adsystem.com",
                "adsservice.google",
                "popads.net",
                "popcash.net",
                "propellerads.com",
                "adnxs.com",
                "adsrvr.org",
                "outbrain.com",
                "taboola.com"
            )
            
            return adDomains.any { url.contains(it, ignoreCase = true) }
        }
        
        private fun injectAdBlockingCSS() {
            val css = """
                javascript:(function() {
                    var style = document.createElement('style');
                    style.textContent = `
                        /* Hide common ad selectors */
                        .ad, .ads, .advertisement, .popup, .banner, .sponsored,
                        [class*="ad-"], [class*="ads-"], [id*="ad-"], [id*="ads-"],
                        [class*="popup"], [id*="popup"], [class*="banner"], [id*="banner"] {
                            display: none !important;
                            visibility: hidden !important;
                        }
                        
                        /* Hide overlay popups */
                        div[style*="position: fixed"], div[style*="position:fixed"],
                        div[style*="z-index: 999"], div[style*="z-index:999"] {
                            display: none !important;
                        }
                        
                        /* Improve mobile experience */
                        body {
                            margin: 0 !important;
                            padding: 0 !important;
                        }
                    `;
                    document.head.appendChild(style);
                })();
            """.trimIndent()
            
            binding.webView.evaluateJavascript(css, null)
        }
        
        private fun injectMobileOptimizations() {
            val js = """
                javascript:(function() {
                    // Remove overlay popups
                    var overlays = document.querySelectorAll('div[style*="position: fixed"], div[style*="position:fixed"]');
                    overlays.forEach(function(overlay) {
                        if (overlay.style.zIndex > 100) {
                            overlay.remove();
                        }
                    });
                    
                    // Enable file upload for subtitles
                    var fileInputs = document.querySelectorAll('input[type="file"]');
                    fileInputs.forEach(function(input) {
                        input.accept = '.srt,.vtt,.ass,.sub';
                    });
                })();
            """.trimIndent()
            
            binding.webView.evaluateJavascript(js, null)
        }
    }
    
    private inner class CustomWebChromeClient : WebChromeClient() {
        
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            binding.loadingProgress.progress = newProgress
            
            if (newProgress == 100) {
                binding.loadingProgress.visibility = android.view.View.GONE
            }
        }
        
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            fileUploadCallback = filePathCallback
            
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                val mimeTypes = arrayOf(
                    "text/plain",
                    "text/srt",
                    "text/vtt",
                    "application/x-subrip",
                    "video/*",
                    "audio/*"
                )
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
            
            val chooserIntent = Intent.createChooser(intent, "Select File")
            filePickerLauncher.launch(chooserIntent)
            
            return true
        }
        
        override fun onPermissionRequest(request: PermissionRequest?) {
            // Grant permissions for video/audio playback
            request?.grant(request.resources)
        }
    }
    
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onDestroy() {
        binding.webView.destroy()
        super.onDestroy()
    }
}