# Resizer
## Installing
- maven
```
<!-- https://mvnrepository.com/artifact/com.sunquakes/resizer -->
<dependency>
    <groupId>com.sunquakes</groupId>
    <artifactId>resizer</artifactId>
    <version>0.3.1</version>
</dependency>
```
- gradle
```
// https://mvnrepository.com/artifact/com.sunquakes/resizer
compile group: 'com.sunquakes', name: 'resizer', version: '0.3.1'
```
## Getting started
### Import Package
```java
import com.sunquakes.resizer.Image;
```
### Usage
- From byte[]
```java
byte[] inImageByteArray = ...;
byte[] outImageByteArray = Image.scaleRange(inImageByteArray, 100 * 1024, 200 * 1024); // Adjust the image size to 100kb ~ 200KB
```
- From pathname
```java
String inImagePathname = "/path/to/image";
byte[] outImageByteArray = Image.scaleRange(inImagePathname, 100 * 1024, 200 * 1024); // Adjust the image size to 100kb ~ 200KB
```
- From url
```java
String inImageUrl = "http://path.to.image";
byte[] outImageByteArray = Image.scaleRange(new URL(inImageUrl), 100 * 1024, 200 * 1024); // Adjust the image size to 100kb ~ 200KB
```
