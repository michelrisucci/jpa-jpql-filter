# JPA JPQL Filter

Project in alpha stage: under development, yet. ;)

To use snapshots from our internal Github Maven repository branch, add this code lines to your pom.xml.

```xml
<repository>
  <id>jpa-jpql-filter</id>
  <name>Simple JPA JPQL Filter</name>
  <url>https://raw.github.com/michelrisucci/jpa-jpql-filter/maven/</url>
  <snapshots>
    <enabled>true</enabled>
    <updatePolicy>always</updatePolicy>
  </snapshots>
</repository>
```

Add this project snapshot release as a dependency.

```xml
<dependency>
  <groupId>com.github.michelrisucci</groupId>
  <artifactId>jpa-jpql-filter</artifactId>
  <version>1.2.0.GA-SNAPSHOT</version>
</dependency>
```
