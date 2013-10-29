/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.compat.progress;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Random;

@ManagedBean(name = LongTaskManager.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class LongTaskManager implements Serializable {
    public static final String BEAN_NAME = "task";

    public static final String PUSH_GROUP = "ourUser" + System.currentTimeMillis();
    private static final int MAX_PERCENT = 100;
    
    private static final int ALL = -1;

    private Random randomizer;
    private boolean taskRunning = false;
    // Used for the multiple progress bar demo
    private int[] progresses = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    private int firstComplete = -1;

    @PostConstruct
    private void init() {
        // Add our session
        PushRenderer.addCurrentSession(PUSH_GROUP);
        // Prep the generator
        randomizer = new Random(System.nanoTime());
    }

    @PreDestroy
    private void deinit() {
        // Ensure our task is stopped
        setTaskRunning(false);
    }

    public void stopTask(ActionEvent event) {
        setTaskRunning(false);
    }

    public void resetTask(ActionEvent event) {
        firstComplete = -1;
        for (int i = 0; i < progresses.length; i++) {
            progresses[i] = 0;
        }
    }

    public void stopAndResetTask(ActionEvent event) {
        stopTask(event);
        resetTask(event);
    }

    public void startThread(int minIncrease, int maxIncrease, int sleepAmount, int outputBar) {
        internalThreadMethod(minIncrease, maxIncrease, sleepAmount, outputBar);
    }

    public void startMultiThread(int minIncrease, int maxIncrease, int sleepAmount) {
        internalThreadMethod(minIncrease, maxIncrease, sleepAmount, ALL);
    }

    private void internalThreadMethod(final int minIncrease, final int maxIncrease, final int sleepAmount, final int outputBar) {
        // Reset the progress / progresses if they are at the maximum
        // Otherwise leave them alone as the user may have stopped/started the progress bar
        //  and in that case we want it to continue from the previous percent
        if (outputBar != ALL) {
            if (progresses[outputBar] == MAX_PERCENT) {
            	progresses[outputBar] = 0;
            }
        } else {
            firstComplete = -1;
            for (int i = 4; i < progresses.length; i++) {
                progresses[i] = 0;
            }
        }

        // Ensure we only have one thread going at once
        if (!taskRunning) {
            // Use our global application wide thread pool
            LongTaskPool pool = (LongTaskPool) FacesUtils.getManagedBean(LongTaskPool.BEAN_NAME);
            final PortableRenderer renderer = PushRenderer.getPortableRenderer();
            // Start a new long running process to simulate a delay
            pool.getThreadPool().execute(new Runnable() {
                public void run() {
                    int completeCount = 0;
                    setTaskRunning(true);
                    // Loop until a break condition inside
                    while (true) {
                        if (outputBar != ALL) {
                        	progresses[outputBar] += minIncrease + randomizer.nextInt(maxIncrease);
                            // Ensure that we don't break the max
                            // Also we can stop if we reach the top, instead of having an extra Thread.sleep
                            if (progresses[outputBar] >= MAX_PERCENT) {
                            	progresses[outputBar] = MAX_PERCENT;
                                break;
                            }
                        } else {
                            // Update each progress in our list
                            for (int i = 4; i < progresses.length; i++) {
                                if (progresses[i] < MAX_PERCENT) {
                                    progresses[i] += minIncrease + randomizer.nextInt(maxIncrease);
                                    // Ensure that we don't break the max for this progress
                                    if (progresses[i] >= MAX_PERCENT) {
                                        progresses[i] = MAX_PERCENT;
                                        completeCount++;
                                        // Note which bar completed first
                                        if (completeCount == 1) {
                                            firstComplete = i;
                                        }
                                    }
                                }
                            }
                            // Stop looping if we have completed all the progresses in our list
                            if (completeCount == progresses.length) {
                                break;
                            }
                        }
                        // Render the updated progress
                        renderer.render(PUSH_GROUP);
                        // Simulate a pause
                        try {
                            Thread.sleep(sleepAmount);
                        } catch (Exception ignored) {
                        }

                        // Ensure we're not supposed to stop
                        if (!taskRunning) {
                            break;
                        }
                    }
                    // Complete the task and update the page
                    setTaskRunning(false);
                    renderer.render(PUSH_GROUP);
                }
            });
        }
    }

    ////////------------------GETTERS & SETTERS BEGIN
    public boolean getTaskRunning() {
        return taskRunning;
    }

    public int[] getProgresses() {
        return progresses;
    }

    public int getFirstComplete() {
        return firstComplete;
    }

    public boolean getHasFirstComplete() {
        return firstComplete >= 0;
    }

    public void setTaskRunning(boolean taskRunning) {
        this.taskRunning = taskRunning;
    }

    public void setProgresses(int[] progresses) {
        this.progresses = progresses;
    }

    public void setFirstComplete(int firstComplete) {
        this.firstComplete = firstComplete;
    }

}
