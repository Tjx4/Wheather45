package com.platform45.weather45.base.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.platform45.weather45.R
import com.platform45.weather45.helpers.showErrorDialog

 abstract class GooglePlayActivity : BaseActivity(), LocationListener {

    protected fun checkPlayServicesAndPermission() {
        checkGoogleApi {
            checkLocationPermissionAndContinue {
                checkGPSAndProceed {
                    isNetworkAvailable {
                        initLocation()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (i in permissions.indices) {
            val permission = permissions[i]
            val grantResult = grantResults[i]

            if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    checkGPSAndProceed {
                        initLocation()
                    }
                } else {
                    onLocationPermissionDenied()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun initLocation() {
        val fusedLocationClient = getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {

                showErrorDialog(
                    this,
                    getString(R.string.unknown_location),
                    getString(R.string.unable_to_get_location),
                    getString(R.string.try_again)
                ) {
                    initLocation()
                }
                return@addOnSuccessListener
            }

            onLocationRequestListenerSuccess(location)
        }

        fusedLocationClient.lastLocation.addOnFailureListener {
            showErrorDialog(
                this,
                getString(R.string.location_error),
                getString(R.string.location_retrieve_error),
                getString(R.string.close)
            ) {
                finish()
            }
        }
    }

    fun checkGoogleApi(onSuccessCallback: () -> Unit = {}) {
    val api = GoogleApiAvailability.getInstance()
    val isAv = api.isGooglePlayServicesAvailable(this)

    if (isAv == ConnectionResult.SUCCESS) {
        onSuccessCallback.invoke()

    } else if (api.isUserResolvableError(isAv)) {
        showErrorDialog(
            this,
            getString(R.string.google_play_error),
            getString(R.string.google_play_error_message),
            getString(R.string.close)
        ) {
            finish()
        }

    } else {
        showErrorDialog(
            this,
            getString(R.string.google_play_error),
            getString(R.string.google_play_error_message),
            getString(R.string.close)
        ) {
            finish()
        }

    }
    }

    fun checkLocationPermissionAndContinue(onSuccessCallback: () -> Unit = {}) {
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
             onSuccessCallback.invoke()
         } else {
             ActivityCompat.requestPermissions(
                 this,
                 arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                 1
             )
         }
     } else {
         onSuccessCallback.invoke()
     }
    }

    fun isGPSOn(context: Context): Boolean {
     return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
         val lm = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
         lm.isLocationEnabled
     } else {
         val mode = Settings.Secure.getInt(
             context.contentResolver, Settings.Secure.LOCATION_MODE,
             Settings.Secure.LOCATION_MODE_OFF
         )
         mode != Settings.Secure.LOCATION_MODE_OFF
     }
    }

    fun checkGPSAndProceed(onSuccessCallback: () -> Unit = {}) {
     if (isGPSOn(this)) {
         onSuccessCallback.invoke()
     } else {
         showErrorDialog(
             this,
             getString(R.string.gps_error_title),
             getString(R.string.gps_error_message),
             getString(R.string.try_again)
         ) {
             checkGPSAndProceed(onSuccessCallback)
         }
     }
    }

    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(onSuccessCallback: () -> Unit = {}) {
     val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
     val isConnected = connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
         .isConnected

     if(isConnected){
         onSuccessCallback.invoke()
     }
     else{
         showErrorDialog(
             this,
             getString(R.string.internet_error),
             getString(R.string.internet_error_message),
             getString(R.string.try_again)
         ) {
             isNetworkAvailable(onSuccessCallback)
         }
     }
    }

    override fun onLocationChanged(location: Location) {}
    abstract fun onLocationPermissionDenied()
    abstract fun onLocationRequestListenerSuccess(location: Location?)
 }