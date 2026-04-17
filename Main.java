import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Step 1: Vehicle Class
enum VehicleType { BIKE, CAR, TRUCK }

class Vehicle {
    private String vehicleId;
    private VehicleType type;
    private LocalDateTime entryTime;

    public Vehicle(String vehicleId, VehicleType type) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.entryTime = LocalDateTime.now(); // Records current time
    }

    public String getVehicleId() { return vehicleId; }
    public VehicleType getType() { return type; }
    public LocalDateTime getEntryTime() { return entryTime; }
}

// Step 2: Parking Slot Class
class Slot {
    private int slotNumber;
    private VehicleType slotType;
    private Vehicle parkedVehicle;

    public Slot(int slotNumber, VehicleType slotType) {
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.parkedVehicle = null;
    }

    public boolean isAvailable() { return parkedVehicle == null; }
    public void occupySlot(Vehicle vehicle) { this.parkedVehicle = vehicle; }
    public void vacateSlot() { this.parkedVehicle = null; }
    
    public int getSlotNumber() { return slotNumber; }
    public VehicleType getSlotType() { return slotType; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }
}

class ParkingLot {
    private List<Slot> slots;
    public ParkingLot(int bikeSlots, int carSlots, int truckSlots) {
        slots = new ArrayList<>();
        int id = 1;
        for (int i = 0; i < bikeSlots; i++) slots.add(new Slot(id++, VehicleType.BIKE));
        for (int i = 0; i < carSlots; i++) slots.add(new Slot(id++, VehicleType.CAR));
        for (int i = 0; i < truckSlots; i++) slots.add(new Slot(id++, VehicleType.TRUCK));
    }
    public void parkVehicle(String vehicleId, String typeStr) {
        try {
            VehicleType type = VehicleType.valueOf(typeStr.toUpperCase());
            
            // Logic: Find available slot matching type
            for (Slot slot : slots) {
                if (slot.isAvailable() && slot.getSlotType() == type) {
                    Vehicle vehicle = new Vehicle(vehicleId, type);
                    slot.occupySlot(vehicle);
                    System.out.println("Vehicle " + vehicleId + " parked in Slot " + slot.getSlotNumber());
                    return;
                }
            }
            System.out.println("No available slots for " + type);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Vehicle Type!");
        }
    }
    public void removeVehicle(String vehicleId) {
        for (Slot slot : slots) {
            if (!slot.isAvailable() && slot.getParkedVehicle().getVehicleId().equalsIgnoreCase(vehicleId)) {
                Vehicle v = slot.getParkedVehicle();
                calculateFee(v);
                slot.vacateSlot();
                System.out.println("Vehicle " + vehicleId + " removed from Slot " + slot.getSlotNumber());
                return;
            }
        }
        System.out.println("Vehicle ID not found!");
    }
    private void calculateFee(Vehicle vehicle) {
        long durationSeconds = Duration.between(vehicle.getEntryTime(), LocalDateTime.now()).getSeconds();
        // Simulation: 1 second = 1 hour for testing
        double rate = (vehicle.getType() == VehicleType.BIKE) ? 10 : (vehicle.getType() == VehicleType.CAR) ? 20 : 50;
        double fee = Math.max(rate, durationSeconds * rate); 
        System.out.println("Parking Duration: " + durationSeconds + " hours (simulated)");
        System.out.println("Total Fee: ₹" + fee);
    }
    public void displayStatus() {
        int available = 0;
        for (Slot slot : slots) {
            if (slot.isAvailable()) available++;
        }
        System.out.println("\n--- Parking Status ---");
        System.out.println("Total Slots: " + slots.size());
        System.out.println("Available Slots: " + available);
        System.out.println("Occupied Slots: " + (slots.size() - available));
    }
}
public class Main {
    public static void main(String[] args) {
        ParkingLot myLot = new ParkingLot(3, 2, 1); 
        Scanner scanner = new Scanner(System.in);        while (true) {
            System.out.println("\n1. Park Vehicle\n2. Remove Vehicle\n3. View Slots\n4. Exit");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter Vehicle ID: ");
                String id = scanner.next();
                System.out.print("Enter Type (Bike/Car/Truck): ");
                String type = scanner.next();
                myLot.parkVehicle(id, type);
            } else if (choice == 2) {
                System.out.print("Enter Vehicle ID to remove: ");
                String id = scanner.next();
                myLot.removeVehicle(id);
            } else if (choice == 3) {
                myLot.displayStatus();
            } else {
                break;
            }
        }
        scanner.close();
    }
}