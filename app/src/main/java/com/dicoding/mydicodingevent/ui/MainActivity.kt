package com.dicoding.mydicodingevent.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mydicodingevent.R
import com.dicoding.mydicodingevent.databinding.ActivityMainBinding
import com.dicoding.mydicodingevent.fragment.DarkModeFragment
import com.dicoding.mydicodingevent.fragment.EventFragment
import com.dicoding.mydicodingevent.fragment.FinishedEventFragment
import com.dicoding.mydicodingevent.fragment.UpcomingEventFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var darkModeViewModel: DarkModeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        supportActionBar?.hide()
        setupBottomNavigation()

        if (savedInstanceState == null) {
            loadFragment(UpcomingEventFragment())
        }

        val pref = SettingPreferences.getInstance(applicationContext.dataStore)
        val factory = DarkModeViewModelFactory(pref)
        darkModeViewModel = ViewModelProvider(this, factory).get(DarkModeViewModel::class.java)

        darkModeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setupBottomNavigation() {
        binding.navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.upcoming_event_fragment -> {
                    loadFragment(UpcomingEventFragment())
                    true
                }
                R.id.finished_event_fragment -> {
                    loadFragment(FinishedEventFragment())
                    true
                }
                R.id.event_fragment -> {
                    loadFragment(EventFragment())
                    true
                }
                R.id.dark_mode_fragment -> {
                    loadFragment(DarkModeFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}
