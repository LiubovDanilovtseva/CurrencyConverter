package com.electrolux.converter.currencyconverter;

import java.util.Currency;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	private TextInputEditText currency1;
	private TextInputEditText currency2;
	private AppCompatSpinner spinner1;
	private AppCompatSpinner spinner2;
	private AppCompatTextView text_description;
	private String[] currencyArray;
	private String[][] currencyDisplayNameArray;
	int posFrom=0; int posTo=0;
	private Converter mConverter = new Converter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		currencyArray = this.getResources().getStringArray(R.array.currencies);
		fillinCurrencyDisplayNames();
		currency1 = (TextInputEditText)findViewById(R.id.currency1);
		currency1.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				calculate();
			}
		});
		currency2 = (TextInputEditText)findViewById(R.id.currency2);
		spinner1 = (AppCompatSpinner)findViewById(R.id.spinner1);
		spinner2 = (AppCompatSpinner)findViewById(R.id.spinner2);
		text_description = (AppCompatTextView)findViewById(R.id.text_description);
		currency1.requestFocus();
		currency2.setFocusable(false);
		if (null == savedInstanceState) {
			setLocaleData();
			currency1.setText("");
		} else {
			String consoleOutput = savedInstanceState.getString("text_description");
			text_description.setText(consoleOutput);
			int posFrom = savedInstanceState.getInt("posFrom");
			int posTo = savedInstanceState.getInt("posTo");
			currency1.setText(savedInstanceState.getString("currencyFrom"));
		}

}

	/**
	 * Fill in the arrays with descriptions of the available currency : symbol and names
	 */
	private void fillinCurrencyDisplayNames() {
		currencyDisplayNameArray = new String[2][currencyArray.length];
		for (int i = 0; i< currencyArray.length; i++) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				currencyDisplayNameArray[0][i] = Currency.getInstance(currencyArray[i]).getDisplayName();
				currencyDisplayNameArray[1][i] = Currency.getInstance(currencyArray[i]).getSymbol();
			} else  {
				currencyDisplayNameArray[0][i] = "";
				currencyDisplayNameArray[1][i] = Currency.getInstance(currencyArray[i]).getSymbol();
			}
		}
	}

	/**
	 * Set current default currency for user when application is started first
	 */
	private void setLocaleData() {
		Locale local = Locale.getDefault();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			String localCode = Currency.getInstance(local).getCurrencyCode();
			for (int i = 0; i < currencyArray.length; i++) {
				if (currencyArray[i].equals(localCode)) {
					posFrom = i;
					return;
				}
			}
		} else {
			posFrom = 0;
		}
	}

	protected void onResume() {
		super.onResume();
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter);
		spinner2.setAdapter(adapter);
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (MainActivity.this.posFrom != position) {
					MainActivity.this.posFrom = position;
					calculate();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (MainActivity.this.posTo != position) {
					MainActivity.this.posTo = position;
					calculate();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spinner1.setSelection(posFrom);
		spinner2.setSelection(posTo);
	}

	/**
	 * Translate the value of selected currency type to the value of second selected currency type.
	 */
	private synchronized void calculate() {
		final double val;
		if ((MainActivity.this.currency1.getText().toString().isEmpty()
				|| (new Double(MainActivity.this.currency1.getText().toString())==0))) {
			val = 0;
			runOnUiThread(new RunnableEx(val));
		} else {
			if (posTo == posFrom) {
				val = new Double(MainActivity.this.currency1.getText().toString());
				runOnUiThread(new RunnableEx(val));
			} else
				try {
					mConverter.calculate(MainActivity.this.currencyArray[posFrom],
							MainActivity.this.currencyArray[posTo],
							new Double(MainActivity.this.currency1.getText().toString()),
							new ICalculateListener() {
								@Override
								public void onCalculate(double value) {
									runOnUiThread(new RunnableEx(value));
								}
							});

				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
		}
	}

	private class RunnableEx implements Runnable {
		private double mvalue;

		public RunnableEx(double value) {
			mvalue = value;
		}

		@Override
		public void run() {
			MainActivity.this.currency2.setText("" + mvalue);
			MainActivity.this.text_description.setText(MainActivity.this.currency1.getText() + " " + currencyDisplayNameArray[1][posFrom]+ " " + currencyDisplayNameArray[0][posFrom]
					+ "\n = " + MainActivity.this.currency2.getText() +" " + currencyDisplayNameArray[1][posTo] + " " + currencyDisplayNameArray[0][posTo]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_exit) {
			finish();
			System.exit(0);

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("text_description", text_description.getText().toString());
		outState.putInt("posFrom", posFrom);
		outState.putInt("posTo", posTo);
		outState.putString("currencyFrom", currency1.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String consoleOutput = savedInstanceState.getString("text_description");
		text_description.setText(consoleOutput);
		posFrom = savedInstanceState.getInt("posFrom");
		posTo = savedInstanceState.getInt("posTo");
		currency1.setText(savedInstanceState.getString("currencyFrom"));
	}

}
