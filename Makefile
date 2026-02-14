.PHONY: build install run build-release install-release run-release build-play-bundle build-market-apks build-all-market-apks clean test help mock-list

# Default target
help:
	@echo "Available targets:"
	@echo "  build           - Build debug APK"
	@echo "  install         - Build and install debug APK on connected device"
	@echo "  run             - Build, install, and launch the app (debug)"
	@echo "  build-release   - Build release APK"
	@echo "  install-release - Build and install release APK on connected device"
	@echo "  run-release     - Build, install, and launch the app (release)"
	@echo "  build-play-bundle - Build Play Store AAB (playRelease)"
	@echo "  build-market-apks - Build APKs for one market flavor (MARKET=...)"
	@echo "  build-all-market-apks - Build APKs for all non-Play market flavors"
	@echo "  clean           - Clean build artifacts"
	@echo "  test            - Run unit tests"
	@echo "  mock-list       - List available mock server car profiles"
	@echo ""
	@echo "Mock server (requires UPSTREAM and CAR):"
	@echo "  make mock UPSTREAM=http://api:4000 CAR=modely_legacy_silver_apollo"

# Build debug APK
build:
	./gradlew assembleDebug

# Build and install debug APK on connected device
install: build
	adb install -r app/build/outputs/apk/debug/app-debug.apk

# Build, install, and launch the app
run: install
	adb shell am start -n com.matedroid/.MainActivity

# Build release APK
build-release:
	./gradlew assembleRelease

# Build Play Store bundle (AAB)
build-play-bundle:
	./gradlew bundlePlayRelease

# Build release APKs for one market flavor
# Usage: make build-market-apks MARKET=huawei
build-market-apks:
ifndef MARKET
	$(error MARKET is required. Example: make build-market-apks MARKET=huawei)
endif
	@market_cap=$$(printf '%s' "$(MARKET)" | awk '{print toupper(substr($$0,1,1)) substr($$0,2)}'); \
	./gradlew assemble$${market_cap}Release

# Build release APKs for non-Play market flavors
build-all-market-apks:
	./gradlew assembleHuaweiRelease assembleHonorRelease assembleXiaomiRelease assembleOppoRelease assembleSamsungRelease assembleFdroidRelease

# Build and install release APK on connected device
install-release: build-release
	adb install -r app/build/outputs/apk/release/app-release.apk

# Build, install, and launch the app (release)
run-release: install-release
	adb shell am start -n com.matedroid/.MainActivity

# Clean build artifacts
clean:
	./gradlew clean

# Run unit tests
test:
	./gradlew testDebugUnitTest

# List available mock server car profiles
mock-list:
	@cd mockserver && ./server.py --list-cars -u http://localhost -c dummy 2>/dev/null || true

# Start mock server (requires UPSTREAM and CAR variables)
# Usage: make mock UPSTREAM=http://teslamate-api:4000 CAR=modely_legacy_silver_apollo
mock:
ifndef UPSTREAM
	$(error UPSTREAM is required. Example: make mock UPSTREAM=http://api:4000 CAR=modely_legacy_silver_apollo)
endif
ifndef CAR
	$(error CAR is required. Run 'make mock-list' to see available profiles)
endif
	cd mockserver && ./server.py -u $(UPSTREAM) -c $(CAR) -p 4002
