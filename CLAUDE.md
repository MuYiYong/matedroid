# RULES TO FOLLOW FOR THIS PROJECT

## Development style

* create a new branch and associated Github pull request for each new feature or set of bugfixes.
* automatically commit to git any meaningful change, with a short but clear commit message.
* follow the "Keep a changelog" and Semantic versioning best practices but DO NOT release major versions by yourself. Ask me if you think they might be released.
* create a new release only when specifically told.
* keep an always up-to-date README.md, but don't change its writing style.
* always update `docs/DEVELOPMENT.md` and `docs/ASSETS.md` if anything related to those parts changes.

## Gotchas and notes

* Always check the local instance of Teslamate API and its documentation to know what's the returned JSON format.
* The TeslamateApi's parseDateParam function only accepts:
  - RFC3339 format: 2024-12-07T00:00:00Z
  - DateTime format: 2024-12-07 00:00:00

