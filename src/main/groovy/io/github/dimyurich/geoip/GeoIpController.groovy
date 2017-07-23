package io.github.dimyurich.geoip

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import io.github.dimyurich.geoip.logging.LogExecutionTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

@CompileStatic
@TypeChecked
@RestController
class GeoIpController {
    private static final Logger logger = LoggerFactory.getLogger(GeoIpController.class)

    @Autowired
    private GeoIpService service

    @LogExecutionTime
    @GetMapping(path = '/geoip', produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    IpInfo getInfo(@RequestParam('ip') String ip) {
        IpInfo info = null

        try {
            InetAddress addr = InetAddress.getByName(ip)
            info = service.lookup(ip)
        } catch (UnknownHostException uhe) {
            logger.error("Failed to resolve ipAddress={}", ip, uhe)
            throw new IllegalArgumentException(uhe)
        } catch (Exception e) {
            logger.error("Something went wrong for ipAddress={}", ip, e)
            throw new IllegalArgumentException("Something went wrong", e)
        }

        return info
    }


    @ExceptionHandler([IllegalArgumentException.class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURI() + '?' + req.getQueryString(), ex.getMessage())
    }

    @Canonical
    class ErrorInfo {
        final String url
        final String exception

        ErrorInfo(String url, String exception) {
            this.url = url
            this.exception = exception
        }
    }
}
