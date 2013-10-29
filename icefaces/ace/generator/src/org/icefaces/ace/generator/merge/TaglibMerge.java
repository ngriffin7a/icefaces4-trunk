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

package org.icefaces.ace.generator.merge;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Take two files, copy the namespace and tags from the first one, then copy only
 * the tags from the second one and write to a third file. Currently, the features
 * of this merge operation are: <ul>
 * <li>checks for duplicate tags </li>
 * <li>re-writes the handler-class from the compat file to
 * use a class that is now present in the core </li>
 * </ul> 
 */
public class TaglibMerge {

    public static void main(String[] args) {
        if (args.length != 3 || args[0].equals("?") || args[0].contains("help")) {
            usage();
        }

        TaglibMerge.merge( args[0], args[1], args[2]);

    }

    public static void usage() {
        System.out.println("java NamespaceMerge \"taglib File A\" \"taglib File B\" \"Output File\" Merges tags in File B into the taglib File A and writes results into outputFile");
    }

    public static void merge(String taglibFileA, String taglibFileB, String outputFileName) {

        File fileA = new File (taglibFileA);
        File fileB = new File (taglibFileB);
        File outputFile = new File (outputFileName);
        if (! fileA.exists() ) {
            System.out.println("Main merge file: " + fileA.getAbsolutePath() + " does not exist");
            return;
        }

        if (! fileB.exists() ) {
            System.out.println("Sub merge file: " + fileB.getAbsolutePath() + " does not exist");
            return;
        }

//        System.out.println("See if printed output winds up in log!");


        try {
            FileReader fra = new FileReader(fileA);
            FileReader frb = new FileReader(fileB); 
            BufferedReader fa = new BufferedReader( fra );
            BufferedReader fb = new BufferedReader( frb );

            StringBuffer readerA = new StringBuffer( );
            String str;
            while ((str = fa.readLine()) != null) { 
                readerA.append(str).append("\n");
            }
            fra.close();

            StringBuffer readerB = new StringBuffer( );
            while ((str = fb.readLine()) != null) {
                readerB.append(str).append("\n");
            }
            frb.close();

            // Check for duplicate tags
            if (checkForDuplicateTags(readerA, readerB)) {
                return;
            }
            // Replace component handler tags
            replaceComponentHandler(readerB);

            StringBuffer stringBuilder = new StringBuffer();

            // From A, grab everything up to the end tag.
            int epos = readerA.indexOf("</facelet-taglib>");
            if (epos > -1) {
                stringBuilder.append( readerA.subSequence(0, epos) );
                //System.out.println("Contents of stringBuilder: " + stringBuilder.toString());
            }  else {
                System.out.println("No end of file found in A? ");
            }

            // From B, grab everything after the namespace tag and this will close the 
            // xml
            int spos = readerB.indexOf("</namespace>") + "</namespace>".length();
            if (spos > -1) {
                stringBuilder.append( readerB.substring(spos+1, readerB.length()) );
                //System.out.println("Contents of stringBuilder: " + stringBuilder.toString());
            }  else {
                System.out.println("No start of data found in B? ");
            }

            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter( fw );
            bw.write( stringBuilder.toString() );
            bw.close();

        } catch (IOException e) {
            System.out.println("Exception merging files: " + e);
        }

    }

    public static boolean checkForDuplicateTags( StringBuffer fileAContents, StringBuffer fileBContents) {
        int spos = 0;
        int epos;
        String tagName;
        String tagTag = "<tag-name>"; 
        String endTag = "</tag-name>";
        while ( (spos = fileAContents.indexOf(tagTag, spos) ) > -1) {
            epos = fileAContents.indexOf(endTag, spos + tagTag.length());
            tagName = fileAContents.substring(spos + tagTag.length(), epos);
//            System.out.println("Checking for duplicate tag: " + tagName );

            if (fileBContents.indexOf( tagTag + tagName + endTag) > -1) {
                System.out.println("Error Tag name Collision between namespace: " + tagName);
                return true;

            }
            spos = epos + endTag.length();
        }
        System.out.println("Tag file contents are unique! ");
        return false;
    }

    /**
     * From the compat taglib file, replace the Component &lt;handler-class&gt;
     * with one that is in glimmer so the icefaces-compat.jar file doesn't need
     * to be present. 
     * @param fileBContents compat taglib file contents
     */
    public static void replaceComponentHandler(StringBuffer fileBContents) {

         int spos = 0;
        int epos;
        String tagName;
        String tagTag = "<handler-class>";
        String endTag = "</handler-class>";
        int fixCount = 0;
        String handlerClass;
        while ( (spos = fileBContents.indexOf(tagTag, spos) ) > -1) {
            epos = fileBContents.indexOf(endTag, spos + tagTag.length());
            if (epos > -1) {
                handlerClass = fileBContents.substring(spos+tagTag.length(), epos);
                if (handlerClass.endsWith("IceComponentHandler")) {
                    fileBContents.replace(spos + tagTag.length(), epos, "org.icefaces.facelets.tag.icefaces.core.IceComponentHandler");
                    fixCount++;
                } else if (handlerClass.endsWith("TabChangeListenerHandler")) {
                    fileBContents.replace(spos + tagTag.length(), epos, "org.icefaces.facelets.tag.icefaces.core.TabChangeListenerHandler");
                } else {
                    System.out.println("Leaving well enough alone on: " + handlerClass);
                }                     
            }
            spos = epos + endTag.length();
        }
        System.out.println("Repaired " + fixCount + " Component Handler tags! ");
    }
}
