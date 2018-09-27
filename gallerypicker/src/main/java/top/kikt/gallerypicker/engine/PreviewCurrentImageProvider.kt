package top.kikt.gallerypicker.engine

import top.kikt.gallerypicker.entity.ImageEntity

interface PreviewCurrentImageProvider {

    fun currentImage(): ImageEntity

}