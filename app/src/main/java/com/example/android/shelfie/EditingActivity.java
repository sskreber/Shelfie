package com.example.android.shelfie;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.shelfie.data.BookContract.BookEntry;
import com.example.android.shelfie.data.BookDbHelper;

public class EditingActivity extends AppCompatActivity {

    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneNumberEditText;
    private Spinner mProductNameSpinner;
    private String mProductName = "";
    private EditText mQuantityEditText;
    private EditText mAuthorEditText;
    private EditText mTitleEditText;
    private EditText mYearEditText;
    private EditText mLanguageEditText;
    private EditText mPriceEditText;
    private Spinner mStateSpinner;
    private int mState = 0;
    private Spinner mAvailabilitySpinner;
    private int mAvailability = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneNumberEditText = (EditText) findViewById(R.id.edit_supplier_phone_number);

        mProductNameSpinner = (Spinner) findViewById(R.id.spinner_product_name);
        mQuantityEditText = (EditText) findViewById(R.id.edit_book_quantity);

        mAuthorEditText = (EditText) findViewById(R.id.edit_book_author);
        mTitleEditText = (EditText) findViewById(R.id.edit_book_title);
        mYearEditText = (EditText) findViewById(R.id.edit_book_publication_year);
        mLanguageEditText = (EditText) findViewById(R.id.edit_book_language);
        mPriceEditText = (EditText) findViewById(R.id.edit_book_price);
        mStateSpinner = (Spinner) findViewById(R.id.spinner_state);
        mAvailabilitySpinner = (Spinner) findViewById(R.id.spinner_availability);

        setupProductNameSpinner();
        setupStateSpinner();
        setupAvailabilitySpinner();
    }

    private void setupProductNameSpinner() {
        ArrayAdapter productNameSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_product_name_options, android.R.layout.simple_spinner_item);

        productNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mProductNameSpinner.setAdapter(productNameSpinnerAdapter);

        mProductNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.product_name_kindle_book))) {
                        mProductName = BookEntry.PRODUCT_NAME_KINDLE_BOOK;
                    } else if (selection.equals(getString(R.string.product_name_pocket_book))) {
                        mProductName = BookEntry.PRODUCT_NAME_POCKET_BOOK;
                    } else if (selection.equals(getString(R.string.product_name_album))) {
                        mProductName = BookEntry.PRODUCT_NAME_ALBUM;
                    } else {
                        mProductName = BookEntry.PRODUCT_NAME_STANDARD_BOOK;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mProductName = BookEntry.PRODUCT_NAME_STANDARD_BOOK; // Unknown
            }
        });
    }

    private void setupStateSpinner() {
        ArrayAdapter stateSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_state_options, android.R.layout.simple_spinner_item);

        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mStateSpinner.setAdapter(stateSpinnerAdapter);

        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.state_used))) {
                        mState = BookEntry.STATE_USED;
                    } else if (selection.equals(getString(R.string.state_new))) {
                        mState = BookEntry.STATE_NEW;
                    } else {
                        mState = BookEntry.STATE_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mState = 0; // Unknown
            }
        });
    }

    private void setupAvailabilitySpinner() {
        ArrayAdapter availabilitySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_availability_options, android.R.layout.simple_spinner_item);

        availabilitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mAvailabilitySpinner.setAdapter(availabilitySpinnerAdapter);

        mAvailabilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.availability_in_storage))) {
                        mAvailability = BookEntry.AVAILABILITY_IN_STORAGE;
                    } else if (selection.equals(getString(R.string.availability_in_store))) {
                        mAvailability = BookEntry.AVAILABILITY_IN_STORE;
                    } else {
                        mAvailability = BookEntry.AVAILABILITY_NOT_AVAILABLE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAvailability = 0;
            }
        });
    }

    private void getInputBookEntry() {
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        int supplierPhoneNumberInt = Integer.parseInt(mSupplierPhoneNumberEditText.getText().toString());

        int quantityInt = Integer.parseInt(mQuantityEditText.getText().toString().trim());
        String authorString = mAuthorEditText.getText().toString().trim();
        String titleString = mTitleEditText.getText().toString().trim();
        String languageString = mLanguageEditText.getText().toString().trim();

        int yearInt = Integer.parseInt(mYearEditText.getText().toString().trim());
        int priceInt = Integer.parseInt(mPriceEditText.getText().toString().trim());

        BookDbHelper mDbHelper = new BookDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, supplierPhoneNumberInt);
        values.put(BookEntry.COLUMN_BOOK_PRODUCT_NAME, mProductName);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantityInt);
        values.put(BookEntry.COLUMN_BOOK_AUTHOR, authorString);
        values.put(BookEntry.COLUMN_BOOK_TITLE, titleString);
        values.put(BookEntry.COLUMN_BOOK_LANGUAGE, languageString);
        values.put(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR, yearInt);
        values.put(BookEntry.COLUMN_BOOK_PRICE, priceInt);
        values.put(BookEntry.COLUMN_BOOK_STATE, mState);
        values.put(BookEntry.COLUMN_BOOK_STATE, mAvailability);

        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);
        Log.e("ShelfActivity, ", "User input book inserted with newRowId: " + newRowId);
        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                getInputBookEntry();
                finish();
                return true;
            case R.id.action_delete:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
