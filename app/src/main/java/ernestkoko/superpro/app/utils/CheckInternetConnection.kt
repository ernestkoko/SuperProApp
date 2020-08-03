

package ernestkoko.superpro.app.utils


import android.annotation.TargetApi
import android.content.Context
import android.net.*
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData


class CheckInternetConnection(private val context: Context) : LiveData<Boolean>() {
    //create a connectivity manager
    private var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    //create a network callback
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun lollipopNetworkRequest() {
//        val requestBuilder = NetworkRequest.Builder()
//            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
//        connectivityManager.registerNetworkCallback(
//            requestBuilder.build(),
//        connectivityManagerCallback())
//
//    }

    //fun that checks if there is connectivity or not
    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        //check if the version is equal to or greater than Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //initialise the network callback
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                //override the onLost network
                override fun onLost(network: Network) {
                    super.onLost(network)
                    //if network lost set the value to false
                    postValue(false)
                }

                //override the onAvailable to know when the network is available
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    //if available set the value to true
                    postValue(true)
                }
            }
            return networkCallback
        } else {
            throw IllegalAccessError("Error")
        }

    }

//    private fun updateNetwork(){
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//    }


//    companion object{
//        fun isConnected(context: Context): Boolean {
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val nw      = connectivityManager.activeNetwork ?: return false
//                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
//                return when {
//                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                    else -> false
//                }
//            } else {
//                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
//                return nwInfo.isConnected
//            }
//        }
//
//    }


}