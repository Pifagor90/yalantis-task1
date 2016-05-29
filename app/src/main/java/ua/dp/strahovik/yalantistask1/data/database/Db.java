package ua.dp.strahovik.yalantistask1.data.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.Profile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.entities.Address;
import ua.dp.strahovik.yalantistask1.entities.Company;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.entities.EventState;
import ua.dp.strahovik.yalantistask1.entities.helpers.MutableFbAccessToken;

public class Db {

    public Db() {
    }

    public abstract static class EventsTable {
        public static final String TABLE_NAME = "events";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ID_FOR_OUTPUT = "id_for_output";
        /*
        Todo: mb event_state should be normalized i.e. thrown to another table, filled at creation of db
        and joined afterwards
        */
        public static final String COLUMN_EVENT_STATE_ID = "event_state_id";
        public static final String COLUMN_EVENT_STATE_STATE = "event_state_state";
        public static final String COLUMN_CREATION_DATE = "creation_date";
        public static final String COLUMN_REGISTRATION_DATE = "registration_date";
        public static final String COLUMN_DEADLINE_DATE = "deadline_date";
        public static final String COLUMN_RESPONSIBLE_COMPANY = "responsible_company";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_EVENT_TYPE = "event_type";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_ADDRESS_CITY = "address_city";
        public static final String COLUMN_ADDRESS_DISTRICT = "address_district";
        public static final String COLUMN_ADDRESS_STREET_TYPE_NAME = "address_street_type_name";
        public static final String COLUMN_ADDRESS_STREET_TYPE_SHORT_NAME = "address_street_type_short_name";
        public static final String COLUMN_ADDRESS_STREET = "address_street";
        public static final String COLUMN_ADDRESS_HOUSE = "address_house";
        public static final String COLUMN_ADDRESS_FLAT = "address_flat";

        public static final String CREATE = (
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_ID_FOR_OUTPUT + " TEXT, " +
                        COLUMN_EVENT_STATE_ID + " INTEGER NOT NULL, " +
                        COLUMN_EVENT_STATE_STATE + " TEXT, " +
                        COLUMN_CREATION_DATE + " INTEGER NOT NULL, " +
                        COLUMN_REGISTRATION_DATE + " INTEGER, " +
                        COLUMN_DEADLINE_DATE + " INTEGER, " +
                        COLUMN_RESPONSIBLE_COMPANY + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_EVENT_TYPE + " TEXT, " +
                        COLUMN_LIKES + " TEXT, " +
                        COLUMN_ADDRESS_CITY + " TEXT, " +
                        COLUMN_ADDRESS_DISTRICT + " TEXT, " +
                        COLUMN_ADDRESS_STREET_TYPE_NAME + " TEXT, " +
                        COLUMN_ADDRESS_STREET_TYPE_SHORT_NAME + " TEXT, " +
                        COLUMN_ADDRESS_STREET + " TEXT, " +
                        COLUMN_ADDRESS_HOUSE + " TEXT, " +
                        COLUMN_ADDRESS_FLAT + " TEXT" +
                        " ); "
        );

