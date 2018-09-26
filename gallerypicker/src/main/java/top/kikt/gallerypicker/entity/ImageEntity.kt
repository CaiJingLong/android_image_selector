package top.kikt.gallerypicker.entity

import android.os.Parcel
import android.os.Parcelable

data class ImageEntity(val id: String, val path: String, val parentId: String, val parentPathName: String, var thumbPath: String?) : Parcelable {

    val pathEntity: PathEntity = PathEntity(parentId, parentPathName)

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString())

    override fun equals(other: Any?): Boolean {
        if (other !is ImageEntity) {
            return false
        }

        return other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(path)
        parcel.writeString(parentId)
        parcel.writeString(parentPathName)
        parcel.writeString(thumbPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageEntity> {
        override fun createFromParcel(parcel: Parcel): ImageEntity {
            return ImageEntity(parcel)
        }

        override fun newArray(size: Int): Array<ImageEntity?> {
            return arrayOfNulls(size)
        }
    }

}