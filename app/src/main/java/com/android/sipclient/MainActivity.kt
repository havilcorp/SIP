package com.android.sipclient

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.mizuvoip.jvoip.SipStack

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

    }

    fun checkPermissions(){
        val snackbarMultiplePermissionsListener = SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
            .with(findViewById<View>(android.R.id.content).rootView, "Разрешить отображать ваше местоположение?")
            .withOpenSettingsButton("Settings")
            .withCallback(object : Snackbar.Callback() {
                override fun onShown(snackbar: Snackbar) {}
                override fun onDismissed(snackbar: Snackbar, event: Int) {}
            }).build()

        val dialogMultiplePermissionsListener = DialogOnAnyDeniedMultiplePermissionsListener.Builder
            .withContext(this)
            .withTitle("Дать разрешение на ваше местоположение")
            .withMessage("Для того что бы найти вас нам потребуется разрешение на отображение вашего местоположения")
            .withButtonText(android.R.string.ok)
            .withIcon(R.mipmap.ic_launcher)
            .build()

        val permissionListener = object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                Log.d("TAG", report.grantedPermissionResponses.size.toString())
                if(report.grantedPermissionResponses.size == 3){
                    permissionSuccess()
                } else {
                    checkPermissions()
                }
            }
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {}
        }

        val compositePermissionsListener = CompositeMultiplePermissionsListener(snackbarMultiplePermissionsListener, dialogMultiplePermissionsListener, permissionListener)

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.RECEIVE_BOOT_COMPLETED
            )
            .withListener(compositePermissionsListener)
            .check()
    }

    fun permissionSuccess() {
        val sip = SipStack()

        sip.Init (this)



        sip.SetParameter ("serveraddress", "sip.cocos.pro:5060")
        sip.SetParameter("username","590069") //set SIP username
        sip.SetParameter("password","85422368793") //set SIP password
        sip.SetParameter("loglevel","5");

        sip.Start()
    }

}



