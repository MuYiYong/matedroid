package com.matedroid.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.Route
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.PolygonOptions
import com.matedroid.R
import com.matedroid.data.repository.CountryBoundary
import com.matedroid.domain.model.ChargeLocation
import com.matedroid.domain.model.CountryRecord
import com.matedroid.domain.model.DriveLocation
import com.matedroid.domain.model.RegionRecord
import com.matedroid.domain.model.YearFilter
import com.matedroid.domain.model.wgs84ToGcj02
import com.matedroid.ui.icons.CustomIcons
import com.matedroid.ui.components.AmapViewContainer
import com.matedroid.ui.theme.CarColorPalette
import com.matedroid.ui.theme.CarColorPalettes
import com.matedroid.ui.theme.StatusSuccess
import com.matedroid.ui.theme.StatusWarning
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionsVisitedScreen(
    carId: Int,
    countryCode: String,
    countryName: String,
    yearFilter: YearFilter,
    exteriorColor: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: RegionsVisitedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme = isSystemInDarkTheme()
    val palette = remember(exteriorColor, isDarkTheme) {
        CarColorPalettes.forExteriorColor(exteriorColor, isDarkTheme)
    }
    var showSortMenu by remember { mutableStateOf(false) }

    LaunchedEffect(carId, countryCode, yearFilter) {
        viewModel.loadRegions(carId, countryCode, yearFilter)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.regions_visited_title, countryName)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                Icons.AutoMirrored.Filled.Sort,
                                contentDescription = stringResource(R.string.sort)
                            )
                        }
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.sort_by_first_visit)) },
                                onClick = {
                                    viewModel.setSortOrder(RegionSortOrder.FIRST_VISIT)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.sort_alphabetically)) },
                                onClick = {
                                    viewModel.setSortOrder(RegionSortOrder.ALPHABETICAL)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.sort_by_drive_count)) },
                                onClick = {
                                    viewModel.setSortOrder(RegionSortOrder.DRIVE_COUNT)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.sort_by_distance)) },
                                onClick = {
                                    viewModel.setSortOrder(RegionSortOrder.DISTANCE)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.sort_by_energy)) },
                                onClick = {
                                    viewModel.setSortOrder(RegionSortOrder.ENERGY)
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.sort_by_charges)) },
                                onClick = {
                                    viewModel.setSortOrder(RegionSortOrder.CHARGES)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = palette.accent
                    )
                }
                uiState.regions.isEmpty() && uiState.countryRecord == null -> {
                    EmptyState(palette = palette)
                }
                else -> {
                    RegionsContent(
                        countryRecord = uiState.countryRecord,
                        regions = uiState.regions,
                        chargeLocations = uiState.filteredChargeLocations,
                        driveLocations = uiState.filteredDriveLocations,
                        allChargeLocations = uiState.chargeLocations,
                        allDriveLocations = uiState.driveLocations,
                        countryBoundary = uiState.countryBoundary,
                        mapViewMode = uiState.mapViewMode,
                        chargeTypeFilter = uiState.chargeTypeFilter,
                        availableYears = uiState.availableYears,
                        selectedMapYear = uiState.selectedMapYear,
                        onMapViewModeChange = { viewModel.setMapViewMode(it) },
                        onChargeTypeFilterToggle = { viewModel.toggleChargeTypeFilter(it) },
                        onMapYearChange = { viewModel.setMapYearFilter(it) },
                        palette = palette
                    )
                }
            }
        }
    }
}

@Composable
private fun RegionsContent(
    countryRecord: CountryRecord?,
    regions: List<RegionRecord>,
    chargeLocations: List<ChargeLocation>,
    driveLocations: List<DriveLocation>,
    allChargeLocations: List<ChargeLocation>,
    allDriveLocations: List<DriveLocation>,
    countryBoundary: CountryBoundary?,
    mapViewMode: MapViewMode,
    chargeTypeFilter: ChargeTypeFilter,
    availableYears: List<Int>,
    selectedMapYear: Int?,
    onMapViewModeChange: (MapViewMode) -> Unit,
    onChargeTypeFilterToggle: (ChargeTypeFilter) -> Unit,
    onMapYearChange: (Int?) -> Unit,
    palette: CarColorPalette
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header card with country summary
        countryRecord?.let { country ->
            item(key = "header") {
                CountrySummaryCard(
                    country = country,
                    localizedName = getLocalizedCountryName(country.countryCode),
                    palette = palette
                )
            }
        }

        // Year filter chips (only show if there are multiple years)
        if (availableYears.size > 1) {
            item(key = "year_filter") {
                YearFilterRow(
                    availableYears = availableYears,
                    selectedYear = selectedMapYear,
                    onYearSelected = onMapYearChange,
                    palette = palette
                )
            }
        }

        // Map with charge/drive locations (only show if there's data)
        if (allChargeLocations.isNotEmpty() || allDriveLocations.isNotEmpty()) {
            item(key = "map") {
                CountryMapCard(
                    chargeLocations = chargeLocations,
                    driveLocations = driveLocations,
                    countryBoundary = countryBoundary,
                    mapViewMode = mapViewMode,
                    chargeTypeFilter = chargeTypeFilter,
                    onMapViewModeChange = onMapViewModeChange,
                    onChargeTypeFilterToggle = onChargeTypeFilterToggle,
                    palette = palette
                )
            }
        }

        // Region cards
        items(regions, key = { it.regionName }) { region ->
            RegionCard(region = region, palette = palette)
        }
    }
}

