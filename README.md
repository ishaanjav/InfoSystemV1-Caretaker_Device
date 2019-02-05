# CaretakerDevice
## Purpose/Background

**The purpose of this Android app is to serve as part of a system of 3 apps that collects information about visits to the house. The system is intended to serve at the homes of those with declining cognitive abilities such as patients with Alzheimer's or other types of dementias because these patients have difficulty recognizing faces and therefore have trouble identifying visitors that have come to see them. This app is for the patient's caretaker who can view events that the system logs, approve or decline accounts of new visitors, track the patient if they get lost, and analyze their emotions.**

Rather than just providing information about visitors to the patient, the patient's caretaker will also receive useful information through this app. The system not only helps the patient better *understand* who is visiting them, but also keeps a record of information for their caretaker.

###### Please note that this is not meant to be a stand-alone app and is meant to be used alongside the [Visitor Device App](https://github.com/ishaanjav/InfoSystemV1-Visitor_Device) and [Resident App](https://github.com/ishaanjav/InfoSystemV1-Resident_Device) to make up the information system.

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

###### The [Setup Section](#setup) can be found [here](#setup).

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

<br/>

-----
# Setup
This app makes use of the following software *(a '' means that this is something you will have to get your own key for)*:

- [Firebase](https://firebase.google.com/): Reading from Firebase Realtime Database and Firebase Storage to **get information for multiple pages** such as Events Log, Pending Requests, Existing Users, Emotions Analyzer, and more.
- [''Microsoft Face API](https://azure.microsoft.com/en-us/services/cognitive-services/face/): For doing the **emotion analysis**. *I have a repository for a [Face Analyzer app](https://github.com/ishaanjav/Face_Analyzer) that does emotion analysis and more. You can check it out [here](https://github.com/ishaanjav/Face_Analyzer)*.
- [''Google Cloud Platform](https://cloud.google.com/): For the **Google Maps API Key** which is used to display a Google Maps View in the GPS page of the app for tracking the resident. 

Of the 3 listed above, you will have to get your own keys for the [**Microsoft Face API**](#microsoft-face-api) and the [**Google Maps API**](#google-maps-api). Do not worry however, since you only have to change 2 lines of code and all the instructions can be found below. **First start with cloning this repository and opening it in Android Studio. Then continue below:**

## Microsoft Face API
### Making the Azure Account
In order to run the face dectection and analysis, you must get an API Subscription Key from the Azure Portal. [This page](https://azure.microsoft.com/en-us/services/cognitive-services/face/) by Microsoft provides the features and capabilities of the Face API. **You can create a free Azure account that doesn't expire at [this link here](https://azure.microsoft.com/en-us/try/cognitive-services/?api=face-api) by clicking on the "Get API Key" button and choosing the option to create an Azure account**. 
### Getting the Face API Key from Azure Portal
Once you have created your account, head to the [Azure Portal](https://portal.azure.com/#home). Follow these steps:
1. Click on **"Create a resource"** on the left side of the portal.
2. Underneath **"Azure Marketplace"**, click on the **"AI + Machine Learning"** section. 
3. Now, under **"Featured"** you should see **"Face"**. Click on that.
4. You should now be at [this page](https://portal.azure.com/#create/Microsoft.CognitiveServicesFace). **Fill in the required information and press "Create" when done**.
5. Now, click on **"All resources"** on the left hand side of the Portal.
6. Click on the **name you gave the API**.
7. Underneath **"Resource Management"**, click on **"Manage Keys"**.

<p align="center">
  <img width="900" src="https://github.com/ishaanjav/Face_Analyzer/blob/master/Azure-FaceAPI%20Key.PNG">
</p>

You should now be able to see two different subscription keys that you can use. Follow the additional instructions to see how to use the API Key in the app.
### Using the API Key in the App
**You only need to replace one line of code in [`TakePicture.java`](https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/app/src/main/java/com/example/anany/caretakerdevice/TakePicture.java)** since this is where the processing for emotions takes place. 

**On line 93 you should see the following line:
    
    faceServiceClient = new FaceServiceRestClient("<YOUR API ENDPOINT HERE>", "<YOUR API KEY HERE>");
Replace `<YOUR API SUBSCRIPTION KEY>` with one of your 2 keys from the [Azure Portal](https://portal.azure.com/#home). *(If you haven't gotten your API Key yet, read [this section](#making-the-azure-account))*. `<YOUR ENDPOINT HERE>` should be replaced with one of the following examples from [this API Documentation link](https://westus.dev.cognitive.microsoft.com/docs/services/563879b61984550e40cbbe8d/operations/563879b61984550f30395236). The format should be similar to: 
  
    "https://<LOCATION>/face/v1.0"
  
where `<LOCATION>` should be replaced with something like `uksouth.api.cognitive.microsoft.com`.

**That covers using the Microsoft Face API Key. Now, go to the [Google Maps API](#google-maps-api) section to get the API for Google Maps.**

## Google Maps API
The process of getting the API Key for Google Maps is rather lengthy and I will add the steps here once I get the time. Until then, you can follow the following links for the Google Maps Platform's dcoumentation:
- [**Getting the API Key**](https://developers.google.com/maps/documentation/android-sdk/signup)
- [**Get Started with the API Key in Android Studio**](https://developers.google.com/maps/documentation/android-sdk/start)

Once you have the API Key, go to the [`google_maps_api.xml`](https://github.com/ishaanjav/InfoSystemV1-Caretaker_Device/blob/master/app/src/release/res/values/google_maps_api.xml) file in Android Studio and replace `YOUR KEY HERE` on Line 19 with the key that you have obtained.

-----

# Other Information System Apps
This app has many applications whether it be in the homes of individuals with Alzheimer's or dementia, or in residential homes for the elderly. Because of the features that it provides to the patient's caretaker, it makes it far easier for the caretaker to ensure the patient's safety, whether it be through the Emotions Analyzer page or GPS Tracker, or monitor the events happening within the system, like Log of Events and Existing Users.

It is part of my project that I call Info System V1, *(Information System Version 1)*, and it functions alongside 2 other apps:

- [**Visitor Device App Repository:**](https://github.com/ishaanjav/InfoSystemV1-Visitor_Device) The purpose of this Android application is to serve as part of a system of 3 apps that collect information about visits to the house. This app, in particular, is used to validate visitors who are logging in to the system. Once a visitor has signed into the app, the Alzheimer's patient and their caretaker are notified.
- [**Resident App Repository:**](https://github.com/ishaanjav/InfoSystemV1-Resident_Device) The purpose of this Android application is to serve as part of a system of 3 apps that collect information about visits to the house. This app in particular provides patients who have the Alzheimer's disease or dementia with information about visitors so that they can better identify and understand who is visiting them.


