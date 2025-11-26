🎬🍿 Movie Ticket Booking System – Core Java Project
<p align="center"> <img src="https://img.shields.io/badge/Language-Java-red?style=for-the-badge"> <img src="https://img.shields.io/badge/Project-Type%20:Core%20Java-blue?style=for-the-badge"> <img src="https://img.shields.io/badge/Data-CSV%20File-orange?style=for-the-badge"> <img src="https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge"> </p>

📌 Overview
This is a Movie Ticket Booking System built using Core Java, designed according to university guidelines.
The project implements real-world Java concepts such as:

🔹 Object-Oriented Programming (OOP)

🔹 File Handling using CSV

🔹 Collections Framework

🔹 Custom Exceptions

🔹 Multithreading

🔹 Clean Package Structure

All bookings are stored permanently inside bookings.csv (No SQL Database used).

Perfect for:

🎓 College Project (Chitkara University, 2022–2026)

🧑‍💻 Java Beginners

🚀 GitHub Portfolio

🧱 Features
🎟 1. Book Tickets
Select movie
Choose show
Pick seat type (General / Premium)
Enter ticket quantity
System generates a unique Booking ID
Saves booking in data/bookings.csv

❌ 2. Cancel Tickets
Cancel using Booking ID
Calculates refund amount
Updates CSV file

🎬 3. View Movies (Available List)
Displays a list of 10 Bollywood movies, including classics and latest hits.

🔮 4. Upcoming Movies (Multithreading)
Loading animation using Java Thread
Shows future movies list

📄 5. Show My Bookings
Reads booking history from CSV
Displays all stored bookings

🚪 6. Exit Program
Ends safely with a friendly message.

🎥 Movies Included
ANIMAL
KGF: CHAPTER 2
3 IDIOTS
GADAR 2
WAR
SOORYAVANSHI
CHHICHHORE
PATHAAN
JAWAN
DRISHYAM 2

🏗 Project Structure
MovieTicketBookingSystem/
│
├── src/
│   ├── main/            → MovieTicketBooking.java (Main Menu + App Start)
│   ├── model/           → Movie.java, Booking.java
│   ├── service/         → BookingSystem.java (Core Logic)
│   ├── util/            → FileUtil.java (CSV Storage)
│   └── exceptions/      → Custom exception classes
│
├── data/
│   └── bookings.csv      → Permanent storage of bookings
│
└── README.md

🚀 How to Run
▶ 1. Compile
javac -d bin (Get-ChildItem -Recurse -Filter *.java).FullName

▶ 2. Run
java -cp bin main.MovieTicketBooking

▶ 3. Rechange
javac -d bin (Get-ChildItem -Recurse -Filter *.java).FullName

📚 Core Java Concepts Used
Concept	Explanation
OOP	Classes for Movie, Booking & system behavior
Encapsulation	Private fields + Getters/Setters
File Handling	CSV read/write with FileWriter & BufferedReader
Collections	ArrayList for movies + bookings
Custom Exceptions	BookingNotFound, InvalidSeatSelection
Multithreading	Used for UI animation in “Upcoming Movies”
Packages	Clean, modular, real-world architecture

💡 Future Improvements
Add GUI (Swing / JavaFX)
Add login (User/Admin)
Add seat layout visualization
Replace CSV with MySQL
Add logs for transactions

👨‍💻 Author
Souvik Dhar
Chitkara University
Batch: 2022–2026