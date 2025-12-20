package com.matedroid.domain.model

/**
 * Resolves Tesla car image assets based on vehicle configuration.
 *
 * Maps TeslamateAPI values (e.g., "MidnightSilver", "Pinwheel18CapKit")
 * to Tesla compositor codes (e.g., "PMNG", "W38B") to construct asset paths.
 */
object CarImageResolver {

    // Model code mappings (TeslamateAPI -> Compositor)
    private val MODEL_CODES = mapOf(
        "3" to "m3",
        "Y" to "my",
        "S" to "ms",
        "X" to "mx"
    )

    // Color code mappings (TeslamateAPI -> Compositor)
    // Keys are normalized (lowercase, no spaces)
    private val COLOR_CODES = mapOf(
        "black" to "PBSB",
        "solidblack" to "PBSB",
        "obsidianblack" to "PMBL",
        "midnightsilver" to "PMNG",
        "midnightsilvermetallic" to "PMNG",
        "silver" to "PMSS",
        "silvermetallic" to "PMSS",
        "white" to "PPSW",
        "pearlwhite" to "PPSW",
        "pearlwhitemulticoat" to "PPSW",
        "deepblue" to "PPSB",
        "deepbluemetallic" to "PPSB",
        "blue" to "PPSB",
        "red" to "PPMR",
        "redmulticoat" to "PPMR",
        "quicksilver" to "PN00",
        "stealthgrey" to "PN01",
        "stealthgray" to "PN01",
        "midnightcherryred" to "PR00",
        "ultrared" to "PR01"
    )

    // Wheel code mappings per model
    // Keys are patterns that match the start of the TeslamateAPI wheel type
    private val WHEEL_PATTERNS_M3 = listOf(
        "pinwheel18" to "W38B",
        "aero18" to "W38B",
        "aeroturbine19" to "W39B",
        "stiletto19" to "W39B",
        "sport19" to "W39B",
        "performance20" to "W32P",
        "19" to "W39B",  // Default 19" to Sport
        "20" to "W32P",  // Default 20" to Performance
        "18" to "W38B"   // Default 18" to Aero
    )

    private val WHEEL_PATTERNS_MY = listOf(
        "pinwheel18" to "WY18B",
        "aero18" to "WY18B",
        "gemini19" to "WY19B",
        "aeroturbine19" to "WY19B",
        "stiletto19" to "WY19B",
        "sport19" to "WY19B",
        "apollo19" to "WY9S",
        "induction20" to "WY0S",
        "performance20" to "WY20P",
        "uberturbine21" to "WY1S",
        "21" to "WY1S",  // Default 21" to Uberturbine
        "20" to "WY0S",  // Default 20" to Induction
        "19" to "WY19B", // Default 19" to Sport
        "18" to "WY18B"  // Default 18" to Aero
    )

    // Default wheels per model (most common configuration)
    private val DEFAULT_WHEELS = mapOf(
        "m3" to "W38B",  // 18" Aero
        "my" to "WY19B"  // 19" Sport
    )

    // Default color
    private const val DEFAULT_COLOR = "PPSW" // Pearl White

    /**
     * Get the asset path for a car image based on its configuration.
     *
     * @param model The car model from TeslamateAPI (e.g., "3", "Y")
     * @param exteriorColor The exterior color from TeslamateAPI (e.g., "MidnightSilver")
     * @param wheelType The wheel type from TeslamateAPI (e.g., "Pinwheel18CapKit")
     * @return The asset path (e.g., "car_images/m3_PMNG_W38B.png")
     */
    fun getAssetPath(
        model: String?,
        exteriorColor: String?,
        wheelType: String?
    ): String {
        val modelCode = mapModel(model) ?: return getDefaultAssetPath(model)
        val colorCode = mapColor(exteriorColor) ?: DEFAULT_COLOR
        val wheelCode = mapWheel(modelCode, wheelType) ?: DEFAULT_WHEELS[modelCode] ?: "W38B"

        return "car_images/${modelCode}_${colorCode}_${wheelCode}.png"
    }

    /**
     * Get the default asset path for a model when configuration is unavailable.
     */
    fun getDefaultAssetPath(model: String?): String {
        val modelCode = mapModel(model) ?: "m3"
        val defaultWheel = DEFAULT_WHEELS[modelCode] ?: "W38B"
        return "car_images/${modelCode}_${DEFAULT_COLOR}_${defaultWheel}.png"
    }

    /**
     * Check if a specific asset exists (for fallback logic).
     * This should be called with actual asset checking from the AssetManager.
     */
    fun getFallbackAssetPath(
        model: String?,
        exteriorColor: String?,
        wheelType: String?,
        assetExists: (String) -> Boolean
    ): String {
        // Try exact match first
        val exactPath = getAssetPath(model, exteriorColor, wheelType)
        if (assetExists(exactPath)) return exactPath

        // Try with default wheel
        val modelCode = mapModel(model) ?: "m3"
        val colorCode = mapColor(exteriorColor) ?: DEFAULT_COLOR
        val defaultWheelPath = "car_images/${modelCode}_${colorCode}_${DEFAULT_WHEELS[modelCode]}.png"
        if (assetExists(defaultWheelPath)) return defaultWheelPath

        // Try with default color
        val wheelCode = mapWheel(modelCode, wheelType) ?: DEFAULT_WHEELS[modelCode] ?: "W38B"
        val defaultColorPath = "car_images/${modelCode}_${DEFAULT_COLOR}_${wheelCode}.png"
        if (assetExists(defaultColorPath)) return defaultColorPath

        // Fall back to complete default
        return getDefaultAssetPath(model)
    }

    private fun mapModel(model: String?): String? {
        return model?.let { MODEL_CODES[it.uppercase()] ?: MODEL_CODES[it] }
    }

    private fun mapColor(color: String?): String? {
        if (color == null) return null
        val normalized = color.lowercase().replace(" ", "").replace("-", "").replace("_", "")
        return COLOR_CODES[normalized]
    }

    private fun mapWheel(modelCode: String, wheelType: String?): String? {
        if (wheelType == null) return null

        val normalized = wheelType.lowercase().replace(" ", "").replace("-", "").replace("_", "")

        val patterns = when (modelCode) {
            "m3" -> WHEEL_PATTERNS_M3
            "my" -> WHEEL_PATTERNS_MY
            else -> WHEEL_PATTERNS_M3 // Default to Model 3 patterns
        }

        // Find the first pattern that matches the start of the wheel type
        for ((pattern, code) in patterns) {
            if (normalized.startsWith(pattern)) {
                return code
            }
        }

        return null
    }
}
