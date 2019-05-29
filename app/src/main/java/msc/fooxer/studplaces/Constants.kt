package msc.fooxer.studplaces

var CATEGORY: Array <SearchOption> = arrayOf(SearchOption("Бары"),
    SearchOption("Антикафе"), SearchOption("Музеи"),
    SearchOption("Кинотеатры"), SearchOption("Разное"), SearchOption("буль"))

var METRO: Array <SearchOption> = arrayOf(SearchOption("Сокольническая линия", R.color.Metro_Line1), SearchOption("Замоскворецкая линия", R.color.Metro_Line2),
    SearchOption("Арбатско-Покровская линия", R.color.Metro_Line3), SearchOption("Филевская линия", R.color.Metro_Line4),SearchOption("Кольцевая линия", R.color.Metro_Line5),
    SearchOption("Калужско-Рижская линия", R.color.Metro_Line6), SearchOption("Таганско-Краснопресненская линия", R.color.Metro_Line7), SearchOption("Калининско-Солнцевская линия", R.color.Metro_Line8),
    SearchOption("Серпуховско-Тимирязевская линия", R.color.Metro_Line9), SearchOption("Люблюнско-Дмитровская линия", R.color.Metro_Line10), SearchOption("Каховская линия", R.color.Metro_Line11),
    SearchOption("Бутовсская линия", R.color.Metro_Line12),SearchOption("МЦК", R.color.Metro_Line14), SearchOption("Большая кольцевая линия", R.color.Metro_Line15))
//var LINE_1: Array <SearchOption> = arrayOf()

var METRO_NEW: MutableList<Line> = ArrayList() // Добавить
var STATIONS: ArrayList<Station> = ArrayList()

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



/*val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val metro: String,
    val phoneNumbers: String,
    val price: Int,
    val address: String, // Сюда нужен код для получения картинки и ее обработки BitMap
    var picture: String,
    var isFavorite: Boolean = false*/
