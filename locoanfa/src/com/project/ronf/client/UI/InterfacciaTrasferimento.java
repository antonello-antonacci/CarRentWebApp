package com.project.ronf.client.UI;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.project.ronf.client.LOCOANFA;
import com.project.ronf.client.SincronizzatoreDati;
import com.project.ronf.shared.entities.Trasferimento;

/**
 * Interfaccia trasferimenti (in arrivo e in partenza)
 */
public class InterfacciaTrasferimento extends Composite {
	TabPanel mainPanel = new TabPanel();
	public static GridWidget<Trasferimento> trasferimentiPartenza,
			trasferimentiArrivo;
	public VerticalPanel tabTrasfPartenza, tabTrasfArrivo;
	static final SingleSelectionModel<Trasferimento> arrivoSelection = new SingleSelectionModel<Trasferimento>();
	static final SingleSelectionModel<Trasferimento> partenzaSelection = new SingleSelectionModel<Trasferimento>();

	// I tipi di
	private static final int TYPE_ARRIVO = 0;
	private static final int TYPE_PARTENZA = 1;

	public InterfacciaTrasferimento() {
		initWidget(mainPanel);

		creaTabPartenza();
		creaTabArrivo();

		mainPanel.add(tabTrasfPartenza, "Trasferimenti in partenza");
		mainPanel.add(tabTrasfArrivo, "Trasferimenti in arrivo");
		mainPanel.selectTab(0);

		mainPanel.setPixelSize(Window.getClientWidth()
				- (LOCOANFA.EAST_PANEL + 10) - (LOCOANFA.WEST_PANEL + 10),
				Window.getClientHeight() - 2 * (LOCOANFA.NORTH_PANEL + 10));

		SincronizzatoreDati.aggiornaDatiTrasferimenti();
	}

	/**
	 * Tab trasferimenti in arrivo
	 */
	private void creaTabArrivo() {
		trasferimentiArrivo = new GridWidget<Trasferimento>(
				Trasferimento.class, LOCOANFA.me);
		trasferimentiArrivo.griglia.setSelectionModel(arrivoSelection);

		Button regArrivo = new Button("Registra fine trasferimento");
		regArrivo.addClickHandler(new RegTrasfHandler(0));

		tabTrasfArrivo = new VerticalPanel();

		tabTrasfArrivo.add(trasferimentiArrivo);
		tabTrasfArrivo.add(regArrivo);

	}

	/**
	 * Tab trasferimenti in partenza
	 */
	private void creaTabPartenza() {
		trasferimentiPartenza = new GridWidget<Trasferimento>(
				Trasferimento.class, LOCOANFA.me);
		trasferimentiPartenza.griglia.setSelectionModel(partenzaSelection);

		Button regPartenza = new Button("Registra inizio Trasferimento");
		regPartenza.addClickHandler(new RegTrasfHandler(1));

		tabTrasfPartenza = new VerticalPanel();

		tabTrasfPartenza.add(trasferimentiPartenza);
		tabTrasfPartenza.add(regPartenza);

	}

	/**
	 * Handler per il click sulla registrazione trasferimenti (in arrivo o in
	 * partenza)
	 */
	private class RegTrasfHandler implements ClickHandler {
		int tipo;

		/**
		 * @param type
		 *            il tipo di trasferimento (arrivo o partenza) a cui fa
		 *            riferimento l'handler
		 */
		public RegTrasfHandler(int type) {
			tipo = type;
		}

		@Override
		public void onClick(ClickEvent event) {

			if (tipo == TYPE_ARRIVO) {
				// Recupera e controlla l'effettiva selezione di un
				// trasferimento sulla griglia
				Trasferimento selected = arrivoSelection.getSelectedObject();
				if (selected == null) {
					MessageNotifier messageNotifier = new MessageNotifier(
							NotifStrings.WARN_SELECT_REMOTE_AUTO_REQUEST,
							MessageNotifier.MESSAGE_WARNING, true, true);
					messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					return;
				}
				// Il trasferimento è completato, invia i dati al server
				LOCOANFA.trasfService.terminaTrasferimento(selected,
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								MessageNotifier messageNotifier = new MessageNotifier(
										NotifStrings.ERR_END_TRASF,
										MessageNotifier.MESSAGE_ERROR, true,
										true);
								messageNotifier
										.show(MessageNotifier.NOTIF_DELAY);
							}

							@Override
							public void onSuccess(Void result) {
								MessageNotifier messageNotifier = new MessageNotifier(
										NotifStrings.SUCCESS_END_TRASF,
										MessageNotifier.MESSAGE_SUCCESS,
										true, true);

								messageNotifier
										.show(MessageNotifier.NOTIF_DELAY);
								arrivoSelection.clear();

								SincronizzatoreDati.aggiornaDatiTrasferimenti();
								SincronizzatoreDati.aggiornaDatiAuto();

							}

						});

			} else if (tipo == TYPE_PARTENZA) {
				// Recupera e controlla l'effettiva selezione di un
				// trasferimento sulla griglia
				final Trasferimento selected = partenzaSelection
						.getSelectedObject();
				if (selected == null) {
					MessageNotifier messageNotifier = new MessageNotifier(
							NotifStrings.WARN_SELECT_REMOTE_AUTO_REQUEST,
							MessageNotifier.MESSAGE_WARNING, true, true);
					messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					return;
				}
				// notifica al server l'avvio del trasferimento
				LOCOANFA.trasfService.avvioTrasferimento(
						selected.getDestinazione(), selected,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								MessageNotifier messageNotifier = new MessageNotifier(
										NotifStrings.ERR_START_TRASF,
										MessageNotifier.MESSAGE_ERROR, true,
										true);
								messageNotifier
										.show(MessageNotifier.NOTIF_DELAY);

							}

							@Override
							public void onSuccess(Boolean result) {
								LOCOANFA.trasfService.rimuoviTrasferimento(
										selected, new AsyncCallback<Void>() {

											@Override
											public void onFailure(
													Throwable caught) {
												MessageNotifier messageNotifier = new MessageNotifier(
														NotifStrings.ERR_RM_TRASF,
														MessageNotifier.MESSAGE_ERROR,
														true, true);

												messageNotifier
														.show(MessageNotifier.NOTIF_DELAY);

											}

											@Override
											public void onSuccess(Void result) {
												partenzaSelection.clear();

												SincronizzatoreDati
														.aggiornaDatiTrasferimenti();
												SincronizzatoreDati
														.aggiornaDatiAuto();
											}

										});
								MessageNotifier messageNotifier = new MessageNotifier(
										NotifStrings.SUCCESS_START_TRASF,
										MessageNotifier.MESSAGE_SUCCESS,
										true, true);

								messageNotifier
										.show(MessageNotifier.NOTIF_DELAY);

							}

						});

			}
		}
	}
}