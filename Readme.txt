GoggleBird (Prototype)
-------------------------------------------------------------------------------------------
DEVELOPERS: Saiven Bisetty  (ST10084599)
	  : Matthew Kidwell (ST10084433)

VERSION: v1.0

CREATED ON: 18 October 2023
-------------------------------------------------------------------------------------------
INTRODUCTION:
-------------------------------------------------------------------------------------------
GoggleBird is a bombastic and intuitive birdwatching app designed to assist users in 
finding users to their desired local bird watching hotspots, while also providing 
directions to a selected hotspot using their current location via GPS. Users can also log 
and save their bird sightings on the app, which can be viewed later. 
GoggleBird is also home to a few additional features that make this app the perfect 
companion for both experienced and novice birders!
-------------------------------------------------------------------------------------------
SYSTEM SPECIFICATIONS
-------------------------------------------------------------------------------------------
--------------------------
Android Studio Emulation:
--------------------------
Tested on Android Studio Electric Eel | 2022.1.1 Patch 2

Recommended Computer specifications (Application has been tested on the following specs):
------------------------------------------------------------------------------------------
11th Gen Intel(R) Core(TM) i5-11400H @ 2.70GHz   2.69 GHz
8 GB RAM
Windows 11 Home Single Language

Minimum Computer Specifications for Android Studio:
----------------------------------------------------
64-bit Microsoft® Windows® 8/10/11
x86_64 CPU architecture; 2nd generation Intel Core or newer, or AMD CPU with support for a 
Windows Hypervisor
8 GB RAM or more
8 GB of available disk space minimum (IDE + Android SDK + Android Emulator)
1280 x 800 minimum screen resolution

---------------------------------
Using Physical Device (APK file)
---------------------------------

Recommended Device Specifications (Application has been tested on the following specs):
----------------------------------------------------------------------------------------
Android 10 (API 29) and above


Minimum Device Specifications
------------------------------
 Android 8 (API 26) and above

-------------------------------------------------------------------------------------------
SOFTWARE REQUIREMENTS:
-------------------------------------------------------------------------------------------
Run via Emulation:
Android Studio (Electric Eel | 2022.1.1 Patch 2)

-------------------------------------------------------------------------------------------
PLUGINS/DEPENDENCIES:
-------------------------------------------------------------------------------------------
The following appears as found in the build.gradle (Project: GoggleBird) file
------------------------------------------------------------------------------
plugins {
    id 'com.android.application' version '7.4.1' apply false
    id 'com.android.library' version '7.4.1' apply false
    id 'org.jetbrains.kotlin.android' version '1.8.0' apply false
}


The following appears as found in the build.gradle (Module: app) file
----------------------------------------------------------------------
dependencies {

    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'com.google.firebase:firebase-auth-ktx:21.0.3'
    implementation 'com.google.firebase:firebase-database-ktx:20.0.4'
    implementation 'com.google.firebase:firebase-storage-ktx:20.0.1'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    // navigation rail
    implementation 'androidx.navigation:navigation-ui:2.6.0'
    implementation 'androidx.navigation:navigation-fragment:2.6.0'

    //Picasso - view images using URL
    implementation 'com.squareup.picasso:picasso:2.71828'

    //Gson
    implementation 'com.google.code.gson:gson:2.8.9'

    //Location Implementations
   // implementation 'com.google.android.gms:play-services-location:21.0.1'
    implementation 'com.google.android.gms:play-services-location:18.0.0'

    // dependency for open street maps
    implementation 'org.osmdroid:osmdroid-android:6.1.16'
    // dependency for open street maps nav routing
    implementation 'com.github.MKergall:osmbonuspack:6.9.0'
}

The following appears as found in the settings.gradle (Project Settings) file
----------------------------------------------------------------------

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // to allow osm road manager import to detect
        maven { url "https://jitpack.io"}
        mavenCentral()
    }
}


-------------------------------------------------------------------------------------------
INSTRUCTIONS 
-------------------------------------------------------------------------------------------
-------------------------------------------------------------
Prior to running the application, the following must be done:
-------------------------------------------------------------
1. Download the zip folder

2. Unzip the folder 

NB. WHEN USING ANDROID STUDIO, place unzipped folder in the AndroidStudioProjects folder 
(Usually found at C:\Users\(username)\AndroidStudioProjects)

-------------------------- 
METHOD 1 : ANDROID STUDIO
--------------------------
1. Once the file has been placed in the AndroidStudioProjects folder, select the open 
   button from the Project Menu in the Android Studio Main Menu.

2. Select the desired android project, in this case GoggleBird and open it.

3. Once the file has been opened in Android Studio, click on the green play button to run 
   the program. This will start your emulator, install and run the app.

