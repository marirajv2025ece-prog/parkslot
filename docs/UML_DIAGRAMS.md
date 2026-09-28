# UML diagrams (Mermaid)

View them by pasting each block into https://mermaid.live (export PNG/SVG for the report), or open this file in VS Code / GitHub.

## 1. Use case diagram
```mermaid
flowchart LR
    G([Gate Security])
    subgraph ParkSpot System
        UC0([Login])
        UC1([Register Visitor Vehicle])
        UC2([Assign Parking Slot])
        UC3([View Available Slots])
        UC4([View Occupied Slots])
        UC5([Record Vehicle Exit])
        UC6([View Active Parking])
        UC7([Generate Daily Visitor Report])
        UC8([Search Parking History])
    end
    G --- UC0
    G --- UC1
    G --- UC2
    G --- UC3
    G --- UC4
    G --- UC5
    G --- UC6
    G --- UC7
    G --- UC8
    UC1 -. includes .-> UC2
```

## 2. Class diagram
```mermaid
classDiagram
    class VisitorVehicle {
        Long id
        String vehicleNumber
        String vehicleType
        String visitorName
        String mobileNumber
    }
    class Flat {
        Long id
        String flatNumber
        String residentName
    }
    class ParkingSlot {
        Long id
        String slotNumber
        SlotStatus status
    }
    class ParkingRecord {
        Long id
        String purpose
        LocalDateTime entryTime
        LocalDateTime exitTime
        ParkingStatus status
    }
    class ParkingController
    class ParkingService {
        registerEntry()
        recordExit()
        getActiveRecords()
        getHistory()
        getDailyReport()
    }
    class ParkingRecordRepository
    class ParkingSlotRepository
    class VehicleService
    class FlatService

    VisitorVehicle "1" --> "0..*" ParkingRecord : has visits
    Flat "1" <-- "0..*" ParkingRecord : visits
    ParkingSlot "1" <-- "0..*" ParkingRecord : uses
    ParkingController --> ParkingService
    ParkingService --> ParkingRecordRepository
    ParkingService --> ParkingSlotRepository
    ParkingService --> VehicleService
    ParkingService --> FlatService
    ParkingRecordRepository ..> ParkingRecord
    ParkingSlotRepository ..> ParkingSlot
```

## 3. ER diagram
```mermaid
erDiagram
    VISITOR_VEHICLE ||--o{ PARKING_RECORD : "has visits"
    FLAT ||--o{ PARKING_RECORD : "is visited in"
    PARKING_SLOT ||--o{ PARKING_RECORD : "is used in"

    VISITOR_VEHICLE {
        bigint id PK
        varchar vehicle_number UK
        varchar vehicle_type
        varchar visitor_name
        varchar mobile_number
    }
    FLAT {
        bigint id PK
        varchar flat_number UK
        varchar resident_name
    }
    PARKING_SLOT {
        bigint id PK
        varchar slot_number UK
        varchar status
    }
    PARKING_RECORD {
        bigint id PK
        bigint vehicle_id FK
        bigint flat_id FK
        bigint slot_id FK
        varchar purpose
        datetime entry_time
        datetime exit_time
        varchar status
    }
    STAFF_USER {
        bigint id PK
        varchar username UK
        varchar password_hash
        varchar full_name
    }
```

## 4. Sequence diagram – vehicle entry
```mermaid
sequenceDiagram
    actor S as Security
    participant F as Frontend (register.js)
    participant C as ParkingController
    participant V as ParkingService
    participant R as Repositories
    participant DB as MySQL
    S->>F: Fill form, click Register
    F->>C: POST /api/parking/entry (JSON)
    C->>C: @Valid validation
    C->>V: registerEntry(request)
    V->>R: find slot (locked)
    R->>DB: SELECT ... FOR UPDATE
    DB-->>R: slot
    V->>R: find flat, find or register vehicle
    V->>V: vehicle already parked? slot occupied?
    alt rule broken
        V-->>C: throws exception
        C-->>F: 404 / 409 JSON error
        F-->>S: red error message
    else all rules OK
        V->>R: save record (ACTIVE), slot = OCCUPIED
        R->>DB: INSERT / UPDATE
        V-->>C: ParkingRecordResponse
        C-->>F: 201 Created
        F-->>S: success message
    end
```

## 5. Sequence diagram – vehicle exit
```mermaid
sequenceDiagram
    actor S as Security
    participant F as Frontend (active.js)
    participant C as ParkingController
    participant V as ParkingService
    participant R as Repositories
    participant DB as MySQL
    S->>F: Click Exit
    F->>C: POST /api/parking/exit/{id}
    C->>V: recordExit(id)
    V->>R: findById(id)
    R->>DB: SELECT
    DB-->>R: record
    V->>V: already COMPLETED? entry time exists?
    alt already completed
        V-->>C: InvalidParkingOperationException
        C-->>F: 409 "Parking record has already been completed."
    else active
        V->>V: exitTime = now, status = COMPLETED
        V->>R: save record, slot = AVAILABLE
        R->>DB: UPDATE parking_record, UPDATE parking_slot
        V-->>C: ParkingRecordResponse
        C-->>F: 200 OK
        F->>F: reload active table
        F-->>S: "Slot is now AVAILABLE"
    end
```
