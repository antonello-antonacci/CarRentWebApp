package com.project.ronf.client.UI;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Pannello di navigazione
 */
public class NavPanel extends FlowPanel {

	public void add(Widget w, String styleName) {
		SimplePanel wrapper = new SimplePanel();
		wrapper.addStyleName(styleName);
		wrapper.add(w);
		super.add(wrapper);
	}
}
