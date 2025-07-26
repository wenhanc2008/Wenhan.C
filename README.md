# Wenhan.C
📦 Multi-Project Showcase

This repository contains three independent full-stack applications built during my graduate studies. Each project demonstrates proficiency with a different technology stack and problem domain:
    •    CampusInk — A full-stack anonymous feedback system for university students
    •    YouTube Web Server — A Flask-based web server that fetches and caches YouTube search results
    •    Chat App — A desktop chat interface with OpenAI integration and persistent chat history
    
🏫 CampusInk

Anonymous feedback platform for students to express opinions on courses, instructors, or campus life.

🌐 Tech Stack
    •    Frontend: React + Tailwind CSS
    •    Backend: Node.js + Express
    •    Database: MongoDB
    •    Authentication: Anonymous (no login required)
    •    Hosting: Vercel (frontend), Render (backend)

📌 Features
    •    Anonymous message submission
    •    Message listing and filtering
    •    Custom tag support
    •    Responsive and mobile-friendly design
    •    Minimal, distraction-free interface


🌐 YoutubeWebServer

YoutubeWebServer is a RESTful Spring Boot application that lets users search for YouTube videos by topic, fetches results via the YouTube Data API, and caches them in a Supabase PostgreSQL database using JOOQ.

⚙️ Tech Stack
    •    Framework: Spring Boot
    •    Build Tool: Gradle
    •    Database: Supabase PostgreSQL
    •    ORM/Querying: JOOQ
    •    External API: YouTube Data API v3
    •    Deployment: fly.io

📌 Features
    •    Search YouTube videos via /news?topic=bitcoin
    •    First request fetches via API and caches the result
    •    Later requests served directly from PostgreSQL cache
    •    Database schema auto-generated via JOOQ

💬 Chat App

A desktop chat application built in Java, with OpenAI GPT-3.5 integration and local SQLite storage.

📦 Tech Stack
    •    Frontend: Java Swing (desktop GUI)
    •    Backend/API: Java HTTP client (calls OpenAI API)
    •    Database: SQLite via JDBC
    •    Model: GPT-3.5 (via OpenAI API)

📌 Features
    •    GUI chat interface with scrollable chat history
    •    Sends messages to OpenAI and displays responses
    •    Stores all chats locally using SQLite
    •    Simple, self-contained desktop app

🧠 Author

Wenhan Cheng — [LinkedIn](https://www.linkedin.com/in/wenhan-c-3a7101339/)
Northeastern University · MS in Information Systems · Class of 2026
