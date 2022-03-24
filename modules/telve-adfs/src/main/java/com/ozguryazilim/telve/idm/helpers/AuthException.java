// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.


package com.ozguryazilim.telve.idm.helpers;

/*
Required exception class for using AuthHelper.java
*/

public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }
}
