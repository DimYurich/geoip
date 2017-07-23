package io.github.dimyurich.geoip

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

import static groovy.json.JsonOutput.toJson
import static org.mockito.BDDMockito.given
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner.class)
@WebMvcTest(GeoIpController.class)
@CompileStatic
@TypeChecked
class GeoIpControllerTests {
    @Autowired
    private MockMvc mvc

    @MockBean
    private GeoIpService service

    @Test
    void ipv4Succeeds() {
        def ip = '8.8.8.8'
        given(service.lookup(ip)).willReturn(ipInfoFor(ip))

        mvc.perform(get("/geoip?ip=${ip}"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(toJson(["ipAddress": ip,
                                                  "countryCode": "US",
                                                  "country": "United States of America",
                                                  "region": "California",
                                                  "city": "Oakland",
                                                  "latitude": 37.8044,
                                                  "longitude": 122.2711]), true))
    }

    @Test
    void ipv6Succeeds() {
        def ip = '2001:4860:4860::8888'
        given(service.lookup(ip)).willReturn(ipInfoFor(ip))

        mvc.perform(get("/geoip?ip=${ip}"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(
                    toJson([
                            "ipAddress": ip,
                            "countryCode": "US",
                            "country": "United States of America",
                            "region": "California",
                            "city": "Oakland",
                            "latitude": 37.8044,
                            "longitude": 122.2711
                            ]), true))
    }

    @Test
    void blahFails() {
        def ip = 'blah'
        given(service.lookup(ip)).willReturn(ipInfoFor(ip))

        mvc.perform(get("/geoip?ip=${ip}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(
                    toJson([
                            "url": "/geoip?ip=${ip}",
                            "exception": "java.net.UnknownHostException: ${ip}: Name or service not known"
                            // 2nd blah is how Inet6Address reports an unknown host. A little ugly but we can live with it, right? :)
                           ]), true))
    }

    private static IpInfo ipInfoFor(String ip) {
        return new IpInfo(ip, 'US', 'United States of America', 'California', 'Oakland', 37.8044f, 122.2711f)
    }
}
