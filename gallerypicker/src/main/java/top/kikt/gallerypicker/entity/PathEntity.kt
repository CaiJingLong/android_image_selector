package top.kikt.gallerypicker.entity

class PathEntity(val id: String, val title: String) {

    override fun equals(other: Any?): Boolean {
        if (other !is PathEntity) {
            return false
        }
        return this.title == other.title
    }

    override fun hashCode(): Int {
        return this.title.hashCode()
    }

}