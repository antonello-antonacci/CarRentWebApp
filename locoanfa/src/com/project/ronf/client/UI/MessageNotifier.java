package com.project.ronf.client.UI;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.project.ronf.client.LOCOANFA;

public class MessageNotifier extends PopupPanel {

	public static final int MESSAGE_SUCCESS = 0;
	public static final int MESSAGE_WARNING = 1;
	public static final int MESSAGE_ERROR = 2;

	public static final int NOTIF_DELAY = 5000;

	/**
	 * Mostra una notifica a scomparsa
	 * 
	 * @param text
	 *            il testo da mostrare
	 * @param msgType
	 *            Il tipo di messaggio
	 * @param autoHide
	 *            Se il pannello deve essere a scomparsa
	 * @param modal
	 *            Se ogni evento di tastiera/mouse al di fuori dal pannello deve
	 *            essere ignorato
	 */
	public MessageNotifier(String text, int msgType, boolean autoHide,
			boolean modal) {
		super(autoHide, modal);

		HorizontalPanel table = null;
		switch (msgType) {
		case 0:
			this.setStyleName("notifier-success");
			table = createTable(text, "locoanfa/gwt/mytheme/icons/success.png");
			break;
		case 1:
			this.setStyleName("notifier-warning");
			table = createTable(text, "locoanfa/gwt/mytheme/icons/warning.png");
			break;
		case 2:
			this.setStyleName("notifier-error");
			table = createTable(text, "locoanfa/gwt/mytheme/icons/error.png");
			break;
		default:
			this.setStyleName("notifier-success");
			table = createTable(text, "locoanfa/gwt/mytheme/icons/success.png");
			break;
		}

		setWidget(new HTML(table.toString()));
		this.addDomHandler(myClkHandler, ClickEvent.getType());

		int leftPosition = LOCOANFA.messageViewerHTML.getAbsoluteLeft();
		int topPosition = LOCOANFA.messageViewerHTML.getAbsoluteTop();
		this.setPopupPosition(leftPosition, topPosition);

	}

	private HorizontalPanel createTable(String text, String imgPath) {

		HorizontalPanel notifPanel = new HorizontalPanel();
		Image icon = new Image();
		icon.setUrl(imgPath);
		icon.setStylePrimaryName("floatLeft");
		icon.setWidth("20px");
		icon.setHeight("20px");
		notifPanel.add(icon);
		notifPanel.add(new HTML(text));
		return notifPanel;
	}

	/**
	 * @param delayMilliseconds
	 *            il tempo per cui mostrare la notifica
	 */
	public void show(int delayMilliseconds) {
		show();
		Timer t = new Timer() {
			@Override
			public void run() {
				MessageNotifier.this.hide();
			}
		};

		// Schedule the timer to close the popup in 3 seconds.
		t.schedule(delayMilliseconds);
	}

	/**
	 * Onclick, nasconde la notifica
	 */
	private ClickHandler myClkHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			MessageNotifier.this.hide();
		}
	};
}