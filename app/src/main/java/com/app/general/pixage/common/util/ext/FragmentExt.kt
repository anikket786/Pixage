package com.app.general.pixage.common.util.ext

import androidx.navigation.NavController
import androidx.navigation.NavDirections

/**
 * The navigate method crashes when an event that should call the navigation action fires
 * multiple times in quick succession usually as a result of network response callback, sensor,
 * view events and internet connectivity callback, that calls the navigation action is
 * invoked more than once, button clicks and other user-triggered view events and the probability of
 * this occurring increases if the device is slow or laggy.
 *
 * This extension method is a wrapper over default navigate method and prevents the navigation
 * calls when destination doesn't exist. It prevents crashes on slow and laggy devices
 */
fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}