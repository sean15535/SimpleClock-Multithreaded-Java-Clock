import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

/**
 * SimpleClock demonstrates a two-thread clock:
 *  - A background updater thread refreshes the current time frequently.
 *  - A display thread prints the time every second.
 *
 * The display thread is given a higher priority than the updater, to illustrate
 * task prioritization in Java's thread model.
 *
 * Usage:
 *   javac SimpleClock.java
 *   java SimpleClock            // runs until interrupted (Ctrl+C)
 *   java SimpleClock 15         // runs for 15 seconds then stops
 */
public class SimpleClock {

    public static void main(String[] args) {
        // Optional: run duration in seconds (default: run until interrupted)
        Long runDurationSeconds = null;
        if (args.length > 0) {
            try {
                runDurationSeconds = Long.parseLong(args[0]);
                if (runDurationSeconds <= 0) {
                    System.err.println("Run duration must be a positive integer. Running until interrupted instead.");
                    runDurationSeconds = null;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid argument. Provide a positive integer for run duration in seconds.");
                System.err.println("Example: java SimpleClock 20");
            }
        }

        Clock clock = new Clock("HH:mm:ss dd-MM-yyyy");

        // Ensure we stop cleanly if the program is terminated (Ctrl+C, kill, etc.)
        Runtime.getRuntime().addShutdownHook(new Thread(clock::stop, "Clock-ShutdownHook"));

        try {
            clock.start(); // Start both threads

            if (runDurationSeconds == null) {
                // Run indefinitely until user interrupts
                Thread.currentThread().join();
            } else {
                // Run for the specified duration, then stop
                Thread.sleep(runDurationSeconds * 1000);
                clock.stop();
            }
        } catch (InterruptedException e) {
            // Restore interrupt flag and stop gracefully
            Thread.currentThread().interrupt();
            clock.stop();
        }
    }

    /**
     * Clock is responsible for:
     *  - Holding the current date-time safely (shared between threads).
     *  - Running an updater thread to refresh the time frequently.
     *  - Running a display thread to print the time every second.
     */
    static class Clock {
        private final AtomicReference<LocalDateTime> currentDateTime = new AtomicReference<>(LocalDateTime.now());
        private final DateTimeFormatter formatter;

        private volatile boolean running = false;

        private Thread updaterThread;
        private Thread displayThread;

        // Tuning knobs:
        private static final long UPDATE_INTERVAL_MS = 200L;  // how often we refresh LocalDateTime.now()
        private static final long DISPLAY_INTERVAL_MS = 1000L; // how often we print to console

        public Clock(String pattern) {
            this.formatter = DateTimeFormatter.ofPattern(pattern);
        }

        /**
         * Starts the updater and display threads. Safe to call once.
         */
        public synchronized void start() {
            if (running) return;
            running = true;

            // Create and configure the updater (background) thread
            updaterThread = new Thread(this::runUpdater, "Clock-Updater");
            // Lower priority (hint) than display thread to favor printing precision
            updaterThread.setPriority(Math.max(Thread.NORM_PRIORITY - 1, Thread.MIN_PRIORITY));

            // Create and configure the display (foreground) thread
            displayThread = new Thread(this::runDisplay, "Clock-Display");
            // Higher priority (hint) to favor timely printing
            displayThread.setPriority(Math.min(Thread.NORM_PRIORITY + 2, Thread.MAX_PRIORITY));

            // Start both
            updaterThread.start();
            displayThread.start();
        }

        /**
         * Requests both threads to stop and waits for them to finish.
         */
        public synchronized void stop() {
            if (!running) return;
            running = false;

            // Interrupt both threads to break sleep() promptly
            if (updaterThread != null) updaterThread.interrupt();
            if (displayThread != null) displayThread.interrupt();

            // Join threads to ensure a clean shutdown
            try {
                if (updaterThread != null) updaterThread.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt
            }
            try {
                if (displayThread != null) displayThread.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        /**
         * Background updater: refreshes the shared time frequently.
         */
        private void runUpdater() {
            try {
                while (running) {
                    currentDateTime.set(LocalDateTime.now());
                    Thread.sleep(UPDATE_INTERVAL_MS);
                }
            } catch (InterruptedException e) {
                // Expected on shutdown: exit loop
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("[Updater] Unexpected error: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }

        /**
         * Display thread: prints the current time at a steady interval.
         */
        private void runDisplay() {
            try {
                while (running) {
                    LocalDateTime snapshot = currentDateTime.get();
                    String formatted = snapshot.format(formatter);
                    System.out.println(formatted);
                    Thread.sleep(DISPLAY_INTERVAL_MS);
                }
            } catch (InterruptedException e) {
                // Expected on shutdown: exit loop
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("[Display] Unexpected error: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }
}