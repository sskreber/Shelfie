package com.example.android.shelfie;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.shelfie.data.BookContract;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView bookAuthorTextView = (TextView) view.findViewById(R.id.book_author);
        TextView bookTitleTextView = (TextView) view.findViewById(R.id.book_title);

        int authorNameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_AUTHOR);
        int titleColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE);

        String bookAuthor = cursor.getString(authorNameColumnIndex);
        String bookTitle = cursor.getString(titleColumnIndex);

        bookAuthorTextView.setText(bookAuthor);
        bookTitleTextView.setText(bookTitle);
    }
}