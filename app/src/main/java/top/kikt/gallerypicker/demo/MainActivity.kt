package top.kikt.gallerypicker.demo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import top.kikt.gallerypicker.GalleryOption
import top.kikt.gallerypicker.GalleryPicker
import top.kikt.gallerypicker.logi

class MainActivity : AppCompatActivity() {
    private val picker = GalleryPicker(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addContentView(Button(this).apply {
            this.text = "请求权限"
            setOnClickListener {
                picker.requestPermission()
            }
        }, ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))


        addContentView(Button(this).apply {
            this.text = "开启"
            setOnClickListener {
                picker.openWithOption(
                        GalleryOption().apply {
                            themeColor = Color.parseColor("#3772E0")
                            textColor = Color.WHITE
                            itemRadio = 2.0f
                        }
                )
            }
        }, ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            this.topMargin = 300
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        picker.permissionsUtils.dealResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GalleryPicker.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                picker.getResultFromIntent(data)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                logi("取消选择")
            }
        }
    }
}
