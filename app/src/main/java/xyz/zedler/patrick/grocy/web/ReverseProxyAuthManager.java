/*
 * This file is part of Grocy Android.
 *
 * Grocy Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Copyright (c) 2020-2024 by Patrick Zedler and Dominic Zedler
 * Copyright (c) 2024-2026 by Patrick Zedler
 */

package xyz.zedler.patrick.grocy.web;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.Locale;

/** Coordinates reauthentication when an API response is replaced by a proxy login page. */
public final class ReverseProxyAuthManager {

  private static final MutableLiveData<String> authenticationRequired = new MutableLiveData<>();
  private static Uri configuredServer;

  private ReverseProxyAuthManager() {}

  public static void configure(@Nullable String serverUrl) {
    configuredServer = serverUrl == null || serverUrl.isEmpty() ? null : Uri.parse(serverUrl);
  }

  public static LiveData<String> getAuthenticationRequired() {
    return authenticationRequired;
  }

  public static void consumeAuthenticationRequest() {
    authenticationRequired.setValue(null);
  }

  public static boolean handleResponse(String requestUrl, @Nullable String response) {
    if (!isConfiguredServerRequest(requestUrl) || !looksLikeLoginPage(response)) {
      return false;
    }
    authenticationRequired.postValue(configuredServer.toString());
    return true;
  }

  public static boolean looksLikeLoginPage(@Nullable String response) {
    if (response == null) {
      return false;
    }
    String lower = response.toLowerCase(Locale.ROOT);
    return lower.contains("<!doctype html")
        || lower.contains("<html")
        || lower.contains("outpost.goauthentik.io")
        || lower.contains("/if/flow/");
  }

  private static boolean isConfiguredServerRequest(String requestUrl) {
    if (configuredServer == null) {
      return false;
    }
    Uri request = Uri.parse(requestUrl);
    return configuredServer.getScheme() != null
        && configuredServer.getScheme().equalsIgnoreCase(request.getScheme())
        && configuredServer.getHost() != null
        && configuredServer.getHost().equalsIgnoreCase(request.getHost())
        && effectivePort(configuredServer) == effectivePort(request);
  }

  private static int effectivePort(Uri uri) {
    if (uri.getPort() >= 0) {
      return uri.getPort();
    }
    return "https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
  }
}
