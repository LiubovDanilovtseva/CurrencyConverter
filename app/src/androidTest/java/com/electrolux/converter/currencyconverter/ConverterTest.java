package com.electrolux.converter.currencyconverter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import android.os.Handler;

import static org.junit.Assert.*;

/**
 * Created by Liubov on 17.11.2017.
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ConverterTest {

	@Test
	public void calculate() throws Exception {

		new Converter().calculate("RUB", "EUR", (double) 50,
				new ICalculateListener() {
					@Override
					public void onCalculate(double value) {
						assertNotNull(value);
					}
				});
		Thread.sleep(500);
	}
}