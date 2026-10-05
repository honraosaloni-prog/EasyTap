# EasyTap

**Just Ask. We'll Guide You.**

EasyTap is a privacy-first, multilingual AI assistant for Android, designed for elderly and digitally inexperienced users. The user asks, by voice or text, what they want to do on their phone. EasyTap replies with simple guidance, one step at a time, and when the user turns on Screen Assistance it adapts to what is currently on screen.

> **Principle: guide, don't blindly act.** EasyTap explains and points. The user stays in control. When uncertain: stop, then explain.

## Project status

Early development. This repository currently contains the **project skeleton** (Phase 0 of 10):

- Gradle setup (Kotlin, Jetpack Compose, Hilt, KSP)
- Hardened manifest (no cleartext traffic, backups disabled, `INTERNET` is the only launch permission)
- Core types: `AppResult` and `AppError`, `RiskLevel`, `GuidanceStep`
- The `AIService` provider interface
- Hilt dispatcher module
- Package layout for every planned layer

Nothing beyond that is implemented yet. Features below are the roadmap, not the current state.

## Planned features

- Text and voice input, with step-by-step answers
- Screen-aware guidance through Android accessibility and UI-hierarchy information, with optional temporary capture and OCR
- A visual overlay that shows where to tap
- Languages: English, Hindi and Marathi, with room for more Indian languages
- Elderly-first UI: large touch targets, high contrast, TalkBack support, adjustable text and speech speed
- Offline mode for basic tasks (open Settings, Wi-Fi, Bluetooth, alarms, text size)

First supported tasks: Wi-Fi, Bluetooth, font size, alarm, Camera, WhatsApp, and how to send a photo, make a call and take a screenshot.

## Safety model

Actions are classified before any guidance is shown:

| Risk | Examples | Behaviour |
|------|----------|-----------|
| LOW | Open Settings, Wi-Fi settings, font size | Guided normally |
| MEDIUM | Send a message, share a photo, delete a file | Confirmation before the final step |
| HIGH | Banking, UPI, passwords, OTPs, account deletion | Never performed. EasyTap explains how to do it safely |

AI output is never executed. The model returns a structured step that is validated against an allow-list of actions:

```
User → AI → Task Planner → Safety Validator → Guidance Engine → User
```

Screen text is treated as untrusted content and is kept separate from user intent and system instructions, which protects against prompt injection. Low-confidence results stop the flow instead of guessing.

## Privacy

- No screenshots or screen history are stored. Capture, process, discard.
- Sensitive screens (banking, payments, OTP, password and card fields) are detected locally, and analysis pauses.
- Passwords, PINs, OTPs and CVVs are never requested, stored or sent to a model.
- Conversation history is off by default. When enabled it is encrypted and deletable.
- Permissions are requested only at the moment of use, after a plain-language explanation.
- No API keys in the app. AI calls go through an authenticated backend proxy.
- Logs contain only anonymised event codes, never user content.

## Architecture

Clean Architecture with dependency injection (Hilt):

```
app/src/main/java/com/easytap/app/
├── presentation/    Compose UI, ViewModels
├── domain/          models, use cases, repository and AIService interfaces
├── data/            repositories
├── ai/              AIService implementations (cloud, local)
├── screen/          temporary screen analysis, OCR
├── accessibility/   accessibility service, UI hierarchy reader
├── privacy/         sensitive-screen detection, data filtering
├── security/        response validation, injection guard
├── permissions/     permission explanations and requests
├── network/         HTTPS client, auth, retries, rate-limit handling
├── storage/         encrypted preferences and history
└── core/            AppResult, errors, DI modules
```

`AIService` keeps the app independent of any one provider, so cloud, on-device and different LLMs can be swapped without rewriting the app.

## Roadmap

1. Compose UI and navigation
2. Text AI assistant
3. Voice input and output
4. Task planning and structured responses
5. Accessibility and screen understanding
6. Temporary screen analysis and visual guidance
7. Privacy and security layer
8. Sensitive-screen protection
9. Multilingual support
10. Testing, security audit and polish

## Getting started

Requirements: Android Studio (latest stable), JDK 17, Android SDK 35.

**Run on a phone**

1. On the phone, enable Developer options (tap Build number 7 times), then turn on USB debugging.
2. Connect it by USB and accept the debugging prompt.
3. Open the project in Android Studio, wait for Gradle sync, and press **Run**.

**Command line**

```bash
gradle assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Build in CI (GitHub Actions)**

A workflow that builds the debug APK and uploads it as an artifact can be added at `.github/workflows/build.yml`. GitHub cannot run the app itself because it has no device or emulator.

## Platform notes

- Modern Android does not let apps silently toggle Wi-Fi or Bluetooth. EasyTap opens the relevant settings screen and guides the user.
- Screen Assistance uses an accessibility service that the user must enable manually in Settings. A visible indicator (🟢 Active / ⚪ Off) and a Stop button are always available.
- Accessibility APIs are used only to understand the screen for guidance, never as hidden automation.

## Testing

Planned coverage: unit tests (intent parsing, risk classification, privacy filtering, sensitive-screen detection, response validation), UI tests (assistant, voice, settings, screen-assistance controls) and security tests (no secrets in the app, no sensitive data persisted or sent, invalid AI output rejected).

## License

To be decided.

