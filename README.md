# [FNX.io](https://www.fnx.io/) Appengine backend template

This repository is starting point for new backends which FNX creates for its customers.

The backend has two parts: the API server (java 7 compatible) and the admin app (angular2.dart)

## Prerequisities

 1. There are 2 prerequisities for this template to be used successfuly (leaving out working git and jdk8 setup):
   - installed [maven](https://maven.apache.org/) (3.3 +-): `brew install maven` (MacOS + Homebrew)
   - installed dart with dartium (preferably for nice dev experience) `brew install dart --with-dartium`
 2. `git clone https://github.com/fnx-io/backend new-app && cd new-app`
 3. After the project is imported, rename `io.fnx.backend` packages, log4j logging configuration and web.xml to appropriate package
 / name
 4. Change group id for your new project in [server/pom.xml](server/pom.xml)
 5. Change appengine project id in [server/src/main/webapp/WEB-INF/appengine-web.xml](server/src/main/webapp/WEB-INF/appengine-web.xml)
 6. Download dart project dependencies `cd admin && pub install; cd ..`
 
## Idea setup

Idea has great support for Appengine projects and Dart language. Start with installing the dart plugin for IntelliJ.

There is non trivial amount of steps involved when setting such project up. Please follow the video to make the necessary steps to have working Idea setup.

<a href="http://www.youtube.com/watch?feature=player_embedded&v=_HA0gb1QwBM
" target="_blank"><img src="http://img.youtube.com/vi/_HA0gb1QwBM/0.jpg" width="240" height="180" border="10" /></a>

After you have working setup and managed to start the Appengine local server on [localhost:8081](http://localhost:8081) you can create first user: 

```
curl -H "Content-Type: application/json"  -X POST -d '{"name":"First User","password":"tajneheslo", "email": "email@example.com"}' http://localhost:8081/api/v1/users
```

Server response will be similar to this one, grap the user id and lets make the user admin:

```
{"id":1,"email":"email@example.com","name":"First User","role":"USER"}
```

To make the user administrator, access protected URL [http://localhost:8081/api/v1/secure/system/users/1/admin](http://localhost:8081/api/v1/secure/system/users/1/admin) in your browser. You will be presented with login screen, check the Admin button and login. This will use Appengine authentication system in production and allow to be called only by Administrators of the project. This is mocked in the dev mode. The response should look similar to:

```
{"id":1,"email":"email@example.com","name":"First User","role":"ADMIN"}
```

Notice the role `ADMIN` attribute. You can use this your information into the [admin app](http://localhost:8081/admin)

## Development

### New entity

Create following:

- entity class (package domain)
- entity service and it's implementation - remember to annotate implementation methods with `@AllowedForFooBar` annotations
- entity REST resource

And register objects in:

- ObjectifyModule (entity)
- ServiceModule (service and implementation)
- JerseyApplication (REST resource)

Consider using scaffolding tool in Dart administration:

     pub run fnx_ui:scaffold_module super_entity super_entity_listing super_entity_edit
     