# Dogs - an Android App
> exploring proper navigation and the use of Ktor

This Android app lets you retrieve some dog breed info by making
use of the API: [thedogapi.com](https://www.thedogapi.com/).

The purpose with this app was to explore a number of things:

* The use of the Ktor http client
* Proper Navigation
* The use of the ROOM database

The app is organized according to the MVI principle, which means:

* The UI observes changes to any data it depends on and re-renders accordingly.
* The UI signals any interaction with the Domain layer via an Intent
which basically is an action telling the Domain layer what the User want to do.
* The Domain layer exist in between the UI and Data layer. It contains the actual
machinery (business logic) that connects the user interactions with the underlying
data model.
* The Data layer deals with anything southbound, i.e network traffic, DB access, etc.

## Use of Ktor http client

This was taken from the 15:th season of the YT channel `The Android Factory`.

## Navigation

Was developed with the help of the Github Copilot.

## Room DB

TBD
