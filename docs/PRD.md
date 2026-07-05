Coffee Brewing Journal — App Specifications

This app helps you remember your exact coffee recipes so you never have to repeat the frustrating process of "dialing in" your beans once you find the perfect cup.

Screen 1: Main Dashboard ("Coffee Log")
What it shows (Data to display)

    A list of all the coffee bags you have added, sorted alphabetically by name (case-insensitive).

    If you have marked any brew as a favorite for a specific coffee, that row shows a metric summary of the most recently favorited brew for that coffee, main metrics only:

        Dose (grounds weight), grind size, brew time, final liquid yield (weight).

    Empty state: when no coffees have been added yet, only the "ADD COFFEE" button is shown.

    A settings button (gear icon) fixed at the top right of the screen.

How you interact with it (Page navigation & Buttons)

    Tap a coffee name: Takes you to Screen 2 (Coffee History Log).

    Tap the "ADD COFFEE" button: A fixed full-width button at the bottom of the screen that takes you to Screen 4 (Add New Coffee).

    Tap the settings button (top right): Takes you to Screen 6 (Settings).

    Press and hold (Long tap) a row: Opens a pop-up window asking: "Are you sure you want to delete?" with a [Yes] and [Cancel] button. Deletes the coffee and all of its brews.

Screen 2: Coffee History Log
What it shows (Data to display)

    The coffee's name as the screen title.

    A list of every brewing attempt logged for this specific coffee bag.

    The list is sorted chronologically, showing the newest brew at the top and oldest at the bottom.

    Each brew card clearly displays:

        Settings used: Dose (grounds weight), grind setting (e.g., 5.1), brew time (in seconds), water temperature.

        Tools used: Status tags showing whether a dispenser tool and/or a leveler tool was used, displayed separately: DISP (on/off), LEVEL (on/off).

        Result: Final liquid yield (weight), liquid volume in ml (if entered), and score rating (1 to 5 stars, shown muted if not rated).

        The Golden Ratio: automatically calculated and displayed from dose and liquid yield (e.g., 1:2).

        Favorite status: A "★ FAV" status tag highlighted if this brew was marked as a favorite. Multiple brews can be favorites.

        The date the brew was logged.

    Empty state: when no brews have been logged yet, only the "ADD BREW" button is shown.

How you interact with it (Page navigation & Buttons)

    An edit (pencil) icon in the header: Takes you to Screen 5 (Edit Coffee), to rename the coffee or change its roast level.

    Tap the "ADD BREW" button: A fixed full-width button at the bottom of the screen that takes you to Screen 3 (New Brew Input).

    Press and hold (Long tap) a brew card: Opens a pop-up window asking: "Are you sure you want to delete?" with a [Yes] and [Cancel] button.

    Simple tap on a brew card: goes into the edit screen for that entry (Screen 3 in edit mode).

    Navigation: The back chevron (‹) in the header, or the Android system back gesture, returns to Screen 1.

Screen 3: New Brew Input Screen / Edit Screen
What you type in (Data input)

    Each numeric field is a "stepper": a `− value +` control where the value can also be tapped and typed directly (tapping selects the whole value for quick replacement).

    Dose — grounds weight in grams (stepper, decimal).

    Grind size number (e.g., 5.1) (stepper, decimal).

    Brew time in seconds (stepper, whole number).

    Yield — final liquid weight in grams (stepper, decimal).

    Ratio — read-only, live-calculated from Dose and Yield as they're entered (e.g., 1:2), displayed as a pill, not user-editable.

    Water temperature (stepper, decimal; no unit enforced — user enters whatever they use).

    Final liquid volume in milliliters (stepper, whole number, optional).

    Dispenser tool used? (Yes/No switch, labeled DISP).

    Leveler tool used? (Yes/No switch, labeled LEVEL).

    Mark as Favorite? (Yes/No switch, labeled FAV).

    Rating score: tap 1 to 5 stars to set; tapping the currently-set top star clears the rating.

    Notes — free-form multiline tasting notes (optional).

Buttons (Fixed to the bottom of the screen)

    SAVE button: Saves this brew to the top of the history log and returns to Screen 2. Disabled until the mandatory fields (Dose, Yield) are filled.

    CANCEL button: Returns to Screen 2 without saving or applying any changes.

    Navigation: The back chevron (‹) in the header, and the Android system back gesture, behave the same as CANCEL.

    Header subtitle: shows the coffee name, plus "· UNSAVED" when creating a new brew (vs. editing an existing one).

Screen 4: Add New Coffee Screen
What you type in (Data input)

    Name — coffee brand / bean name (Mandatory).

    Roast level — LIGHT / MEDIUM / DARK segmented single-choice control (optional).

Buttons (Fixed to the bottom of the screen)

    ADD button: Saves the new coffee bag to the master list and returns to Screen 1. Disabled until Name is filled.

    CANCEL button: Discards what was typed and returns to Screen 1 without saving.

    Navigation: The back chevron (‹) in the header, and the Android system back gesture, behave the same as CANCEL.

Screen 5: Edit Coffee Screen
What you type in (Data input)

    Name — coffee brand / bean name (Mandatory).

    Roast level — LIGHT / MEDIUM / DARK segmented single-choice control (optional).

    Pre-filled with the coffee's current name and roast level.

Buttons (Fixed to the bottom of the screen)

    SAVE button: Updates the coffee bag and returns to Screen 2. Disabled until Name is filled.

    CANCEL button: Discards changes and returns to Screen 2.

    Navigation: The back chevron (‹) in the header, and the Android system back gesture, behave the same as CANCEL.

Screen 6: Settings Screen
What it shows (Data to display)

    A "DATA" section with two cards: "Import Data" (subtitle: "Restore from a JSON backup") and "Export Data" (subtitle: "Download all brews as JSON").

    An "APPEARANCE" section with a "Dark theme" card (subtitle: "Graphite palette") containing an on/off switch. Defaults to the system theme until explicitly toggled.

How you interact with it (Page navigation & Buttons)

    Tap "Import Data": Opens a file picker to choose a previously exported JSON file and loads it, replacing the current coffees and brew history. Shows a confirmation/error message when done.

    Tap "Export Data": Opens a file picker to choose where to save a JSON file containing all current coffees and brew history. Shows a confirmation/error message when done.

    Tap "Dark theme" (row or switch): Toggles the app's theme between light and the dark "Graphite" palette.

    Navigation: The back chevron (‹) in the header, or the Android system back gesture, returns to Screen 1.
