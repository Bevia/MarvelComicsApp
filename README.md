# MarvelComicsApp
Marvel Comics Android App  - DONE!

1	Functional requirements (Using the Job to be Done framework)
◦	When i open the application I want to see a list of all Marvel’s released comic books covers ordered from most recent to the oldest so I can scroll through the the Marvel universe.
◦	When I select one of the comics I want to be able to change the cover picture with a photo taken from my camera so I can be a Marvel character!
◦	When I change a comic cover image I want to able to store it in my dropbox account so I won’t lose it when I reopen the application.

Marvel account needed!
Dropbox:

<activity
            android:name="com.dropbox.client2.android.AuthActivity"
            android:configChanges="orientation|keyboard"
            android:launchMode="singleTask" >
            <intent-filter>

                <!-- Change this to be db- followed by your app key -->
                <data android:scheme="db-your key" />  //a key is included for testing!
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.BROWSABLE" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>

            dependencies {
                compile fileTree(dir: 'libs', include: ['*.jar'])
                testCompile 'junit:junit:4.12'
                compile files('libs/dropboximageone-android-sdk-1.6.3.jar')
                compile 'com.android.support:appcompat-v7:23.2.1'
                compile 'com.android.support:support-v4:23.2.1'
                compile 'com.android.support:recyclerview-v7:23.2.1'
                compile 'com.android.support:cardview-v7:23.2.1'
                compile 'com.squareup.retrofit:retrofit:1.9.0'
                compile 'com.squareup.okhttp:okhttp-urlconnection:2.0.0'
                compile 'com.squareup.okhttp:okhttp:2.5.0'
                compile 'com.jakewharton:butterknife:7.0.1'
                compile 'com.squareup.picasso:picasso:2.5.2'
                compile files('libs/dropboximageone-android-sdk-1.6.3.jar')
            }

