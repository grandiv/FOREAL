# FOREAL (Food Rescue and Allocation)

Mobile app to support Sustainable Development Goal 2: Zero Hunger by redistributing surplus food from restaurants to people in need.

## Minimum Requirements
- **Android Version:** Android 7.0 (API level 24) and above
- **Minimum SDK:** 24
- **Kotlin Version:** Kotlin 1.8 and above
- **Firebase BoM Version:** 32.7.1
- **Firebase Auth Version:** 22.31
- **Firebase Firestore Version:** 24.10.1
- **Play Services Auth Version:** 20.7.0

## Tech Stack
1. Google Authentication
2. Firebase Firestore

## Features
1. **Login Authentication with Google**
2. **Donation:**
   - Integrated with Firestore database to save data from user input
3. **Request:**
   - User is able to input recipient data and retrieve donations randomly from the database
4. **Be a Volunteer:**
   - Users can voluntarily deliver donations to recipients
   - Volunteers can choose which delivery they want to take

## Future Development
1. Status on how donations are being distributed (e.g., donation history, request history, delivery status)
2. User authentication for donator, recipient, and volunteer roles to avoid app abuse
3. Donations quantity to update whenever it's requested/delivered
