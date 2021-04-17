package com.example.githubuserslist.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuserslist.R
import com.example.githubuserslist.SettingPreference
import com.example.githubuserslist.databinding.ActivityReminderBinding

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.reminder_holder, SettingPreference()).commit()
        setActionBarTitle()
    }
    private fun setActionBarTitle() {
        supportActionBar?.title = getString(R.string.set_reminder)
    }

}