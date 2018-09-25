package top.kikt.gallerypicker.engine

import top.kikt.gallerypicker.entity.ImageEntity

interface ImageSelectedProvider {
    val selectedList: List<ImageEntity>

    fun addImageEntity(entity: ImageEntity): Boolean

    fun removeImageEntity(entity: ImageEntity)

    fun containsImageEntity(entity: ImageEntity): Boolean

    fun indexOfImageEntity(entity: ImageEntity): Int

}