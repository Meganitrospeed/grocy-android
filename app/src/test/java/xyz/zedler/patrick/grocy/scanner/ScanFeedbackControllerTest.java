package xyz.zedler.patrick.grocy.scanner;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class ScanFeedbackControllerTest {

  @Test
  public void playsBothEnabledFeedbackTypes() {
    AtomicInteger sounds = new AtomicInteger();
    AtomicInteger vibrations = new AtomicInteger();
    ScanFeedbackController controller = new ScanFeedbackController(
        () -> true,
        () -> true,
        sounds::incrementAndGet,
        vibrations::incrementAndGet
    );

    controller.play();

    assertEquals(1, sounds.get());
    assertEquals(1, vibrations.get());
  }

  @Test
  public void skipsDisabledFeedbackTypes() {
    AtomicInteger sounds = new AtomicInteger();
    AtomicInteger vibrations = new AtomicInteger();
    ScanFeedbackController controller = new ScanFeedbackController(
        () -> false,
        () -> false,
        sounds::incrementAndGet,
        vibrations::incrementAndGet
    );

    controller.play();

    assertEquals(0, sounds.get());
    assertEquals(0, vibrations.get());
  }

  @Test
  public void soundAndVibrationAreIndependent() {
    AtomicInteger sounds = new AtomicInteger();
    AtomicInteger vibrations = new AtomicInteger();
    ScanFeedbackController controller = new ScanFeedbackController(
        () -> true,
        () -> false,
        sounds::incrementAndGet,
        vibrations::incrementAndGet
    );

    controller.play();

    assertEquals(1, sounds.get());
    assertEquals(0, vibrations.get());
  }

  @Test
  public void readsCurrentSettingForEveryScan() {
    boolean[] soundEnabled = {false};
    AtomicInteger sounds = new AtomicInteger();
    ScanFeedbackController controller = new ScanFeedbackController(
        () -> soundEnabled[0],
        () -> false,
        sounds::incrementAndGet,
        () -> { }
    );

    controller.play();
    soundEnabled[0] = true;
    controller.play();

    assertEquals(1, sounds.get());
  }
}
