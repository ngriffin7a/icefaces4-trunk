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

import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry

readmeF = new File("README.html")
commonF = new File("common.xml")
cwd = new File(".")

for (File tutDir : cwd.listFiles(new TutorialFilenameFilter())) {
    pack(tutDir, readmeF, commonF, cwd.getPath())
}



class SubDirFilenameFilter implements FilenameFilter {
    boolean accept(File dir, String name) {
        if (name.startsWith("."))
            return false
        return true
    }
}

class TopDirFilenameFilter implements FilenameFilter {
    private excludedF = ["target", "build"];

    boolean accept(File dir, String name) {
        if (excludedF.contains(name) || name.startsWith("."))
            return false
        return true
    }
}

class TutorialFilenameFilter implements FilenameFilter {
    private excludedF = ["README.html", "common.xml", "packTutorials.groovy"];

    boolean accept(File dir, String name) {
        if (excludedF.contains(name) || name.startsWith("."))
            return false
        return true
    }
}



def zipF(File f, String base, ZipOutputStream zip) {
    byte[] buf = new byte[1024];

    fileIn = new FileInputStream(f)
    name = base == null ? f.name : f.getPath().substring(base.length()+1)
    zip.putNextEntry(new ZipEntry(name))

    int len;
    while ((len = fileIn.read(buf)) > 0)
        zip.write(buf, 0, len);

    fileIn.close()
}

def zipD(File dir, String base, ZipOutputStream zip, FilenameFilter filter) {
    zip.putNextEntry(new ZipEntry(dir.getPath().substring(base.length()+1)+"/"))

    for (File f : dir.listFiles(filter))
        if (f.isDirectory()) zipD(f, base, zip, new SubDirFilenameFilter())
        else zipF(f, base, zip)
}

def pack(File tutDir, File readmeF, File commonF, String base) {
    zip = new ZipOutputStream(new FileOutputStream(tutDir.name+".zip"))

    zipF(readmeF, null, zip)
    zipF(commonF, null, zip)
    zipD(tutDir, base, zip, new TopDirFilenameFilter())

    zip.close();
}