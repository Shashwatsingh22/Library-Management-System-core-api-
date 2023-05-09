# ProjectSISA - core_api

# How do I get set up?
**Intellij Setup**
- Import code as Maven project or Gradle Project
- Install [Env plugin](https://plugins.jetbrains.com/plugin/7861-envfile)
- Run GradleRun or MavenRun config

**Mac Setup**
```
$ brew install postgresql
$ psql postgres

# create role lms_api superuser login;
# create database lmsdb owner lms_api;
# alter role lms_api password 'lms@123';
# create database lmsdbtest owner lms_api;
# exit;

- Install Intellij Idea Community Edition
- Install [Env plugin](https://plugins.jetbrains.com/plugin/7861-envfile)
- Import as maven project with JDK 17
- You will find `CoreApplication` run configuration already present. Run it to start application

Hit the following urls
http://localhost:9000/v1/ping
http://localhost:9000/v1/api-docs
http://localhost:9000/v1/swagger-ui/index.html
```

### Dev Env Setup
- Install Intellij Idea Community Edition
- Install [Env plugin](https://plugins.jetbrains.com/plugin/7861-envfile)
- Import as maven project with JDK 17
- You will find `CoreApplication` run configuration already present. Run it to start application

Hit the following urls
http://localhost:9000/v1/ping
http://localhost:9000/v1/api-docs
http://localhost:9000/v1/swagger-ui/index.html
