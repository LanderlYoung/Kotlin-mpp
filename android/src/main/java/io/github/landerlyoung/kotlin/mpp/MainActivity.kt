package io.github.landerlyoung.kotlin.mpp

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

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
