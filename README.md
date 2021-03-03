# Pseudo Packets
A library to write quick pseudo packet communication.
### Disclaimer
As the name implies, this library does not use real networking packets. It uses the default java socket api to send data. The data is bundled in so called "Pseudo Packets". The main goal of this library is not to achieve the most efficient network communication but to make it easy to transmit data between servers and clients.
## About
This library is intended for quick network communication between a server and a few clients. The networking data is going to be transmitted in pseudo packets. Since these packets are not typical networking packets, it makes it very easy to transmit any set of data. For more information and comparison to normal networking packets, refer to the Disclaimer. 
### How It Works
In the back, the data is being transmitted over vanilla Java Sockets. A packet, represented by a subclass object of a certain class, can then be transmitted with all its data. To transmit, a packet is parsed to json, then encoded with Base64 and sent off along with the id of a packet. To on the other side of the transmission, the packet is then being decoded and parsed back into the correct subclass, using the packet id provided. The arrived subclass object is then passed into the correct handler method of a handler class, using reflection.
### Features
* Simple Creation of Client and Server
* Custom Packets with custom Information
* Easy Handling of incoming Packets and Connections
* Multithreading
* Reliable Packet Transfer
## Usage
### Version
The current version is **1.0**
### Getting the Library
To get the latest version of the library, you could add the dependency to your build.gradle file via [JitPack](https://jitpack.io/#VirtCode/PseudoPackets/ "PseudoPackets on JitPack"). (Visit [JitPack](https://jitpack.io/#VirtCode/PseudoPackets/ "PseudoPackets on JitPack") for other Dependency Managers)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    compile 'com.github.VirtCode:PseudoPackets:[Version]'
}
```
Or you could download a built Jar in the release section.
### In Code
To allow successful communication between clients and servers, they must both understand a set of predefined packets. So the following few steps should be the same or largely similar on both client and server. There are still things that are separate for both sides, though.
#### Common Steps
To Start off, you need to create a few Packets that transmitted between the client and the server. To do that, you should create a class that is extending the ```Packet``` class. You also have to assign each packet a unique packet id, which is passed to it in the constructor.
```java
public class ExamplePacket extends Packet {
    public String s;
    public int i;

    public ExamplePacket(String s, int i) {
        super(0); // Insert packet ID here
        
        this.s = s;
        this.i = i;
    }
}
```
Next, you should define your so-called Protocol. It contains all information about which packets are how recognized by your system. To do that, you should define a ```Protocol``` class and assign packets to it.
```java
Protocol protocol = new Protocol();
protocol.addPacket(ExamplePacket.class, 0); // The id of the packet which class is assigned
```
Until now, you have defined everything that your server and client should ideally have in common. Next, we are going to define a Server.
#### Server Steps
To do that, you should create your packet handler. To do that, you have to create a class which extends the class ```ServerPacketHandler```. This class should first override the necessary methods. The ```connected(UUID)``` and ```disconnected(UUID)``` methods will be executed when a client connects or disconnects. Each client is also assigned its custom ```UUID```, which is passed into that method. To now being able to handle a packet, you should create a handler method for that specific packet. To define one, you can just create a method that takes the packet class and a ```UUID``` as parameters, but note that the method **must** be called ```handle``` in order for it to work. The Library will call those methods on runtime using reflection.
```java
public class ExampleServerHandler extends ServerPacketHandler {

    @Override
    public void connected(UUID client) { } // Client has connected

    @Override
    public void disconnected(UUID client) { } // Client has disconnected

    public void handle(ExamplePacket packet, UUID id) { // Packet of type ExamplePacket received from client with UUID
        System.out.println(packet.s);
        int i = packet.i;
    }
}
```
To now open a server for clients to connect, you should instantiate the class ```Server```. In the constructor, it will need a protocol, we created, and an instantiated version of the server packet handler we also defined. Lastly, it will also take a port, the server will run on. Please note that instantiating a server class will immediately open the server. <br>
```java
ExampleServerHandler customHandler = new ExampleServerHandler();

Server server = new Server(protocol, customHandler, [your-port]);
```

To send packets as a server, you can call the ```sendPacket(Packet, UUID)``` method. It will take the packet to send, aswell as the ```UUID``` of the client to send to. You can also disconnect a client or close the server.

```java
server.sendPacket(new ExamplePacket("Hello from the Server!", 3), [uuid-of-the-client]); // Sends a Packet
server.disconnectClient([uuid-of-the-client]); // Disconnects a client
server.close(); // Closes the server
```
Now that the server is running, you can start creating a client for it.
#### Client Steps
To create a client, you also need to have a packet handler. This is pretty similar to the server packet handler. You should also override the necessary methods. Keep in mind, that the handler methods for the client do **not** have a ```UUID``` as a Parameter and still has to be called ```handle```, or else, it won't find those methods.
```java
public class ExampleClientHandler extends ClientPacketHandler {

    @Override
    public void connected() { } // Connected to the server

    @Override
    public void disconnected() { } // Disconnected from the Server

    public void handle(ExamplePacket packet){ // Packet of type ExamplePacket was received
        System.out.println(packet.s);
        int i = packet.i;
    }
}
```
To be able to connect to a server, you have to instantiate a ```Client```. The constructor of the client will also require the protocol, and an instantiated version of the newly defined client packet handler.
```java
ExampleClientHandler customHandler = new ExampleClientHandler();

Client client = new Client(protocol, customHandler);
```
With that client you are now able to finally connect to a server. For that, you can call the ```connect(String, int)``` method, which takes in a hostname and a port.
```java
client.connect([your-hostname], [your-port]);
```
After a successful connections you can send packets using ```sendPacket(Packet)``` and you can also disconnect or close the connection.
```java
client.sendPacket(new ExamplePacket("Hello from the Client!", 7)); // Sends a Packet
client.close(); // Disconnects
```

That is pretty much all there is to using the library. If you have never worked with networking frameworks / libraries, you may still find it really elaborate. But compared to other libraries, this is still very simple, although with a penalty in resource and bandwidth usage.
## License
This Library is licensed under the MIT License. If you want more information about that, have a look at the LICENSE file.
