package com.project.ronf.client.UI;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.project.ronf.client.LOCOANFA;
import com.project.ronf.client.SincronizzatoreDati;
import com.project.ronf.shared.entities.Cliente;

public class InterfacciaClienti extends Composite {

	TabPanel mainPanel = new TabPanel();

	FlowPanel panelGestioneClienti = new FlowPanel();

	Grid formRegistrazioneCliente;

	public static GridWidget<Cliente> grigliaClienti;

	/**
	 * L'interfaccia per la registrazione e visualizzazione dei clienti
	 */
	public InterfacciaClienti() {
		initWidget(mainPanel);
		grigliaClienti = new GridWidget<Cliente>(Cliente.class, LOCOANFA.me);

		mainPanel.add(panelGestioneClienti, "Gestione Clienti");
		mainPanel.add(grigliaClienti, "Lista Clienti");
		mainPanel.selectTab(0);

		mainPanel.setPixelSize(Window.getClientWidth()
				- (LOCOANFA.EAST_PANEL + LOCOANFA.PANEL_PADDING)
				- (LOCOANFA.WEST_PANEL + LOCOANFA.PANEL_PADDING),
				Window.getClientHeight() - 2
						* (LOCOANFA.NORTH_PANEL + LOCOANFA.PANEL_PADDING));

		formRegistrazioneCliente = creaForm();

		HTML data = new HTML("Inserisci dati del nuovo cliente");
		data.setStyleName("italic");
		panelGestioneClienti.add(data);

		panelGestioneClienti.add(formRegistrazioneCliente);
	}

	/**
	 * @return il form di registrazione dei clienti
	 */
	private Grid creaForm() {
		Grid form = new Grid(5, 2);

		Button buttonInvia = new Button("Invia");
		Button buttonAnnulla = new Button("Annulla");
		buttonInvia.addClickHandler(new RegButtonHandler());
		buttonAnnulla.addClickHandler(new CancelRegButtonHandler());

		form.setText(0, 0, "Nome");
		form.setWidget(0, 1, new TextBox());
		form.setText(1, 0, "Cognome");
		form.setWidget(1, 1, new TextBox());
		form.setText(2, 0, "Indirizzo");
		form.setWidget(2, 1, new TextBox());
		form.setText(3, 0, "N° Patente");
		form.setWidget(3, 1, new TextBox());
		form.setWidget(4, 0, buttonInvia);
		form.setWidget(4, 1, buttonAnnulla);

		return form;
	}

	/**
	 * Handler per il tasto annulla del form
	 */
	private class CancelRegButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			resetCampi();
		}

	}

	/**
	 * Azzera tutti i campi del form
	 */
	private void resetCampi() {
		// recupera e azzera le textbox del form
		TextBox nameTextBox = (TextBox) formRegistrazioneCliente
				.getWidget(0, 1);
		nameTextBox.setText("");
		TextBox lastNameTextBox = (TextBox) formRegistrazioneCliente.getWidget(
				1, 1);
		lastNameTextBox.setText("");
		TextBox addressTextBox = (TextBox) formRegistrazioneCliente.getWidget(
				2, 1);
		addressTextBox.setText("");
		TextBox dLicenceTextBox = (TextBox) formRegistrazioneCliente.getWidget(
				3, 1);
		dLicenceTextBox.setText("");
	}

	/**
	 * Handler per il pulsante di registrazione dei clienti
	 */
	private class RegButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			// recupera le textbox del form
			TextBox nameTextBox = (TextBox) formRegistrazioneCliente.getWidget(
					0, 1);
			TextBox lastNameTextBox = (TextBox) formRegistrazioneCliente
					.getWidget(1, 1);
			TextBox addressTextBox = (TextBox) formRegistrazioneCliente
					.getWidget(2, 1);
			TextBox dLicenceTextBox = (TextBox) formRegistrazioneCliente
					.getWidget(3, 1);

			// controlla che i campi non siano vuoti
			if (nameTextBox.getText().isEmpty()) {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.WARN_INSERT_NOME_CLIENTE,
						MessageNotifier.MESSAGE_WARNING, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
				return;
			}
			if (lastNameTextBox.getText().isEmpty()) {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.WARN_INSERT_COGNOME_CLIENTE,
						MessageNotifier.MESSAGE_WARNING, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
				return;
			}
			if (addressTextBox.getText().isEmpty()) {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.WARN_INSERT_ADDR_CLIENTE,
						MessageNotifier.MESSAGE_WARNING, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
				return;
			}
			if (dLicenceTextBox.getText().isEmpty()) {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.WARN_INSERT_PATENTE_CLIENTE,
						MessageNotifier.MESSAGE_WARNING, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
				return;
			}

			// registra il nuovo cliente sul server
			LOCOANFA.clientiService.registraCliente(nameTextBox.getText(),
					lastNameTextBox.getText(), addressTextBox.getText(),
					dLicenceTextBox.getText(), new AsyncCallback<Cliente>() {

						@Override
						public void onFailure(Throwable caught) {
							MessageNotifier messageNotifier = new MessageNotifier(
									NotifStrings.ERR_CREATE_CLIENTE,
									MessageNotifier.MESSAGE_ERROR, true,
									true);
							messageNotifier
									.show(MessageNotifier.NOTIF_DELAY);
						}

						@Override
						public void onSuccess(Cliente result) {
							MessageNotifier messageNotifier = new MessageNotifier(
									NotifStrings.SUCCESS_CREATE_CLIENTE,
									MessageNotifier.MESSAGE_SUCCESS, true,
									true);
							messageNotifier
									.show(MessageNotifier.NOTIF_DELAY);
							SincronizzatoreDati.aggiornaDatiClienti();
							resetCampi();
						}

					});
		}

	}

}