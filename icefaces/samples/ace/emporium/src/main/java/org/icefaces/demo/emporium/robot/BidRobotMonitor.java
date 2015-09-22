package org.icefaces.demo.emporium.robot;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class used to monitor each BidRobot
 * This is necessary in case we shutdown the app server and want to properly clean up any threads
 */
public class BidRobotMonitor implements Serializable {
	private static final long serialVersionUID = -1362622742201870812L;

	private static BidRobotMonitor singleton = null;

    private final Lock bidRobotSetLock = new ReentrantLock();
    private final Set<BidRobot> bidRobotSet = new HashSet<BidRobot>();

    private BidRobotMonitor() {
    }

    public static BidRobotMonitor getInstance() {
		if (singleton == null) {
			singleton = new BidRobotMonitor();
		}
		return singleton;
    }
    
    public void addBidRobot(BidRobot toAdd) {
        if (!bidRobotSet.contains(toAdd)) {
            bidRobotSetLock.lock();
            try {
                if (!bidRobotSet.contains(toAdd)) {
                    bidRobotSet.add(toAdd);
                }
            }finally {
                bidRobotSetLock.unlock();
            }
        }
    }

    public void removeBidRobot(BidRobot toRemove) {
        if (bidRobotSet.contains(toRemove)) {
            bidRobotSetLock.lock();
            try {
                if (bidRobotSet.contains(toRemove)) {
                    bidRobotSet.remove(toRemove);
                }
            }finally {
                bidRobotSetLock.unlock();
            }
        }
    }
    
    public void stop() {
        if (!bidRobotSet.isEmpty()) {
            bidRobotSetLock.lock();
            try {
                if (!bidRobotSet.isEmpty()) {
                    for (BidRobot loopRobot : bidRobotSet) {
                        loopRobot.stop();
                    }
                }
            }finally {
                bidRobotSetLock.unlock();
            }
        }
    }
}
