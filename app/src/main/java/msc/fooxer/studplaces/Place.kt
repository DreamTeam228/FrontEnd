package msc.fooxer.studplaces

import android.os.Parcel
import android.os.Parcelable

class Place (
    val id: Int,
    val name: String,
    val Сategory: String,
    val description: String,
    val metro: String,
    val phoneNumbers: String,
    val price: Int,
    val address: String, // Сюда нужен код для получения картинки и ее обработки BitMap
    var picture: String,
    var isFavorite: Boolean = false
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt() != 0
    )

    fun ShowInfo (place: Place): String {
        var info = ""
        info =
            "Имя:  " + place.name + " Описание: " + place.description + " адрес: " + " цена:  " + place.price + " время: "
        return info
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(Сategory)
        parcel.writeString(description)
        parcel.writeString(metro)
        parcel.writeString(phoneNumbers)
        parcel.writeInt(price)
        parcel.writeString(address)
        parcel.writeString(picture)
        parcel.writeInt(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Place> {
        override fun createFromParcel(parcel: Parcel): Place {
            return Place(parcel)
        }

        override fun newArray(size: Int): Array<Place?> {
            return arrayOfNulls(size)
        }
    }


}

