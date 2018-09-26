package top.kikt.gallerypicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import top.kikt.gallerypicker.permission.PermissionsListener
import top.kikt.gallerypicker.permission.PermissionsUtils
import top.kikt.gallerypicker.ui.GalleryActivity

class GalleryPicker(val context: Activity) {

    companion object {
        const val REQUEST_CODE = 3000
        const val RESULT_LIST: String = "result_list"
    }

    val permissionsUtils: PermissionsUtils = PermissionsUtils()
    private var hasPermission = false

    fun requestPermission() {
        permissionsUtils
                .apply {
                    withActivity(context)
                    permissionsListener = object : PermissionsListener {
                        override fun onDenied(deniedPermissions: Array<out String>?) {
                            hasPermission = false
                            Toast.makeText(context, "权限被拒绝", Toast.LENGTH_SHORT).show()
                            getAppDetailSettingIntent(context)
                        }

                        override fun onGranted() {
                            hasPermission = true
                        }
                    }
                }.getPermissions(context, 3001, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun openWithOption(option: GalleryOption, requestCode: Int = REQUEST_CODE) {
        if (hasPermission.not()) {
            Toast.makeText(context, "权限被拒绝", Toast.LENGTH_SHORT).show()
            return
        }
        GalleryOption.config = option
        val intent = Intent(context, GalleryActivity::class.java)
        context.startActivityForResult(intent, requestCode)
    }

}