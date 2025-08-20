# Real-Time Chat Application  

This is **Internship Task 5**, a real-time chat application built with **Android Studio** using **Firebase Realtime Database** and **Firebase Cloud Messaging (FCM)**. The app provides seamless real-time messaging, notifications, and a clean user interface.  

---

## Features  

### Core Features  
- **Chat List Screen**  
  - Displays all users or recent conversations from Firebase.  

- **Chat Detail Screen**  
  - Real-time messaging between two users.  
  - Message bubbles for sender/receiver with timestamps.  

- **Push Notifications**  
  - New message notifications using Firebase Cloud Messaging.  

- **UI Enhancements**  
  - Clean & responsive chat interface.  
  - Smooth navigation between Chat List and Chat Detail.  
  - Loading indicators while sending/receiving messages.  

### Technical Requirements  
- **Firebase Realtime Database** → For storing & syncing messages.  
- **Firebase Cloud Messaging (FCM)** → For push notifications.  
- **MVVM Architecture** → Lifecycle-safe listeners to avoid memory leaks.  

---

## Tech Stack  
- **Language**: Java  
- **UI**: XML  
- **Database**: Firebase Realtime Database  
- **Notifications**: Firebase Cloud Messaging  
- **Architecture**: MVVM  

---

## Project Structure  

```
app/
 ├── data/          # Firebase models & repositories
 ├── ui/            # UI screens (Chat List, Chat Detail)
 │   ├── chatlist/
 │   ├── chatdetail/
 ├── viewmodel/     # MVVM ViewModels
 ├── utils/         # Helpers (connection check, encryption, etc.)
 ├── MainActivity.kt
 └── ...
```

---

## Firebase Database Structure  

```
root
 ├─ Users
 │   └─ {uid}
 │       ├─ name: "Rayyan"
 │       ├─ email: "r@x.com"
 │       ├─ photoUrl: "" (optional)
 │       ├─ fcmToken: "..."
 │       └─ online: true/false
 └─ Conversations
     └─ {conversationId}   // deterministic: min(uidA,uidB)+"_"+max(uidA,uidB)
         └─ {messageId}
             ├─ fromId
             ├─ toId
             ├─ text
             ├─ timestamp   // System.currentTimeMillis()
             └─ seen        // true/false (optional)

```
## Firebase Databse Rules
```json
{
  "rules": {
    "Users": {
      ".read": "auth != null",
      "$uid": {
        ".write": "auth != null && auth.uid == $uid"
      }
    },
    "Conversations": {
      ".read": "auth != null",
      "$conv": {
        ".write": "auth != null"
      }
    }
  }
}

```


## How to Run the Project
- Clone the repository:
  ```
  git clone https://github.com/rzjaffery/Chat-Application-DEN.git
  ```
- Open in Android Studio.
- Add your Firebase configuration:
- Download google-services.json from Firebase Console.
- Place it inside app/.
- Sync Gradle and run the app on an emulator or physical device.
