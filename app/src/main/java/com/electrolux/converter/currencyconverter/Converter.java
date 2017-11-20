package com.electrolux.converter.currencyconverter;

/**
 * Created by Liubov on 17.11.2017.
 * Class converts the currency from one country locale to the other country locale
 * by www.fixer.io web service
 */
import com.afollestad.ason.Ason;
import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Response;
import com.afollestad.bridge.ResponseConvertCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Converter {

	private CountDownLatch mSignal;

	public synchronized void calculate(final String currency, final String desiredCurrency, final Double value,
									   final ICalculateListener listener) throws InterruptedException {

		Bridge.get("https://api.fixer.io/latest?base=%s", currency)
				.asAsonObject(new ResponseConvertCallback<Ason>() {
					@Override
					public void onResponse(Response response, Ason object, BridgeException e) {
						double d = object.getDouble("rates." + desiredCurrency);
						listener.onCalculate(d * value);
					}
				});

	}
}
