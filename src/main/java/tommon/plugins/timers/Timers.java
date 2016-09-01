package tommon.plugins.timers;

import javax.servlet.ServletContext;
import java.util.TimerTask;

/**
 * Created by majlen on 17.6.15.
 */
public abstract class Timers extends TimerTask {
    public Timers(int periodInMinutes, ServletContext context) {}
}
