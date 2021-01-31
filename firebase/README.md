# Firebase

Firebase Authentication is a service provided by Google that supports authentication using passwords, phone numbers, and
other identity providers (such as Google, Facebook, and Twitter), etc. It provides an easy-to-use SDK for many
languages, including JavaScript, Golang and Kotlin.

The authentication, can be used in conjunction with Spring Boot Security to authenticate the API requests, depending on
the permissions of the user (explained in the Implementation section).

It could also be manually implemented and stored in a DB, but to achieve the levels of security and integrations that
Firebase offers, would be a project by itself, which is why this solution has been chosen.

## Firebase Setup

After the setup wizard, the only configuration needed on the developers end is to activate which services are allowed to
do the signin with, in the case of this project email/password and Google.

Then, in the project settings the json file with the credentials of the project can be downloaded. This will be used
later on, to interact with the firebase project from the program itself.

## Firestore Setup

Cloud Firestore is a cloud-hosted, NoSQL database that your iOS, Android, and web apps can access directly via native
SDKs. Cloud Firestore is also available in native Node.js, Java, Python, Unity, C++ and Go SDKs, in addition to REST and
RPC APIs.

Following Cloud Firestore's NoSQL data model, you store data in documents that contain fields mapping to values. These
documents are stored in collections, which are containers for your documents that you can use to organize your data and
build queries. Documents support many different data types, from simple strings and numbers, to complex, nested objects.

The following code, is the security configuration, that will accept or deny the read requests depending on the user.

For this project, is configured in a way where the user that is authenticated can only read its own data, while the
admin is the responsible for creating, updating and deleting it.

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    function isAdmin() {
    	return get(/databases/$(database)/documents/users/$(request.auth.uid)).data.isAdmin;
    }
    match /{document=**} {
    	allow read, write: if false;
    }
    // Make sure the uid of the requesting user matches name of the user
    // document. The wildcard expression {userId} makes the userId variable
    // available in rules.
    match /users/{userId} {
      allow read: if request.auth != null && request.auth.uid == userId;
      allow create, delete, write, update: if isAdmin();
    }
  }
}
```

### Documentation

https://firebase.google.com/docs/firestore/manage-data/add-data#java_21

https://firebase.google.com/docs/firestore/security/rules-conditions