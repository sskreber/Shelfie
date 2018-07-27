package com.example.android.shelfie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.shelfie.data.BookContract.BookEntry;
import com.example.android.shelfie.data.BookDbHelper;
import com.facebook.stetho.Stetho;

public class ShelfActivity extends AppCompatActivity {

    private BookDbHelper mBookDbHelper;
    private int dataBaseOldVersion = 1;
    private int dataBaseNewVersion = dataBaseOldVersion + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_shelf);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShelfActivity.this, EditingActivity.class);
                startActivity(intent);
            }
        });

        mBookDbHelper = new BookDbHelper(this);
        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    // helper method to check database entries on screen - TODO: delete when frontend complete
    private void displayDatabaseInfo() {
        SQLiteDatabase db = mBookDbHelper.getReadableDatabase();

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

        Cursor cursor = db.query(BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.text_view_book);

        try {
            displayView.setText("The books table contains " + cursor.getCount() + " entries.\n\n");
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " - "
                    + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER + " - "
                    + BookEntry.COLUMN_BOOK_PRODUCT_NAME + " - "
                    + BookEntry.COLUMN_BOOK_QUANTITY + " - "
                    + BookEntry.COLUMN_BOOK_AUTHOR + " - "
                    + BookEntry.COLUMN_BOOK_TITLE + " - "
                    + BookEntry.COLUMN_BOOK_PUBLICATION_YEAR + " - "
                    + BookEntry.COLUMN_BOOK_LANGUAGE + " - "
                    + BookEntry.COLUMN_BOOK_PRICE + " - "
                    + BookEntry.COLUMN_BOOK_STATE + " - "
                    + BookEntry.COLUMN_BOOK_AVAILABILITY + " - "
                    + "\n");

            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
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

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
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

                displayView.append(("\n" + currentID + " - "
                        + currentSupplierName + " - "
                        + currentSupplierPhoneNumber + " - "
                        + currentProductName + " - "
                        + "quantity: " + currentQuantity + " - "
                        + currentAuthor + " - "
                        + currentTitle + " - "
                        + currentYear + " - "
                        + currentLanguage + " - "
                        + currentPrice + " euros - "
                        + "state " + currentState + " - "
                        + currentAvailability));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shelf, menu);
        return true;
    }

    private void insertBook() {
        SQLiteDatabase db = mBookDbHelper.getWritableDatabase();
        dataBaseOldVersion = db.getVersion();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Antique Books Ltd");
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, 3406306891690L);
        values.put(BookEntry.COLUMN_BOOK_PRODUCT_NAME, BookEntry.PRODUCT_NAME_STANDARD_BOOK);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 1);

        values.put(BookEntry.COLUMN_BOOK_AUTHOR, "Labiche");
        values.put(BookEntry.COLUMN_BOOK_TITLE, "Plays I.");
        values.put(BookEntry.COLUMN_BOOK_PUBLICATION_YEAR, 1989);
        values.put(BookEntry.COLUMN_BOOK_LANGUAGE, "French");
        values.put(BookEntry.COLUMN_BOOK_PRICE, 12);
        values.put(BookEntry.COLUMN_BOOK_STATE, BookEntry.STATE_USED);
        values.put(BookEntry.COLUMN_BOOK_AVAILABILITY, BookEntry.AVAILABILITY_IN_STORE);

        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);
        Log.e("ShelfActivity, ", "newRowId: " + newRowId);
    }

    private void deleteAllDatabaseEntries() {
        SQLiteDatabase db = mBookDbHelper.getWritableDatabase();
        dataBaseOldVersion++;
        mBookDbHelper.onUpgrade(db, dataBaseOldVersion, dataBaseNewVersion);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_dummy_data:
                insertBook();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_database_entries:
                deleteAllDatabaseEntries();
                onStart();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
