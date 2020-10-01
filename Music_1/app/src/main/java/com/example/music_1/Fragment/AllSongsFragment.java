package com.example.music_1.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.Model.Song;
import com.example.music_1.MusicDB;
import com.example.music_1.MusicProvider;
import com.example.music_1.R;

public class AllSongsFragment extends BaseSongListFragment implements PopupMenu.OnMenuItemClickListener /* View.OnClickListener, MediaPlaybackService.SongManageListener,MediaPlaybackFragment.SongManageListenerMedia,*/
        /*SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener*//*,MediaPlaybackService.notificationUpdateAllSong*/ {
public AllSongsFragment() {
}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllSongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllSongsFragment newInstance(Boolean isPortrait) {
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        mSongData = new SongData(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void updatePopup(View v, Song song, int pos) {
        mPopup = new android.widget.PopupMenu(getContext(), v.findViewById(R.id.popup_button));
        mPopup.getMenuInflater().inflate(R.menu.popup_menu, mPopup.getMenu());

        // Set a listener so we are notified if a menu item is clicked
        mPopup.setOnMenuItemClickListener(this);

        mPopup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
//        MusicDB favoriteSongsDB = new MusicDB(getContext());
//        Song song = (Song) mAdapter().get(mPosition);
//        int id = song.getmId();
//        switch (item.getItemId()) {
//            case R.id.popup_remove:
//                favoriteSongsDB.setFavorite(id, 1);
//                favoriteSongsDB.updateCount(id, 0);
//                mIsFavorite = false;
//                Toast.makeText(getContext(), R.string.remove_favorite, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.popup_add:
//                favoriteSongsDB.setFavorite(id, 2);
//                mIsFavorite = true;
//                Toast.makeText(getContext(), R.string.add_to_favorite, Toast.LENGTH_SHORT).show();
//                break;
//        }
//        song.setmIsFavorite(mIsFavorite);
//        mFavoriteControl.updateUI(mIsFavorite);
//        updateAdapter();
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}