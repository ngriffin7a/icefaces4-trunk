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

package org.icefaces.ace.model.table;

import org.icefaces.ace.component.column.Column;
import org.icefaces.ace.component.column.IProxiableColumn;
import org.icefaces.ace.component.column.ProxiedBodyColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColumnModel {
    private static Logger log = Logger.getLogger(ColumnModel.class.getName());

    private ColumnGroupModel headerModel;
    private ColumnGroupModel bodyModel;

    public ColumnModel(ColumnGroupModel headerModel, ColumnGroupModel bodyModel) {
        this.headerModel = headerModel;
        this.bodyModel = bodyModel;
    }

    public boolean isHeaderDifferentThanBody() {
        return headerModel != null && headerModel != bodyModel;
    }

    public ColumnGroupModel getHeaderModel() {
        return ((headerModel != null) ? headerModel : bodyModel);
    }

    public ColumnGroupModel getBodyModel() {
        return bodyModel;
    }

    public List<IProxiableColumn> getProxiedBodyColumns() {
        List<IProxiableColumn> proxiedColumns = new ArrayList<IProxiableColumn>();
        ColumnGroupModel.TreeIterator bodyIterator = bodyModel.iterate();
        if (bodyIterator.empty()) {
            return proxiedColumns;
        }
        ColumnGroupModel.TreeIterator headerIterator =
            headerModel != null ? headerModel.iterate() : null;

        do {
            List<Column> currBodyColumns = bodyIterator.columns();
            List<Column> currHeadColumns =
                (headerIterator != null && headerIterator.
                    correspondingColumnInHeaderForBody(bodyIterator, true)) ?
                    headerIterator.columns() : null;
            if (currHeadColumns != null) {
                for (int i = 0; i < currBodyColumns.size(); i++) {
                    proxiedColumns.add(new ProxiedBodyColumn(
                        currHeadColumns.get(i), currBodyColumns.get(i)));
                    if (log.isLoggable(Level.FINER)) {
                        log.finer("Matching BODY " + currBodyColumns.get(i).
                            getHeaderText() + " to HEADER " + currHeadColumns.
                            get(i).getHeaderText());
                    }
                }
            } else {
                proxiedColumns.addAll(currBodyColumns);
            }
        } while(bodyIterator.nextPeer(false, true));

        return proxiedColumns;
    }

    /**
     * Given a header ColumnGroupModel's TreeIterator, which has iterated to a
     * specific column position, find the body ColumnGroupModel's corresponding
     * column position. If there is no distinct header ColumnGroupModel,
     * meaning that the body one is being used for both the body and the header,
     * then return null. Not all header column positions have corresponding
     * body positions.
     * @param headerIterator Header ColumnGroupModel.TreeIterator
     * @return Body ColumnGroupModel.TreeIterator, seeked to corresponding position.
     */
    public ColumnGroupModel.TreeIterator findCorrespondingBodyColums(
            ColumnGroupModel.TreeIterator headerIterator) {
        if (isHeaderDifferentThanBody()) {
            ColumnGroupModel.TreeIterator bodyIterator = getBodyModel().iterate();
            if (bodyIterator.correspondingColumnInBodyForHeader(headerIterator, true)) {
                return bodyIterator;
            }
        }
        return null;
    }

    public void verifyCorresponding(String dataTableClientId) {
        if (isHeaderDifferentThanBody()) {
            if (getHeaderModel().getColumns() !=
                getBodyModel().getColumns()) {
                String err = "For DataTable "+dataTableClientId+
                    ", the header column spanning ("+
                    getHeaderModel().getColumns()+
                    ") does not match the body column spanning ("+
                    getBodyModel().getColumns()+")";
                log.severe(err);
                //throw new RuntimeException(err);
            }
        }
    }
}
