package io.github.dimyurich.geoip

import groovy.transform.Canonical

@Canonical
class IpInfo {
    String ipAddress
    String countryCode
    String country
    String region
    String city
    float latitude
    float longitude
}
