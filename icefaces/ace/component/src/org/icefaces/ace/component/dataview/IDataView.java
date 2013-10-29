package org.icefaces.ace.component.dataview;

import org.icefaces.ace.util.ClientDescriptor;
/**
 * Created with IntelliJ IDEA.
 * User: Nils
 * Date: 4/2/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IDataView {

    public String getVar();
    public void setVar(String var);

    public Object getValue();
    public void setValue(Object value);

    public Integer getActiveRowIndex();
    public void setActiveRowIndex(Integer index);
	
    public void setDisabled(boolean disabled);
    public boolean isDisabled();
    public ClientDescriptor getClient();
}