# VideoLibrary

<p> This app is used to stream videos. When clicked on video, the app will detect the face of person and play the video. </p>

## Technologies and Libraries
 <h3> Technologies
 <ul>
 <li> Android Studio: 4.1.3
 <li> Android SDK API LEVEL: Android R 30
 <li> Language: Java
 </ul>
 <h3> Libraries
 <ul>
 <li> Ml kit
 <li> cameraX
 </ul>
 
## Code

<p> Started with the First Acitivity that will contain the videos. Stored the videos in raw folder(android.resource://app/main/res/raw/). 
    On video click, the intent created and sent to the camera activity which will perform the face detection. I used cameraX for the same and android Mlkit face detection library.
    On performing the face detection, if the eyes don't blink, i.e. the Left and Right Eye probability is greater than certain range, the intnent will be passed to the Single Video view Activity.
</p>

