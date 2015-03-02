package com.project.ronf.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.project.ronf.client.UI.ContentPanel;
import com.project.ronf.client.UI.InterfacciaAuto;
import com.project.ronf.client.UI.InterfacciaClienti;
import com.project.ronf.client.UI.InterfacciaNoleggio;
import com.project.ronf.client.UI.InterfacciaNoleggioRiforn;
import com.project.ronf.client.UI.InterfacciaTrasferimento;
import com.project.ronf.client.UI.MessageNotifier;
import com.project.ronf.client.UI.NavPanel;
import com.project.ronf.client.UI.NotifStrings;
import com.project.ronf.client.services.Autenticazione;
import com.project.ronf.client.services.AutenticazioneAsync;
import com.project.ronf.client.services.GestioneAgenzia;
import com.project.ronf.client.services.GestioneAgenziaAsync;
import com.project.ronf.client.services.GestioneAuto;
import com.project.ronf.client.services.GestioneAutoAsync;
import com.project.ronf.client.services.GestioneCliente;
import com.project.ronf.client.services.GestioneClienteAsync;
import com.project.ronf.client.services.GestioneNoleggio;
import com.project.ronf.client.services.GestioneNoleggioAsync;
import com.project.ronf.client.services.GestioneTrasferimento;
import com.project.ronf.client.services.GestioneTrasferimentoAsync;
import com.project.ronf.shared.entities.DipFrontOffice;
import com.project.ronf.shared.entities.DipTrasferimento;
import com.project.ronf.shared.entities.Dipendente;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LOCOANFA implements EntryPoint {
	final HTML showUserType = new HTML();

	// contentpanel che dipende dal tipo di sessione (vedi sopra)

	public static DeckLayoutPanel contentPanel; // era private
	public static Dipendente me;

	public static final int INFO_GRID_ROW = 3;
	public static final int INFO_GRID_COL = 1;
	public static Grid infoGrid = new Grid(INFO_GRID_ROW, INFO_GRID_COL);

	public static HTML messageViewerHTML = new HTML();

	public static final int NORTH_PANEL = 100;
	public static final int WEST_PANEL = 200;
	public static final int EAST_PANEL = 300;
	public static final int SOUTH_PANEL = 50;
	public static final int PANEL_PADDING = 10;

	// Servizi asincroni
	public static final AutenticazioneAsync loginService = GWT
			.create(Autenticazione.class);
	public static final GestioneAutoAsync carService = GWT
			.create(GestioneAuto.class);
	public static final GestioneClienteAsync clientiService = GWT
			.create(GestioneCliente.class);
	public static final GestioneNoleggioAsync noleggiService = GWT
			.create(GestioneNoleggio.class);
	public static final GestioneAgenziaAsync agenziaService = GWT
			.create(GestioneAgenzia.class);
	public static final GestioneTrasferimentoAsync trasfService = GWT
			.create(GestioneTrasferimento.class);

	public void onModuleLoad() {
		// Controlla se la sessione è valida, altrimenti mostra la schermata di
		// login
		String sessionID = Cookies.getCookie("sid");

		if (sessionID != null) {
			checkSessionServer();
		} else {
			displayLoginInterface();
		}
	}

	/**
	 * Contolla se la sessione è ancora valida
	 */
	private void checkSessionServer() {
		loginService.loginSessione(new AsyncCallback<Dipendente>() {
			@Override
			public void onFailure(Throwable caught) {
				me = null;
				displayLoginInterface();
			}

			@Override
			public void onSuccess(Dipendente logged) {
				if (logged == null) {
					displayLoginInterface();
					return;
				}
				me = logged;
				displayMainInterface();
			}
		});
	}

	/**
	 * Mostra interfaccia di login
	 */
	private void displayLoginInterface() {
		// creo mainPanel dock
		DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);

		// creo intestazione
		HTML header = new HTML("<h1>locoanfa RONF</h1>");

		// creo pannello orizzontale per textbox
		HorizontalPanel loginUserPass = new HorizontalPanel();

		// creo flowpanel per contenere textbox e radiobutton
		FlowPanel centerPanel = new FlowPanel();

		// creo textbox per username e password con button di login
		TextBox userName = new TextBox();
		PasswordTextBox password = new PasswordTextBox();
		final Button sendButton = new Button("login");

		// assegno username di default
		userName.setText("frontoffice");
		// assegno handler per lanciare login semplicemente premendo Return
		userName.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendButton.click();
				}
			}

		});

		// assegno password di default
		password.setText("prova");
		// assegno handler per lanciare login semplicemente premendo Return
		password.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendButton.click();
				}
			}

		});
		// assegno click handler al button di login
		sendButton.addClickHandler(new LoginButtonHandler(userName, password));

		loginUserPass.add(userName);
		loginUserPass.add(password);
		loginUserPass.add(sendButton);

		centerPanel.add(loginUserPass);

		centerPanel.setStyleName("login-block");

		mainPanel.addNorth(header, NORTH_PANEL);

		mainPanel.add(centerPanel);
		RootLayoutPanel.get().add(mainPanel);
	}

	/**
	 * Mostra interfaccia in base al tipo di login
	 */
	private void displayMainInterface() {

		if (me instanceof DipFrontOffice) {
			showFrontOfficeInterface();
		} else if (me instanceof DipTrasferimento) {
			showTransferInterface();
		} else {
			Window.alert("Error");
		}
	}

	/**
	 * Interfaccia dipendente front office
	 */
	private void showFrontOfficeInterface() {
		DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);

		// Titolo
		HTML headerTitle = new HTML("" + "<h1>FRONTOFFICE " + "(Agenzia "
				+ LOCOANFA.me.agenzia.getName().toUpperCase() + ")</h1>");
		mainPanel.addNorth(headerTitle, NORTH_PANEL);

		// Pannello di navigazione front office
		FlowPanel navPanel = makeFrontOfficeNavPanel();
		mainPanel.addWest(navPanel, WEST_PANEL);

		// griglia delle informazioni
		mainPanel.addEast(infoGrid, EAST_PANEL);

		// notificatore messaggi
		mainPanel.addSouth(messageViewerHTML, SOUTH_PANEL);
		MessageNotifier messageNotifier = new MessageNotifier(
				NotifStrings.LOGGED_FRONTOFFICE,
				MessageNotifier.MESSAGE_SUCCESS, true, true);
		messageNotifier.show(MessageNotifier.NOTIF_DELAY);

		// Widget per pannello principale
		InterfacciaNoleggio interfacciaNoleggio = new InterfacciaNoleggio();
		InterfacciaClienti interfacciaClienti = new InterfacciaClienti();
		InterfacciaAuto interfacciaAuto = new InterfacciaAuto();

		HTML logout = new HTML("Logout");
		logout.setStylePrimaryName("bold");
		Widget[] contentWidgets = { interfacciaClienti, interfacciaNoleggio,
				interfacciaAuto, logout };

		contentPanel = new ContentPanel(contentWidgets);

		mainPanel.add(contentPanel);

		RootLayoutPanel.get().add(mainPanel);
		SincronizzatoreDati.updateAll(me);
	}

	/**
	 * Interfaccia dipendente trasferimento/rifornimento
	 */
	private void showTransferInterface() {

		DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
		// Titolo
		HTML headerTitle = new HTML("" + "<h1>TRASFERIMENTO - RIFORNIMENTO "
				+ "(Agenzia " + LOCOANFA.me.agenzia.getName().toUpperCase()
				+ ")</h1>");
		mainPanel.addNorth(headerTitle, NORTH_PANEL);

		// Pannello di navigazione dipendente trasferimento/rifornimento
		FlowPanel navPanel = makeTransferNavPanel();
		mainPanel.addWest(navPanel, WEST_PANEL);

		// Notificatore messaggi
		mainPanel.addSouth(messageViewerHTML, SOUTH_PANEL);
		MessageNotifier messageNotifier = new MessageNotifier(
				NotifStrings.LOGGED_RIF_TRASF,
				MessageNotifier.MESSAGE_SUCCESS, true, true);
		messageNotifier.show(MessageNotifier.NOTIF_DELAY);

		// Popola il pannello di navigazione
		InterfacciaNoleggioRiforn interfacciaNoleggioRiforn = new InterfacciaNoleggioRiforn();
		InterfacciaTrasferimento interfacciaTrasferimento = new InterfacciaTrasferimento();
		HTML logout = new HTML("Logout");
		logout.setStylePrimaryName("bold");
		Widget[] contentWidgets = { interfacciaNoleggioRiforn,
				interfacciaTrasferimento, logout };
		contentPanel = new ContentPanel(contentWidgets);
		mainPanel.add(contentPanel);

		RootLayoutPanel.get().add(mainPanel);
		// Aggiorna tutti i dati dal server e li mostra nei campi appropriati
		SincronizzatoreDati.updateAll(me);
	}

	/**
	 * @return Il pannello di navigazione per il dipendente front office
	 */
	private FlowPanel makeFrontOfficeNavPanel() {
		NavPanel navPanel = new NavPanel();
		String cssStyle = "frontoffice-nav-button";

		navPanel.addStyleName("frontoffice-nav-panel");
		// I bottoni del menu
		Button[] navButtons = { makeNavButton("Clienti", cssStyle),
				makeNavButton("Noleggi", cssStyle),
				makeNavButton("Auto", cssStyle), };
		for (int i = 0; i < navButtons.length; i++) {
			Button button = navButtons[i];
			button.addClickHandler(new NavButtonHandler(i));
			navPanel.add(button, "frontoffice-center-block");
		}

		// creazione tasto di logout
		Button logoutButton = new Button("Logout");
		logoutButton.setStyleName(cssStyle);
		logoutButton.addClickHandler(new LogoutButtonHandler());
		navPanel.add(logoutButton, "frontoffice-center-block");

		return navPanel;
	}

	/**
	 * @return Il pannello di navigazione del dipendente di trasferimento
	 */
	private FlowPanel makeTransferNavPanel() {
		NavPanel navPanel = new NavPanel();
		String cssStyle = "transfer-nav-button";

		navPanel.addStyleName("transfer-nav-panel");
		// I bottoni del menu
		Button[] navButtons = { makeNavButton("Rifornimenti", cssStyle),
				makeNavButton("Trasferimenti", cssStyle) // ,
		};
		for (int i = 0; i < navButtons.length; i++) {
			Button button = navButtons[i];
			button.addClickHandler(new NavButtonHandler(i));
			navPanel.add(button, "transfer-center-block");
		}

		// creazione tasto di logout
		Button logoutButton = new Button("Logout");
		logoutButton.setStyleName(cssStyle);
		logoutButton.addClickHandler(new LogoutButtonHandler());
		navPanel.add(logoutButton, "transfer-center-block");

		return navPanel;
	}

	/**
	 * @param label
	 *            Il testo del bottone
	 * @param cssStyle
	 *            Lo stile da applicare al bottone
	 * @return bottone con stile css associato
	 */
	private Button makeNavButton(String label, String cssStyle) {
		Button navButton = new Button(label);
		navButton.addStyleName(cssStyle);
		return navButton;
	}

	/**
	 * Handler per il pulsante "login"
	 */
	private class LoginButtonHandler implements ClickHandler {

		TextBox user;
		PasswordTextBox password;

		public LoginButtonHandler(TextBox usr, PasswordTextBox pwd) {
			user = usr;
			password = pwd;
		}

		public void onClick(ClickEvent event) {
			String us = user.getText();
			String pas = password.getText();
			// Manda credenziali al server
			loginService.login(us, pas, new AsyncCallback<Dipendente>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Invalid username or password");
					Window.Location.reload();
				}

				@Override
				public void onSuccess(Dipendente result) {
					if (result == null) {
						Window.alert("Invalid username or password");
						Window.Location.reload();
					}
					Cookies.setCookie("sid", result.getSessionId());
					me = result;
					RootLayoutPanel.get().clear();
					displayMainInterface();
				}

			});
		}
	}

	/**
	 * Hanlder per il pulsante di logout
	 */
	private class LogoutButtonHandler implements ClickHandler {
		public void onClick(ClickEvent event) {
			loginService.logOut(new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Unable to logout");
				}

				@Override
				public void onSuccess(Void result) {
					Window.Location.reload();
				}

			});
		}
	}

	/**
	 * Button Handler per i tasti del pannello di navigazione (uguali per ogni
	 * tipo di utente)
	 */
	private class NavButtonHandler implements ClickHandler {
		private int subPanelIndex;

		public NavButtonHandler(int subPanelIndex) {
			this.subPanelIndex = subPanelIndex;
		}

		@Override
		public void onClick(ClickEvent event) {
			infoGrid.clear(true);
			contentPanel.showWidget(subPanelIndex);
		}
	}

}