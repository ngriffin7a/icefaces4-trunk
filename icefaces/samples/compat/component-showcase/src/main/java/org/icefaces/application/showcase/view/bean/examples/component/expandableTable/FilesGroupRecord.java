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

package org.icefaces.application.showcase.view.bean.examples.component.expandableTable;

import java.util.ArrayList;

/**
 * <p>The <code>FilesGroupRecord</code> is responsible for storing a list of
 * child <code>FilesRecords</code>.  It is this list of child records which make
 * up an expandable node in the dataTable.  When a node is expanded its'
 * children are added to the dataTable.</p>
 */
public class FilesGroupRecord extends FilesRecord {

    // list of child FilesRecords.
    protected ArrayList childFilesRecords = new ArrayList(5);

    public ArrayList getChildFilesRecords() {
        return childFilesRecords;
    }

}