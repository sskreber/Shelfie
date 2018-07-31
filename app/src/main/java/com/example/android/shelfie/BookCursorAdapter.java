package com.example.android.shelfie;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shelfie.data.BookContract;

import java.util.Currency;
import java.util.Locale;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context contextParam, Cursor cursor) {
        LinearLayout listViewItem = (LinearLayout) view.findViewById(R.id.layout_list_item);
        TextView bookAuthorTextView = (TextView) view.findViewById(R.id.book_author);
        TextView bookTitleTextView = (TextView) view.findViewById(R.id.book_title);
        final Context context = contextParam;
        TextView productNameTextView = (TextView) view.findViewById(R.id.book_product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.book_price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.book_quantity);
        String euro = Currency.getInstance(Locale.FRANCE).getCurrencyCode();

        int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
        int authorNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_AUTHOR);
        int titleColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE);
        int productNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);

        final int itemId = cursor.getInt(idColumnIndex);
        String bookAuthor = cursor.getString(authorNameColumnIndex);
        String bookTitle = cursor.getString(titleColumnIndex);
        String bookProductName = cursor.getString(productNameColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        String bookQuantity = cursor.getString(quantityColumnIndex);
        final Integer bookQuantityInt = Integer.parseInt(bookQuantity);

        bookAuthorTextView.setText(bookAuthor);
        bookTitleTextView.setText(bookTitle);
        productNameTextView.setText(bookProductName);
        priceTextView.setText(bookPrice + " " + euro);
        quantityTextView.setText(bookQuantity);

        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditingActivity.class);
                Uri currentInventoryUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, itemId);
                intent.setData(currentInventoryUri);
                context.startActivity(intent);
            }
        });

        Button saleButton = (Button) view.findViewById(R.id.button_sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bookQuantityForButtonPress = 0;
                if (bookQuantityInt == null) {
                    return;
                } else {
                    bookQuantityForButtonPress = bookQuantityInt;
                }

                if (bookQuantityForButtonPress == 0) {
                    Toast.makeText(context, R.string.button_sale_item_unavailable_msg, Toast.LENGTH_SHORT).show();
                } else if (bookQuantityForButtonPress > 0) {
                    bookQuantityForButtonPress--;
                    String newQuantityString = Integer.toString(bookQuantityForButtonPress);

                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, newQuantityString);

                    Uri currentInventoryUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, itemId);
                    int rowsAffected = context.getContentResolver().update(currentInventoryUri, values, null, null);

                    if (rowsAffected != 0) {
                        quantityTextView.setText(String.valueOf(bookQuantityForButtonPress));
                    } else {
                        Toast.makeText(context, R.string.button_sale_failure_msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
