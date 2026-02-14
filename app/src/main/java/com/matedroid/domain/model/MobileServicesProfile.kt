package com.matedroid.domain.model

enum class MobileServicesStack {
    GMS,
    HMS,
    AOSP
}

enum class DistributionChannel {
    PLAY,
    HUAWEI,
    HONOR,
    XIAOMI,
    OPPO,
    SAMSUNG,
    FDROID,
    UNKNOWN
}

data class MobileServicesProfile(
    val channel: DistributionChannel,
    val stack: MobileServicesStack,
    val supportsPushSdk: Boolean,
    val supportsProprietaryAccountSdk: Boolean,
    val supportsProprietaryMapSdk: Boolean
) {
    companion object {
        fun from(channelRaw: String, stackRaw: String): MobileServicesProfile {
            val channel = channelRaw.toChannel()
            val stack = stackRaw.toStack()

            val supportsPushSdk = stack == MobileServicesStack.GMS || stack == MobileServicesStack.HMS
            val supportsAccountSdk = stack == MobileServicesStack.GMS || stack == MobileServicesStack.HMS

            return MobileServicesProfile(
                channel = channel,
                stack = stack,
                supportsPushSdk = supportsPushSdk,
                supportsProprietaryAccountSdk = supportsAccountSdk,
                supportsProprietaryMapSdk = false
            )
        }

        private fun String.toStack(): MobileServicesStack {
            return when (trim().lowercase()) {
                "gms" -> MobileServicesStack.GMS
                "hms" -> MobileServicesStack.HMS
                else -> MobileServicesStack.AOSP
            }
        }

        private fun String.toChannel(): DistributionChannel {
            return when (trim().lowercase()) {
                "play" -> DistributionChannel.PLAY
                "huawei" -> DistributionChannel.HUAWEI
                "honor" -> DistributionChannel.HONOR
                "xiaomi" -> DistributionChannel.XIAOMI
                "oppo" -> DistributionChannel.OPPO
                "samsung" -> DistributionChannel.SAMSUNG
                "fdroid" -> DistributionChannel.FDROID
                else -> DistributionChannel.UNKNOWN
            }
        }
    }
}
