package top.kikt.gallerypicker.engine

import top.kikt.gallerypicker.entity.ImageEntity

interface  ImageSelectFinishCallback {
    fun selectedSure(selectList: ArrayList<ImageEntity>)

    fun selectedCancel()
}