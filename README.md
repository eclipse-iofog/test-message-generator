# Test Message Generator

As developers build ioElement containers, they start by building the code in a non-container environment. They need to test the code before spending the time turning it into a published container, but they can't actually test the processing of messages or connection to the ioFabric instance without going through the publishing and deployment process.

Test Message Generator is a surrogate version of the ioFog Local API. It mimics the API endpoints of the real ioFog Local API, including offering the Control Websocket and Message Websocket. It runs on "localhost" so it can be reached directly on the computer that is being used to build the ioElement container.

A developer can precisely mimic the production environment on their build machine by mapping a host. If they map "127.0.0.1" with the host name "iofog" then their local code will be able to operate with the same "http://iofog:54321/" endpoints found in the SDKs and described in the API specification.

#### Configuration (under files folder examples) :

* The developer can set up a list of fully defined ioMessages that the Test Message Generator will output in messages.xml . The messages will be send randomly. 

* The messages that are posted into the Test Message Generator will be stored in receivedmessages.xml file.

* Configuration JSON for the ioElement container that the Test Message Generator will give as output can be specified in containerconfig.json file.

* "new configuration" control message and "new message" data message will be send to the ioElement container at the interval specified by the developer im milliseconds at config.xml:

<pre>
	&lt;configuration&gt;
		&lt;datamessageinterval&gt;500&lt;/datamessageinterval&gt;
		&lt;controlmessageinterval&gt;10000&lt;/controlmessageinterval&gt;
	&lt;/configuration&gt;
</pre>

#### Run instructions:

To run TMG just download latest jar file and provide all configurations files (the full list and examples can be found under files folder):

java -jar jar_file_name.jar 

Notice! Before running TMG don't forget to turn off iofog: sudo service iofog stop.
