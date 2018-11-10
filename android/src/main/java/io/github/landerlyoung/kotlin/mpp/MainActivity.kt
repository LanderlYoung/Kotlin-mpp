package io.github.landerlyoung.kotlin.mpp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import io.github.landerlyoung.kotlin.mpp.io.github.landerlyoung.kotlin.mpp.zhihudaily.ZhihuDailyRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.text).text = createApplicationScreenMessage()

        GlobalScope.launch {
            println(ZhihuDailyRepository.getLatestNews())
            println(ZhihuDailyRepository.getNewsContent(3892357))
        }
    }
}
