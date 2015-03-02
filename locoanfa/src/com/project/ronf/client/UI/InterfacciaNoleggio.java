package com.project.ronf.client.UI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
import com.project.ronf.client.LOCOANFA;
import com.project.ronf.client.SincronizzatoreDati;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.Noleggio;
import com.project.ronf.shared.entities.Optional;

/**
 * L'interfaccia per la registrazione, visualizzazione dei noleggi attivi
 * utilizzata dal dipendente del front office
 */
public class InterfacciaNoleggio extends Composite {

	TabPanel mainPanel = new TabPanel();

	// tendine per selezionare auto e agenzie (popolate dal sincronizzatore
	// dati)
	public static final ListBox autoTendina = new ListBox();
	public final static ListBox agenziaTendina = new ListBox();

	FlowPanel panelNoleggio = new FlowPanel();
	final DateBox boxDataArrivo = new DateBox();

	// tendine per gli optional (checkbox per il navigatore)
	public static final ListBox seggioliniTendina = new ListBox();
	public static final ListBox guidatoriTendina = new ListBox();
	public static final CheckBox checkBoxNavigatore = new CheckBox();

	public static GridWidget<Noleggio> grigliaNoleggi;
	FlexTable formNoleggio;

	public static List<Optional> seggList = new ArrayList<Optional>();
	public static List<Optional> navigList = new ArrayList<Optional>();

	// suggestbox per guidatori aggiuntivi e clienti
	public static MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	public SuggestBox suggestCliente;
	public List<SuggestBox> suggestGuidatori = new ArrayList<SuggestBox>();

	HTML[] infoCampi = { new HTML(), new HTML(), new HTML() };

