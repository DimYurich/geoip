package io.github.dimyurich.geoip

import org.springframework.stereotype.Service

@Service
class GeoIpService {
    IpInfo lookup(String ip) {
        // Return some dummy stuff for now
        return dummyIpInfo(ip)
    }

    IpInfo dummyIpInfo(String ip) {
        return new IpInfo(ip, 'US', 'United States of America', 'California', 'Oakland', 37.8044f, 122.2711f)
    }
}