---------------------
METHOD 2 : DEBUG APK
---------------------
1. In the project folder, navigate to the debug apk file, which is found at: C:\Users
   \(username)\AndroidStudioProjects\GoggleBird\app\build\outputs\apk\debug
   The apk file is also available alongside the Android Studio Files in the     
   GoggleBird file.

2. Copy this file to your android device, preferably save it to your desired folder or in
   your downloads folder for easy access.

3. Navigate to your files app and select the folder you saved the debug apk file to.

4. Install the apk file by tapping it and selecting install.

5. Once the app has installed, you may run the app.

-------------------------------------------------------------------------------------------
TROUBLESHOOTING:
-------------------------------------------------------------------------------------------
--------------------------------------
Problem
--------------------------------------
Unable to view all saved sightings.

Options
-------
Please ensure the following:
- Make sure you are logged in to the correct account.
- Make sure to scroll through the list to find the desired sighting.

----
FAQ:
----

Q: What do I do if I spot a bird I'm not sure of?

A: Thats perfectly fine too! You can enter the species common name manually, saving it as 
   unidentified if you wish. You can then view the image you took of the bird, that you 
   saved with the sighting to research the species at a later stage if you wish to.

Q: Am I able to view the saved image in a full screen?

A: Yes, you can. Tap the desired image in your saved sightings list to open a fullscreen
   version of the image. You can use the back button on you device to go back to the saved     
   sightings page when you are done.

Q: Can other users view and edit my information?

A: No, your information is unique to your account, hence only you can edit your    
   information.

Q: Is my data stored locally or online?

A: Your data is stored in a Firebase database, more specifically in a Firebase Realtime    
   database, which enables you to access your data from any device through your account.

Q: Is there a .apk file for PC?

A: There is no .apk file for PC as you will run the app through the emulator. However, if    
   you want to use the apk file found in the debug folder, you can drag and drop it into 
   your desired emulator (Android Studio or external). 
   Please note, it is recommended that you use the Android Studio option above if you wish 
   to test the application on your computer. 

Q: How do I install plugins?

A: Plugins should already be installed in the software. However, if you wish to install  
   plugins, you can copy the plugins and dependencies found in this readme file to the 
   correct gradle files in your project folder. 

Q: Why do the Github links return a 404 error page?

A: This means that you are not authorised to access those files. If you wish to access 
   these repositories, then please contact the developer to give you access.

If the above does not aid in troubleshooting, please find our contact information below 
and we will respond as soon as we can.

-------------------------------------------------------------------------------------------
DEVELOPER CONTACT INFORMATION:
-------------------------------------------------------------------------------------------
Saiven Bisetty:
---------------
Student Number - ST10084599

Email - st10084599@vcconnect.edu.za

Matthew Kidwell:
----------------
Student Number - ST10084433

Email - st10084433@vcconnect.edu.za

-------------------------------------------------------------------------------------------
DEFAULT LOG DETAILS
-------------------------------------------------------------------------------------------
------------------
Default Admin
------------------
Email   : admin@gmail.com
Password: Password@123

-------------------------------------------------------------------------------------------
CODE ATTRIBUTION :
-------------------------------------------------------------------------------------------
----------
References 
----------
* Chugh, A., 2022. Android sharedpreferences using Kotlin, DigitalOcean. [online] Available 
  at: https://www.digitalocean.com/community/tutorials/android-sharedpreferences-kotlin 
  [Accessed: 18 October 2023]. 
* Developer.android.com, 2022. Take photos  :  android developers, Android Developers.  
  [online] Available at: https://developer.android.com/training/camera- 
  deprecated/photobasics [Accessed: 18 October 2023]. 
* ebird.org, 2023. EBird status and Trends, eBird Status and Trends - eBird Science. 
  [online]. Available at: https://science.ebird.org/en/status-and-trends [Accessed: 18 
  October 2023]. 
* ebird.org, 2023. Explore hotspots - ebird, Explore Hotspots - eBird. [online]. Available 
  at: https://ebird.org/hotspots [Accessed: 18 October 2023]. 
* Joshi, V., 2019. How to write an image file in internal storage in Android, How to write   
  an image file in internal storage in android. [online] Available at:   
  https://www.tutorialspoint.com/how-to-write-an-image-file-in-internal-storage-in-android 
  [Accessed: 18 October 2023]. 
* Kanaujiya, S., 2017. How to populate spinner in Android with JSON data [snippet] - DZone,   
  dzone.com. [Online] Available at: https://dzone.com/articles/populate-spinner-from-json-
  data [Accessed: 18 October 2023]. 
* Picasso, 2013. Picasso. [online]. Available at: https://square.github.io/picasso/   
  [Accessed: 18 October 2023].

-------------------------------------------------------------------------------------------
GIT REPOS LINK
-------------------------------------------------------------------------------------------
https://github.com/VCWVL/opsc7312-part-2-ST10084599
