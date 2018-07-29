package com.example.android.shelfie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.android.shelfie.EditingActivity;
import com.example.android.shelfie.R;

public class BookProvider extends ContentProvider {

    private static final int BOOKS = 100;

    private static final int BOOK_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_ID);
    }

    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    // Database helper object
    private BookDbHelper mBookDbHelper;

    @Override
    public boolean onCreate() {
        mBookDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mBookDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(BookContract.BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // set notification URI on the cursor so we know what content URI the cursor was created for.
        // if data at this URI changes, we'll know we need to update the cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertBook(Uri uri, ContentValues values) {
        validateInputData(values);

        SQLiteDatabase database = mBookDbHelper.getWritableDatabase();
        long id = database.insert(BookContract.BookEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // notify all listeners that the data has changed for the ...books/books book content uri
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private void validateInputData(ContentValues values) {
        validateInputAuthor(values);
        validateInputTitle(values);
        validateInputLanguage(values);
        validateInputQuantity(values);
        validateInputPublicationYear(values);
        validateInputPrice(values);
        validateInputProductName(values);
        validateInputState(values);
        validateInputAvailability(values);
    }

    private void validateInputAuthor(ContentValues values) {
        String author = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_AUTHOR);
        if (author == null) {
            throw new IllegalArgumentException("Book requires an author");
        }
    }

    private void validateInputTitle(ContentValues values) {
        String title = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Book requires a title");
        }
    }

    private void validateInputLanguage(ContentValues values) {
        String language = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_LANGUAGE);
        if (language == null) {
            throw new IllegalArgumentException("Book requires a language");
        }
    }

    private void validateInputQuantity(ContentValues values) {
        Integer quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Book requires valid quantity");
        }
    }

    // app doesn't accept books dating back earlier than 1600
    private void validateInputPublicationYear(ContentValues values) {
        Integer publicationYear = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_PUBLICATION_YEAR);
        if (publicationYear != null && publicationYear != 0 && publicationYear <= 1600) {
            throw new IllegalArgumentException("Book requires valid publication year");
        }
    }

    private void validateInputPrice(ContentValues values) {
        Integer price = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Book requires valid price");
        }
    }

    private void validateInputProductName(ContentValues values) {
        String productName = values.getAsString(BookContract.BookEntry.COLUMN_BOOK_PRODUCT_NAME);
        if (productName != null && !BookContract.BookEntry.isValidProductName(productName)) {
            throw new IllegalArgumentException("Book requires valid product name");
        }
    }

    private void validateInputState(ContentValues values) {
        Integer state = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_STATE);
        if (state != null && !BookContract.BookEntry.isValidState(state)) {
            throw new IllegalArgumentException("Book requires valid state");
        }
    }

    private void validateInputAvailability(ContentValues values) {
        Integer availability = values.getAsInteger(BookContract.BookEntry.COLUMN_BOOK_AVAILABILITY);
        if (availability == null || !BookContract.BookEntry.isValidAvailability(availability)) {
            throw new IllegalArgumentException("Book requires valid availability");
        }
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update not supported for " + uri);
        }

    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_AUTHOR)) {
            validateInputAuthor(values);
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_TITLE)) {
            validateInputTitle(values);
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_LANGUAGE)) {
            validateInputLanguage(values);
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_QUANTITY)) {
            validateInputQuantity(values);
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_PUBLICATION_YEAR)) {
            validateInputPublicationYear(values);
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_PRICE)) {
            validateInputPrice(values);
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_PRODUCT_NAME)) {
            validateInputProductName(values);
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_STATE)) {
            validateInputState(values);
        }
        if (values.containsKey(BookContract.BookEntry.COLUMN_BOOK_AVAILABILITY)) {
            validateInputAvailability(values);
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mBookDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update statement
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mBookDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
