# [FNX.io](https://www.fnx.io/) Appengine backend template

This repository is starting point for new backends which FNX creates for its customers.

The backend has two parts: the API server (Java 8+ compatible) and the admin app (Angular Dart)

## Prerequisities

 * Git
 * Jdk 8+
 * [Maven](https://maven.apache.org/) 3.3+
 * Dart Sdk 2.3+
 * IntelliJ 2018+
 
## Setup
 
### Sources download
 
 * Clone repository: `git clone https://github.com/fnx-io/backend new-app && cd new-app`
 * Delete git metadata to obtain pure local sources: `rm -rf .git`
 
 
### Idea setup

 * Install dart plugin for IntelliJ.

 * There is non trivial amount of steps involved when setting such project up. Please follow the video to make the necessary steps to have working Idea setup.

<a href="http://www.youtube.com/watch?feature=player_embedded&v=_HA0gb1QwBM
" target="_blank"><img src="http://img.youtube.com/vi/_HA0gb1QwBM/0.jpg" width="240" height="180" border="10" /></a>

### Project personalization 
 
 * Rename `io.fnx.backend` packages, log4j logging configuration and web.xml to appropriate package / name
 * Change group id for your new project in [server/pom.xml](server/pom.xml)
 * Change AppEngine application id in `application` tag in [server/src/main/webapp/WEB-INF/appengine-web.xml](server/src/main/webapp/WEB-INF/appengine-web.xml)
 * Update all system property names to new AppEngine id in [server/src/main/webapp/WEB-INF/appengine-web.xml](server/src/main/webapp/WEB-INF/appengine-web.xml)

### Run AppEngine local server 

 * Change port of AppEngine Dev run configuration in Idea, `Run/Edit Configurations...` menu to 8085
 * Run or debug AppEngine Dev
 * Verify that server is running on [localhost:8085](http://localhost:8085)
 
### Run Backend admin UI

 * Download `admin` dart project dependencies `cd admin && pub get`
 * Run dart development server `webdev serve`
 * Verify that backend UI is running on [localhost:8080](http://localhost:8080)   

### Creating Admin user 

 * Create user
```
curl -H "Content-Type: application/json"  -X POST -d '{"firstName":"First", "lastName":"User", "password":"tajneheslo", "email": "email@example.com"}' http://localhost:8085/api/v1/users/register
```

Server response will be similar to this one:

```
{"id":1,"email":"email@example.com","name":"First User","role":"USER"}
```

 * Give user ADMIN role by accessing protected URL (replace 'user_id' to id from server response) 
 [http://localhost:8085/api/v1/secure/system/users/1/admin](http://localhost:8085/api/v1/secure/system/users/1/admin)
  
 * You will be presented with login screen, check the Admin button and login. 
 This will use Appengine authentication system in production and allow to be called only by Administrators of the project. 
 This is mocked in the dev mode. The response should look similar to:

```
{"id":1,"email":"email@example.com","name":"First User","role":"ADMIN"}
```

Notice the role `ADMIN` attribute. 

 * Verify admin account by logging into the [admin app](http://localhost:8085/admin)

## Development

### New entity

Create following:

- entity class (package domain)
- entity service and it's implementation - remember to annotate implementation methods with `@AllowedFor<Role>` annotations
- entity REST resource

And register objects in:

- ObjectifyModule (entity)
- ServiceModule (service and implementation)
- JerseyApplication (REST resource)

Consider using scaffolding tool in Dart administration:

     pub run fnx_ui:scaffold_module super_entity super_entity_listing super_entity_edit
     
### Content Management System
     
Backend project template contains support for basic CMS (`CmsArticleEntity`). Structure of an article
is defined in Dart backend administration, and it's stored in `Map<String, Object> data`. 
Different kinds of articles are distinguished by `String type` property.
In Dart administration, type is taken from routing variable, see the main menu in `app.html`:

    <li><a [routerLink]="['Cms', {'type':'news'}]">News</a></li>
    <li><a [routerLink]="['Cms', {'type':'events'}]">Events</a></li>

Use the type value to adjust content of article form in `screen_cms_edit.html` or content of article preview in
`screen_cms_listing.html`:

    <fnx-input label="Main image" *ngIf="type == 'news'">
        <fnx-image [(ngModel)]="entity['data']['image']" [required]="true" [imageSet]="imageSet"></fnx-image>
    </fnx-input>

    <fnx-input label="Event date" *ngIf="type == 'events'">
        <fnx-date [(ngModel)]="entity['data']['eventDate']" [dateTime]="true"></fnx-date>
    </fnx-input>

This allows you to easily customize content of different types of articles, as well as share some basic properties among
different types of articles (HTML meta, project specific meta data, etc.).


It's very convenient, but it also might be limiting. In such case simply create new module and
listing and edit screens. It's also completely legal to add more properties to `CmsArticleEntity` itself,
specially when you need to search or order by some of article properties.

### Files and images

### Audit logs

To use built-in audit logging support:

- create audit log REST endpoint
    - see `CmsArticleResource : listLogEvents`
- write log events using `AuditLogManager`
    - see `CmsArticleServiceImpl` 
- use `log-listing-modal` Angular component to display log events
    - see `screen_cms_edit.dart`
    
### Frontend

Server-side rendered frontend is prepared using mint42. Please notice following:

- Default values for HTML meta-tags in WebModule
- customized values for HTML meta-tags in PagesController
- simple support for semi-static pages in PagesController
- HTTPs enforcing in HttpsRedirect (see appengine-web.xml config) 
- prepared Thyme layout templates in resources/thyme-web
      

### Configuration

#### Server side

Configuration is stored in `BackendConfiguration.java` and is loaded from system environment
properties which are defined in `appengine-web.xml`. You WILL have to change for example `file.bucket` property.

#### Client (Dart) side

There are two configurations:

- *compile time*, which is stored in `lib/conf/*` and handled by
[fnx_config](https://pub.dartlang.org/packages/fnx_config). See app initialization in `admin.dart`. It usually contains
only the API endpoint which is different for development and for production.
- *runtime* configuration, which is downloaded from the server during start. See `ClientConfiguration.java`,
`ConfigResource.java` and again `admin.dart`. This configuration should for example contain all shared enums.


     