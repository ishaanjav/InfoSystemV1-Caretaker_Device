# CaretakerDevice
## Purpose/Background

**The purpose of this Android app is to serve as part of a system of 3 apps that collects information about visits to the house. The system is intended to serve at the homes of those with declining cognitive abilities such as patients with Alzheimer's or other types of dementias because these patients have difficulty recognizing faces and therefore have trouble identifying visitors that have come to see them. This app is for the patient's caretaker who can view events that the system logs, approve or decline accounts of new visitors, track the patient if they get lost, and analyze their emotions.**

Rather than just providing information about visitors to the patient, the patient's caretaker will also receive useful information through this app. The system not only helps the patient better *understand* who is visiting them, but also keeps a record of information for their caretaker.

-----

<img src="https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/Existing%20Users.png" height="450" align ="right">
<img src="https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/Log%20of%20Events.png" height="450" align ="right">

# Usage

The app has multiple pages that serve various purposes. The process of [setting up](#setup) the app can be found [here](#setup). Below is a list of the **app's pages**:

- **Initial Setup**.
- **Sign In**.
- [**Homepage**.](#homepage)
- **Pending Requests** of existing users.
- **Existing Users**.
- **Log of Events** that contains all events.
- **Emotion Analyzer** for viewing record of all events.
- **Emotion Results** for viewing analysis after taking a picture.
- **GPS** for tracking the resident.
- **Account Info** for updating account information.
- **About This App**.

## Features of this App

<img src="https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/Usage%20Demo.gif" width="300" align="right">

Features and purposes of this app include, but are not limited to:

- Viewing a list of:
  * All accounts pending requests.
  * All accounts of existing users
  * All events and times that the system has logged such as:
    * Accounts Pending Approval
    * Accounts Approved
    * Accounts Declined
    * Accounts Deleted
    * Account Info Updates
    * Visitor Sign-ins
    * Worker Sign-ins
    * Logouts
    * Failed Logins
- Querying for events by event type or account info.
- Tracking the patient in case they get lost.
- Analyzing the patient's emotions.
- Viewing a record of the patient's emotions along with their picture, in a `ListView` or graph.
- Easily contacting visitors through by email or phone.
- Receiving notifications for: 
  * visitor sign-ins.
  * failed sign-ins.
  * account creation by new visitors.

<img src="https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/Homepage%20Demo.gif" width="205" align="right">

<br/>

## Homepage

This page contains links to the rest of the pages in the app. It uses a `ScrollView` to hold the content. Additionally, at the bottom of the page, there is a `RelativeLayout`, that repeatedly fades in and out every few seconds and displays information for the caretaker to view such as the latest login, latest account pending approval, and latest account creation.

<img src="https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/Log%20of%20Events.png" width="205" align="left">

<br/>

## Log of Events

This page contains a `ListView` of all events and times recorded by the system. These events include: 
*Accounts Pending Approval, Accounts Approved, Accounts Declined, Accounts Deleted, Account Info Updates, Visitor Sign-ins, Worker Sign-ins, Logouts, and Failed Logins.*

Where applicable, the caretaker can tap on a row to view the details of that person. *This is not available for Failed Logins or Visitor Logouts*. Furthermore, the caretaker can apply filters to query for more specific results such as by event type and/or account info *(username, name, email, phone #, etc)*. 

###### Please note that the phone numbers are made up.

<img src="https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/Emotion%20Analyzer.png" width="207" align="right">

<img src="https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/Emotion%20Results.png" width="207" align="left">

## Emotions Analyzer
Another important page is the Emotions Analyzer page. On this page, the caretaker can view pictures of the resident, the time they were taken, and the emotion analysis for each picture. This information is displayed in a `ListView`. From this, the caretaker can study what the app analyzed to understand how the resident is feeling over a period of time. *It is recommended that at least 5 pictures are taken per day to build up a record of analyzations.*

From the Emotions Analyzer page, the caretaker can tap on the bar graph icon in the top `MenuActionBar` to view a bar chart containing the frequencies of emotions. This is especially useful if the caretaker has taken a lot of pictures and wants to obtain a generalization from the collected data. 

