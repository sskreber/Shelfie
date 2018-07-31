package com.example.android.shelfie;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.shelfie.data.BookContract.BookEntry;
import com.facebook.stetho.Stetho;

public class ShelfActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected static final int BOOK_LOADER = 0;
    BookCursorAdapter mBookCursorAdapter;
    private ListView bookListView;
    public static final String LOG_TAG = ShelfActivity.class.getSimpleName();

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
        setEmptyListView();

        mBookCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mBookCursorAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShelfActivity.this, EditingActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                intent.setData(currentBookUri);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    private void setEmptyListView() {
        bookListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shelf, menu);
        return true;
    }

    private void insertDummyBookData() {
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

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_dummy_data:
                insertDummyBookData();
                return true;
            case R.id.action_delete_database_entries:
                deleteAllBookEntries();
                onStart();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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

        return new CursorLoader(this,
                BookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mBookCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBookCursorAdapter.swapCursor(null);
    }

    private void deleteAllBookEntries() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        Log.e(LOG_TAG, rowsDeleted + " " + "rows_deleted_from_db");

        Toast.makeText(this, getString(R.string.editor_delete_all_entries_successful),
                Toast.LENGTH_SHORT).show();
    }
}
