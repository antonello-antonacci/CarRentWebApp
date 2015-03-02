package com.project.ronf.client;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.project.ronf.client.UI.InterfacciaAuto;
import com.project.ronf.client.UI.InterfacciaClienti;
import com.project.ronf.client.UI.InterfacciaNoleggio;
import com.project.ronf.client.UI.InterfacciaNoleggioRiforn;
import com.project.ronf.client.UI.InterfacciaTrasferimento;
import com.project.ronf.client.UI.MessageNotifier;
import com.project.ronf.client.UI.NotifStrings;
import com.project.ronf.shared.entities.Agenzia;
import com.project.ronf.shared.entities.Auto;
import com.project.ronf.shared.entities.Cliente;
import com.project.ronf.shared.entities.DipFrontOffice;
import com.project.ronf.shared.entities.DipTrasferimento;
import com.project.ronf.shared.entities.Dipendente;
import com.project.ronf.shared.entities.Noleggio;
import com.project.ronf.shared.entities.Optional;
import com.project.ronf.shared.entities.Trasferimento;

/**
 * Si occupa di sincronizzare i dati con il server, popolando poi tendine e/o
 * griglie in modo appropriato
 */
public class SincronizzatoreDati {

	public static HashMap<String, Agenzia> hashAgenzie;
	public static HashMap<String, Auto> hashAuto;
	public static HashMap<String, Cliente> hashClienti;

	public static <T> void updateAll(Dipendente dip) {

		if (dip instanceof DipFrontOffice) {
			aggiornaDatiAgenzie();
			aggiornaDatiAuto();
			aggiornaDatiClienti();
			aggiornaDatiOptional();

		} else if (dip instanceof DipTrasferimento) {
			aggiornaDatiTrasferimenti();
		}
		aggiornaDatiNoleggi(dip);

	}

