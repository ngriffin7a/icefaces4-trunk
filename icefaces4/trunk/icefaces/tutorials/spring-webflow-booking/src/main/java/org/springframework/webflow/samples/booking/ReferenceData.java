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

package org.springframework.webflow.samples.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Service;

@Service
public class ReferenceData {

    private List<SelectItem> bedOptions;

    private List<SelectItem> smokingOptions;

    private List<SelectItem> creditCardExpMonths;

    private List<SelectItem> creditCardExpYears;

    private List<SelectItem> pageSizeOptions;

    public List<SelectItem> getBedOptions() {
	if (bedOptions == null) {
	    bedOptions = new ArrayList<SelectItem>();
	    bedOptions.add(new SelectItem(new Integer(1), "One king-size bed"));
	    bedOptions.add(new SelectItem(new Integer(2), "Two double beds"));
	    bedOptions.add(new SelectItem(new Integer(3), "Three beds"));
	}
	return bedOptions;
    }

    public List<SelectItem> getSmokingOptions() {
	if (smokingOptions == null) {
	    smokingOptions = new ArrayList<SelectItem>();
	    smokingOptions.add(new SelectItem(Boolean.TRUE, "Smoking"));
	    smokingOptions.add(new SelectItem(Boolean.FALSE, "Non-Smoking"));
	}
	return smokingOptions;
    }

    public List<SelectItem> getCreditCardExpMonths() {
	if (creditCardExpMonths == null) {
	    creditCardExpMonths = new ArrayList<SelectItem>();
	    creditCardExpMonths.add(new SelectItem(new Integer(1), "Jan"));
	    creditCardExpMonths.add(new SelectItem(new Integer(2), "Feb"));
	    creditCardExpMonths.add(new SelectItem(new Integer(3), "Mar"));
	    creditCardExpMonths.add(new SelectItem(new Integer(4), "Apr"));
	    creditCardExpMonths.add(new SelectItem(new Integer(5), "May"));
	    creditCardExpMonths.add(new SelectItem(new Integer(6), "Jun"));
	    creditCardExpMonths.add(new SelectItem(new Integer(7), "Jul"));
	    creditCardExpMonths.add(new SelectItem(new Integer(8), "Aug"));
	    creditCardExpMonths.add(new SelectItem(new Integer(9), "Sep"));
	    creditCardExpMonths.add(new SelectItem(new Integer(10), "Oct"));
	    creditCardExpMonths.add(new SelectItem(new Integer(11), "Nov"));
	    creditCardExpMonths.add(new SelectItem(new Integer(12), "Dec"));
	}
	return creditCardExpMonths;
    }

    public List<SelectItem> getCreditCardExpYears() {
	if (creditCardExpYears == null) {
	    creditCardExpYears = new ArrayList<SelectItem>();
        Calendar calendar = Calendar.getInstance();
        for (int y = 0; y < 5; y++) {
            int year = calendar.get(Calendar.YEAR);
            creditCardExpYears.add(new SelectItem(new Integer(year), Integer.toString(year)));
            calendar.add(Calendar.YEAR, 1);
        }
	}
	return creditCardExpYears;
    }

    public List<SelectItem> getPageSizeOptions() {
	if (pageSizeOptions == null) {
	    pageSizeOptions = new ArrayList<SelectItem>();
	    pageSizeOptions.add(new SelectItem(new Integer(5), "5"));
	    pageSizeOptions.add(new SelectItem(new Integer(10), "10"));
	    pageSizeOptions.add(new SelectItem(new Integer(20), "20"));
	}
	return pageSizeOptions;
    }

}
