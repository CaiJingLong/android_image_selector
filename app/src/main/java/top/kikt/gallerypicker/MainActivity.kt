package top.kikt.gallerypicker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button

class MainActivity : AppCompatActivity() {
    val picker = GalleryPicker(this)
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
                picker.openWithOption(GalleryOption())
            }
        }, ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            this.topMargin = 300
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        picker.permissionsUtils.dealResult(requestCode, permissions, grantResults)
    }
}
