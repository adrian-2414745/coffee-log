# Coffee Log — Navigation

Screen flow as implemented in `CoffeeLogNavGraph.kt`.

```mermaid
flowchart TD
    Dashboard["Screen 1: Dashboard\n(Coffee Index)"]
    History["Screen 2: Coffee History Log"]
    BrewEdit["Screen 3: New/Edit Brew"]
    AddCoffee["Screen 4: Add New Coffee"]
    EditCoffee["Screen 5: Edit Coffee"]
    Settings["Screen 6: Settings"]

    Dashboard -- "tap coffee name" --> History
    Dashboard -- "tap +" --> AddCoffee
    Dashboard -- "tap settings icon" --> Settings

    History -- "tap +" --> BrewEdit
    History -- "tap brew card" --> BrewEdit
    History -- "tap edit (pencil) icon" --> EditCoffee
    History -- "back ‹ / system back" --> Dashboard

    BrewEdit -- "SAVE / CANCEL / back ‹" --> History

    AddCoffee -- "ADD TO INDEX / CANCEL / back ‹" --> Dashboard

    EditCoffee -- "SAVE / CANCEL / back ‹" --> History

    Settings -- "back ‹ / system back" --> Dashboard
```

Notes:
- All non-dashboard screens pop back to their caller rather than navigating forward, so the diagram's return edges are back-stack pops, not new destinations.
- `BrewEdit` is reused for both "New Brew" (no `brewId`) and "Edit Brew" (existing `brewId`); see PRD.md Screen 3.
- Long-press on a Dashboard row or History brew card opens a delete confirmation dialog in place — no navigation.
