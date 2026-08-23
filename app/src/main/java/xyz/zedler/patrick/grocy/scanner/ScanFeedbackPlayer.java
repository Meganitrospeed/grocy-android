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

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import androidx.preference.PreferenceManager;
import xyz.zedler.patrick.grocy.Constants.SETTINGS.SCANNER;
import xyz.zedler.patrick.grocy.Constants.SETTINGS_DEFAULT;
import xyz.zedler.patrick.grocy.util.HapticUtil;

/** Plays optional feedback after the camera scanner decodes a non-empty barcode. */
final class ScanFeedbackPlayer {

  private static final int BEEP_DURATION_MS = 150;
  private static final int BEEP_VOLUME = 80;

  private final ScanFeedbackController controller;
  private ToneGenerator toneGenerator;

  ScanFeedbackPlayer(Context context) {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    HapticUtil hapticUtil = new HapticUtil(context);
    controller = new ScanFeedbackController(
        () -> sharedPrefs.getBoolean(
            SCANNER.SOUND_FEEDBACK,
            SETTINGS_DEFAULT.SCANNER.SOUND_FEEDBACK
        ),
        () -> sharedPrefs.getBoolean(
            SCANNER.VIBRATION_FEEDBACK,
            SETTINGS_DEFAULT.SCANNER.VIBRATION_FEEDBACK
        ),
        this::playSound,
        hapticUtil::tick
    );
  }

  void play() {
    controller.play();
  }

  void release() {
    if (toneGenerator != null) {
      toneGenerator.release();
      toneGenerator = null;
    }
  }

  private void playSound() {
    if (toneGenerator == null) {
      toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, BEEP_VOLUME);
    }
    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, BEEP_DURATION_MS);
  }
}
