grails-melody-plugin
====================

[JavaMelody](https://github.com/javamelody/javamelody/wiki) monitoring plugin for Grails 5, to monitor application performance.

<a href='Screenshots#charts'><img src='https://github.com/javamelody/javamelody/wiki/resources/screenshots/graphs.png' alt='Screenshots' width='50%' title='Screenshots' /></a>

_The goal of JavaMelody is to monitor applications in QA and production environments. It is not a tool to simulate requests from users, it is a tool to measure and calculate statistics on real operation of an application depending on the usage of the application by users._

### Installation ###

To install the plugin, add the following dependency:
```yaml
dependencies {
    runtimeOnly 'org.grails.plugins:grails-melody-plugin:2.0.0'
}
```

Then you will be able to monitor the application at ```http://localhost:8080/<YourContext>/monitoring```.

[![Download Plugin](...) ](...)

[JavaMelody Release Notes](https://github.com/javamelody/javamelody/wiki/ReleaseNotes) 

### More configuration ###

A few things you might want to know:
* the plugin overwrites original grails 'dataSource' bean in spring context with a JavaMelody datasource proxy.
* the plugin uses groovy meta programming to intercept grails services method calls.
  
All parameters described in the [JavaMelody User's guide](https://github.com/javamelody/javamelody/wiki/UserGuide#6-optional-parameters)
can be configured in your grails-app/conf/application.yml file. For example, add the following to disable the monitoring:
```yaml
javamelody:
    disabled: true
```

JavaMelody uses URIs to resolve HTTP requests. This means that
```
/book/show/1 and 
/book/show/23 
```

will resolve as different requests.  While that's desirable in some cases, often you want the statistics to be gathered for the show action, irrespective of parameters. In that case, add the following configuration in your grails-app/conf/application.yml file and the above URIs will show up as /book/show/$. 
```yaml
javamelody:
    # filter out numbers from URI
    http-transform-pattern: \d+
```

Similar issue may come for SQL monitoring - you can use a similar Regex to filter it.
```yaml
javamelody:
    sql-transform-pattern: \d+
```

Other parameters such as storage-directory, url-exclude-pattern, log, monitoring-path, authorized-users or allowed-addr-pattern can also be configured.

You can also add [rules for the spring security plugin](https://grails-plugins.github.io/grails-spring-security-core/v3/index.html#requestMappings), if installed. If configuring with `application.groovy`:
```groovy
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
  [pattern: '/monitoring', access: ['ROLE_ADMIN']]
]
```
If configuring with `application.yml`:
```yaml
grails:
  plugin:
    springsecurity:
      controllerAnnotations:
        staticRules:
          - pattern: '/monitoring'
            access: ['ROLE_ADMIN']
```

Or you can add an authorized-users parameter:
```yaml
javamelody:
    authorized-users: user1:pwd1, user2:pwd2
```
