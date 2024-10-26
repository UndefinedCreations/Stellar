package com.undefined.stellar.exception

import java.lang.RuntimeException

class UnsupportedSubCommandException : RuntimeException("This SubCommand is unsupported by Stellar! This is totally unintentional behaviour.")