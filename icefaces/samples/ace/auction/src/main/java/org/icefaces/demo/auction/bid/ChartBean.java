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

package org.icefaces.demo.auction.bid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.model.chart.CartesianSeries;
import org.icefaces.demo.auction.bid.model.AuctionHistory;
import org.icefaces.demo.auction.util.FacesUtils;

@ManagedBean(name=ChartBean.BEAN_NAME)
@ViewScoped
@SuppressWarnings("serial")
public class ChartBean implements Serializable {
	public static final String BEAN_NAME = "chartBean";
	
	public enum ChartType {
		BID_INCREASE, PRICE_CHANGE
	};
	
	private List<CartesianSeries> historyData;
    private Axis[] yAxis;
    private Axis xAxis;
    private ChartType type;
    private Date minDate;
    
    private Axis[] generateYAxis() {
        Axis yAxis = new Axis();
        yAxis.setTickPrefix("$");
        yAxis.setAutoscale(true);
        yAxis.setLabel("Amount");
        
    	Axis[] toReturn = new Axis[] { yAxis };
    	
    	return toReturn;
    }
    
    private Axis generateXAxis() {
    	Axis toReturn = new Axis();
        toReturn.setType(AxisType.DATE);
        toReturn.setAutoscale(true);
        toReturn.setLabel("Date/Time");
        toReturn.setFormatString("%H:%M:%S");
        toReturn.setMin(getMinDate());
        
        return toReturn;
    }
    
	private List<CartesianSeries> generateChart() {
		BidBean bidBean = (BidBean)FacesUtils.getManagedBean(BidBean.BEAN_NAME);
		
		if ((bidBean.getBidItem() != null) && (bidBean.getBidItem().getBids() > 0)) {
			List<AuctionHistory> history = bidBean.getBidItem().getHistory();
			
			if ((history != null) && (!history.isEmpty())) {
				CartesianSeries chartData = new CartesianSeries();
				
				if ((type == null) || (type == ChartType.BID_INCREASE)) {
					chartData.setLabel("Bid Increment");
				}
				else {
					chartData.setLabel("Price Change");
				}
				
				// Loop through our auction item history and convert it to graphable data
				// Also note we'll keep track of the lowest (oldest) date to use as a minimum for the X Axis
				Date lowDate = new Date();
				for (AuctionHistory loopHistory : history) {
					if (loopHistory.getDate().before(lowDate)) {
						lowDate = loopHistory.getDate();
					}

					if ((type == null) || (type == ChartType.BID_INCREASE)) {
						chartData.add(loopHistory.getDate(), loopHistory.getBidIncrease());
					}
					else {
						chartData.add(loopHistory.getDate(), loopHistory.getPrice());
					}
				}
				
				// Set our minDate for the X Axis based on what we looped through
				minDate = lowDate;
				// Then regenerate the X Axis using the new minimum date
				setxAxis(null);
				
		    	List<CartesianSeries> toReturn = new ArrayList<CartesianSeries>(1);
				toReturn.add(chartData);
				return toReturn;
			}
		}
		
		return null;
    }
	
	public void refresh() {
		setHistoryData(null);
		setyAxis(null);
		setxAxis(null);
		minDate = null;
	}
	
	public void clear() {
		refresh();
		
		setType(null);
	}
	
	private Date getMinDate() {
		// If we don't have a minimum date just use a day ago
		// Generally this shouldn't happen as we'll manually set this date before generating the axis
		if (minDate == null) {
			Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DAY_OF_MONTH, -1);
	        minDate = cal.getTime();
		}
		
		return minDate;
	}

	public List<CartesianSeries> getHistoryData() {
		if (historyData == null) {
			historyData = generateChart();
		}
		
		return historyData;
	}

	public void setHistoryData(List<CartesianSeries> historyData) {
		this.historyData = historyData;
	}

	public Axis[] getyAxis() {
		if (yAxis == null) {
			yAxis = generateYAxis();
		}
		
		return yAxis;
	}

	public void setyAxis(Axis[] yAxis) {
		this.yAxis = yAxis;
	}

	public Axis getxAxis() {
		if (xAxis == null) {
			xAxis = generateXAxis();
		}
		
		return xAxis;
	}

	public void setxAxis(Axis xAxis) {
		this.xAxis = xAxis;
	}

	public ChartType getType() {
		return type;
	}

	public void setType(ChartType type) {
		this.type = type;
	}
}
