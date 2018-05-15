package com.zype.android.core.provider.helpers;

import com.google.gson.Gson;

import com.zype.android.core.provider.Contract;
import com.zype.android.core.provider.CursorHelper;
import com.zype.android.webapi.model.playlist.PlaylistData;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

/**
 * @author vasya
 * @version 1
 *          date 7/8/15
 */
public class PlaylistHelper {

    @NonNull
    public static ContentValues objectToContentValues(@NonNull PlaylistData playlistData) {
        final ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.Playlist.COLUMN_ID, playlistData.getId());
        contentValues.put(Contract.Playlist.COLUMN_CREATED_AT, playlistData.getCreatedAt());
        contentValues.put(Contract.Playlist.COLUMN_TITLE, playlistData.getTitle());
        contentValues.put(Contract.Playlist.COLUMN_PRIORITY, playlistData.getPriority());
        contentValues.put(Contract.Playlist.COLUMN_PARENT_ID, playlistData.getParentId());
        contentValues.put(Contract.Playlist.COLUMN_THUMBNAILS, new Gson().toJson(playlistData.getThumbnails()));
        contentValues.put(Contract.Playlist.COLUMN_PLAYLIST_ITEM_COUNT, playlistData.getPlaylistItemCount() );
        contentValues.put(Contract.Playlist.COLUMN_IMAGES, new Gson().toJson(playlistData.getImages()));

        return contentValues;
    }

    public static String getNextVideoId(String currentVideoId, Cursor playlistCursor) {
        String result = null;
        boolean nextVideoFound = false;
        if (playlistCursor != null) {
            if (playlistCursor.moveToFirst()) {
                do {
                    if (nextVideoFound) {
                        result = playlistCursor.getString(playlistCursor.getColumnIndex(Contract.PlaylistVideo.VIDEO_ID));
                        break;
                    }
                    if (playlistCursor.getString(playlistCursor.getColumnIndex(Contract.PlaylistVideo.VIDEO_ID)).equals(currentVideoId)) {
                        nextVideoFound = true;
                    }
                } while (playlistCursor.moveToNext());
            }
            playlistCursor.close();
        }
        return result;
    }
}
