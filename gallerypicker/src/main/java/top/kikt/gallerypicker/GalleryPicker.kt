package top.kikt.gallerypicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.permission.PermissionsListener
import top.kikt.gallerypicker.permission.PermissionsUtils
import top.kikt.gallerypicker.ui.GalleryActivity
import java.util.*

class GalleryPicker(val context: Activity) {

    companion object {
        const val REQUEST_CODE = 3000
        const val RESULT_LIST: String = "result_list"
    }

    val permissionsUtils: PermissionsUtils = PermissionsUtils()
    private var hasPermission = false

    fun requestPermission(successRunner: (() -> Unit)? = null) {
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
                            successRunner?.invoke()
                        }
                    }
                }.getPermissions(context, 3001, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun openWithOption(option: GalleryOption, requestCode: Int = REQUEST_CODE, ignorePermission: Boolean = false) {
        if (ignorePermission) {
            openPicker(option, requestCode)
            return
        }

        if (hasPermission.not()) {
            requestPermission {
                openPicker(option, requestCode)
            }
            return
        }

        openPicker(option, requestCode)
    }

    private fun openPicker(option: GalleryOption, requestCode: Int) {
        GalleryOption.config = option
        val intent = Intent(context, GalleryActivity::class.java)
        context.startActivityForResult(intent, requestCode)
    }

    fun getResultFromIntent(intent:Intent): ArrayList<ImageEntity>? {
        return intent.getParcelableArrayListExtra(RESULT_LIST)
    }
}