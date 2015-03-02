package com.project.ronf.client.UI;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.project.ronf.client.LOCOANFA;
import com.project.ronf.client.SincronizzatoreDati;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;

/**
 * Interfaccia per la registrazione, la visualizzazione e la richiesta remota di
 * auto
 */
public class InterfacciaAuto extends Composite {

	TabPanel mainPanel = new TabPanel();

	FlowPanel panelGestioneAuto = new FlowPanel();
	public static GridWidget<Auto> grigliaAuto;
	public static GridWidget<Auto> grigliaAutoRemote;
	public static VerticalPanel tabRichiesta;

	public static ListBox reqAgenzie;

	static final SingleSelectionModel<Auto> trasfSelectionModel = new SingleSelectionModel<Auto>();

	Grid formAuto;

	public InterfacciaAuto() {
		initWidget(mainPanel);
		grigliaAuto = new GridWidget<Auto>(Auto.class, LOCOANFA.me);
		grigliaAutoRemote = new GridWidget<Auto>(Auto.class, LOCOANFA.me);

		tabRichiesta = creaPannelloRichieste();

		mainPanel.add(panelGestioneAuto, "Gestione Auto");
		mainPanel.add(grigliaAuto, "Lista Auto");
		mainPanel.add(tabRichiesta, "Richiesta auto");
		mainPanel.selectTab(0);

		mainPanel.setPixelSize(Window.getClientWidth()
				- (LOCOANFA.EAST_PANEL + LOCOANFA.PANEL_PADDING)
				- (LOCOANFA.WEST_PANEL + LOCOANFA.PANEL_PADDING),
				Window.getClientHeight() - 2
						* (LOCOANFA.NORTH_PANEL + LOCOANFA.PANEL_PADDING));

		formAuto = creaForm();

		HTML data = new HTML("Inserisci dati della nuova auto");
		data.setStyleName("italic");
		panelGestioneAuto.add(data);

		panelGestioneAuto.add(formAuto);
	}

	/**
	 * @return la tab per richiedere auto ad altre agenzie
	 */
	private VerticalPanel creaPannelloRichieste() {

		reqAgenzie = new ListBox();
		reqAgenzie.addChangeHandler(new AgenzieRemoteListener());
		grigliaAutoRemote.griglia.setSelectionModel(trasfSelectionModel);

		VerticalPanel richieste = new VerticalPanel();

		richieste.add(new HTML("Seleziona Agenzia"));
		richieste.add(reqAgenzie);

		richieste.add(grigliaAutoRemote);

		Button trasferimentoButton = new Button("Richiedi Trasferimento");
		trasferimentoButton.addClickHandler(new TrasferimentoClickHandler());

		richieste.add(trasferimentoButton);

		return richieste;
	}

	/**
	 * @return una form per la registrazione di nuove auto
	 */
	private Grid creaForm() {
		Grid form = new Grid(4, 2);

		Button buttonInvia = new Button("Invia");
		Button buttonAnnulla = new Button("Annulla");
		buttonInvia.addClickHandler(new RegButtonHandler());
		buttonAnnulla.addClickHandler(new CancelRegButtonHandler());

		form.setText(0, 0, "Modello");
		form.setWidget(0, 1, new TextBox());
		form.setText(1, 0, "Targa");
		form.setWidget(1, 1, new TextBox());
		form.setText(2, 0, "Tipo");

		ListBox tipoListBox = new ListBox();

		for (String tipo : Auto.tipi) {

			tipoListBox.addItem(tipo);
		}

		form.setWidget(2, 1, tipoListBox);

		form.setWidget(3, 0, buttonInvia);
		form.setWidget(3, 1, buttonAnnulla);

		return form;
	}

	/**
	 * Resetta tutti i campi della form
	 */
	private void resetCampi() {
		TextBox modelloTextBox = (TextBox) formAuto.getWidget(0, 1);
		modelloTextBox.setText("");

		TextBox targaTextBox = (TextBox) formAuto.getWidget(1, 1);
		targaTextBox.setText("");
		ListBox listTipo = (ListBox) formAuto.getWidget(2, 1);
		listTipo.setItemSelected(0, true);
	}

