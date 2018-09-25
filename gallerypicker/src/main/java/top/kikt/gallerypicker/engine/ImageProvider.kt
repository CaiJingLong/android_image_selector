package top.kikt.gallerypicker.engine

import top.kikt.gallerypicker.entity.ImageEntity
import top.kikt.gallerypicker.entity.PathEntity

interface ImageProvider {

    fun getImageWithPath(pathEntity: PathEntity?): List<ImageEntity>
    fun getPathEntityList(): List<PathEntity>

}