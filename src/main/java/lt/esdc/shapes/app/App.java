package lt.esdc.shapes.app;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.entity.Point;
import lt.esdc.shapes.exception.InvalidShapeException;
import lt.esdc.shapes.factory.OvalFactory;
import lt.esdc.shapes.reader.ShapeFileReader;
import lt.esdc.shapes.repository.ShapeRepository;
import lt.esdc.shapes.service.OvalService;
import lt.esdc.shapes.specifications.*;
import lt.esdc.shapes.warehouse.ShapeWarehouse;
import lt.esdc.shapes.warehouse.ShapeWarehouse.ShapeParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static final String OVALS_FILE_PATH = "resources/ovals.txt";

    private final Scanner scanner;
    private final ShapeFileReader fileReader;
    private final OvalService ovalService;
    private final OvalFactory ovalFactory;
    private final ShapeRepository shapeRepository;
    private final ShapeWarehouse shapeWarehouse;

    public App() {
        this.scanner = new Scanner(System.in);
        this.fileReader = new ShapeFileReader();
        this.ovalService = new OvalService();
        this.ovalFactory = new OvalFactory();
        this.shapeRepository = ShapeRepository.getInstance(this.ovalService);
        this.shapeWarehouse = ShapeWarehouse.getInstance();
    }

    public static void main(String[] args) {
        logger.info("Application starting...");
        App app = new App();
        app.run();
        logger.info("Application finished.");
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    loadOvalsFromFile();
                    break;
                case 2:
                    displayAllOvals();
                    break;
                case 3:
                    displayOvalDetails();
                    break;
                case 4:
                    searchOvalsMenu();
                    break;
                case 5:
                    sortOvalsMenu();
                    break;
                case 6:
                    displayWarehouseInformation();
                    break;
                case 7:
                    addNewOvalManually();
                    break;
                case 8:
                    editOval();
                    break;
                case 9:
                    removeOval();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n--- Oval Management System ---");
        System.out.println("1. Load ovals from file (" + OVALS_FILE_PATH + ")");
        System.out.println("2. Display all ovals");
        System.out.println("3. Display details for a specific oval");
        System.out.println("4. Search ovals by specification");
        System.out.println("5. Sort ovals");
        System.out.println("6. Display warehouse information");
        System.out.println("7. Add new oval manually");
        System.out.println("8. Edit an oval (demonstrates observer)");
        System.out.println("9. Remove an oval");
        System.out.println("0. Exit");
    }

    private int readIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter an integer: ");
                scanner.nextLine();
            }
        }
    }

    private long readLongInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                long value = scanner.nextLong();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a long integer: ");
                scanner.nextLine();
            }
        }
    }

    private double readDoubleInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number (e.g., 10.5): ");
                scanner.nextLine();
            }
        }
    }

    private String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }



    private void loadOvalsFromFile() {
        logger.info("Attempting to load ovals from file: {}", OVALS_FILE_PATH);
        List<Oval> ovals = fileReader.readOvalsFromFile(OVALS_FILE_PATH);
        shapeRepository.addAll(ovals);
        System.out.println(ovals.size() + " ovals processed from file.");
        if (!ovals.isEmpty()) {
            System.out.println("Warehouse should now be populated/updated with these ovals.");
        }
    }

    private void displayAllOvals() {
        List<Oval> ovals = shapeRepository.getAll();
        if (ovals.isEmpty()) {
            System.out.println("No ovals in the repository.");
            return;
        }
        System.out.println("\n--- All Ovals ---");
        for (Oval oval : ovals) {
            System.out.println(oval);
            shapeWarehouse.getParameters(oval.getId()).ifPresent(params ->
                    System.out.printf("  Warehouse: Area=%.2f, Perimeter=%.2f%n", params.area(), params.perimeter())
            );
        }
    }

    private void displayOvalDetails() {
        long id = readLongInput("Enter Oval ID to display details: ");
        Optional<Oval> ovalOpt = shapeRepository.getById(id);

        if (ovalOpt.isEmpty()) {
            System.out.println("Oval with ID " + id + " not found.");
            return;
        }

        Oval oval = ovalOpt.get();
        System.out.println("\n--- Details for Oval ID: " + id + " ---");
        System.out.println("Oval object: " + oval);
        System.out.println("Is Circle: " + ovalService.isCircle(oval));
        System.out.printf("Calculated Area: %.2f%n", ovalService.calculateArea(oval));
        System.out.printf("Calculated Perimeter: %.2f%n", ovalService.calculatePerimeter(oval));
        double[] semiAxes = ovalService.getSemiAxes(oval);
        System.out.printf("Semi-axes (a, b): %.2f, %.2f%n", semiAxes[0], semiAxes[1]);

        System.out.println("\n--- Warehouse data for Oval ID: " + id + " ---");
        shapeWarehouse.getParameters(oval.getId()).ifPresentOrElse(
                params -> System.out.printf("  Warehouse: Area=%.2f, Perimeter=%.2f%n", params.area(), params.perimeter()),
                () -> System.out.println("  No data in warehouse for this oval (this indicates an issue if oval exists).")
        );
    }

    private void searchOvalsMenu() {
        System.out.println("\n--- Search Ovals By ---");
        System.out.println("1. Area greater than a value");
        System.out.println("2. Is a Circle");
        System.out.println("3. ID less than a value");
        System.out.println("4. Perimeter less than a value");

        int criteriaChoice = readIntInput("Choose search criteria: ");
        Specification<Oval> spec;

        try {
            switch (criteriaChoice) {
                case 1:
                    double areaThreshold = readDoubleInput("Enter minimum area: ");
                    spec = new AreaGreaterThanTen(ovalService); // This spec uses hardcoded 10
                    spec = (oval) -> ovalService.calculateArea(oval) > areaThreshold;
                    System.out.printf("Searching for Ovals with Area > %.2f%n", areaThreshold);
                    break;
                case 2:
                    spec = new IsCircle(ovalService);
                    System.out.println("Searching for Ovals that are Circles.");
                    break;
                case 3:
                    long idThreshold = readLongInput("Enter maximum ID (exclusive): ");
                    spec = new IdLessThanOneThousand(); // This spec uses hardcoded 1000.
                    spec = (oval) -> oval.getId() < idThreshold;
                    System.out.printf("Searching for Ovals with ID < %d%n", idThreshold);
                    break;
                case 4:
                    double perimeterThreshold = readDoubleInput("Enter maximum perimeter: ");
                    spec = new PerimeterLessThanTwenty(ovalService); // This spec uses hardcoded 20.

                    spec = (oval) -> ovalService.calculatePerimeter(oval) < perimeterThreshold;
                    System.out.printf("Searching for Ovals with Perimeter < %.2f%n", perimeterThreshold);
                    break;
                default:
                    System.out.println("Invalid criteria choice.");
                    return;
            }
        } catch (Exception e) {
            System.out.println("Error setting up search criteria: " + e.getMessage());
            return;
        }


        if (spec != null) {
            List<Oval> results = shapeRepository.query(spec);
            if (results.isEmpty()) {
                System.out.println("No ovals found matching the criteria.");
            } else {
                System.out.println("\n--- Search Results (" + results.size() + " found) ---");
                results.forEach(System.out::println);
            }
        }
    }

    private void sortOvalsMenu() {
        System.out.println("\n--- Sort Ovals By ---");
        System.out.println("1. ID (Ascending)");
        System.out.println("2. ID (Descending)");
        System.out.println("3. Area (Ascending)");
        System.out.println("4. Area (Descending)");
        System.out.println("5. Perimeter (Ascending)");
        System.out.println("6. Perimeter (Descending)");

        int sortChoice = readIntInput("Choose sort criteria: ");
        List<Oval> sortedOvals;

        switch (sortChoice) {
            case 1: sortedOvals = shapeRepository.sortById(true); break;
            case 2: sortedOvals = shapeRepository.sortById(false); break;
            case 3: sortedOvals = shapeRepository.sortByArea(ovalService, true); break;
            case 4: sortedOvals = shapeRepository.sortByArea(ovalService, false); break;
            case 5: sortedOvals = shapeRepository.sortByPerimeter(ovalService, true); break;
            case 6: sortedOvals = shapeRepository.sortByPerimeter(ovalService, false); break;
            default:
                System.out.println("Invalid sort choice.");
                return;
        }

        if (sortedOvals == null || sortedOvals.isEmpty()) {
            System.out.println("No ovals to sort or repository is empty.");
        } else {
            System.out.println("\n--- Sorted Ovals ---");
            for (Oval o : sortedOvals) {
                System.out.print(o);
                if (sortChoice >= 3 && sortChoice <= 6) {
                    System.out.printf(" (Area: %.2f, Perimeter: %.2f)%n",
                            ovalService.calculateArea(o),
                            ovalService.calculatePerimeter(o));
                } else {
                    System.out.println();
                }
            }
        }
    }

    private void displayWarehouseInformation() {
        System.out.println("\n--- Warehouse Contents ---");
        Map<Long, ShapeParameters> allParams = shapeWarehouse.getAllParameters();
        if (allParams.isEmpty()) {
            System.out.println("Warehouse is empty.");
            return;
        }
        allParams.forEach((id, params) ->
                System.out.printf("Oval ID: %d -> Area: %.2f, Perimeter: %.2f%n",
                        id, params.area(), params.perimeter())
        );
    }

    private void addNewOvalManually() {
        System.out.println("\n--- Add New Oval Manually ---");
        System.out.println("Enter coordinates for the first point (P1) of the bounding rectangle:");
        double x1 = readDoubleInput("P1.X: ");
        double y1 = readDoubleInput("P1.Y: ");
        System.out.println("Enter coordinates for the second point (P2) of the bounding rectangle (diagonally opposite P1):");
        double x2 = readDoubleInput("P2.X: ");
        double y2 = readDoubleInput("P2.Y: ");

        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);

        try {
            Oval newOval = ovalFactory.createShape(p1, p2);
            shapeRepository.add(newOval);
            System.out.println("Successfully added new oval with ID: " + newOval.getId());
            System.out.println("Warehouse should now be updated.");
        } catch (InvalidShapeException e) {
            System.out.println("Error creating oval: " + e.getMessage());
            logger.warn("Failed to create oval manually: P1={}, P2={}, Error: {}", p1, p2, e.getMessage());
        }
    }

    private void editOval() {
        long id = readLongInput("Enter Oval ID to edit: ");
        Optional<Oval> ovalOpt = shapeRepository.getById(id);

        if (ovalOpt.isEmpty()) {
            System.out.println("Oval with ID " + id + " not found.");
            return;
        }
        Oval ovalToEdit = ovalOpt.get();

        System.out.println("Editing Oval: " + ovalToEdit);
        System.out.println("Warehouse state BEFORE edit:");
        shapeWarehouse.getParameters(id).ifPresent(p -> System.out.printf("  ID %d: Area=%.2f, Perimeter=%.2f%n", id, p.area(), p.perimeter()));


        System.out.println("\nCurrent Point 1 (e.g., top-left): " + ovalToEdit.getPointX());
        String newX1Str = readStringInput("Enter new X for Point 1 (or press Enter to keep current " + ovalToEdit.getPointX().x() + "): ");
        String newY1Str = readStringInput("Enter new Y for Point 1 (or press Enter to keep current " + ovalToEdit.getPointX().y() + "): ");

        System.out.println("\nCurrent Point 2 (e.g., bottom-right): " + ovalToEdit.getPointY());
        String newX2Str = readStringInput("Enter new X for Point 2 (or press Enter to keep current " + ovalToEdit.getPointY().x() + "): ");
        String newY2Str = readStringInput("Enter new Y for Point 2 (or press Enter to keep current " + ovalToEdit.getPointY().y() + "): ");

        try {
            Point currentP1 = ovalToEdit.getPointX();
            Point currentP2 = ovalToEdit.getPointY();

            double newX1 = newX1Str.trim().isEmpty() ? currentP1.x() : Double.parseDouble(newX1Str.trim());
            double newY1 = newY1Str.trim().isEmpty() ? currentP1.y() : Double.parseDouble(newY1Str.trim());
            double newX2 = newX2Str.trim().isEmpty() ? currentP2.x() : Double.parseDouble(newX2Str.trim());
            double newY2 = newY2Str.trim().isEmpty() ? currentP2.y() : Double.parseDouble(newY2Str.trim());

            Point newP1 = new Point(newX1, newY1);
            Point newP2 = new Point(newX2, newY2);


            if (Math.abs(newP1.x() - newP2.x()) < 1e-9 || Math.abs(newP1.y() - newP2.y()) < 1e-9) {
                System.out.println("Error: New points would make the oval degenerate. Edit cancelled.");
                return;
            }

            boolean p1Changed = !currentP1.equals(newP1);
            boolean p2Changed = !currentP2.equals(newP2);

            if (p1Changed) {
                ovalToEdit.setPointX(newP1);
                System.out.println("Point 1 updated.");
            }
            if (p2Changed) {
                ovalToEdit.setPointY(newP2);
                System.out.println("Point 2 updated.");
            }

            if (!p1Changed && !p2Changed) {
                System.out.println("No changes made to points.");
            } else {
                System.out.println("Oval ID " + id + " successfully updated.");
                logger.info("Oval ID {} updated. Old P1:{}, Old P2:{}. New P1:{}, New P2:{}", id, currentP1, currentP2, newP1, newP2);
                System.out.println("Warehouse state AFTER edit (observer should have updated it):");
                shapeWarehouse.getParameters(id).ifPresent(p -> System.out.printf("  ID %d: Area=%.2f, Perimeter=%.2f%n", id, p.area(), p.perimeter()));
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for coordinates. Edit cancelled.");
            logger.warn("Failed to parse coordinates during edit for oval ID {}: {}", id, e.getMessage());
        }
    }

    private void removeOval() {
        long id = readLongInput("Enter Oval ID to remove: ");
        Optional<Oval> ovalOpt = shapeRepository.getById(id);

        if (ovalOpt.isEmpty()) {
            System.out.println("Oval with ID " + id + " not found. Nothing to remove.");
            return;
        }

        System.out.println("Removing oval: " + ovalOpt.get());
        boolean removed = shapeRepository.remove(id);

        if (removed) {
            System.out.println("Oval ID " + id + " successfully removed.");
            System.out.println("Parameters for this oval should also be removed from the warehouse.");
            shapeWarehouse.getParameters(id).ifPresentOrElse(
                    params -> System.out.println("  Error: Parameters still exist in warehouse for ID " + id), // Should not happen
                    () -> System.out.println("  Confirmed: Parameters for ID " + id + " are not in warehouse.")
            );
        } else {
            System.out.println("Failed to remove oval ID " + id + ". It might have been removed by another process or an error occurred.");
        }
    }
}