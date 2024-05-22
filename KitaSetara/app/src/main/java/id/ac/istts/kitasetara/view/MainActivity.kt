package id.ac.istts.kitasetara.view

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import id.ac.istts.kitasetara.Helper
import id.ac.istts.kitasetara.R
import id.ac.istts.kitasetara.model.forum.User
import id.ac.istts.kitasetara.pref.UserPreference
import id.ac.istts.kitasetara.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        @Suppress("DEPRECATION")
        //make app become fullscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {//old version
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        //check login status
        val pref = UserPreference.getInstance(dataStore)
        val checkLogin = runBlocking { pref.getSession().first().isLoggedIn }

        // Get the NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        if (checkLogin){
            navController.navigate(R.id.homeFragment)
            val tempUser = runBlocking{ pref.getSession().first() }
            Helper.currentUser = User(tempUser.id,tempUser.username,tempUser.name,tempUser.password,tempUser.email,true,tempUser.imageUrl)
        }else{
            navController.navigate(R.id.loginFragment)
        }
    }

    companion object {
        var postID:String? = null
    }

}