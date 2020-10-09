package com.example.music_1.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music_1.Adapter.SongAdapter;
import com.example.music_1.Model.Song;
import com.example.music_1.MusicDB;
import com.example.music_1.MusicProvider;
import com.example.music_1.R;

import java.util.ArrayList;


public class FavoritesFragment extends BaseSongListFragment {

    public FavoritesFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllSongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(Boolean isPortrait) {
        FavoritesFragment fragment1 = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment1.setArguments(args);
        return fragment1;
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
        PopupMenu popup = new PopupMenu(v.getContext(), v);             //gán menu_popup  khi click vào các option
        // Inflate the Popup using XML file.
        final int id = (int) song.getSongID();
        final Uri uri = Uri.parse(MusicProvider.CONTENT_URI + "/" + id);
        final Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        popup.getMenuInflater().inflate(R.menu.popup_menu_delete, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                             @Override
                                             public boolean onMenuItemClick(MenuItem item) {                      //setClick cho option menu
                                                 ContentValues values = new ContentValues();
                                                 values.put(MusicDB.IS_FAVORITE, 0);
                                                 if (item.getItemId() == R.id.action_delete_songs) {
                                                     values.put(MusicDB.IS_FAVORITE, 0);
                                                     if (cursor != null) {
                                                         getContext().getContentResolver().delete(MusicProvider.CONTENT_URI, MusicDB.ID+"="+id, null);

                                                     }
                                                     Toast.makeText(getActivity().getApplicationContext(), "Remove Favorite", Toast.LENGTH_SHORT).show();
                                                     UpdateFragment();
                                                 }

                                                 return false;

                                             }
                                         }
        );
    }
    public  void UpdateFragment()
    {
        mList=Song.getSongFavorite(getContext());
        mAdapter.setList(mList);
        mAdapter.notifyDataSetChanged();
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
    protected void absSetFavorite() {
        mMediaPlaybackService.setFavorite(true);

    }

//    @Override
//    public void updateAdapter() {
//
//        mList=Song.getSongFavorite(getContext());
//        mAdapter = new SongAdapter(getContext(), mList);
//        mRecycle.setAdapter(mAdapter);
//
//    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}