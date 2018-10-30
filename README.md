[![Contentstack](https://www.contentstack.com/docs/static/images/contentstack.png)](https://www.contentstack.com/)

# Android SDK for Contentstack Persistence


Contentstack provides [Android Persistence Library](https://www.contentstack.com/docs/guide/synchronization/using-realm-persistence-library-with-android-sync-sdk) that lets your application store data on the device's local storage. This helps you build apps that can work offline too. Given below is a detailed guide and helpful resources to get started with our android Persistence Library.

## Prerequisite

 * [Android Studio](https://developer.android.com/studio/)
 * [Java jdk 8](https://www.oracle.com/technetwork/es/java/javase/downloads/jdk8-downloads-2133151.html)

## Setup and Initialize library

You can use the android Persistence Library with Realm databases.  Let's understand how to set these up for your project.

### For Realm

You download below four files from [Repository](https://github.com/contentstack/contentstack-android-persistence/tree/master/app/src/main/java/com/contentstack/persistence) and keep it in your src folder.  

    - RealmStore
    - SyncManager  
    - SyncPersistable  
    - SyncStore  


#### Initialize the library

To start using the library in your application, you will need to initialize it by providing the stack details:

```
    //Get stack instance like below
    Stack stack = Contentstack.stack(context, "api_key", "access_token", "environment");
    
    //Provide realmInstance using 
    Realm realmInstance = Realm.getDefaultInstance();
    //Provide realmInstance to RealmStore's constructor like below.
    RealmStore realmStore = new RealmStore(realmInstance);
    
    SyncManager manager = new SyncManager(realmStore, stack);
    manager.stackRequest();
 
```
We have created an example app using android Persistence Library that stores data on the device's local storage. [Read the tutorial](https://github.com/contentstack/contentstack-android-persistence-example) to get started with the example app.   

### Helpful Links

- [Android Persistence Library Docs](https://www.contentstack.com/docs/guide/synchronization/using-realm-persistence-library-with-android-sync-sdk)
- [Android Persistence Example App](https://github.com/contentstack/contentstack-android-persistence-example)
- [Content Delivery API Docs](https://contentstack.com/docs/apis/content-delivery-api/)
