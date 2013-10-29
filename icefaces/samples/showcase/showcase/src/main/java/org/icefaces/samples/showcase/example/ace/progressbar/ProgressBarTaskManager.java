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

package org.icefaces.samples.showcase.example.ace.progressbar;

import java.io.Serializable;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.samples.showcase.example.compat.progress.LongTaskPool;
import org.icefaces.samples.showcase.util.FacesUtils;

@ManagedBean(name = ProgressBarTaskManager.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ProgressBarTaskManager implements Serializable {
	public static final String BEAN_NAME = "longtask";

	public static final String PUSH_GROUP = "ourUser"
			+ System.currentTimeMillis();
	private static final int MAX_PERCENT = 100;

	private Random randomizer;
	private boolean taskRunning = false;

	private int progress = 0;

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
		// Reset to zero
		progress = 0;
	}

	public void stopAndResetTask(ActionEvent event) {
		stopTask(event);
		resetTask(event);
	}

	public void startThread(int minIncrease, int maxIncrease, int sleepAmount) {
		internalThreadMethod(minIncrease, maxIncrease, sleepAmount);
	}

	private void internalThreadMethod(final int minIncrease,
			final int maxIncrease, final int sleepAmount) {
		// Reset the progress if it is at the maximum
		// Otherwise leave them alone as the user may have stopped/started the
		// progress bar
		// and in that case we want it to continue from the previous percent
		if (progress == MAX_PERCENT) {
			progress = 0;
		}

		// Ensure we only have one thread going at once
		if (!taskRunning) {
			// Use our global application wide thread pool
			LongTaskPool pool = (LongTaskPool) FacesUtils
					.getManagedBean(LongTaskPool.BEAN_NAME);
			final PortableRenderer renderer = PushRenderer
					.getPortableRenderer();
			// Start a new long running process to simulate a delay
			pool.getThreadPool().execute(new Runnable() {
				public void run() {
					setTaskRunning(true);
					// Loop until a break condition inside
					while (true) {
						progress += minIncrease
								+ randomizer.nextInt(maxIncrease);
						// Ensure that we don't break the max
						// Also we can stop if we reach the top, instead of
						// having an extra Thread.sleep
						if (progress >= MAX_PERCENT) {
							progress = MAX_PERCENT;
							break;
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

	// //////------------------GETTERS & SETTERS BEGIN
	public boolean getTaskRunning() {
		return taskRunning;
	}

	public int getProgress() {
		return progress;
	}

	public void setTaskRunning(boolean taskRunning) {
		this.taskRunning = taskRunning;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
