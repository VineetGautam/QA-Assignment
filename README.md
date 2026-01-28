# UI Automation Playwright Tests - Java

This is a Maven-based project containing comprehensive Playwright tests for the UI automation assignment covering 5 different test scenarios.

## Project Structure

```
.
├── src/
│   └── test/
│       └── java/
│           └── com/qa/automation/tests/
│               ├── DelayedButtonTest.java        # Test 1: Delayed Button Flow
│               ├── LazyListTest.java             # Test 2: Load and Verify List Items
│               ├── DynamicIdsTest.java           # Test 3: Dynamic ID Handling
│               ├── ConditionalRenderTest.java    # Test 4: Conditional Login Flow
│               └── ModalFlowTest.java            # Test 5: Modal Confirmation Flow
├── pom.xml                           # Maven configuration
├── .gitignore                        # Git ignore rules
└── README.md                         # This file
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Installation

1. Clone or download the project
2. Install dependencies:
```bash
mvn clean install
```

This will download and install:
- Playwright Java library
- JUnit 4 testing framework
- All required Maven plugins

## Running Tests

Run all tests:
```bash
mvn test
```

Run a specific test:
```bash
mvn test -Dtest=DelayedButtonTest
```

Run tests with verbose output:
```bash
mvn test -X
```

## Test Descriptions

### Test 1: Delayed Button Flow (`DelayedButtonTest.java`)
Tests the ability to handle asynchronous button state changes:
- Navigates to Timing Challenges tab
- Clicks "Start Process" button
- Waits for "Confirm Action" button to become **enabled** (not just visible)
- Clicks the confirm button
- Verifies success message appears
- **No hardcoded waits** - uses proper wait conditions
- Completes in under 10 seconds

### Test 2: Load and Verify List Items (`LazyListTest.java`)
Tests lazy loading and list verification:
- Navigates to Timing Challenges tab
- Clicks "Load More Items" 3 times
- Waits for loading to complete after each click
- Verifies exactly 15 items are displayed
- Confirms at least one "active" status item exists
- Confirms at least one "pending" status item exists

### Test 3: Dynamic ID Handling (`DynamicIdsTest.java`)
Tests selector resilience with dynamic attributes:
- Navigates to Flaky Selectors tab
- Regenerates IDs multiple times
- Selects "Beta" item using **text selector only** (not ID attribute)
- Verifies "Beta" is shown as selected
- Works regardless of ID regeneration

### Test 4: Conditional Login Flow (`ConditionalRenderTest.java`)
Tests conditional rendering based on login state:
- Navigates to Flaky Selectors tab
- Logs in as "Admin User"
- Verifies Admin Panel is visible, Standard Panel is hidden
- Logs out
- Logs in as "Standard User"
- Verifies Standard Panel is visible, Admin Panel is hidden
- Handles loading states between login and dashboard display

### Test 5: Modal Confirmation Flow (`ModalFlowTest.java`)
Tests nested modal interaction and z-index handling:
- Navigates to Responsive tab
- Opens the first modal
- Clicks "Show Details" within the modal
- Opens nested modal
- Clicks "Confirm" in the nested modal
- Verifies both modals close properly
- Verifies result shows "confirmed"
- Correctly handles modal layering

## Key Features

✅ **No Hardcoded Waits** - All tests use proper wait conditions:
- `page.waitForLoadState(LoadState.NETWORKIDLE)` for network completion
- `page.waitForSelector()` for element visibility
- `page.waitForFunction()` for condition-based waits

✅ **Flaky Selector Resilience**:
- Uses text-based selectors where possible
- Avoids relying on dynamic ID attributes
- Uses semantic selectors (role attributes)
- Handles hidden/visible state properly

✅ **Proper Async Handling**:
- Waits for loading states between actions
- Handles network delays
- Verifies element state, not just visibility

✅ **Acceptance Criteria Met**:
- All tests complete in under 10 seconds
- Tests pass consistently without timing issues
- No hardcoded `page.waitForTimeout()` except where minimal
- Proper modal layer handling with correct z-index interaction

## Browser Support

Tests run on:
- Chromium (default)
- Firefox (optional)
- WebKit (optional)

## Maven Configuration

The `pom.xml` file includes:
- Playwright Java dependency
- JUnit 4 for testing framework
- Maven Compiler Plugin for Java 11 compilation
- Maven Surefire Plugin for test execution
- Proper test discovery patterns

## Build and Test

```bash
# Clean build
mvn clean

# Build project
mvn build

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=DelayedButtonTest
```

## Notes

- Tests are designed to be resilient to timing variations
- Uses proper wait strategies instead of hardcoded delays
- Selectors prioritize text and semantic attributes
- Modal interactions use proper z-index handling
- Uses Java 11 for better language features and compatibility