	/**
	 * Handler per il click su "annulla" nella form
	 */
	private class CancelRegButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			resetCampi();

		}

	}

	/**
	 * Handler per il click su "avvia trasferimento"
	 */
	private class TrasferimentoClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			// auto selezionata
			final Auto trasfSelectedAuto = trasfSelectionModel
					.getSelectedObject();
			trasfSelectionModel.clear();
			if (trasfSelectedAuto != null) {
				String targetName = reqAgenzie.getValue(reqAgenzie
						.getSelectedIndex());
				Agenzia target = SincronizzatoreDati.hashAgenzie
						.get(targetName);

				// invia la richiesta al server locale, che poi la reindirizza
				// all'agenzia selezionata
				LOCOANFA.carService.richiestaAuto(target, trasfSelectedAuto,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								MessageNotifier messageNotifier = new MessageNotifier(
										NotifStrings.ERR_REMOTE_AUTO_REQUEST,
										MessageNotifier.MESSAGE_ERROR, true,
										true);
								messageNotifier
										.show(MessageNotifier.NOTIF_DELAY);

							}

							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									MessageNotifier messageNotifier = new MessageNotifier(
											NotifStrings.SUCCESS_REMOTE_AUTO_REQUEST,
											MessageNotifier.MESSAGE_SUCCESS,
											true, true);

									messageNotifier
											.show(MessageNotifier.NOTIF_DELAY);
									String targetName = reqAgenzie
											.getValue(reqAgenzie
													.getSelectedIndex());
									SincronizzatoreDati
											.aggiornaDatiAgenzieRemote(targetName);
								} else {
									MessageNotifier messageNotifier = new MessageNotifier(
											NotifStrings.ERR_GET_LIST_AUTO,
											MessageNotifier.MESSAGE_ERROR,
											true, true);

									messageNotifier
											.show(MessageNotifier.NOTIF_DELAY);
								}
							}

						});

			} else {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.WARN_SELECT_REMOTE_AUTO_REQUEST,
						MessageNotifier.MESSAGE_WARNING, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
			}
		}

	}

	/**
	 * Handler per la selezione dell'agenzia nella tab delle richieste
	 */
	private class AgenzieRemoteListener implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			String selected = reqAgenzie
					.getValue(reqAgenzie.getSelectedIndex());
			// mostra le auto dell'agenzia selezionata
			SincronizzatoreDati.aggiornaDatiAgenzieRemote(selected);

		}
	}

	/**
	 * Handler del pulsante di registrazione di una nuova auto
	 */
	private class RegButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			TextBox modelloTextBox = (TextBox) formAuto.getWidget(0, 1);
			TextBox targaTextBox = (TextBox) formAuto.getWidget(1, 1);
			ListBox tipoListBox = (ListBox) formAuto.getWidget(2, 1);

			// controlli per i campi non vuoti
			if (modelloTextBox.getText().isEmpty()) {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.WARN_INSERT_MODELLO,
						MessageNotifier.MESSAGE_WARNING, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
				return;
			}
			if (targaTextBox.getText().isEmpty()) {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.WARN_INSERT_TARGA,
						MessageNotifier.MESSAGE_WARNING, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
				return;
			}
			// registra l'auto sul server
			LOCOANFA.carService.registraAuto(modelloTextBox.getText(),
					targaTextBox.getText(), tipoListBox.getSelectedIndex(),
					new AsyncCallback<Auto>() {

						@Override
						public void onFailure(Throwable caught) {
							MessageNotifier messageNotifier = new MessageNotifier(
									NotifStrings.ERR_CREATE_AUTO,
									MessageNotifier.MESSAGE_ERROR, true,
									true);
							messageNotifier
									.show(MessageNotifier.NOTIF_DELAY);
						}

						@Override
						public void onSuccess(Auto result) {
							MessageNotifier messageNotifier = new MessageNotifier(
									NotifStrings.SUCCESS_CREATE_AUTO,
									MessageNotifier.MESSAGE_SUCCESS, true,
									true);
							messageNotifier
									.show(MessageNotifier.NOTIF_DELAY);
							SincronizzatoreDati.aggiornaDatiAuto();
							resetCampi();
						}

					});
		}

	}

}