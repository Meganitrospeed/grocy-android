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

package xyz.zedler.patrick.grocy.scanner;

import java.util.function.BooleanSupplier;

/** Applies the independently configurable sound and vibration scan feedback. */
final class ScanFeedbackController {

  private final BooleanSupplier soundEnabled;
  private final BooleanSupplier vibrationEnabled;
  private final Runnable playSound;
  private final Runnable vibrate;

  ScanFeedbackController(
      BooleanSupplier soundEnabled,
      BooleanSupplier vibrationEnabled,
      Runnable playSound,
      Runnable vibrate
  ) {
    this.soundEnabled = soundEnabled;
    this.vibrationEnabled = vibrationEnabled;
    this.playSound = playSound;
    this.vibrate = vibrate;
  }

  void play() {
    if (soundEnabled.getAsBoolean()) {
      playSound.run();
    }
    if (vibrationEnabled.getAsBoolean()) {
      vibrate.run();
    }
  }
}
