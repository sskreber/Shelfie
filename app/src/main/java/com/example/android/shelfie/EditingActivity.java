package com.example.android.shelfie;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.shelfie.data.BookContract.BookEntry;


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

    private String mSupplierPhoneNumber = "";
    int quantityInt = 0;

    Uri mCurrentBookUri;
    private boolean mBookHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        if (mCurrentBookUri == null) {
            setTitle(getResources().getString(R.string.editing_activity_add_new_book));
            invalidateOptionsMenu();
        } else {
            setTitle(getResources().getString(R.string.editing_activity_edit_existing_new_book));
            // Initialize loader
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

        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);
        mProductNameSpinner.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mAuthorEditText.setOnTouchListener(mTouchListener);
        mTitleEditText.setOnTouchListener(mTouchListener);
        mYearEditText.setOnTouchListener(mTouchListener);
        mLanguageEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mStateSpinner.setOnTouchListener(mTouchListener);
        mAvailabilitySpinner.setOnTouchListener(mTouchListener);

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
        String authorString = mAuthorEditText.getText().toString().trim();
        String titleString = mTitleEditText.getText().toString().trim();
        String languageString = mLanguageEditText.getText().toString().trim();
        String supplierPhoneNumberString = mSupplierPhoneNumberEditText.getText().toString();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String yearString = mYearEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();

        // Check if this is supposed to be a new book & whether the most important required edit fields were left blank
        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(authorString) && TextUtils.isEmpty(titleString) &&
                TextUtils.isEmpty(languageString)) {
            // Since no fields were modified, return to parent activity without creating a new entry.
            return;
        }

        int supplierPhoneNumberInt = setValidIntegerDataFromEditTextString(supplierPhoneNumberString);
        quantityInt = setValidIntegerDataFromEditTextString(quantityString);
        int yearInt = setValidIntegerDataFromEditTextString(yearString);
        int priceInt = setValidIntegerDataFromEditTextString(priceString);

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
                Toast.makeText(this, getString(R.string.editor_edit_book_failure),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_edit_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int setValidIntegerDataFromEditTextString(String EditTextStringToParse) {
        int attributeInteger = 0;
        if (!TextUtils.isEmpty(EditTextStringToParse)) {
            attributeInteger = Integer.parseInt(EditTextStringToParse);
        }
        return attributeInteger;
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.unsaved_changes_discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.unsaved_changes_stay, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Keep editing" button - dismiss dialog, continue editing.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the entry hasn't changed, handle back button press:
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // if entry has changed:
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // If user clicks "Discard" button:
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editing, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
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
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                // If the book entry hasn't changed, go back to parent activity
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditingActivity.this);
                    return true;
                }

                // Dialog to warn user about unsaved changes
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button: navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditingActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // A projection containing all columns from the table
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

        // Loader executes ContentProvider's query method on a background thread
        return new CursorLoader(this,
                mCurrentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Move to the first (and only) row of the cursor (there's only one chosen book entry now), read its data

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
            mSupplierPhoneNumber = (Integer.toString(currentSupplierPhoneNumber));
            mQuantityEditText.setText(Integer.toString(currentQuantity));
            mAuthorEditText.setText(currentAuthor);
            mTitleEditText.setText(currentTitle);
            mYearEditText.setText(Integer.toString(currentYear));
            mLanguageEditText.setText(currentLanguage);
            mPriceEditText.setText(Integer.toString(currentPrice));

            // Set dropdown spinners to existing entry's attribute

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
        // Clear input fields:
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

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteBook() {
        if (mCurrentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    public void orderItemFromSupplier(View view) {
        Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);
        phoneCallIntent.setData(Uri.parse("tel:" + mSupplierPhoneNumber));
        if (phoneCallIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(phoneCallIntent);
        }
    }
    
    public void decreaseProductQuantity(View view) {
        quantityInt = Integer.valueOf(mQuantityEditText.getText().toString());
        if (quantityInt == 0) {
            Toast.makeText(this, R.string.button_quantity_change_failure_msg, Toast.LENGTH_SHORT).show();
            return;
        }
        quantityInt--;
        mQuantityEditText.setText(String.valueOf(quantityInt));
    }

    public void increaseProductQuantity(View view) {
        quantityInt = Integer.valueOf(mQuantityEditText.getText().toString());
        quantityInt++;
        mQuantityEditText.setText(String.valueOf(quantityInt));
    }
}
