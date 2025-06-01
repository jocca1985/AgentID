package com.incodesmile.idvmcp.dto;

public record DeviceFingerprintInfoDto(
        String desktopDevice,
        String desktopIp,
        GeoLocationDto desktopGeoLocation,
        String mobileDevice,
        String mobileIp,
        GeoLocationDto mobileGeoLocation,
        String appVersion
) {}
