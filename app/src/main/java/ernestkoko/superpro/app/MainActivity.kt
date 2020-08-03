package ernestkoko.superpro.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val nav = findNavController(R.id.nav)
       // val appBar = AppBarConfiguration(setOf(R.id.homeFragment,R.id.loginFragment))
    }
}