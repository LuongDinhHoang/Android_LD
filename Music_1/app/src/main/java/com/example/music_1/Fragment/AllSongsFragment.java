package com.example.music_1.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.example.music_1.Model.Song;
import com.example.music_1.R;

public class AllSongsFragment extends BaseSongListFragment /* View.OnClickListener, MediaPlaybackService.SongManageListener,MediaPlaybackFragment.SongManageListenerMedia,*/
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
        PopupMenu popup = new PopupMenu(v.getContext(), v);             //gán menu_popup  khi click vào các option
        // Inflate the Popup using XML file.
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                             @Override
                                             public boolean onMenuItemClick(MenuItem item) {                      //setClick cho option menu
                                                 Toast.makeText(getActivity(),
                                                         "item Click", Toast.LENGTH_SHORT).show();
                                                 return false;

                                             }
                                         }
        );
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