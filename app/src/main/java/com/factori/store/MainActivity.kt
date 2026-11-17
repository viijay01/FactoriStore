package com.factori.store

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var web: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        web = WebView(this)
        setContentView(web)

        // Keep the website below the status bar / above the nav bar
        ViewCompat.setOnApplyWindowInsetsListener(web) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        val storeHost = Uri.parse(BuildConfig.STORE_URL).host!!.removePrefix("www.")

        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true            // keeps the OTP login
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true)
        // lets the store's server know the visit came from the app
        web.settings.userAgentString += " FactoriApp/${BuildConfig.VERSION_NAME}"

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, req: WebResourceRequest): Boolean {
                val url = req.url

                // DEMO BUILD: payment gateway not integrated.
                // In production these UPI links are handed to GPay/PhonePe instead.
                if (url.scheme == "upi" || url.scheme == "intent") {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Demo build")
                        .setMessage("Payment gateway not integrated in this demo. No money was charged.")
                        .setPositiveButton("OK", null)
                        .show()
                    return true
                }

                // Pages of this store stay inside the app
                if ((url.scheme == "http" || url.scheme == "https") &&
                    url.host?.endsWith(storeHost) == true) return false

                // WhatsApp, tel:, mailto:, Instagram, other sites -> open outside
                try { startActivity(Intent(Intent.ACTION_VIEW, url)) } catch (e: Exception) { }
                return true
            }
        }

        // Back button = back in the website, exit only on the first page
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (web.canGoBack()) web.goBack() else finish()
            }
        })

        if (savedInstanceState == null) web.loadUrl(BuildConfig.STORE_URL)
        else web.restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        web.saveState(outState)
    }

    override fun onPause() {
        super.onPause()
        CookieManager.getInstance().flush()               // save login to disk
    }
}
