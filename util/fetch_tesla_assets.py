#!/usr/bin/env -S uv run --script
# /// script
# requires-python = ">=3.11"
# dependencies = ["httpx", "rich"]
# ///
"""
Tesla Car Image Asset Fetcher

Downloads Tesla car 3D renders from the Tesla compositor service
for use in the MateDroid Android app.

Usage:
    ./fetch_tesla_assets.py [--output-dir PATH] [--dry-run]
"""

import argparse
import asyncio
import sys
from pathlib import Path

import httpx
from rich.console import Console
from rich.progress import Progress, TaskID

console = Console()

# Tesla Compositor Configuration
COMPOSITOR_BASE_URL = "https://static-assets.tesla.com/v1/compositor/"
IMAGE_VIEW = "STUD_3QTR"  # 3/4 front view
IMAGE_SIZE = 800
BACKGROUND_OPT = 1  # Transparent background

# Model configurations
MODELS = {
    "m3": {
        "name": "Model 3",
        "wheels": ["W38B", "W39B", "W32P"],  # Aero 18", Sport 19", Performance 20"
    },
    "my": {
        "name": "Model Y",
        "wheels": ["WY18B", "WY19B", "WY20P", "WY0S", "WY1S"],  # Various Model Y wheels
    },
}

# Color codes - common across models
COLORS = {
    "PBSB": "Solid Black",
    "PMNG": "Midnight Silver Metallic",
    "PMSS": "Silver Metallic",
    "PPSW": "Pearl White Multi-Coat",
    "PPSB": "Deep Blue Metallic",
    "PPMR": "Red Multi-Coat",
    "PN00": "Quicksilver",
    "PN01": "Stealth Grey",
    "PR00": "Midnight Cherry Red",
    "PR01": "Ultra Red",
}


def build_compositor_url(model: str, color: str, wheel: str) -> str:
    """Build the Tesla compositor URL for a specific configuration."""
    options = f"{color},{wheel}"
    return (
        f"{COMPOSITOR_BASE_URL}"
        f"?model={model}"
        f"&view={IMAGE_VIEW}"
        f"&size={IMAGE_SIZE}"
        f"&options={options}"
        f"&bkba_opt={BACKGROUND_OPT}"
    )


def get_output_filename(model: str, color: str, wheel: str) -> str:
    """Generate the output filename for an asset."""
    return f"{model}_{color}_{wheel}.png"


async def download_image(
    client: httpx.AsyncClient,
    url: str,
    output_path: Path,
    progress: Progress,
    task_id: TaskID,
    description: str,
) -> bool:
    """Download a single image from the compositor."""
    try:
        response = await client.get(url, follow_redirects=True)
        response.raise_for_status()

        # Verify it's actually a PNG
        content = response.content
        if not content.startswith(b'\x89PNG'):
            console.print(f"[yellow]Warning: {description} - Not a valid PNG[/yellow]")
            return False

        output_path.write_bytes(content)
        progress.update(task_id, advance=1)
        return True

    except httpx.HTTPStatusError as e:
        console.print(f"[red]Error: {description} - HTTP {e.response.status_code}[/red]")
        progress.update(task_id, advance=1)
        return False
    except Exception as e:
        console.print(f"[red]Error: {description} - {e}[/red]")
        progress.update(task_id, advance=1)
        return False


async def download_all_assets(output_dir: Path, dry_run: bool = False) -> tuple[int, int]:
    """Download all asset combinations."""
    output_dir.mkdir(parents=True, exist_ok=True)

    # Build list of all downloads
    downloads = []
    for model_code, model_config in MODELS.items():
        for color_code in COLORS:
            for wheel_code in model_config["wheels"]:
                filename = get_output_filename(model_code, color_code, wheel_code)
                url = build_compositor_url(model_code, color_code, wheel_code)
                output_path = output_dir / filename
                description = f"{model_config['name']} {COLORS[color_code]} {wheel_code}"
                downloads.append((url, output_path, description))

    total = len(downloads)
    console.print(f"\n[bold]Tesla Car Asset Fetcher[/bold]")
    console.print(f"Total images to download: {total}")
    console.print(f"Output directory: {output_dir}\n")

    if dry_run:
        console.print("[yellow]Dry run mode - showing what would be downloaded:[/yellow]\n")
        for url, output_path, description in downloads:
            console.print(f"  {description}")
            console.print(f"    -> {output_path.name}")
            console.print(f"    URL: {url}\n")
        return total, 0

    # Download with progress bar
    success_count = 0

    async with httpx.AsyncClient(timeout=30.0) as client:
        with Progress() as progress:
            task_id = progress.add_task("[cyan]Downloading...", total=total)

            # Process in batches to avoid overwhelming the server
            batch_size = 5
            for i in range(0, len(downloads), batch_size):
                batch = downloads[i:i + batch_size]
                tasks = [
                    download_image(client, url, output_path, progress, task_id, description)
                    for url, output_path, description in batch
                ]
                results = await asyncio.gather(*tasks)
                success_count += sum(results)

                # Small delay between batches
                if i + batch_size < len(downloads):
                    await asyncio.sleep(0.5)

    return total, success_count


def main():
    parser = argparse.ArgumentParser(
        description="Download Tesla car images from compositor service"
    )
    parser.add_argument(
        "--output-dir",
        type=Path,
        default=Path(__file__).parent.parent / "app/src/main/assets/car_images",
        help="Output directory for images",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Show what would be downloaded without actually downloading",
    )
    args = parser.parse_args()

    total, success = asyncio.run(download_all_assets(args.output_dir, args.dry_run))

    if not args.dry_run:
        console.print(f"\n[bold]Download complete![/bold]")
        console.print(f"  Success: {success}/{total}")
        if success < total:
            console.print(f"  [yellow]Failed: {total - success}[/yellow]")
            sys.exit(1)

    sys.exit(0)


if __name__ == "__main__":
    main()
