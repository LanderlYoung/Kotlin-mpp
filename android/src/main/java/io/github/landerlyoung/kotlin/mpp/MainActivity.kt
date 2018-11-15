package io.github.landerlyoung.kotlin.mpp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Snackbar.make(
                findViewById<TextView>(R.id.activity_content),
                createApplicationScreenMessage(),
                Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            if (supportFragmentManager.popBackStackImmediate()) {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
