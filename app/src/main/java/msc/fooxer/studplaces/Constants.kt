package msc.fooxer.studplaces

var CATEGORY: ArrayList <SearchOption> = arrayListOf(SearchOption("Бар"),
    SearchOption("Антикафе"), SearchOption("Музей"),
    SearchOption("Кино"), SearchOption("Разное"), SearchOption("буль"))

var METRO_NEW: MutableList<Line> = ArrayList()
var STATIONS: ArrayList<SearchOption> = ArrayList()

    val DB_NAME = "LOCAL_DATABASE"
    var CURRENT_VER = 3
    val FAV_TABLE_NAME = "FAVORITES"
    val CASH_TABLE_NAME = "CASH"
    val KEY_INDEX = "ID"
    val KEY_NAME = "NAME"
    val KEY_CATEGORY = "CATEGORY"
    val KEY_DESCR = "DESCRIPTION"
    val KEY_METRO = "METRO"
    val KEY_PHONE = "PHONE"
    val KEY_ADDRESS = "ADDRESS"
    val KEY_PRICE = "PRICE"
    val KEY_PIC = "PICTURE"
    val KEY_FAV = "FAV"
    val KEY_DATE = "ADDED"
    val KEY_URL = "URL"
    val KEY_DISCOUNT = "DISCOUNT"
    val STATION_FILTER = "STATIONS"
    val CATEGORY_FILTER = "CATEGORIES"


