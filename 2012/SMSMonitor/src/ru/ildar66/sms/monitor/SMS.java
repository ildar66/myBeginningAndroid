package ru.ildar66.sms.monitor;

import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMS {
	public final String msg;
	public final String from;

	private SMS(String from, String msg) {
		this.from = from;
		this.msg = msg;
	}

	/** ---retrieve the SMS message from Bundle --- */
	public static SMS create(Bundle bundle) {
		Object[] pdus = (Object[]) bundle.get("pdus");
		SmsMessage[] msgs = new SmsMessage[pdus.length];
		String msg = "", from = "";
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			if (i == 0) {
				from = msgs[i].getOriginatingAddress();
			}
			msg += msgs[i].getMessageBody().toString() + "\n";
		}
		return new SMS(from, msg);
	}
}
