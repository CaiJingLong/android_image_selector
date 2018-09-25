package top.kikt.gallerypicker.entity

data class ImageEntity(val id: String, val path: String, val parentId: String, val parentPathName: String, var thumbPath: String?) {

    val pathEntity: PathEntity = PathEntity(parentId, parentPathName)

    override fun equals(other: Any?): Boolean {
        if (other !is ImageEntity) {
            return false
        }

        return other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}