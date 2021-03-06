package net.aufdemrand.denizencore.utilities.scheduling;

public class RepeatingSchedulable extends Schedulable {

    public RepeatingSchedulable(Runnable runnable, float fireRate) {
        run = runnable;
        fireEverySeconds = fireRate;
        secondsLeft = fireRate;
    }

    public float fireEverySeconds = 0;

    @Override
    public boolean tick(float seconds) {
        if (cancelled) {
            return false;
        }
        secondsLeft -= seconds;
        if (secondsLeft <= 0) {
            run.run();
            secondsLeft += fireEverySeconds;
        }
        return true;
    }
}
