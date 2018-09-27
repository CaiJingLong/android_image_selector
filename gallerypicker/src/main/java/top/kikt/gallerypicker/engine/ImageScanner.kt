package top.kikt.gallerypicker.engine

import android.content.Context
import android.provider.MediaStore
import top.kikt.gallerypicker.entity.ImageEntity
import java.io.File

class ImageScanner(private val context: Context) {

    companion object {
        private val STORE_IMAGES = arrayOf(MediaStore.Images.Media.DISPLAY_NAME, // 显示的名字
                MediaStore.Images.Media.DATA, // 数据
                MediaStore.Images.Media.LONGITUDE, // 经度
                MediaStore.Images.Media._ID, // id
                MediaStore.Images.Media.MINI_THUMB_MAGIC, // id
                MediaStore.Images.Media.TITLE, // id
                MediaStore.Images.Media.BUCKET_ID, // dir id 目录
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
                MediaStore.Images.Media.DATE_TAKEN
        )
    }

    private val imageList = ArrayList<ImageEntity>()

    fun scan(): ArrayList<ImageEntity> {

        val resultList = ArrayList<ImageEntity>()

        val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val mContentResolver = context.contentResolver
        val mCursor = MediaStore.Images.Media.query(mContentResolver, mImageUri, STORE_IMAGES, null, MediaStore.Images.Media.DATE_TAKEN)

        mCursor.moveToLast()
        while (mCursor.moveToPrevious()) {
            val path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA))
            val dir = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
            val dirId = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID))
//            val title = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE))
            val thumb = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC))
            val imgId = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
            val img = ImageEntity(imgId, path, dirId, dir, thumb)

            if (File(path).exists())
                resultList.add(img)
        }
        mCursor.close()

        return resultList
    }

}