@Composable
private fun CountrySummaryCard(
    country: CountryRecord,
    localizedName: String,
    palette: CarColorPalette
) {
    val cardShape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = cardShape,
                spotColor = palette.onSurface.copy(alpha = 0.1f)
            )
            .clip(cardShape)
            .background(palette.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header row with flag and country name
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flag emoji
                Text(
                    text = country.flagEmoji,
                    fontSize = 40.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Country name
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = localizedName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.onSurface
                    )
                }

                // Drive count
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = country.driveCount.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = palette.accent
                    )
                    Text(
                        text = pluralStringResource(
                            R.plurals.drives_count,
                            country.driveCount
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.onSurfaceVariant
                    )
                }
            }

            // First and last visit dates
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.country_first_visit, formatDate(country.firstVisitDate)),
                    style = MaterialTheme.typography.bodySmall,
                    color = palette.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.country_last_visit, formatDate(country.lastVisitDate)),
                    style = MaterialTheme.typography.bodySmall,
                    color = palette.onSurfaceVariant
                )
            }

            // Stats row in chips
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatChip(
                    icon = Icons.Default.Route,
                    value = "%,.0f km".format(country.totalDistanceKm),
                    palette = palette,
                    modifier = Modifier.weight(1f)
                )

                StatChip(
                    icon = Icons.Default.ElectricBolt,
                    value = if (country.totalChargeEnergyKwh > 999) {
                        "%.1f MWh".format(country.totalChargeEnergyKwh / 1000)
                    } else {
                        "%.0f kWh".format(country.totalChargeEnergyKwh)
                    },
                    palette = palette,
                    modifier = Modifier.weight(1f)
                )

                StatChip(
                    icon = Icons.Default.EvStation,
                    value = pluralStringResource(
                        R.plurals.charges_count,
                        country.chargeCount,
                        country.chargeCount
                    ),
                    palette = palette,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Year filter chips for the map.
 */
@Composable
private fun YearFilterRow(
    availableYears: List<Int>,
    selectedYear: Int?,
    onYearSelected: (Int?) -> Unit,
    palette: CarColorPalette
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All" chip
        item {
            FilterChip(
                selected = selectedYear == null,
                onClick = { onYearSelected(null) },
                label = { Text(stringResource(R.string.all_years)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = palette.surface,
                    selectedLabelColor = palette.onSurface
                )
            )
        }

        // Year chips
        items(availableYears) { year ->
            FilterChip(
                selected = selectedYear == year,
                onClick = { onYearSelected(year) },
                label = { Text(year.toString()) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = palette.surface,
                    selectedLabelColor = palette.onSurface
                )
            )
        }
    }
}

/**
 * Map card showing charge or drive locations in a country with a toggle to switch between views.
 * Uses AMap tiles with custom styled markers.
 * Optionally highlights the country when boundary data is available.
 */
@Composable
private fun CountryMapCard(
    chargeLocations: List<ChargeLocation>,
    driveLocations: List<DriveLocation>,
    countryBoundary: CountryBoundary?,
    mapViewMode: MapViewMode,
    chargeTypeFilter: ChargeTypeFilter,
    onMapViewModeChange: (MapViewMode) -> Unit,
    onChargeTypeFilterToggle: (ChargeTypeFilter) -> Unit,
    palette: CarColorPalette
) {
    val cardShape = RoundedCornerShape(20.dp)
    val chargeCount = chargeLocations.size
    val driveCount = driveLocations.size

    // Colors for markers - green for AC, yellow/orange for DC (matching status colors used elsewhere)
    val acColor = StatusSuccess
    val dcColor = StatusWarning
    val driveColor = palette.accent
    val acColorArgb = acColor.toArgb()
    val dcColorArgb = dcColor.toArgb()
    val driveColorArgb = driveColor.toArgb()
    val mapUpdateKey = buildString {
        append(mapViewMode.name)
        append("|")
        append(chargeTypeFilter.name)
        append("|")
        append(chargeLocations.size)
        append("|")
        append(driveLocations.size)
        append("|")
        append(countryBoundary?.polygons?.size ?: 0)
    }

    // Track if initial zoom has been done (only zoom once when page first opens)
    var hasInitialZoom by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = cardShape,
                spotColor = palette.onSurface.copy(alpha = 0.15f)
            )
            .clip(cardShape)
            .background(palette.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header with toggle and count badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Toggle selector with dots
                MapModeToggle(
                    selectedMode = mapViewMode,
                    onModeChange = onMapViewModeChange,
                    chargesEnabled = chargeLocations.isNotEmpty(),
                    drivesEnabled = driveLocations.isNotEmpty(),
                    palette = palette
                )

                // Count badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(palette.accent.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = when (mapViewMode) {
                            MapViewMode.CHARGES -> pluralStringResource(
                                R.plurals.format_charges_on_map,
                                chargeCount,
                                chargeCount
                            )
                            MapViewMode.DRIVES -> pluralStringResource(
                                R.plurals.format_drives_on_map,
                                driveCount,
                                driveCount
                            )
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = palette.accent
                    )
                }
            }

            // Map view
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AmapViewContainer(
                    modifier = Modifier.fillMaxSize(),
                    updateKey = mapUpdateKey,
                    onMapUpdate = { map ->
                        map.clear()

                        countryBoundary?.let { boundary ->
                            boundary.polygons.forEach { ring ->
                                if (ring.size >= 3) {
                                    map.addPolygon(
                                        PolygonOptions()
                                            .addAll(ring.map { (lat, lon) -> LatLng(lat, lon) })
                                            .strokeColor(acColorArgb)
                                            .strokeWidth(3f)
                                            .fillColor(
                                                android.graphics.Color.argb(
                                                    25,
                                                    android.graphics.Color.red(acColorArgb),
                                                    android.graphics.Color.green(acColorArgb),
                                                    android.graphics.Color.blue(acColorArgb)
                                                )
                                            )
                                    )
                                }
                            }
                        }

                        val boundsBuilder = LatLngBounds.builder()
                        var hasPoints = false

                        when (mapViewMode) {
                            MapViewMode.CHARGES -> {
                                chargeLocations.forEach { charge ->
                                    val (gcjLat, gcjLon) = wgs84ToGcj02(charge.latitude, charge.longitude)
                                    val point = LatLng(gcjLat, gcjLon)
                                    hasPoints = true
                                    boundsBuilder.include(point)

                                    val hue = if (charge.isDcCharge) {
                                        BitmapDescriptorFactory.HUE_ORANGE
                                    } else {
                                        BitmapDescriptorFactory.HUE_GREEN
                                    }

                                    map.addMarker(
                                        MarkerOptions()
                                            .position(point)
                                            .title(charge.address)
                                            .snippet("%.1f kWh".format(charge.energyAddedKwh))
                                            .icon(BitmapDescriptorFactory.defaultMarker(hue))
                                    )
                                }
                            }

                            MapViewMode.DRIVES -> {
                                driveLocations.forEach { drive ->
                                    val (gcjLat, gcjLon) = wgs84ToGcj02(drive.latitude, drive.longitude)
                                    val point = LatLng(gcjLat, gcjLon)
                                    hasPoints = true
                                    boundsBuilder.include(point)

                                    map.addMarker(
                                        MarkerOptions()
                                            .position(point)
                                            .title(drive.address)
                                            .snippet("%,.1f km".format(drive.distanceKm))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    )
                                }
                            }
                        }

                        if (!hasInitialZoom && hasPoints) {
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 60)
                            )
                            hasInitialZoom = true
                        }
                    }
                )

                // Legend overlay at bottom-left
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (mapViewMode) {
                        MapViewMode.CHARGES -> {
                            // AC legend (tappable filter)
                            val acSelected = chargeTypeFilter == ChargeTypeFilter.AC_ONLY
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (acSelected) acColor.copy(alpha = 0.2f) else Color.Transparent
                                    )
                                    .clickable { onChargeTypeFilterToggle(ChargeTypeFilter.AC_ONLY) }
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(acColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "AC",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (acSelected) acColor else Color.DarkGray,
                                    fontWeight = if (acSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                            // DC legend (tappable filter)
                            val dcSelected = chargeTypeFilter == ChargeTypeFilter.DC_ONLY
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (dcSelected) dcColor.copy(alpha = 0.2f) else Color.Transparent
                                    )
                                    .clickable { onChargeTypeFilterToggle(ChargeTypeFilter.DC_ONLY) }
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(dcColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "DC",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (dcSelected) dcColor else Color.DarkGray,
                                    fontWeight = if (dcSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                        MapViewMode.DRIVES -> {
                            // Drive legend with colored dot
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(driveColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.drive_start_legend),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Toggle selector with dots for switching between Charges and Drives map views.
 */
@Composable
private fun MapModeToggle(
    selectedMode: MapViewMode,
    onModeChange: (MapViewMode) -> Unit,
    chargesEnabled: Boolean,
    drivesEnabled: Boolean,
    palette: CarColorPalette
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(palette.onSurface.copy(alpha = 0.08f)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Charges option
        val chargesSelected = selectedMode == MapViewMode.CHARGES
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (chargesSelected) palette.accent else Color.Transparent
                )
                .then(
                    if (chargesEnabled) {
                        Modifier.clickable { onModeChange(MapViewMode.CHARGES) }
                    } else Modifier
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EvStation,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = when {
                        chargesSelected -> Color.White
                        chargesEnabled -> palette.onSurface
                        else -> palette.onSurface.copy(alpha = 0.4f)
                    }
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.map_mode_charges),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (chargesSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = when {
                        chargesSelected -> Color.White
                        chargesEnabled -> palette.onSurface
                        else -> palette.onSurface.copy(alpha = 0.4f)
                    }
                )
            }
        }

        // Dot separator
        Box(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(4.dp)
                .clip(CircleShape)
                .background(palette.onSurface.copy(alpha = 0.3f))
        )

        // Drives option
        val drivesSelected = selectedMode == MapViewMode.DRIVES
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (drivesSelected) palette.accent else Color.Transparent
                )
                .then(
                    if (drivesEnabled) {
                        Modifier.clickable { onModeChange(MapViewMode.DRIVES) }
                    } else Modifier
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = CustomIcons.SteeringWheel,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = when {
                        drivesSelected -> Color.White
                        drivesEnabled -> palette.onSurface
                        else -> palette.onSurface.copy(alpha = 0.4f)
                    }
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.map_mode_drives),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (drivesSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = when {
                        drivesSelected -> Color.White
                        drivesEnabled -> palette.onSurface
                        else -> palette.onSurface.copy(alpha = 0.4f)
                    }
                )
            }
        }
    }
}

