# Witness
Subtlely trigger an audio stream to a server on your Wear OS device in case of an unsafe situation. This recording is then transcribed by Azure's Speech-To-Text service and texted to selected emergency contacts through the use of a Twilio API.
## Android Smart Watch
Records audio and other available sensors when triggered by a variety of methodsincluding gestures, rapid heart rate increases not accompanied by accelerometer data indicating physical exertion, and particular phrases heard by the watch.
## Azure Functions
A variety of functions provide functionality without the need for a server.

* Text Emergency Contacts: Sends speech to texted script to emergency contacts via text
* Azure Speech-To-Text: Sends a request to Azure Speech-To-Text and receives a text transcript of the emergency recorded audio then calls Text Emergency Contacts
* Receiving Endpoint: Receives an audio file, other sensors, and calls Azure Speech-To-Text. Saves the emergency entry into the database.
## Hosted Azure Database
Stores upload sessions with user identification and available sensor data.
## Hosted Azure Filestore
Stores uploaded audio files.
## Frontend Web Application
React framework providing configuration settings for the users account. Allows a user to select who receives notification of an emergency upload and what to do if they make an emergency upload.
~                                  
