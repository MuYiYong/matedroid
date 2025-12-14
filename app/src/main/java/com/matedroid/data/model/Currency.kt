package com.matedroid.data.model

/**
 * Currency list sorted by global usage/transaction volume.
 * Based on BIS Triennial Central Bank Survey and SWIFT transaction data.
 */
data class Currency(
    val code: String,
    val symbol: String,
    val name: String
) {
    companion object {
        /**
         * List of currencies sorted by global usage (most used first).
         * Sources: BIS Triennial Survey 2022, SWIFT RMB Tracker.
         */
        val ALL: List<Currency> = listOf(
            Currency("USD", "$", "US Dollar"),
            Currency("EUR", "€", "Euro"),
            Currency("JPY", "¥", "Japanese Yen"),
            Currency("GBP", "£", "British Pound"),
            Currency("CNY", "¥", "Chinese Yuan"),
            Currency("AUD", "A$", "Australian Dollar"),
            Currency("CAD", "C$", "Canadian Dollar"),
            Currency("CHF", "Fr", "Swiss Franc"),
            Currency("HKD", "HK$", "Hong Kong Dollar"),
            Currency("SGD", "S$", "Singapore Dollar"),
            Currency("SEK", "kr", "Swedish Krona"),
            Currency("KRW", "₩", "South Korean Won"),
            Currency("NOK", "kr", "Norwegian Krone"),
            Currency("NZD", "NZ$", "New Zealand Dollar"),
            Currency("INR", "₹", "Indian Rupee"),
            Currency("MXN", "MX$", "Mexican Peso"),
            Currency("TWD", "NT$", "Taiwan Dollar"),
            Currency("ZAR", "R", "South African Rand"),
            Currency("BRL", "R$", "Brazilian Real"),
            Currency("DKK", "kr", "Danish Krone"),
            Currency("PLN", "zł", "Polish Zloty"),
            Currency("THB", "฿", "Thai Baht"),
            Currency("ILS", "₪", "Israeli Shekel"),
            Currency("IDR", "Rp", "Indonesian Rupiah"),
            Currency("CZK", "Kč", "Czech Koruna"),
            Currency("AED", "د.إ", "UAE Dirham"),
            Currency("TRY", "₺", "Turkish Lira"),
            Currency("HUF", "Ft", "Hungarian Forint"),
            Currency("CLP", "CLP$", "Chilean Peso"),
            Currency("SAR", "﷼", "Saudi Riyal"),
            Currency("PHP", "₱", "Philippine Peso"),
            Currency("MYR", "RM", "Malaysian Ringgit"),
            Currency("COP", "COL$", "Colombian Peso"),
            Currency("RUB", "₽", "Russian Ruble"),
            Currency("RON", "lei", "Romanian Leu"),
            Currency("PEN", "S/", "Peruvian Sol"),
            Currency("BGN", "лв", "Bulgarian Lev"),
            Currency("ARS", "ARS$", "Argentine Peso")
        )

        val DEFAULT = ALL.first() // USD

        fun findByCode(code: String): Currency {
            return ALL.find { it.code == code } ?: DEFAULT
        }
    }

    override fun toString(): String = "$code - $name"
}
