package com.project.ronf.client.UI;

import java.util.Date;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.project.ronf.client.LOCOANFA;
import com.project.ronf.client.SincronizzatoreDati;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.DipFrontOffice;
import com.project.ronf.shared.entities.DipTrasferimento;
import com.project.ronf.shared.entities.Dipendente;
import com.project.ronf.shared.entities.Noleggio;
import com.project.ronf.shared.entities.Trasferimento;

/**
 * Una celltable che visualizza vari tipi di dato
 * 
 * @param <T>
 *            il tipo di dato visualizzato
 */
@SuppressWarnings("unchecked")
public class GridWidget<T> extends Composite {

	ProvidesKey<T> KEY_PROVIDER;
	SimplePager.Resources pagerResources;
	SimplePager pager;
	public ListDataProvider<T> dataProvider;
	CellTable<T> griglia;
	private DockPanel dock = new DockPanel();

	/**
	 * Il costruttore, non potendo utilizzare dati parametrici in javascript, ha
	 * come parametro anche la classe dei dati da mostrare
	 * 
	 * @param dataClass
	 *            la classe dei dati da mostrare
	 * @param dip
	 *            il dipendente che visualizza la griglia
	 */
	public GridWidget(Class<T> dataClass, Dipendente dip) {
		super();
		initWidget(dock);

		// a seconda del tipo di dato, la celltable viene popolata con le
		// colonne appropriate
		if (dataClass == Auto.class) {
			grigliaAuto();
		} else if (dataClass == Cliente.class) {
			grigliaClienti();
		} else if (dataClass == Noleggio.class) {
			grigliaNoleggi(dip);
		} else if (dataClass == Trasferimento.class) {
			grigliaTrasferimenti();
		}

		pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(griglia);

		griglia.setEmptyTableWidget(new HTML("No Data to Display"));

		dataProvider = new ListDataProvider<T>();
		dataProvider.addDataDisplay(griglia);

		pager.setVisible(true);
		griglia.setVisible(true);

		dock.add(griglia, DockPanel.CENTER);
		dock.add(pager, DockPanel.SOUTH);
		dock.setWidth("100%");
		dock.setCellWidth(griglia, "100%");
		dock.setCellWidth(pager, "100%");

	}

	private void grigliaTrasferimenti() {
		KEY_PROVIDER = (ProvidesKey<T>) new ProvidesKey<Trasferimento>() {
			@Override
			public Object getKey(Trasferimento item) {
				return item == null ? null : item.getId();
			}

		};

		griglia = new CellTable<T>(5, KEY_PROVIDER);

		TextColumn<Trasferimento> partenzaColumn = new TextColumn<Trasferimento>() {

			@Override
			public String getValue(Trasferimento object) {
				return object.getPartenza().getName();
			}

		};

		griglia.addColumn((Column<T, ?>) partenzaColumn, "Agenzia di partenza");
		griglia.setColumnWidth((Column<T, ?>) partenzaColumn, 40, Unit.PX);

		TextColumn<Trasferimento> arrivoColumn = new TextColumn<Trasferimento>() {
			@Override
			public String getValue(Trasferimento item) {
				return item.getDestinazione().getName();
			}
		};
		griglia.addColumn((Column<T, ?>) arrivoColumn, "Agenzia di arrivo");
		griglia.setColumnWidth((Column<T, ?>) arrivoColumn, 40, Unit.PX);

		TextColumn<Trasferimento> modelloColumn = new TextColumn<Trasferimento>() {
			@Override
			public String getValue(Trasferimento item) {
				return item.getModelloAuto();
			}
		};

		griglia.addColumn((Column<T, ?>) modelloColumn, "Modello Auto");
		griglia.setColumnWidth((Column<T, ?>) modelloColumn, 40, Unit.PX);

		TextColumn<Trasferimento> targaColumn = new TextColumn<Trasferimento>() {
			@Override
			public String getValue(Trasferimento item) {
				return item.getTargaAuto();
			}
		};

		griglia.addColumn((Column<T, ?>) targaColumn, "Targa Auto");
		griglia.setColumnWidth((Column<T, ?>) targaColumn, 40, Unit.PX);

	}