@Composable
private fun RegionCard(
    region: RegionRecord,
    palette: CarColorPalette
) {
    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = cardShape,
                spotColor = palette.onSurface.copy(alpha = 0.08f)
            )
            .clip(cardShape)
            .background(palette.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Region name
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = region.regionName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.onSurface
                    )
                }

                // Drive count
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = region.driveCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = palette.accent
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = pluralStringResource(
                            R.plurals.drives_count,
                            region.driveCount
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.onSurfaceVariant
                    )
                }
            }

            // Stats row
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatChip(
                    icon = Icons.Default.Route,
                    value = "%,.0f km".format(region.totalDistanceKm),
                    palette = palette,
                    modifier = Modifier.weight(1f)
                )

                StatChip(
                    icon = Icons.Default.ElectricBolt,
                    value = if (region.totalChargeEnergyKwh > 999) {
                        "%.1f MWh".format(region.totalChargeEnergyKwh / 1000)
                    } else {
                        "%.0f kWh".format(region.totalChargeEnergyKwh)
                    },
                    palette = palette,
                    modifier = Modifier.weight(1f)
                )

                StatChip(
                    icon = Icons.Default.EvStation,
                    value = pluralStringResource(
                        R.plurals.charges_count,
                        region.chargeCount,
                        region.chargeCount
                    ),
                    palette = palette,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatChip(
    icon: ImageVector,
    value: String,
    palette: CarColorPalette,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(palette.onSurface.copy(alpha = 0.05f))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = palette.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = palette.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EmptyState(palette: CarColorPalette) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = stringResource(R.string.no_regions_found),
                style = MaterialTheme.typography.bodyLarge,
                color = palette.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatDate(dateStr: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
        val dateTime = LocalDateTime.parse(dateStr, inputFormatter)
        dateTime.format(outputFormatter)
    } catch (e: Exception) {
        // Fallback: try parsing just the date portion
        try {
            dateStr.take(10)
        } catch (e2: Exception) {
            dateStr
        }
    }
}

/**
 * Get the localized country name for a given ISO country code.
 * Falls back to the country code if localization fails.
 */
private fun getLocalizedCountryName(countryCode: String): String {
    return try {
        Locale.Builder()
            .setRegion(countryCode.uppercase(Locale.ROOT))
            .build()
            .getDisplayCountry(Locale.getDefault())
            .takeIf { it.isNotBlank() && it != countryCode } ?: countryCode
    } catch (e: Exception) {
        countryCode
    }
}

