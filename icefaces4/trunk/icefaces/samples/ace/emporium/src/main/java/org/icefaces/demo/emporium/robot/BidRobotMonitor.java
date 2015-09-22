package org.icefaces.demo.emporium.robot;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(eager = true, name = BidRobotMonitor.BEAN_NAME)
@ApplicationScoped
public class BidRobotMonitor {
    private static final Logger LOGGER = Logger.getLogger(BidRobotMonitor.class.getName());

    public static final String BEAN_NAME = "bidRobotMonitor";

    private static BidRobotMonitor instance;

    private final Lock bidRobotSetLock = new ReentrantLock();
    private final Set<BidRobot> bidRobotSet = new HashSet<BidRobot>();

    public BidRobotMonitor() {
        instance = this;
    }

    public void cleanUp() {
        if (!getBidRobotSet().isEmpty()) {
            getBidRobotSetLock().lock();
            try {
                if (!getBidRobotSet().isEmpty()) {
                    for (final BidRobot _bidRobot : getBidRobotSet()) {
                        _bidRobot.stop();
                    }
                }
            } finally {
                getBidRobotSetLock().unlock();
            }
        }
    }

    public static BidRobotMonitor getInstance() {
        return instance;
    }

    protected Set<BidRobot> getBidRobotSet() {
        return bidRobotSet;
    }

    protected Lock getBidRobotSetLock() {
        return bidRobotSetLock;
    }

    void addBidRobot(final BidRobot bidRobot) {
        if (!getBidRobotSet().contains(bidRobot)) {
            getBidRobotSetLock().lock();
            try {
                if (!getBidRobotSet().contains(bidRobot)) {
                    getBidRobotSet().add(bidRobot);
                }
            } finally {
                getBidRobotSetLock().unlock();
            }
        }
    }

    void removeBidRobot(final BidRobot bidRobot) {
        if (getBidRobotSet().contains(bidRobot)) {
            getBidRobotSetLock().lock();
            try {
                if (getBidRobotSet().contains(bidRobot)) {
                    getBidRobotSet().remove(bidRobot);
                }
            } finally {
                getBidRobotSetLock().unlock();
            }
        }
    }
}
