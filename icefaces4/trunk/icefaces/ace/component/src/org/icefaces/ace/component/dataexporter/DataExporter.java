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

package org.icefaces.ace.component.dataexporter;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import org.icefaces.ace.component.datatable.DataTable;
import org.icefaces.util.CoreComponentUtils;

public class DataExporter extends DataExporterBase {

	private transient String path = null;
	private transient String source = "";
	
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event != null) {
			DataTable table = null;
			UIComponent compositeParent = null;
			FacesContext facesContext = getFacesContext();
			try {
				Object customExporter = getCustomExporter();
				Exporter exporter;
				if (customExporter == null) {
					exporter = ExporterFactory.getExporterForType(getType());
				} else {
					if (customExporter instanceof Exporter) {
						exporter = (Exporter) customExporter;
					} else {
						throw new FacesException("Object specified as custom exporter does not extend  org.icefaces.ace.component.dataexporter.Exporter.");
					}
				}

				String target = getTarget();
				UIComponent targetComponent = null;
				if (target == null) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).warning("Required attribute 'target' is null in ace:dataExporter component with id "+getId()+" in view "+FacesContext.getCurrentInstance().getViewRoot().getViewId()+".");
				} else {
					targetComponent = event.getComponent().findComponent(target);
				}
				if (targetComponent == null && target != null) targetComponent = CoreComponentUtils.findComponentInView(facesContext.getViewRoot(), target);
				if (targetComponent == null) throw new FacesException("Cannot find component \"" + target + "\" in view.");
				if (!(targetComponent instanceof DataTable)) throw new FacesException("Unsupported datasource target:\"" + targetComponent.getClass().getName() + "\", exporter must target an ACE DataTable.");
				
				table = (DataTable) targetComponent;
				if (!UIComponent.isCompositeComponent(table)) {
					compositeParent = UIComponent.getCompositeComponentParent(table);
				}
				if (compositeParent != null) {
					compositeParent.pushComponentToEL(facesContext, null);
				}
				table.pushComponentToEL(facesContext, null);
				this.path = exporter.export(facesContext, this, table);
			} catch (IOException e) { 
				throw new FacesException(e); 
			} finally {
				if (table != null) {
					table.popComponentFromEL(facesContext);
				}
				if (compositeParent != null) {
					compositeParent.popComponentFromEL(facesContext);
				}
			}
        }
	}
	
	public String getPath(String clientId) {
		if (this.source.equals(clientId)) {
			return this.path;
		} else {
			return null;
		}
	}
	
	public void setSource(String clientId) {
		this.source = clientId;
	}
	
	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
}
