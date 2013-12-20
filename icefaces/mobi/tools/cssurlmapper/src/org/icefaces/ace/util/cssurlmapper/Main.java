
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

package org.icefaces.ace.util.cssurlmapper;

import java.io.*;
import java.util.*;
import org.apache.commons.cli.*;

public class Main {

	public static void main(String[] args) {
		
		Options options = new Options();

		Option helpOpt = OptionBuilder.withDescription("print this message").withLongOpt("help").create('h');
		Option rootDirOpt = OptionBuilder.withArgName("directory").hasArg().withDescription("root directory containing all CSS files to be processed").withLongOpt("root-dir").create('r');		
		Option fileOpt = OptionBuilder.withArgName("file").hasArg().withDescription("individual file to process").withLongOpt("file").create('f');
		Option libraryNameOpt = OptionBuilder.withArgName("name").hasArg().withDescription("name of JSF resource library to include in mapped URLs (required)").withLongOpt("library-name").create('l');
		Option referenceDirOpt = OptionBuilder.withArgName("directory").hasArg().withDescription("directory to use as starting point when building relative paths to resources").withLongOpt("reference-dir").create('n');
		Option outputDirOpt = OptionBuilder.withArgName("directory").hasArg().withDescription("directory to where all output files will be written (required)").withLongOpt("output-dir").create('o');
		
		options.addOption(helpOpt);
		options.addOption(rootDirOpt);
		options.addOption(fileOpt);
		options.addOption(libraryNameOpt);
		options.addOption(referenceDirOpt);
		options.addOption(outputDirOpt);
		
		PosixParser posixParser = new PosixParser();
		
		String libraryNameVal = null;
		String fileVal = null;
		String rootDirVal = null;
		String referenceDirVal = null;
		String outputDirVal = null;
		
		File rootDir = null;
		File file = null;
		File referenceDir = null;
		File outputDir = null;
		
		try {
			CommandLine line = posixParser.parse(options, args);
			
			// check for help option first
			if (line.hasOption('h')) {
				printHelp(options);
				Runtime.getRuntime().exit(0);
			}
			
			// make sure library name was specified
			if (line.hasOption('l')) {
				libraryNameVal = line.getOptionValue('l');
			} else {
				throw new MissingOptionException("ERROR: library name was not supplied.");
			}
			
			// check for root directory or file
			if (line.hasOption('f')) {
				fileVal = line.getOptionValue('f');
			}
			
			if (line.hasOption('r')) {
				rootDirVal = line.getOptionValue('r');
			}
			
			if (fileVal == null && rootDirVal == null) {
				throw new MissingOptionException("ERROR: either file or root directory must be specified.");
			}
			
			if (rootDirVal != null) {
				rootDir = new File(rootDirVal);
				if (!rootDir.exists()) {
					throw new Exception("ERROR: root directory does not exist.");
				} else if (!rootDir.isDirectory()) {
					throw new Exception("ERROR: root directory is not a real directory.");
				}
			} else { // file must be non-null then
				file = new File(fileVal);
				if (!file.exists()) {
					throw new Exception("ERROR: input file does not exist.");
				} else if (!file.isFile()) {
					throw new Exception("ERROR: input file is not a real file.");
				}
			}
			
			// check for output directory
			if (line.hasOption('o')) {
				outputDirVal = line.getOptionValue('o');
				outputDir = new File(outputDirVal);
				outputDir.mkdirs();
				if (!outputDir.exists()) {
					throw new Exception("ERROR: could not create output directory.");
				} else if (!outputDir.isDirectory()) {
					throw new Exception("ERROR: output directory is not a real directory.");
				}
			} else {
				throw new MissingOptionException("ERROR: output directory was not supplied.");
			}
			
			// check for reference directory
			if (line.hasOption('n')) {
				referenceDirVal = line.getOptionValue('n');
				referenceDir = new File(referenceDirVal);
				if (!referenceDir.exists()) {
					throw new Exception("ERROR: reference directory does not exist.");
				} else if (!referenceDir.isDirectory()) {
					throw new Exception("ERROR: reference directory is not a real directory.");
				}
				String referenceDirPath = referenceDir.getCanonicalPath();
				if (rootDir != null) {
					String rootDirPath = rootDir.getCanonicalPath();
					if (!rootDirPath.startsWith(referenceDirPath)) {
						throw new Exception("ERROR: reference directory must be an ancestor of root directory.");
					}
				} else { // file must be non-null then
					String filePath = file.getCanonicalPath();
					if (!filePath.startsWith(referenceDirPath)) {
						throw new Exception("ERROR: reference directory must be an ancestor of input file.");
					}
				}
			} 

		} catch (ParseException e) {
			System.out.println(e.getMessage());
			System.out.println();
			printHelp(options);
			Runtime.getRuntime().exit(0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Runtime.getRuntime().exit(0);
		}
		
		// all input was validated, so proceed with file processing
		
		if (rootDir != null) {
			if (referenceDir != null) {
				processDirectory(rootDir, libraryNameVal, referenceDir, outputDir);
			} else {
				processDirectory(rootDir, libraryNameVal, rootDir, outputDir);
			}
		} else {
			processFile(file, libraryNameVal, referenceDir, outputDir);
		}
	}
	
	private static void printHelp(Options options) {

		System.out.println("This tool parses CSS files and maps URLs from a standard format to the JSF");
		System.out.println("resource loader format. For example, an image in a different directory than");
		System.out.println("the CSS file would be mapped as follows:");
		System.out.println("   url(../icon.gif) => url(#{resource['mylibrary/images/icon.gif']})");
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("cssurlmapper", options);
		System.out.println("It is required to specify the name of the library where the resource will be");
		System.out.println("located in the production environment. Additionally, the JSF resource loader");
		System.out.println("does not accept occurrences of '..' in path names. So, the full path of the");
		System.out.println("resource, starting from the root library folder, will be resolved by this tool.");
		System.out.println("The starting directory for building this path can be specified with the");
		System.out.println("--reference-dir option. Otherwise, the value of --root-dir will be used.");
	}
	
	public static void processDirectory(File dir, String libraryName, File referenceDir, File outputDir) {
		try {
			System.out.println("Entering directory " + dir.getCanonicalPath());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++) {
			try {
				File file = children[i];
				if (file.isHidden()) { // do not process hidden files and directories
					continue;
				}
				if (file.isDirectory()) {
					File newOutputDir = new File(outputDir, file.getName());
					newOutputDir.mkdir();
					processDirectory(file, libraryName, referenceDir, newOutputDir);
				} else if (file.isFile()) {
					String path = file.getCanonicalPath();
					if (path.length() > 4 && path.substring(path.length() - 4).equalsIgnoreCase(".css")) {
						processFile(file, libraryName, referenceDir, outputDir);
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		try {
			System.out.println("Leaving directory " + dir.getCanonicalPath());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void processFile(File file, String libraryName, File referenceDir, File outputDir) {
		CssUrlMapper mapper = new CssUrlMapper(file, libraryName, referenceDir, outputDir);
		mapper.run();
	}
}
