package com.mr28.myip

import android.R.attr.alertDialogIcon
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.Formatter
import android.view.View
import android.widget.Toast
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = " Get  My IP Address"
        titleColor = R.color.myColor1;
        setContentView(R.layout.activity_main)
        GetBut.setOnClickListener(){
            if(isOnline(this)==true){
                doAsync{getMyPubIP()}
                doAsync{getLocalIP()}
            }
        }
        localip.setOnClickListener(){
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("GG", localip.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Local Ip Addr Copied", Toast.LENGTH_SHORT).show();
        }
        publicip.setOnClickListener(){
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("GG", publicip.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Public Addr Copied", Toast.LENGTH_SHORT).show();
        }
    }
    fun getMyPubIP() {
        var pubIP= ""
        val connection = URL("https://api.ipify.org?format=json").openConnection() as HttpURLConnection
        try {
            val data = connection.inputStream.bufferedReader().use { it.readText() }
            var json = JSONObject(data)
            pubIP = json.getString("ip").toString()
        } finally {
            connection.disconnect()
        }
        publicip.text = pubIP
    }
    fun getLocalIP(){
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        localip.text = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }
    fun ipToString(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)

    }
}