        public static ContentValues toContentValues(Event event) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, event.getId());
            values.put(COLUMN_ID_FOR_OUTPUT, event.getIdForOutput());
            values.put(COLUMN_EVENT_STATE_ID, event.getEventState().getStateId());
            values.put(COLUMN_EVENT_STATE_STATE, event.getEventState().getState());
            if (event.getCreationDate() != null) {
                values.put(COLUMN_CREATION_DATE, event.getCreationDate().getTime());
            }
            if (event.getRegistrationDate() != null) {
                values.put(COLUMN_REGISTRATION_DATE, event.getRegistrationDate().getTime());
            }
            if (event.getDeadlineDate() != null) {
                values.put(COLUMN_DEADLINE_DATE, event.getDeadlineDate().getTime());
            }
            if (event.getResponsible() != null) {
                values.put(COLUMN_RESPONSIBLE_COMPANY, event.getResponsible().getName());
            }
            values.put(COLUMN_DESCRIPTION, event.getDescription());
            values.put(COLUMN_EVENT_TYPE, event.getEventType());
            values.put(COLUMN_LIKES, event.getLikeCounter());
            if (event.getAddress() != null) {
                Address address = event.getAddress();
                values.put(COLUMN_ADDRESS_CITY, address.getCity());
                values.put(COLUMN_ADDRESS_DISTRICT, address.getDistrict());
                values.put(COLUMN_ADDRESS_STREET_TYPE_NAME, address.getStreetTypeName());
                values.put(COLUMN_ADDRESS_STREET_TYPE_SHORT_NAME, address.getStreetTypeShortName());
                values.put(COLUMN_ADDRESS_STREET, address.getStreet());
                values.put(COLUMN_ADDRESS_HOUSE, address.getHouse());
                values.put(COLUMN_ADDRESS_FLAT, address.getFlat());
            }
            return values;
        }

        public static Event parseCursor(Cursor cursor) {
            Event event = new Event();
            event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            event.setIdForOutput(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            EventState eventState = new EventState();
            event.setEventState(eventState);
            eventState.setStateId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATE_ID)));
            eventState.setState(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATE_STATE)));
            event.setCreationDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATION_DATE))));
            event.setRegistrationDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REGISTRATION_DATE))));
            event.setDeadlineDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE_DATE))));
            event.setResponsible(new Company(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESPONSIBLE_COMPANY))));
            event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            event.setEventType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TYPE)));
            event.setLikeCounter(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LIKES)));
            Address address = new Address();
            event.setAddress(address);
            address.setCity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_CITY)));
            address.setDistrict(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_DISTRICT)));
            address.setStreetTypeName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_STREET_TYPE_NAME)));
            address.setStreetTypeShortName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_STREET_TYPE_SHORT_NAME)));
            address.setStreet(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_STREET)));
            address.setHouse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_HOUSE)));
            address.setFlat(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_FLAT)));
            return event;
        }
    }

    public abstract static class EventsImagesTable {
        public static final String TABLE_NAME = "events_images";

        public static final String COLUMN_EVENT_ID = "event_id";
        public static final String COLUMN_IMAGE_URL = "image_url";

        public static final String CREATE = (
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_EVENT_ID + " TEXT, " +
                        COLUMN_IMAGE_URL + " TEXT, " +
                        "FOREIGN KEY(" + COLUMN_EVENT_ID + ") REFERENCES " + EventsTable.TABLE_NAME +
                        "(" + EventsTable.COLUMN_ID + ")" + " ON DELETE CASCADE" + " ON UPDATE CASCADE" +
                        " ); "
        );

        public static ContentValues toContentValues(int id, URI uri) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EVENT_ID, id);
            values.put(COLUMN_IMAGE_URL, uri.toString());
            return values;
        }

        public static Event parseCursor(Cursor cursor, Event event, Context context) {
            ArrayList<URI> photos = new ArrayList<>();
            while (cursor.moveToNext()) {
                try {
                    photos.add(new URI(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))));
                } catch (URISyntaxException e) {
                    Log.e(context.getString(R.string.log_tag),
                            context.getString(R.string.error_uri_exception) + e);
                }
            }
            event.setPhotos(photos);
            return event;
        }
    }

    public abstract static class FbProfileTable {
        public static final String TABLE_NAME = "fb_profile";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_MIDDLE_NAME = "middle_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LINK_URI = "link_uri";

        public static final String CREATE = (
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " TEXT PRIMARY KEY, " +
                        COLUMN_FIRST_NAME + " TEXT, " +
                        COLUMN_MIDDLE_NAME + " TEXT, " +
                        COLUMN_LAST_NAME + " TEXT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_LINK_URI + " TEXT" +
                        " ); "
        );

        public static ContentValues toContentValues(Profile profile) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, profile.getId());
            values.put(COLUMN_FIRST_NAME, profile.getFirstName());
            values.put(COLUMN_MIDDLE_NAME, profile.getMiddleName());
            values.put(COLUMN_LAST_NAME, profile.getLastName());
            values.put(COLUMN_NAME, profile.getName());
            values.put(COLUMN_LINK_URI, profile.getLinkUri().toString());
            return values;
        }

        public static Profile parseCursor(Cursor cursor) {
            return new Profile(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MIDDLE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK_URI)) == null ? null :
                            Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LINK_URI)))
            );
        }

    }

    public abstract static class FbAccessTokenTable {
        public static final String TABLE_NAME = "fb_access_token";

        public static final String COLUMN_TOKEN = "token";
        public static final String COLUMN_ACCOUNT_ID = "account_id";
        public static final String COLUMN_EXPIRES_IN = "expires_in";
        public static final String COLUMN_LAST_REFRESH_TIME = "last_refresh_time";
        public static final String COLUMN_ACCESS_TOKEN_SOURCE = "access_token_source";


        public static final String CREATE = (
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_TOKEN + " TEXT PRIMARY KEY, " +
                        COLUMN_ACCOUNT_ID + " TEXT, " +
                        COLUMN_EXPIRES_IN + " INTEGER, " +
                        COLUMN_LAST_REFRESH_TIME + " INTEGER, " +
                        COLUMN_ACCESS_TOKEN_SOURCE + " INTEGER" +
                        " ); "
        );

        public static ContentValues toContentValues(AccessToken accessToken) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TOKEN, accessToken.getToken());
            values.put(COLUMN_ACCOUNT_ID, accessToken.getUserId());
            values.put(COLUMN_EXPIRES_IN, accessToken.getExpires().getTime());
            values.put(COLUMN_LAST_REFRESH_TIME, accessToken.getLastRefresh().getTime());
            values.put(COLUMN_ACCESS_TOKEN_SOURCE, accessToken.getSource().ordinal());
            return values;
        }

        public static MutableFbAccessToken parseCursor(Cursor cursor, Context context) {
            MutableFbAccessToken token = new MutableFbAccessToken();
            token.setApplicationId(context.getString(R.string.facebook_app_id));
            token.setToken(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOKEN)));
            token.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_ID)));
            token.setExpires(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_EXPIRES_IN))));
            token.setLastRefresh(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_REFRESH_TIME))));
            token.setSource(AccessTokenSource.values()[cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACCESS_TOKEN_SOURCE))]);
            return token;
        }
    }

    public abstract static class FbAccessTokenPermissionsTable {
        public static final String TABLE_NAME = "fb_access_token_permissions";

        public static final String COLUMN_TOKEN = "token";
        public static final String COLUMN_PERMISSION = "permission";
        public static final String COLUMN_IS_DECLINED = "is_declined";

        public static final int IS_DECLINED_TRUE = 1;
        public static final int IS_DECLINED_FALSE = 0;


        public static final String CREATE = (
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_TOKEN + " TEXT PRIMARY KEY, " +
                        COLUMN_PERMISSION + " TEXT, " +
                        COLUMN_IS_DECLINED + " INTEGER" +
                        " ); "
        );

        public static ContentValues toContentValues(AccessToken accessToken, String permission, int isDeclined) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TOKEN, accessToken.getToken());
            values.put(COLUMN_PERMISSION, permission);
            values.put(COLUMN_IS_DECLINED, isDeclined);
            return values;
        }

        public static MutableFbAccessToken parseCursor(Cursor cursor, MutableFbAccessToken token) {
            Set<String> permissions = new HashSet<>();
            Set<String> declinedPermissions = new HashSet<>();
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_DECLINED)) == IS_DECLINED_TRUE) {
                    declinedPermissions.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PERMISSION)));
                } else {
                    permissions.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PERMISSION)));
                }
            }
            token.setPermissions(permissions);
            token.setDeclinedPermissions(declinedPermissions);
            return token;
        }
    }

    public abstract static class FbMePhotosTable {
        public static final String TABLE_NAME = "fb_me_photos_table";

        public static final String COLUMN_URI = "uri";

        public static final String CREATE = (
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_URI + " TEXT PRIMARY KEY" +
                        " ); "
        );

        public static ContentValues toContentValues(String uri) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_URI, uri);
            return values;
        }

        public static String parseCursor(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URI));
        }
    }


}