package com.codetech.insta.inappupdate

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

fun String.println(){
    Log.d("inAppUpdateInfo", this)
}

class MainActivity : AppCompatActivity() {
    private val inAppUpdateLauncher=registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
        // handle callback
        if (result.resultCode != RESULT_OK) {
            "Update flow failed! Result code: ${result.resultCode}".println()
            // If the update is canceled or fails,
            // you can request to start the update again.
            finish()
        }
    }
    private val inAppUpdateManager= AppUpdateManagerFactory.create(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        //Immediate Update
        checkForImmediateUpdate()
        //Flexible Update
        //checkForFlexibleUpdate()
    }
    private fun checkForFlexibleUpdate(){
        try {
            inAppUpdateManager.appUpdateInfo.addOnSuccessListener {appUpdateInfo->
                if (appUpdateInfo.updateAvailability()
                    ==UpdateAvailability.UPDATE_AVAILABLE&&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                    inAppUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // an activity result launcher registered via registerForActivityResult
                        inAppUpdateLauncher,
                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                        // flexible updates.
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                    )
                    "Update Available ${appUpdateInfo.availableVersionCode()}".println()
                }
            }
        }catch (e:IntentSender.SendIntentException){
            e.printStackTrace()
            e.message?.println()
        }catch (e:Exception){
            e.printStackTrace()
            e.message?.println()
        }
    }
    private fun checkForImmediateUpdate(){
        try {
            inAppUpdateManager.appUpdateInfo.addOnSuccessListener {appUpdateInfo->
                if (appUpdateInfo.updateAvailability()
                    ==UpdateAvailability.UPDATE_AVAILABLE&&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    inAppUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // an activity result launcher registered via registerForActivityResult
                        inAppUpdateLauncher,
                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                        // flexible updates.
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                    "Update Available ${appUpdateInfo.availableVersionCode()}".println()
                }
            }
        }catch (e:IntentSender.SendIntentException){
            e.printStackTrace()
            e.message?.println()
        }catch (e:Exception){
            e.printStackTrace()
            e.message?.println()
        }
    }
}
