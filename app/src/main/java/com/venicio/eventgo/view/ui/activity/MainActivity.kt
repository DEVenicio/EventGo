package com.venicio.eventgo.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.venicio.eventgo.R
import com.venicio.eventgo.databinding.ActivityMainBinding
import com.venicio.eventgo.view.ui.framgents.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        if (savedInstanceState == null) {

            val fragManager = supportFragmentManager
            val fragTransaction = fragManager.beginTransaction()
            val fragHome = HomeFragment()

            fragTransaction.replace(R.id.container_root, fragHome, "HomeFragment")
            fragTransaction.commit()
        }

        setContentView(binding.root)
    }

}