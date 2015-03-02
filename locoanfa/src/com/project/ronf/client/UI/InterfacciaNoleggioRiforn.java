package com.project.ronf.client.UI;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.project.ronf.client.LOCOANFA;
import com.project.ronf.client.SincronizzatoreDati;
import com.project.ronf.shared.entities.Noleggio;

/**
 * Interfaccia dei noleggi visti dall'addetto al rifornimento/trasferimento
 */
public class InterfacciaNoleggioRiforn extends Composite {
	public static GridWidget<Noleggio> grigliaRifornimenti;
	FlowPanel mainPanel = new FlowPanel();

	public InterfacciaNoleggioRiforn() {
		initWidget(mainPanel);
		grigliaRifornimenti = new GridWidget<Noleggio>(Noleggio.class,
				LOCOANFA.me);

		mainPanel.add(grigliaRifornimenti);

		mainPanel.setPixelSize(Window.getClientWidth()
				- (LOCOANFA.EAST_PANEL + 10) - (LOCOANFA.WEST_PANEL + 10),
				Window.getClientHeight() - 2 * (LOCOANFA.NORTH_PANEL + 10));

		SincronizzatoreDati.updateAll(LOCOANFA.me);
	}
}
