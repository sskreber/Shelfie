package com.example.android.shelfie.data;

import android.provider.BaseColumns;

public final class BookContract {

    public BookContract() {
    }

    public static abstract class BookEntry implements BaseColumns {

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
        public static final String COLUMN_BOOK_CATEGORY = "category";
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

        public static final String CATEGORY_UNSPECIFIED = "unspecified";
        public static final String CATEGORY_ARTS = "arts";
        public static final String CATEGORY_BIOGRAPHY = "biography";
        public static final String CATEGORY_COMICS = "comics";
        public static final String CATEGORY_CRIME = "crime";
        public static final String CATEGORY_DRAMA = "drama";
        public static final String CATEGORY_EROTICA = "erotica";
        public static final String CATEGORY_HISTORY = "history";
        public static final String CATEGORY_HORROR = "horror";
        public static final String CATEGORY_HUMOUR = "humour";
        public static final String CATEGORY_LINGUISTICS = "linguistics";
        public static final String CATEGORY_MYSTERY = "mystery";
        public static final String CATEGORY_POETRY = "poetry";
        public static final String CATEGORY_SCIENCE = "science";
        public static final String CATEGORY_SCI_FI = "sci-fi";
        public static final String CATEGORY_THRILLER = "thriller";
        public static final String CATEGORY_YOUNG_ADULT = "young adult";
    }
}
