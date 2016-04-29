#Android App – Friend Locator

##ARCHITECTURE

###Overview
Friend Locator is a simple app which helps you locate your friends on Google Map. Every new installation of the app generates a unique UserKey, that is needed to know the location of another friend. To know the location of your friends, their unique UserKey is all you need. Add your friends and go to the map to see their location.
The app updates your location on the server every time you open the app, or press the refresh button.

###Activities
The app has the following activities:
* Main Activity
 * Fetches location from Google API Client.
 * Generates new userKey.
 * Updates user’s location to server.
* Friends Activity
 * Displays all added friends.
* Add Friends Activity
 * Add new friend.
 * Checks friends userKey is valid before adding friend.
* Remove Friends Activity
 * Removes a friend from list.
* Map Friends Activity
 * Displays current location of all friends on Google Map.

Architecture of all the activities has been explained in the architecture diagrams.

###Other Helper Files
* HTTPHelper.java
 * Encapsulates all network communication – GET, POST, PUT and DELETE http calls.
* JsonHelper.java
 * Helps decode HTTP JSON response to program usable values such as latitude, longitude, user key string, etc.

###HTTP Server and Cloud Database
The app stores locations of all users on a cloud MongoDB. All communication to the database are handled by a front-end server, FLServer, which can be deployed on any cloud service. FLServer is written in GOLANG to handle high volume of concurrent HTTP requests. 
Currently, the FLServer is deployed on Cloud9. FLServer is a simple REST based HTTP service. The app on mobile communicates to FLServer via HTTP REST calls GET, PUT, UPDATE and DELETE.

###Local Database – Shared Preferences
The app stores list of friends locally on SharedPreferences. The friends userKey and friends’ names are stored locally, and also the current user’s user key. These keys are used to fetch latest location data from FLServer.

##SET-UP AND INSTALLATION
Deploy FLServer.go server on cloud.
* Update HTTPHelper.java with the cloud URL.
* Install the app.
* Share userKey with friends, add friends by userKey.
* Note – As of now, the app has only been tested and works on Android API 23 or higher, but this can be easily changed to support more APIs.
