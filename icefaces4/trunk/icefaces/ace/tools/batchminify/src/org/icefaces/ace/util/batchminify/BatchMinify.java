/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.util.batchminify;

import com.google.javascript.jscomp.CommandLineRunner;

public class BatchMinify {

	public static void main(String[] args) {
		if (args.length > 0) {
			String paths = args[0];
			String[] files = paths.split(";");
			for (int i = 0; i < files.length; i++) {
				System.out.println("Compressing " + files[i]);
				String[] arguments = new String[8];
				arguments[0] = "--jscomp_off";
				arguments[1] = "misplacedTypeAnnotation";
				arguments[2] = "--jscomp_off";
				arguments[3] = "uselessCode";
				arguments[4] = "--js";
				arguments[5] = files[i];
				arguments[6] = "--js_output_file";
				arguments[7] = files[i].replaceAll("\\.js$", ".c.js");
				MyCommandLineRunner runner = new MyCommandLineRunner(arguments);
				runner.myRun();
			}
			System.exit(0);
		}
    }

	public static class MyCommandLineRunner extends CommandLineRunner {

		public MyCommandLineRunner(String[] args) {
			super(args);
		}

		public void myRun() {
			try {
				doRun();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
