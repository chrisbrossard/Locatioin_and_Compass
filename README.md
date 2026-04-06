I wrote this Android application because I wanted to learn a little about Kotlin, Android's new programming language, and Compose, Android's new system for developing user interfaces. I have written quite a few Android apps using Java and XML and I felt that it was time to update my skills. Because I like walking and hiking, I wrote an app which would be useful for that. It takes information from Android's various sensors and software libraries and presents it on the screen. It also allows some information to be saved in a Room database for future viewing.

The app has only been tested on one device, the Moto G Play 2024. Android is an open source operating system, so there can be variations in the Android operating system between different devices. The app may not work correctly or crash when used on other devices. The user is encouraged to report any crashes that occur.

The name "Location and Compass" isn't a really good name for the app as it is. I just named it that because that's all it did when I created the app. Creating a new app with a better name ("Trail Companion") and copying all the files over would be a lot of work so I just left it as it is. It is fine for demonstration purposes.

I used the location services only minimally. If I had location services on all the time I could provide more features like waypoints, GPS speed, and GPS altitude. But I became concerned that with the location icon all the time in the notification bar, users would see the app as a battery hog. The fused location provider is apparently more efficient than the old location provider, so the fused location provider can be set up so that the GPS radio is used only minimally. But the users don't know that. If the location services notification system is changed in the future to give better information to the user, I may add some of those features. There is also the question of coarse vs fine locations. I think Android apps are supposed to allow both, but at the moment the app only accepts fine.

I used Android's ViewModel class but I am not sure if I am following best practices for that. Whenever I needed to access something in a composable that I couldn't access because of Compose rules, I created a ViewModel for it. But I am not sure if that is the best way of doing that.

I used Android's navigation controller to split up the information into several screens. And I used a mixture of text and Android's canvas to present the information.

The charts were generated using MPAndroidChart which I found easier to use than Vico. The API for Vico changed significantly between versions so I had difficulty getting later versions to run.

The shared preferences were used to communicate between different parts of the app. So when the user taps on the "steps" value on the overview screen, a steps session entity is created in the step sessions table.  That returns a "sessionId" which is placed in shared preferences and is retrieved in the altitude / steps service to create the steps samples which have a foreign key to the step session entity in the step sessions table. When the user taps on a steps row in the bottom sheet in the overview screen, the sessionId is retrieved from that step session entity and is placed in the shared preferences so that the composable which displays the step samples for that session, it can filter out the step samples in the step sample table which don't belong to that session. The altitude values work in the same way.

I use a room database to store step samples, altitude samples, step sessions, and altitude sessions. Tapping on the steps or altitude values on the overview screen starts recording for that input. Backing out of the recording screen by tapping the back button stop the recording.

The overview screen consists of several text values: steps, vertical speed, barometric altitude, sun direction, and compass heading. The vertical speed was derived from atmospheric pressure values which were smoothed and run through a linear regression. The steps, altitude, and compass heading come directly from the sensors. The sun direction is calculated using the Kastro astronomy library.

Tapping on the steps value shows a chart of steps values that are collected and displayed in real time. It also starts the recording of the steps values into the database. Backing out of that screen by pressing the back button stops the recording.

Tapping on the vertical speed number shows a dial with a needle showing the same value. The value is derived from the pressure sensor which is very sensitive so if it is a windy day, the vertical speed value will fluctuate.

Tapping on the altitude value shows a chart of altitude value that are collected in real time. It also starts the recording of the steps values into the database. Backing out of that screen by pressing the back button stops the recording.

Tapping on the sun direction value shows a sun/moon chart which shows the time during the day when the moon and sun are above the horizon. The thickness of the moon arc is tied to the current moon phase, so when the moon is full, the moon arc is thick, and when the moon is new, the moon arc is thin or invisible. There is a red needle which points to the current time. There is also a thinner red needle which points to the time the app was started.

Tapping on the heading value shows a compass dial similar to a baseplate compass. The compass dial can be rotated with two fingers. The compass needle is shown as an "uncertainty cone", the width of which is determined by the compass accuracy. Also shown on the compass dial is the declination and GPS coordinates at that location. Tapping on the compass dial shows a chart of the positions of the navigation satellites in the sky.

