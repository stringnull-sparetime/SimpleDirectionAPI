# SimpleDirectionAPI
Use Direction API in native android (experimental)

#### Prerequisite
- Enable Map and Direction APIs
#
![image](https://github.com/stringnull-sparetime/SimpleDirectionAPI/assets/22344756/1eb6521c-1b90-4dac-8da8-7ae190c69c19)
![image](https://github.com/stringnull-sparetime/SimpleDirectionAPI/assets/22344756/6f0642cc-f1a6-438a-b058-e06ad9141304)

#### Api Key
- Create your api key and add to your manifest.xml
![image](https://github.com/stringnull-sparetime/SimpleDirectionAPI/assets/22344756/7dc7b3a1-9bb8-4db0-bf7e-2139d263bd01)

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools" >

    <application
        ... >

        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="${MAPS_API_KEY}"/>

        ...
    </application>
</manifest>

- Add google map sdk to your gradle project
  ```
    //enable map sdk
    implementation("com.google.android.gms:play-services-maps:18.1.0")
  ```
### Screenshot
  ![image](https://github.com/stringnull-sparetime/SimpleDirectionAPI/assets/22344756/e2b2964c-1651-4d2d-973c-fe319d9b0bd1)
