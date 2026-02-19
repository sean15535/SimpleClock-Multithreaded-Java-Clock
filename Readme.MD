# ⏰ SimpleClock --- Multithreaded Java Digital Clock

![Java](https://img.shields.io/badge/Java-8%2B-orange)
![Concurrency](https://img.shields.io/badge/Concept-Multithreading-blue)
![Status](https://img.shields.io/badge/Status-Completed-success)
![License](https://img.shields.io/badge/License-Educational-lightgrey)

A lightweight **multithreaded Java console application** that
demonstrates real-world thread coordination, prioritization, and safe
shared-state management using Java concurrency utilities.

SimpleClock runs a live digital clock powered by two independent threads
working concurrently to update and display time efficiently.

------------------------------------------------------------------------

## 🚀 Overview

This project showcases how Java threads can collaborate safely while
performing different responsibilities:

-   A **background updater thread** continuously refreshes system time.
-   A **display thread** prints formatted time every second.

The application also demonstrates **graceful shutdown handling**,
**thread priorities**, and **lock-free synchronization**.

------------------------------------------------------------------------

## ✨ Features

✅ Real-time digital clock output\
✅ Dual-thread architecture\
✅ Thread priority demonstration\
✅ Thread-safe data sharing (`AtomicReference`)\
✅ Clean shutdown using JVM shutdown hooks\
✅ Optional runtime duration argument\
✅ Customizable date-time format

------------------------------------------------------------------------

## 🧠 Core Concepts Demonstrated

### Multithreading

Independent threads execute concurrently to separate computation from
presentation.

### Thread Prioritization

The display thread is assigned higher priority to favor consistent
output timing:

``` java
displayThread.setPriority(Thread.NORM_PRIORITY + 2);
```

> Thread priority acts as a scheduling hint and may vary across
> operating systems.

------------------------------------------------------------------------

### Thread-Safe Shared State

The current time is stored using:

``` java
AtomicReference<LocalDateTime>
```

Benefits: - Lock-free synchronization - Safe concurrent access - Avoids
race conditions

------------------------------------------------------------------------

### Graceful Shutdown

The program shuts down safely when interrupted:

``` java
Runtime.getRuntime().addShutdownHook(...)
```

This ensures threads terminate cleanly without leaving background
processes running.

------------------------------------------------------------------------

## ⚙️ Requirements

-   Java Development Kit (**JDK 8 or higher**)
-   Command line / terminal

Check installation:

``` bash
java -version
javac -version
```

------------------------------------------------------------------------

## 🛠 Installation & Compilation

Clone or download the repository:

``` bash
git clone https://github.com/sean15535/SimpleClock-Multithreaded-Java-Clock

cd simpleclock
```

Compile:

``` bash
javac SimpleClock.java
```

------------------------------------------------------------------------

## ▶️ Running the Application

### Run indefinitely

``` bash
java SimpleClock
```

Stop execution anytime using:

Ctrl + C

------------------------------------------------------------------------

### Run for a fixed duration

Provide runtime in seconds:

``` bash
java SimpleClock 15
```

The example above runs the clock for **15 seconds** before shutting down
automatically.

------------------------------------------------------------------------

## 🖥 Example Output

    09:41:01 19-02-2026
    09:41:02 19-02-2026
    09:41:03 19-02-2026

------------------------------------------------------------------------

## 🎯 Educational Value

This project is useful for students and developers learning:

-   Java thread lifecycle
-   Concurrency design patterns
-   Safe inter-thread communication
-   Scheduling and timing control
-   Interrupt handling and synchronization

Ideal for: - Java coursework - Operating Systems concepts - Concurrent
programming practice - Portfolio demonstrations

------------------------------------------------------------------------

## 🔧 Possible Enhancements

-   JavaFX or Swing GUI version
-   Timezone selector
-   Pause / Resume controls
-   Configurable refresh intervals
-   Logging instead of console output

------------------------------------------------------------------------

## 👤 Author

**Alli Oluwaseun**\
Software Developer \| Java Enthusiast
(Lin[kedIn](https://www.linkedin.com/in/oluwaseun-alli-aa4a812ba/))


This project was developed as part of a practical demonstration of Java
multithreading, concurrency control, and thread lifecycle management.

------------------------------------------------------------------------

## 📄 License

This project is open for educational and personal use.\
You are free to modify and extend it for learning purposes.
