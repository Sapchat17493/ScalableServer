package cs455.scaling.client;

import cs455.scaling.helpers.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is a Timer Task that utilises a timer and subscribes itself to it. This class utilises an interval of
 * Constants.STATS_LOGGER_INTERVAL_MILLIS in order to schedule the printing of Client Statistics at regular intervals.
 */
public class ClientStats extends TimerTask {
    private Timer timer;
    private Client client;

    ClientStats(Client client) {
        this.client = client;
        this.timer = new Timer("ClientStatsDisplayer");
    }

    @Override
    public void run() {
        client.printStats(); //Invoke print statistics method in Client class
    }

    void startExecution() {
        timer.scheduleAtFixedRate(this, Constants.STATS_LOGGER_START_DELAY_MILLIS, Constants.STATS_LOGGER_INTERVAL_MILLIS);
    }
}