	private void grigliaNoleggi(final Dipendente dip) {
		KEY_PROVIDER = (ProvidesKey<T>) new ProvidesKey<Noleggio>() {
			@Override
			public Long getKey(Noleggio item) {
				return item == null ? null : item.getId();
			}
		};

		griglia = new CellTable<T>(5, KEY_PROVIDER);

		TextColumn<Noleggio> noPatenteClienteColumn = new TextColumn<Noleggio>() {
			@Override
			public String getValue(Noleggio object) {
				return object.getCliente().getNumeroPatente();
			}
		};
		griglia.addColumn((Column<T, ?>) noPatenteClienteColumn,
				"Patente Cliente");
		griglia.setColumnWidth((Column<T, ?>) noPatenteClienteColumn, 40,
				Unit.PX);

		TextColumn<Noleggio> noTargaAutoColumn = new TextColumn<Noleggio>() {
			@Override
			public String getValue(Noleggio object) {
				return object.getAuto().getTarga();
			}
		};
		griglia.addColumn((Column<T, ?>) noTargaAutoColumn, "Targa Auto");
		griglia.setColumnWidth((Column<T, ?>) noTargaAutoColumn, 40, Unit.PX);

		TextColumn<Noleggio> agArrivoColumn = new TextColumn<Noleggio>() {

			@Override
			public String getValue(Noleggio object) {
				return object.getAgenziaArrivo().getName();
			}
		};
		griglia.addColumn((Column<T, ?>) agArrivoColumn, "Agenzia Arrivo");
		griglia.setColumnWidth((Column<T, ?>) agArrivoColumn, 40, Unit.PX);

		TextColumn<Noleggio> partenzaColumn = new TextColumn<Noleggio>() {

			@Override
			public String getValue(Noleggio object) {
				DateTimeFormat format = DateTimeFormat
						.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
				Date start = new Date(object.getPartenza());
				return format.format(start);
			}

		};

		griglia.addColumn((Column<T, ?>) partenzaColumn, "Partenza");
		griglia.setColumnWidth((Column<T, ?>) partenzaColumn, 40, Unit.PX);

		TextColumn<Noleggio> arrivoColumn = new TextColumn<Noleggio>() {

			@Override
			public String getValue(Noleggio object) {
				DateTimeFormat format = DateTimeFormat
						.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
				Date stop = new Date(object.getArrivo());
				return format.format(stop);
			}

		};

		griglia.addColumn((Column<T, ?>) arrivoColumn, "Arrivo");
		griglia.setColumnWidth((Column<T, ?>) arrivoColumn, 40, Unit.PX);

		ButtonCell buttonCell = new ButtonCell();

		if (dip instanceof DipFrontOffice) {
			Column<Noleggio, String> buttonColumn = new Column<Noleggio, String>(
					buttonCell) {

				@Override
				public String getValue(Noleggio object) {
					return "Registra fine noleggio";
				}

			};

			griglia.addColumn((Column<T, ?>) buttonColumn, "");
			buttonColumn.setFieldUpdater(new FieldUpdater<Noleggio, String>() {

				@Override
				public void update(int index, final Noleggio object,
						String value) {
					InterfacciaNoleggio.creaDialog(object);
				}

			});
		} else if (dip instanceof DipTrasferimento) {
			Column<Noleggio, String> buttonColumn = new Column<Noleggio, String>(
					buttonCell) {

				@Override
				public String getValue(Noleggio object) {
					return "Registra riconsegna";
				}
			};

			final Column<Noleggio, Boolean> checkColumn = new Column<Noleggio, Boolean>(
					new CheckboxCell(false, false)) {
				@Override
				public Boolean getValue(Noleggio object) {
					return object.getRifornimento();
				}
			};
			griglia.setColumnWidth((Column<T, ?>) checkColumn, 40, Unit.PX);
			griglia.addColumn((Column<T, ?>) checkColumn, "Rifornimento");

			griglia.addColumn((Column<T, ?>) buttonColumn, "");

			checkColumn.setFieldUpdater(new FieldUpdater<Noleggio, Boolean>() {

				@Override
				public void update(int index, Noleggio object, Boolean value) {
					object.setRifornimento(value);
				}

			});

			buttonColumn.setFieldUpdater(new FieldUpdater<Noleggio, String>() {

				@Override
				public void update(int index, final Noleggio object,
						String value) {
					LOCOANFA.noleggiService.registraRiconsegna(object.getId(),
							checkColumn.getValue(object),
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									MessageNotifier messageNotifier = new MessageNotifier(
											NotifStrings.ERR_RM_RICONSEGNA,
											MessageNotifier.MESSAGE_ERROR,
											true, true);

									messageNotifier
											.show(MessageNotifier.NOTIF_DELAY);

								}

								@Override
								public void onSuccess(Void result) {
									MessageNotifier messageNotifier = new MessageNotifier(
											NotifStrings.SUCCESS_RICONSEGNA,
											MessageNotifier.MESSAGE_SUCCESS,
											true, true);

									messageNotifier
											.show(MessageNotifier.NOTIF_DELAY);
									SincronizzatoreDati.updateAll(LOCOANFA.me);
								}

							});
				}
			});

		}
	}

	private void grigliaAuto() {
		KEY_PROVIDER = (ProvidesKey<T>) new ProvidesKey<Auto>() {
			@Override
			public Object getKey(Auto item) {
				return item == null ? null : item.getTarga();
			}
		};

		griglia = new CellTable<T>(5, KEY_PROVIDER);

		TextColumn<Auto> targaColumn = new TextColumn<Auto>() {
			@Override
			public String getValue(Auto object) {
				return object.getTarga();
			}
		};

		griglia.addColumn((Column<T, ?>) targaColumn, "Targa");
		griglia.setColumnWidth((Column<T, ?>) targaColumn, 40, Unit.PX);

		Column<Auto, String> modelloColumn = new TextColumn<Auto>() {
			@Override
			public String getValue(Auto object) {
				return object.getModello();
			}
		};

		griglia.addColumn((Column<T, ?>) modelloColumn, "Modello");
		griglia.setColumnWidth((Column<T, ?>) modelloColumn, 40, Unit.PX);

		Column<Auto, String> tipoColumn = new TextColumn<Auto>() {
			@Override
			public String getValue(Auto object) {
				return Auto.tipi[object.getTipo()];
			}
		};

		griglia.addColumn((Column<T, ?>) tipoColumn, "Tipo");
		griglia.setColumnWidth((Column<T, ?>) tipoColumn, 40, Unit.PX);
	}

	public void grigliaClienti() {
		KEY_PROVIDER = (ProvidesKey<T>) new ProvidesKey<Cliente>() {
			@Override
			public String getKey(Cliente item) {
				return item == null ? null : item.getNumeroPatente();
			}
		};

		griglia = new CellTable<T>(5, KEY_PROVIDER);

		TextColumn<Cliente> nomeColumn = new TextColumn<Cliente>() {
			@Override
			public String getValue(Cliente object) {
				return object.getNome();
			}
		};
		griglia.addColumn((Column<T, ?>) nomeColumn, "Nome");
		griglia.setColumnWidth((Column<T, ?>) nomeColumn, 40, Unit.PX);

		TextColumn<Cliente> cognomeColumn = new TextColumn<Cliente>() {

			@Override
			public String getValue(Cliente object) {
				return object.getCognome();
			}
		};
		griglia.addColumn((Column<T, ?>) cognomeColumn, "Cognome");
		griglia.setColumnWidth((Column<T, ?>) cognomeColumn, 40, Unit.PX);

		TextColumn<Cliente> indirizzoColumn = new TextColumn<Cliente>() {

			@Override
			public String getValue(Cliente object) {
				return object.getIndirizzo();
			}

		};

		griglia.addColumn((Column<T, ?>) indirizzoColumn, "Indirizzo");
		griglia.setColumnWidth((Column<T, ?>) indirizzoColumn, 40, Unit.PX);

		TextColumn<Cliente> noPatenteColumn = new TextColumn<Cliente>() {

			@Override
			public String getValue(Cliente object) {
				return object.getNumeroPatente();
			}

		};

		griglia.addColumn((Column<T, ?>) noPatenteColumn, "Patente");
		griglia.setColumnWidth((Column<T, ?>) noPatenteColumn, 40, Unit.PX);

	}

	public void update() {
		Range range = griglia.getVisibleRange();
		griglia.setVisibleRangeAndClearData(range, true);
	}
}