	public InterfacciaNoleggio() {
		initWidget(mainPanel);

		grigliaNoleggi = new GridWidget<Noleggio>(Noleggio.class, LOCOANFA.me);

		mainPanel.add(panelNoleggio, "Noleggio");
		mainPanel.add(grigliaNoleggi, "Lista Noleggi Terminati");
		mainPanel.selectTab(0);

		// setta le dimensioni del pannello principale
		mainPanel.setPixelSize(Window.getClientWidth()
				- (LOCOANFA.EAST_PANEL + LOCOANFA.PANEL_PADDING)
				- (LOCOANFA.WEST_PANEL + LOCOANFA.PANEL_PADDING),
				Window.getClientHeight() - 2
						* (LOCOANFA.NORTH_PANEL + LOCOANFA.PANEL_PADDING));

		formNoleggio = creaForm();

		HTML data = new HTML("Inserisci dati relativi al noleggio");
		data.setStyleName("italic");
		panelNoleggio.add(data);

		panelNoleggio.add(formNoleggio);

		autoTendina.addClickHandler(new AutoClickHandler());
		agenziaTendina.addClickHandler(new AgenziaClickHandler());
		guidatoriTendina.addChangeHandler(new GuidatoriChangeHandler());

		Date dataArrivo = new Date();
		CalendarUtil.addDaysToDate(dataArrivo, 1);
		boxDataArrivo.setValue(dataArrivo);
		boxDataArrivo.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getFormat(PredefinedFormat.DATE_SHORT)));

		for (int nguid = 0; nguid < 5; nguid++) {
			guidatoriTendina.addItem(Integer.toString(nguid));
		}

	}

	/**
	 * @return il form di registrazione del noleggio
	 */
	@SuppressWarnings("unchecked")
	private FlexTable creaForm() {
		FlexTable flexFormNoleggio = new FlexTable();

		// lega la suggestbox dei clienti all'oracolo e al suo handler
		suggestCliente = new SuggestBox(oracle);
		suggestCliente.addSelectionHandler(new SuggestSelectionHandler());

		// crea i bottoni di conferma e cancellazione
		Button buttonInvia = new Button("Invia");
		Button buttonAnnulla = new Button("Annulla");
		buttonInvia.addClickHandler(new RegButtonHandler(suggestCliente));
		buttonAnnulla.addClickHandler(new CancelRegButtonHandler());

		// popola la flextable con i campi del form
		flexFormNoleggio.setText(0, 0, "Seleziona Cliente");
		flexFormNoleggio.setWidget(0, 1, suggestCliente);

		flexFormNoleggio.setText(1, 0, "Seleziona Auto");
		flexFormNoleggio.setWidget(1, 1, autoTendina);

		flexFormNoleggio.setText(2, 0, "Seleziona Data di arrivo");
		flexFormNoleggio.setWidget(2, 1, boxDataArrivo);
		flexFormNoleggio.setWidget(2, 2, new HTML());

		flexFormNoleggio.setText(3, 0, "Seleziona Agenzia di arrivo");
		flexFormNoleggio.setWidget(3, 1, agenziaTendina);

		flexFormNoleggio.setText(4, 0, "Seleziona optional");
		flexFormNoleggio.getCellFormatter().setStyleName(4, 0, "bold");

		flexFormNoleggio.setText(5, 0, "Seggiolini");
		flexFormNoleggio.setWidget(5, 1, seggioliniTendina);

		flexFormNoleggio.setText(6, 0, "Navigatore");
		flexFormNoleggio.setWidget(6, 1, checkBoxNavigatore);

		flexFormNoleggio.setText(7, 0, "Guidatori Aggiuntivi");
		flexFormNoleggio.setWidget(7, 1, guidatoriTendina);

		flexFormNoleggio.setText(8, 0, "");
		flexFormNoleggio.setWidget(8, 1, buttonInvia);
		flexFormNoleggio.setWidget(8, 2, buttonAnnulla);
		return flexFormNoleggio;
	}

	/**
	 * Crea tabella con informazioni riguardanti il noleggio
	 * 
	 * @param row
	 *            numero di righe
	 * @param col
	 *            numero di colonne
	 * @return tabella generata
	 */
	private FlexTable createInfoTab(int row, int col) {
		FlexTable tab = new FlexTable();
		tab.getRowFormatter().setStyleName(0, "bold");
		for (int i = 1; i < row; i++) {
			tab.getCellFormatter().setStyleName(i, 0, "italic");
			tab.getCellFormatter().setWidth(i, 0, "100px");
		}
		return tab;
	}

	/**
	 * Handler per il click sulla tendina di selezione auto
	 */
	private class AutoClickHandler implements ClickHandler {
		public void onClick(ClickEvent event) {

			String chiaveInfo = autoTendina.getValue(autoTendina
					.getSelectedIndex());
			Auto car = SincronizzatoreDati.hashAuto.get(chiaveInfo);
			String infoModello = car.getModello();
			String infoTarga = car.getTarga();
			int infoTipo = car.getTipo();

			// mostra le info sull'auto nell'infogrid
			FlexTable infoTab = createInfoTab(4, 2);
			infoTab.setText(0, 0, "Info Auto");
			infoTab.setText(1, 0, "Modello");
			infoTab.setText(1, 1, infoModello);
			infoTab.setText(2, 0, "Targa");
			infoTab.setText(2, 1, infoTarga);
			infoTab.setText(3, 0, "Tipo");
			infoTab.setText(3, 1, Auto.tipi[infoTipo]);
			LOCOANFA.infoGrid.setWidget(1, 0, infoTab);

		}
	}

	/**
	 * Handler per il click sulla tendina delle agenzie di arrivo
	 */
	private class AgenziaClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			String chiaveInfo = agenziaTendina.getValue(agenziaTendina
					.getSelectedIndex());
			Agenzia ag = SincronizzatoreDati.hashAgenzie.get(chiaveInfo);
			String infoAddress = ag.getAddress();
			String infoName = ag.getName();

			// popola l'infogrid con le informazioni sull'agenzia di arrivo
			FlexTable infoTab = createInfoTab(3, 2);
			infoTab.setText(0, 0, "Destinazione");
			infoTab.setText(1, 0, "Nome agenzia");
			infoTab.setText(1, 1, infoName);
			infoTab.setText(2, 0, "Indirizzo");
			infoTab.setText(2, 1, infoAddress);

			LOCOANFA.infoGrid.setWidget(2, 0, infoTab);
		}

	}

	/**
	 * Handler di selezione per il form del cliente
	 */
	@SuppressWarnings("rawtypes")
	private class SuggestSelectionHandler implements SelectionHandler {

		@Override
		public void onSelection(SelectionEvent event) {

			String chiaveInfo = suggestCliente.getValue();
			Cliente cli = SincronizzatoreDati.hashClienti.get(chiaveInfo);
			String infoNome = cli.getNome();
			String infoCognome = cli.getCognome();
			String infoAddress = cli.getIndirizzo();
			String infoPatente = cli.getNumeroPatente();

			// popola infogrid con le informazioni sul cliente
			FlexTable infoTab = createInfoTab(5, 2);

			infoTab.setText(0, 0, "Info Cliente");
			infoTab.setText(1, 0, "Nome");
			infoTab.setText(1, 1, infoNome);
			infoTab.setText(2, 0, "Cognome");
			infoTab.setText(2, 1, infoCognome);
			infoTab.setText(3, 0, "Indirizzo");
			infoTab.setText(3, 1, infoAddress);
			infoTab.setText(4, 0, "N° Patente");
			infoTab.setText(4, 1, infoPatente);

			LOCOANFA.infoGrid.setWidget(0, 0, infoTab);
		}

	}

	/**
	 * Handler per la registrazione del noleggio
	 */
	private class RegButtonHandler implements ClickHandler {

		SuggestBox suggest;

		/**
		 * @param suggestCliente
		 *            la suggestbox del cliente
		 */
		public RegButtonHandler(SuggestBox suggestCliente) {
			suggest = suggestCliente;
		}

		@Override
		public void onClick(ClickEvent event) {
			String chiave = suggest.getValue();

			// controlla che sia stato inserito un cliente
			if (chiave.isEmpty()) {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.WARN_NOLEGGIO_INSERT_CLIENTE,
						MessageNotifier.MESSAGE_WARNING, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
				return;
			}

			final Cliente user = SincronizzatoreDati.hashClienti.get(chiave);

			// recupera auto e agenzia dalla tendina
			String chiaveAuto = autoTendina.getValue(autoTendina
					.getSelectedIndex());
			String chiaveAgenzia = agenziaTendina.getValue(agenziaTendina
					.getSelectedIndex());

			List<Cliente> guidatori = new ArrayList<Cliente>();
			List<Optional> optionals = new ArrayList<Optional>();
			String chiaveGuidatore;
			Cliente guidatore;
			// per ogni suggestbox relativa ai guidatori aggiuntivi (aggiunte e
			// rimosse dinamicamente) controlla che i campi non siano vuoti e
			// memorizza i guidatori
			for (SuggestBox guid : suggestGuidatori) {
				chiaveGuidatore = guid.getValue();
				if (chiaveGuidatore.isEmpty()) {
					MessageNotifier messageNotifier = new MessageNotifier(
							NotifStrings.WARN_NOLEGGIO_GUIDATORI,
							MessageNotifier.MESSAGE_WARNING, true, true);
					messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					return;
				}
				guidatore = SincronizzatoreDati.hashClienti
						.get(chiaveGuidatore);
				if (guidatori.contains(guidatore)) {
					MessageNotifier messageNotifier = new MessageNotifier(
							NotifStrings.WARN_NOLEGGIO_SAME_GUIDATORI_AGGIUNTIVI,
							MessageNotifier.MESSAGE_WARNING, true, true);
					messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					return;
				} else if (user.equals(guidatore)) {
					MessageNotifier messageNotifier = new MessageNotifier(
							NotifStrings.WARN_NOLEGGIO_SAME_DRIVER,
							MessageNotifier.MESSAGE_WARNING, true, true);
					messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					return;
				} else {
					guidatori.add(guidatore);
				}
			}

			// memorizza gli optional (guidatori e navigatore)
			int noSeggiolini = seggioliniTendina.getSelectedIndex();
			for (int i = 0; i < noSeggiolini; i++) {
				optionals.add(seggList.remove(0));
			}
			if (checkBoxNavigatore.getValue()) {
				optionals.add(navigList.remove(0));
			}

			// invia il noleggio al server
			LOCOANFA.noleggiService.nuovoNoleggio(user,
					SincronizzatoreDati.hashAuto.get(chiaveAuto), new Date(),
					boxDataArrivo.getValue(),
					SincronizzatoreDati.hashAgenzie.get(chiaveAgenzia),
					optionals, guidatori, new AsyncCallback<Noleggio>() {

						@Override
						public void onFailure(Throwable caught) {
							MessageNotifier messageNotifier = new MessageNotifier(
									NotifStrings.ERR_NEW_NOLEGGIO,
									MessageNotifier.MESSAGE_ERROR, true, true);
							messageNotifier.show(MessageNotifier.NOTIF_DELAY);

						}

						@Override
						public void onSuccess(Noleggio result) {
							MessageNotifier messageNotifier = new MessageNotifier(
									user.getNome()
											+ " "
											+ user.getCognome()
											+ " "
											+ autoTendina.getValue(autoTendina
													.getSelectedIndex())
											+ " "
											+ agenziaTendina.getValue(agenziaTendina
													.getSelectedIndex()) + " "
											+ boxDataArrivo.getValue(),
									MessageNotifier.MESSAGE_SUCCESS, true, true);
							messageNotifier.show(MessageNotifier.NOTIF_DELAY);

							SincronizzatoreDati.updateAll(LOCOANFA.me);
							resetCampi();

						}

					});
		}

	}

	/**
	 * Handler che aggiunge al form le suggestbox per i guidatori aggiuntivi (a
	 * seconda di quanti ne seleziono nella tendina)
	 */
	private class GuidatoriChangeHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			int nrighe = formNoleggio.getRowCount();
			int nguid = nrighe - 9;
			int ntomod = guidatoriTendina.getSelectedIndex() - nguid;
			if (ntomod > 0) {
				for (int i = 0; i < ntomod; i++) {
					SuggestBox guid = new SuggestBox(oracle);
					suggestGuidatori.add(guid);
					int nriga = formNoleggio.insertRow(nrighe - 1);
					formNoleggio.setText(nriga, 0, "Guidatore:");
					formNoleggio.setWidget(nriga, 1, guid);
				}
			} else if (ntomod < 0) {
				for (int i = 0; i > ntomod; i--) {
					formNoleggio.removeRow(nrighe - 2);
					suggestGuidatori.remove(suggestGuidatori.size() - 1);
					nrighe = formNoleggio.getRowCount();
				}
			}
		}

	}

	/**
	 * Resetta i campi del form se si clicca su annulla
	 */
	private class CancelRegButtonHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			resetCampi();
		}

	}

	private void resetCampi() {
		SuggestBox suggest = (SuggestBox) formNoleggio.getWidget(0, 1);
		suggest.setText("");

		ListBox listAuto = (ListBox) formNoleggio.getWidget(1, 1);
		listAuto.setItemSelected(0, true);

		DateBox date = (DateBox) formNoleggio.getWidget(2, 1);
		Date today = new Date();
		CalendarUtil.addDaysToDate(today, 1);
		date.setValue(today);
		date.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getFormat(PredefinedFormat.DATE_SHORT)));

		ListBox listAgenzia = (ListBox) formNoleggio.getWidget(3, 1);
		listAgenzia.setItemSelected(0, true);

		ListBox listSeggiolini = (ListBox) formNoleggio.getWidget(5, 1);
		listSeggiolini.setItemSelected(0, true);

		CheckBox checkNavigatore = (CheckBox) formNoleggio.getWidget(6, 1);
		checkNavigatore.setValue(false);

		ListBox listGuidatori = (ListBox) formNoleggio.getWidget(7, 1);
		listGuidatori.setItemSelected(0, true);
		LOCOANFA.infoGrid.clear(true);
		NativeEvent event = Document.get().createChangeEvent();
		DomEvent.fireNativeEvent(event, listGuidatori);
	}

	/**
	 * Crea un dialog per mostrare l'importo dovuto per il noleggio, con un
	 * tasto fine che registra l'avvenuto pagamento
	 */
	public static void creaDialog(final Noleggio object) {
		final DialogBox prezzoBox = new DialogBox();
		// la tabella per mostrare le varie componenti del prezzo
		final DockPanel boxPanel = new DockPanel();
		final HorizontalPanel buttonPanel = new HorizontalPanel();
		final Button annullaButton = new Button("Annulla");
		final Button fineButton = new Button("Fine");
		List<Noleggio> list = new ArrayList<Noleggio>();
		list.add(object);

		// se clicco su fine, viene registrata la fine del noleggio
		fineButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				prezzoBox.hide();
				LOCOANFA.noleggiService.fineNoleggio(object,
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								MessageNotifier messageNotifier = new MessageNotifier(
										NotifStrings.ERR_END_NOLEGGIO,
										MessageNotifier.MESSAGE_ERROR, true,
										true);
								messageNotifier
										.show(MessageNotifier.NOTIF_DELAY);

							}

							@Override
							public void onSuccess(Void result) {
								MessageNotifier messageNotifier = new MessageNotifier(
										NotifStrings.SUCCESS_END_NOLEGGIO,
										MessageNotifier.MESSAGE_SUCCESS, true,
										true);

								messageNotifier
										.show(MessageNotifier.NOTIF_DELAY);
								SincronizzatoreDati.updateAll(LOCOANFA.me);
								prezzoBox.hide();
							}

						});
			}

		});

		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				prezzoBox.hide();
			}

		});

		RiepilogoTable tableResoconto = new RiepilogoTable(object);

		// popolo il dockpanel con la tabella e i bottoni
		boxPanel.add(tableResoconto, DockPanel.CENTER);
		buttonPanel.setWidth("100%");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.add(annullaButton);
		buttonPanel.add(fineButton);
		boxPanel.add(buttonPanel, DockPanel.SOUTH);
		prezzoBox.add(boxPanel);

		prezzoBox.center();
	}

	/**
	 * La tabella di riepilogo del noleggio, mostra optional, guidatori,
	 * navigatore e prezzo
	 */
	public static class RiepilogoTable extends FlexTable {
		/**
		 * @param object
		 *            Il noleggio da mostrare
		 */
		public RiepilogoTable(Noleggio object) {
			super();

			// dati utli per la tabella
			int nSeggiolini = 0;
			int nGuidatori = object.getGuidatoriAggiuntivi().size();
			boolean nav = false;
			boolean rifornimento = object.getRifornimento();
			for (Optional optional : object.getOptional()) {
				if (optional.getTipo() == Optional.IND_NAVIG)
					nav = true;
				else if (optional.getTipo() == Optional.IND_SEGG)
					nSeggiolini++;
			}

			// dettagli auto
			Label dettagliAuto = new Label("Dettagli Auto");
			Label.setStylePrimaryName(dettagliAuto.getElement(), "bold");
			this.setWidget(0, 0, dettagliAuto);
			this.setText(1, 1, "Tipo");
			this.setText(1, 2, Auto.tipi[object.getAuto().getTipo()]);
			this.setText(2, 1, "Giorni " + object.getGiorni());
			String prezzoGiorni = Float.toString(object.getGiorni()
					* Auto.prezzo[object.getAuto().getTipo()]);
			this.setText(2, 2, prezzoGiorni);

			// Optional
			Label dettagliOptional = new Label("Dettagli Optional");
			Label.setStylePrimaryName(dettagliOptional.getElement(), "bold");

			this.setWidget(3, 0, dettagliOptional);

			this.setText(4, 1, Integer.toString(nSeggiolini));
			this.setText(4, 2, "seggiolini");
			this.setText(4, 3,
					Integer.toString(nSeggiolini * Optional.PREZZO_SEGGIOLINO));

			this.setText(5, 1, Integer.toString(nGuidatori));
			this.setText(5, 2, "guidatori aggiuntivi");
			this.setText(
					5,
					3,
					Integer.toString(nGuidatori
							* Optional.PREZZO_GUID_AGGIUNTIVO));

			Label labelNavigatore = new Label("Navigatore");
			Label.setStylePrimaryName(labelNavigatore.getElement(), "bold");
			this.setWidget(6, 0, labelNavigatore);
			this.setText(6, 1, (nav ? "Sì" : "No"));
			this.setText(6, 3,
					(nav ? Integer.toString(Optional.PREZZO_NAVIGATORE)
							: Integer.toString(0)));

			// Rifornimento
			Label labelRifornimento = new Label("Rifornimento");
			Label.setStylePrimaryName(labelRifornimento.getElement(), "bold");
			this.setWidget(7, 0, labelRifornimento);
			this.setText(7, 1, rifornimento ? "Sì" : "No");
			this.setText(
					7,
					3,
					rifornimento ? Integer
							.toString(Noleggio.PREZZO_RIFORNIMENTO) : Integer
							.toString(0));

			// Totale
			Label labelTotale = new Label("Totale");
			Label.setStylePrimaryName(labelTotale.getElement(), "bold");
			this.setWidget(8, 0, labelTotale);
			String prezzo = Float.toString(object.getPrice()) + "€";
			Label labelPrezzo = new Label(prezzo);
			Label.setStylePrimaryName(labelPrezzo.getElement(), "bold");
			this.setWidget(8, 1, labelPrezzo);

			// Larghezza colonne
			this.getColumnFormatter().setWidth(0, "117px");
			this.getColumnFormatter().setWidth(1, "58px");
			this.getColumnFormatter().setWidth(2, "66px");
		}
	}

}