	public static <T> void aggiornaDatiAuto() {
		LOCOANFA.carService.listaAuto(new AsyncCallback<List<Auto>>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageNotifier messageNotifier = new MessageNotifier(
						NotifStrings.ERR_GET_LIST_AUTO,
						MessageNotifier.MESSAGE_ERROR, true, true);
				messageNotifier.show(MessageNotifier.NOTIF_DELAY);
			}

			@Override
			public void onSuccess(List<Auto> result) {
				// Popola l'hash e la tendina per la selezione auto nel
				// noleggio, la griglia delle auto
				hashAuto = new HashMap<String, Auto>(result.size());
				InterfacciaNoleggio.autoTendina.clear();
				List<Auto> list = InterfacciaAuto.grigliaAuto.dataProvider
						.getList();
				list.clear();
				list.addAll(result);
				InterfacciaAuto.grigliaAuto.dataProvider.refresh();
				for (Auto cur : result) {
					hashAuto.put(cur.getTarga(), cur);
					InterfacciaNoleggio.autoTendina.addItem(cur.getModello(),
							cur.getTarga());
				}
			}

		});

	}

	public static <T> void aggiornaDatiAgenzie() {
		LOCOANFA.agenziaService
				.listaAgenzia(new AsyncCallback<List<Agenzia>>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageNotifier messageNotifier = new MessageNotifier(
								NotifStrings.ERR_GET_LIST_AGENZIE,
								MessageNotifier.MESSAGE_ERROR, true, true);
						messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					}

					@Override
					public void onSuccess(List<Agenzia> result) {
						// Popola la tendina delle agenzie per la richiesta di
						// auto remota e per la selezione destinazione nel
						// noleggio
						hashAgenzie = new HashMap<String, Agenzia>(result
								.size());
						InterfacciaNoleggio.agenziaTendina.clear();
						for (Agenzia cur : result) {
							String chiave = Long.toString(cur.getId());
							if (!cur.isLocale())
								InterfacciaAuto.reqAgenzie.addItem(
										cur.getName(), chiave);
							InterfacciaNoleggio.agenziaTendina.addItem(
									cur.getName(), chiave);
							hashAgenzie.put(chiave, cur);
						}
						NativeEvent event = Document.get().createChangeEvent();
						DomEvent.fireNativeEvent(event,
								InterfacciaAuto.reqAgenzie);
					}

				});
	}

	public static <T> void aggiornaDatiClienti() {
		LOCOANFA.clientiService
				.listaClienti(new AsyncCallback<List<Cliente>>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageNotifier messageNotifier = new MessageNotifier(
								NotifStrings.ERR_GET_LIST_CLIENTI,
								MessageNotifier.MESSAGE_ERROR, true, true);
						messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					}

					@Override
					public void onSuccess(List<Cliente> result) {
						// popola l'oracolo per la suggestbox dei clienti e dei
						// guidatori aggiuntivi
						InterfacciaNoleggio.oracle.clear();
						List<Cliente> list = InterfacciaClienti.grigliaClienti.dataProvider
								.getList();
						list.clear();
						list.addAll(result);
						InterfacciaClienti.grigliaClienti.dataProvider
								.refresh();
						hashClienti = new HashMap<String, Cliente>(result
								.size());
						String chiave;
						for (Cliente cl : result) {
							chiave = cl.getNumeroPatente() + " " + cl.getNome()
									+ " " + cl.getCognome();
							hashClienti.put(chiave, cl);
							InterfacciaNoleggio.oracle.add(chiave);
						}
					}

				});
	}

	public static <T> void aggiornaDatiNoleggi(final Dipendente dip) {
		LOCOANFA.noleggiService.listaNoleggi(dip,
				new AsyncCallback<List<Noleggio>>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageNotifier messageNotifier = new MessageNotifier(
								NotifStrings.ERR_GET_LIST_NOLEGGI,
								MessageNotifier.MESSAGE_ERROR, true, true);
						messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					}

					@Override
					public void onSuccess(List<Noleggio> result) {
						if (dip instanceof DipFrontOffice) {
							// popola la griglia dei noleggi per il dipendente
							// del front office
							List<Noleggio> list = InterfacciaNoleggio.grigliaNoleggi.dataProvider
									.getList();
							list.clear();
							list.addAll(result);
							InterfacciaNoleggio.grigliaNoleggi.dataProvider
									.refresh();
						} else if (dip instanceof DipTrasferimento) {
							// popola la griglia dei noleggi per l'addetto ai
							// rifornimenti/trasferimenti
							List<Noleggio> list = InterfacciaNoleggioRiforn.grigliaRifornimenti.dataProvider
									.getList();
							list.clear();
							list.addAll(result);
							InterfacciaNoleggioRiforn.grigliaRifornimenti.dataProvider
									.refresh();
						}
					}

				});
	}

	public static <T> void aggiornaDatiOptional() {
		LOCOANFA.noleggiService
				.getAllOptional(new AsyncCallback<List<Optional>>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageNotifier messageNotifier = new MessageNotifier(
								NotifStrings.ERR_GET_LIST_OPTIONAL,
								MessageNotifier.MESSAGE_ERROR, true, true);
						messageNotifier.show(MessageNotifier.NOTIF_DELAY);

					}

					@Override
					public void onSuccess(List<Optional> result) {
						// popola la tendina per i seggiolini, rende attiva o
						// meno la checkbox per il navigatore
						int nsegg = 0, nnavig = 0;
						String value = Integer.toString(nsegg);
						InterfacciaNoleggio.seggioliniTendina.clear();
						InterfacciaNoleggio.seggioliniTendina.addItem(value);
						for (Optional opt : result) {
							if (opt.getTipo() == Optional.IND_SEGG & nsegg < 4) {
								nsegg++;
								value = Integer.toString(nsegg);
								InterfacciaNoleggio.seggioliniTendina
										.addItem(value);
								InterfacciaNoleggio.seggList.add(opt);

							} else if (opt.getTipo() == Optional.IND_NAVIG) {
								nnavig++;
								InterfacciaNoleggio.navigList.add(opt);
							}
						}
						if (nnavig == 0)
							InterfacciaNoleggio.checkBoxNavigatore
									.setEnabled(false);
					}

				});
	}

	public static void aggiornaDatiAgenzieRemote(String selected) {
		Agenzia selezionata = hashAgenzie.get(selected);

		LOCOANFA.carService.elencoAuto(selezionata,
				new AsyncCallback<List<Auto>>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageNotifier messageNotifier = new MessageNotifier(
								NotifStrings.ERR_SYNC_AUTO,
								MessageNotifier.MESSAGE_ERROR, true, true);
						messageNotifier.show(MessageNotifier.NOTIF_DELAY);

					}

					@Override
					public void onSuccess(List<Auto> result) {
						// aggiorna la griglia delle auto dell'agenzia remota
						// selezionata
						List<Auto> list = InterfacciaAuto.grigliaAutoRemote.dataProvider
								.getList();
						list.clear();
						list.addAll(result);
						InterfacciaAuto.grigliaAutoRemote.dataProvider
								.refresh();
					}

				});

	}

	public static void aggiornaDatiTrasferimenti() {
		LOCOANFA.trasfService
				.getTrasferimentiPartenza(new AsyncCallback<List<Trasferimento>>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageNotifier messageNotifier = new MessageNotifier(
								NotifStrings.ERR_GET_LIST_TRASF_PARTENZA,
								MessageNotifier.MESSAGE_ERROR, true, true);
						messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					}

					@Override
					public void onSuccess(List<Trasferimento> result) {
						// aggiorna la griglia dei trasferimenti in partenza
						List<Trasferimento> gridList = InterfacciaTrasferimento.trasferimentiPartenza.dataProvider
								.getList();
						gridList.clear();
						gridList.addAll(result);
						InterfacciaTrasferimento.trasferimentiPartenza.dataProvider
								.refresh();
					}

				});

		LOCOANFA.trasfService
				.getTrasferimentiArrivo(new AsyncCallback<List<Trasferimento>>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageNotifier messageNotifier = new MessageNotifier(
								NotifStrings.ERR_GET_LIST_TRASF_ARRIVO,
								MessageNotifier.MESSAGE_ERROR, true, true);
						messageNotifier.show(MessageNotifier.NOTIF_DELAY);
					}

					@Override
					public void onSuccess(List<Trasferimento> result) {
						// aggiorna la griglia dei trasferimenti in arrivo
						List<Trasferimento> gridList = InterfacciaTrasferimento.trasferimentiArrivo.dataProvider
								.getList();
						gridList.clear();
						gridList.addAll(result);
						InterfacciaTrasferimento.trasferimentiArrivo.dataProvider
								.refresh();
					}

				});

	}

}