package com.project.ronf.client.UI;

import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Un generico pannello che viene popolato con i widget specificati
 */
public class ContentPanel extends DeckLayoutPanel {
	public ContentPanel(Widget[] panels) {
		for (Widget panel : panels)
			add(panel);
		showWidget(0);

	}
}
