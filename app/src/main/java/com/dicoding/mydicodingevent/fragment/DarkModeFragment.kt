package com.dicoding.mydicodingevent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mydicodingevent.R
import com.dicoding.mydicodingevent.ui.DarkModeViewModel
import com.dicoding.mydicodingevent.ui.DarkModeViewModelFactory
import com.dicoding.mydicodingevent.ui.SettingPreferences
import com.dicoding.mydicodingevent.ui.dataStore
import com.google.android.material.switchmaterial.SwitchMaterial

class DarkModeFragment : Fragment() {

    private lateinit var switchTheme: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dark_mode, container, false)

         switchTheme = view.findViewById(R.id.switch_theme)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val mainViewModel = ViewModelProvider(this, DarkModeViewModelFactory(pref)).get(
            DarkModeViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        return view
    }
}
