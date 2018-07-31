package com.example.android.shelfie.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.shelfie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "books";
    public BookContract() {
    }

    public static abstract class BookEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_BOOK_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
        public static final String COLUMN_BOOK_PRODUCT_NAME = "product_name";
        public static final String COLUMN_BOOK_QUANTITY = "quantity";
        public static final String COLUMN_BOOK_AUTHOR = "author";
        public static final String COLUMN_BOOK_TITLE = "title";
        public static final String COLUMN_BOOK_PUBLICATION_YEAR = "publication_year";
        public static final String COLUMN_BOOK_LANGUAGE = "language";
        public static final String COLUMN_BOOK_PRICE = "price";
        public static final String COLUMN_BOOK_STATE = "state";
        public static final String COLUMN_BOOK_AVAILABILITY = "availability";

        public static final String PRODUCT_NAME_KINDLE_BOOK = "kindle book";
        public static final String PRODUCT_NAME_POCKET_BOOK = "pocket book";
        public static final String PRODUCT_NAME_STANDARD_BOOK = "standard book";
        public static final String PRODUCT_NAME_ALBUM = "album";

        public static final int STATE_UNKNOWN = 0;
        public static final int STATE_USED = 1;
        public static final int STATE_NEW = 2;

        public static final int AVAILABILITY_NOT_AVAILABLE = 0;
        public static final int AVAILABILITY_IN_STORAGE = 1;
        public static final int AVAILABILITY_IN_STORE = 2;

        public static boolean isValidProductName(String productName) {
            if (productName.equals(PRODUCT_NAME_KINDLE_BOOK) ||
                    productName.equals(PRODUCT_NAME_POCKET_BOOK) ||
                    productName.equals(PRODUCT_NAME_STANDARD_BOOK) ||
                    productName.equals(PRODUCT_NAME_ALBUM)) {
                return true;
            }
            return false;
        }

        public static boolean isValidState(int state) {
            if (state == STATE_UNKNOWN || state == STATE_USED || state == STATE_NEW) {
                return true;
            }
            return false;
        }

        public static boolean isValidAvailability(int availability) {
            if (availability == AVAILABILITY_NOT_AVAILABLE ||
                    availability == AVAILABILITY_IN_STORAGE ||
                    availability == AVAILABILITY_IN_STORE) {
                return true;
            }
            return false;
        }
    }
}
