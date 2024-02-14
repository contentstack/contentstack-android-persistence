[![Contentstack](https://www.contentstack.com/docs/static/images/contentstack.png)](https://www.contentstack.com/)

# Contentstack Android Persistence Library

Contentstack provides [Android Persistence Library](https://www.contentstack.com/docs/guide/synchronization/using-realm-persistence-library-with-android-sync-sdk) that lets your application store data on the device's local storage. This helps you build apps that can work offline too. Given below is a detailed guide and helpful resources to get started with our android Persistence Library.

## Prerequisites

* [Android Studio](https://developer.android.com/studio/) IDE
* [Java JDK 8](https://www.oracle.com/technetwork/es/java/javase/downloads/jdk8-downloads-2133151.html) or above installed in your machine
* [Realm](https://www.mongodb.com/docs/realm/sdk/java/install/#std-label-java-install) should be installed

## Setup and initialize library

### To use realm local database

Download the following four files from [Repository](https://github.com/contentstack/contentstack-android-persistence/tree/master/app/src/main/java/com/contentstack/persistence)  and place them in your src folder:

- `RealmStore`
- `SyncManager`
- `SyncPersistable`
- `SyncStore`
- `Utils`

### Initialize the library

You can initialize the Persistence SDK using the code below in your application.

```java  
Stack stack = Contentstack.stack(applicationContext,"apiKey","deliveryToken","environment");    
Realm realmInstance = Realm.getDefaultInstance(); // gets default realm instance
RealmStore realmStore = new RealmStore(realmInstance); ////Provide realmInstance to RealmStore's constructor like below
SyncManager manager = new SyncManager(realmStore,stack);  
manager.stackRequest();
```  

***Note:** Explore our example app built with the Android Persistence SDK, which securely stores data on your Android device's local storage. [Read the tutorial](https://github.com/contentstack/contentstack-android-persistence-example) to begin with the example app.*

*Please write migrations in case table schema changes are expected.*

### Helpful Links

- [Android Persistence Library Docs](https://www.contentstack.com/docs/guide/synchronization/using-realm-persistence-library-with-android-sync-sdk)
- [Android Persistence Example App](https://github.com/contentstack/contentstack-android-persistence-example)
- [Content Delivery API Docs](https://contentstack.com/docs/apis/content-delivery-api/)