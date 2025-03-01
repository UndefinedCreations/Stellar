package com.undefined.stellar.exception

class UnsupportedVersionException(supportedVersions: Collection<String>) : Exception("This minecraft version is unsupported! Supported versions: $supportedVersions")