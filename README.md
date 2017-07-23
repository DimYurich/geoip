GeoIP pet project
====

Just another pet project.
I intend to be using it for various blog posts.
I'm planning to use Ip2Location Lite DB5 data with it.  
We'll see how it comes around.

```bash
gradle build
docker build -t geoip .
docker run -p 8080:8080 geoip
``` 

This project, as well as content of the blog, is licensed under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/) license.