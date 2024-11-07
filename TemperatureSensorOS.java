import java.util.Scanner;

class TemperatureSensorOS {
    private static final int BUFFER_SIZE = 10; // Size of the circular buffer for storing temperature readings
    private double[] temperatureBuffer; // Array to hold temperature readings
    private int index; // Current index for storing new temperature readings
    private boolean isPowerSavingMode; // Flag to indicate if the system is in power-saving mode
    private int dataCount; // Counter to keep track of the number of valid temperature readings
    private volatile boolean running; // Flag to control the running state of the data collection
    private Thread dataCollectionThread; // Thread for collecting temperature data

    // Constructor to initialize the TemperatureSensorOS
    public TemperatureSensorOS() {
        temperatureBuffer = new double[BUFFER_SIZE]; // Initialize the buffer
        index = 0; // Start at the beginning of the buffer
        isPowerSavingMode = false; // System starts in normal mode
        dataCount = 0; // No data collected initially
        running = true; // Start the running state
        initializeSystem(); // Call the initialization method
    }

    // System Initialization
    private void initializeSystem() {
        System.out.println("Initializing Temperature Sensor OS...");
        // Simulate sensor initialization
        System.out.println("Temperature sensor initialized.");
        System.out.println("Data storage initialized.");
        // Simulate setting up interrupt handler for power-saving features
        System.out.println("Power-saving mode set up.");
        System.out.println("System initialized successfully.");
    }

    // Data Collection Loop
    public void collectData() {
        System.out.println("Starting data collection...");
        while (running) { // Run while the system is in the running state
            if (!isPowerSavingMode) {
                double newTemperature = readTemperature(); // Read temperature from the sensor
                storeTemperature(newTemperature); // Store the new temperature reading
                System.out.println("Collected Temperature: " + newTemperature); // Output the collected temperature
            }
            // Simulate delay for data collection
            try {
                Thread.sleep(1000); // Collect data every second
            } catch (InterruptedException e) {
                System.out.println("Data collection interrupted."); // Handle interruption gracefully
            }
        }
    }

    // Simulated method to read temperature from the sensor
    private double readTemperature() {
        // Simulate reading temperature (for example, between 15 to 30 degrees Celsius)
        return 15 + Math.random() * 15; // Random temperature between 15 and 30
    }

    // Store temperature in the circular buffer
    private void storeTemperature(double temperature) {
        temperatureBuffer[index] = temperature; // Store the new temperature at the current index
        index = (index + 1) % BUFFER_SIZE; // Move to the next index, wrap around using modulo for circular behavior
        if (dataCount < BUFFER_SIZE) {
            dataCount++; // Increment data count if buffer is not yet full
        }
    }

    // Calculate average temperature from stored readings
    private double calculateAverage() {
        if (dataCount == 0) {
            System.out.println("No data available to calculate average."); // Inform user if no data is present
            return 0; // Return 0 to avoid NaN
        }
        double sum = 0; // Initialize sum variable to accumulate temperatures
        for (int i = 0; i < dataCount; i++) {
            sum += temperatureBuffer[i]; // Add each temperature to the sum
        }
        return sum / dataCount; // Return average
    }

    // Calculate minimum temperature from stored readings
    private double calculateMin() {
        if (dataCount == 0) {
            System.out.println("No data available to calculate minimum."); // Inform user if no data is present
            return 0; // Return 0 to avoid NaN
        }
        double min = Double.MAX_VALUE; // Start with the highest possible value
        for (int i = 0; i < dataCount; i++) {
            if (temperatureBuffer[i] < min) { // If current temperature is lower than min
                min = temperatureBuffer[i]; // Update min
            }
        }
        return min; // Return minimum temperature
    }

    // Calculate maximum temperature from stored readings
    private double calculateMax() {
        if (dataCount == 0) {
            System.out.println("No data available to calculate maximum."); // Inform user if no data is present
            return 0; // Return 0 to avoid NaN
        }
        double max = Double.MIN_VALUE; // Start with the lowest possible value
        for (int i = 0; i < dataCount; i++) {
            if (temperatureBuffer[i] > max) { // If current temperature is higher than max
                max = temperatureBuffer[i]; // Update max
            }
        }
        return max; // Return maximum temperature
    }

    // User command interface for interacting with the system
    public void commandInterface() {
        Scanner scanner = new Scanner(System.in); // Create a scanner for user input
        System.out.println("Enter command (DISPLAY AVG, DISPLAY MINMAX, RESET, TOGGLE POWER, EXIT):");

        while (true) {
            String command = scanner.nextLine().trim().toUpperCase(); // Read command from user, trim whitespace, and convert to uppercase
            switch (command) {
                case "DISPLAY AVG":
                    // Display the average temperature
                    System.out.println("Average Temperature: " + calculateAverage());
                    break;
                case "DISPLAY MINMAX":
                    // Display minimum and maximum temperatures
                    System.out.println("Minimum Temperature: " + calculateMin());
                    System.out.println("Maximum Temperature: " + calculateMax());
                    break;
                case "RESET":
                    // Clear all stored temperature data
                    resetData();
                    System.out.println("Data reset.");
                    break;
                case "TOGGLE POWER":
                    // Toggle the power-saving mode
                    togglePowerSavingMode();
                    break;
                case "EXIT":
                    running = false; // Stop the data collection
                    System.out.println("Exiting the system...");
                    scanner.close(); // Close the scanner
                    return; // Exit the command interface
                default:
                    System.out.println("Unknown command."); // Handle unrecognized commands
                    break;
            }
        }
    }

    // Reset temperature data to clear stored readings
    private void resetData() {
        temperatureBuffer = new double[BUFFER_SIZE]; // Reinitialize the buffer
        index = 0; // Reset index
        dataCount = 0; // Reset data count
    }

    // Toggle power-saving mode to stop/resume data collection
    private void togglePowerSavingMode() {
        isPowerSavingMode = !isPowerSavingMode; // Toggle the power-saving flag
        if (isPowerSavingMode) {
            System.out.println("Power-saving mode activated. Data collection paused."); // Notify user
        } else {
            System.out.println("Power-saving mode deactivated. Resuming data collection."); // Notify user
        }
    }

    // Main method to start the program
    public static void main(String[] args) {
        TemperatureSensorOS sensorOS = new TemperatureSensorOS(); // Create an instance of the OS
        sensorOS.dataCollectionThread = new Thread(sensorOS::collectData); // Create a thread for data collection
        sensorOS.dataCollectionThread.start(); // Start the data collection thread
        sensorOS.commandInterface(); // Start the command line interface for user interaction
        try {
            sensorOS.dataCollectionThread.join(); // Wait for the data collection thread to finish before exiting
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }
    }
}