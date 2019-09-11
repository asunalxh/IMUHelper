package com.example.imuhelper.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.imuhelper.R
import com.example.imuhelper.activities.ScoreList
import com.example.imuhelper.utils.logout
import com.google.android.material.navigation.NavigationView

class MineFragment : Fragment() {

    private lateinit var navigationView: NavigationView
    private lateinit var logOutBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_mine, container, false)
        navigationView = view.findViewById(R.id.mine_navigation_view)
        navigationView.itemIconTintList = null
        logOutBtn = view.findViewById(R.id.mine_logOut_Btn)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_mine_score -> {
                    var intent = Intent(activity, ScoreList::class.java)
                    startActivity(intent)
                    true
                }
            }
            false
        }
        logOutBtn.setOnClickListener {
            Thread(Runnable {
                logout()
            }).start()
        }

        return view
    }
}