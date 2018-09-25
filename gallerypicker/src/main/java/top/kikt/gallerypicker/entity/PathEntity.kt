package top.kikt.gallerypicker.entity

class PathEntity(val id: String, val title: String) {

    override fun equals(other: Any?): Boolean {
        if (other !is PathEntity) {
            return false
        }
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }

}