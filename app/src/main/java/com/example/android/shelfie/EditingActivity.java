package com.example.android.shelfie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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


public class EditingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_BOOK_LOADER = 0;

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

    Uri mCurrentBookUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        if (mCurrentBookUri == null) {
            setTitle(getResources().getString(R.string.editing_activity_add_new_book));
        } else {
            setTitle(getResources().getString(R.string.editing_activity_edit_existing_new_book));
            // Kick off the loader
            getSupportLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }



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

    private void saveBook() {
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        int supplierPhoneNumberInt = Integer.parseInt(mSupplierPhoneNumberEditText.getText().toString());

        int quantityInt = Integer.parseInt(mQuantityEditText.getText().toString().trim());
        String authorString = mAuthorEditText.getText().toString().trim();
        String titleString = mTitleEditText.getText().toString().trim();
        String languageString = mLanguageEditText.getText().toString().trim();

        int yearInt = Integer.parseInt(mYearEditText.getText().toString().trim());
        int priceInt = Integer.parseInt(mPriceEditText.getText().toString().trim());

        // Check if this is supposed to be a new pet & whether the most important required edit fields were left blank
        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(authorString) && TextUtils.isEmpty(titleString) &&
                TextUtils.isEmpty(languageString)) {
            // Since no fields were modified, we can return early without creating a new entry.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

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
        values.put(BookEntry.COLUMN_BOOK_AVAILABILITY, mAvailability);

        // Check if we're in edit or save new book mode
        if (mCurrentBookUri == null) {
            // save new book:
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_book_failure),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // update existing book:
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_insert_book_failure),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
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
                saveBook();
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {BookEntry._ID,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER,

                BookEntry.COLUMN_BOOK_PRODUCT_NAME,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_PUBLICATION_YEAR,
                BookEntry.COLUMN_BOOK_LANGUAGE,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_STATE,
                BookEntry.COLUMN_BOOK_AVAILABILITY,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBookUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)

        if (cursor.moveToFirst()) {
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER);
            int productNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_AUTHOR);
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
            int yearColumnIndex = cursor.getColumnIndex((BookEntry.COLUMN_BOOK_PUBLICATION_YEAR));
            int languageColumnIndex = cursor.getColumnIndex((BookEntry.COLUMN_BOOK_LANGUAGE));
            int priceColumnIndex = cursor.getColumnIndex((BookEntry.COLUMN_BOOK_PRICE));
            int stateColumnIndex = cursor.getColumnIndex((BookEntry.COLUMN_BOOK_STATE));
            int availabilityColumnIndex = cursor.getColumnIndex((BookEntry.COLUMN_BOOK_AVAILABILITY));

            String currentSupplierName = cursor.getString(supplierNameColumnIndex);
            int currentSupplierPhoneNumber = cursor.getInt(supplierPhoneNumberColumnIndex);
            String currentProductName = cursor.getString(productNameColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            String currentAuthor = cursor.getString(authorColumnIndex);
            String currentTitle = cursor.getString(titleColumnIndex);
            int currentYear = cursor.getInt(yearColumnIndex);
            String currentLanguage = cursor.getString(languageColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentState = cursor.getInt(stateColumnIndex);
            int currentAvailability = cursor.getInt(availabilityColumnIndex);

            mSupplierNameEditText.setText(currentSupplierName);
            mSupplierPhoneNumberEditText.setText(Integer.toString(currentSupplierPhoneNumber));
            mQuantityEditText.setText(Integer.toString(currentQuantity));
            mAuthorEditText.setText(currentAuthor);
            mTitleEditText.setText(currentTitle);
            mYearEditText.setText(Integer.toString(currentYear));
            mLanguageEditText.setText(currentLanguage);
            mPriceEditText.setText(Integer.toString(currentPrice));

            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.

            switch (currentProductName) {
                case BookEntry.PRODUCT_NAME_KINDLE_BOOK:
                    mProductNameSpinner.setSelection(0);
                    break;
                case BookEntry.PRODUCT_NAME_POCKET_BOOK:
                    mProductNameSpinner.setSelection(1);
                    break;
                case BookEntry.PRODUCT_NAME_ALBUM:
                    mProductNameSpinner.setSelection(3);
                    break;
                default:
                    mProductNameSpinner.setSelection(2);
                    break;
            }

            switch (currentState) {
                case BookEntry.STATE_USED:
                    mStateSpinner.setSelection(BookEntry.STATE_USED);
                    break;
                case BookEntry.STATE_NEW:
                    mStateSpinner.setSelection(BookEntry.STATE_NEW);
                    break;
                default:
                    mStateSpinner.setSelection(BookEntry.STATE_UNKNOWN);
                    break;
            }

            switch (currentAvailability) {
                case BookEntry.AVAILABILITY_IN_STORAGE:
                    mAvailabilitySpinner.setSelection(BookEntry.AVAILABILITY_IN_STORAGE);
                    break;
                case BookEntry.AVAILABILITY_IN_STORE:
                    mAvailabilitySpinner.setSelection(BookEntry.AVAILABILITY_IN_STORE);
                    break;
                default:
                    mAvailabilitySpinner.setSelection(BookEntry.AVAILABILITY_NOT_AVAILABLE);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mSupplierNameEditText.setText("");
        mSupplierPhoneNumberEditText.setText("");
        mQuantityEditText.setText("");
        mAuthorEditText.setText("");
        mTitleEditText.setText("");
        mYearEditText.setText("");
        mLanguageEditText.setText("");
        mPriceEditText.setText("");

        mProductNameSpinner.setSelection(2);
        mStateSpinner.setSelection(BookEntry.STATE_UNKNOWN);
        mAvailabilitySpinner.setSelection(BookEntry.AVAILABILITY_NOT_AVAILABLE);
    }
}
