package com.example.nb.androidfinalproject.bottom_navigation_fragments;

import com.example.nb.androidfinalproject.R;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Switch;
import androidx.fragment.app.DialogFragment;

public class SettingsFragment extends DialogFragment {

    //Notification
    private AlarmManager alarmManager;
    private String movieId;

    private final String ACTION_GENRE = "action_genre";
    private final String ADBENTURE_GENRE = "adventure_genre";
    private final String ANIMATION_GENRE = "animation_genre";
    private final String COMEDY_GENRE = "comedy_genre";
    private final String FAMILY_GENRE = "family_genre";
    private final String FANTASY_GENRE = "fantasy_genre";
    private final String HISTORY_GENRE = "history genre";
    private final String ROMANCE_GENRE = "romance_genre";
    private final String WAR_GENRE = "war_genre";

    /*/Settings/*/
    private SharedPreferences sp;
    private RelativeLayout notifLayout;
    private Switch switchBtn;
    private CheckBox action_cb, adventure_cb, animation_cb, comedy_cb, family_cb;
    private CheckBox fantasy_cb, history_cb, romance_cb, war_cb;
    private Button saveBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sp = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        switchBtn = rootView.findViewById(R.id.switch_btn);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchBtn.isChecked())
                    notifLayout.setVisibility(View.VISIBLE);
                else
                    notifLayout.setVisibility(View.INVISIBLE);
            }
        });

        action_cb = rootView.findViewById(R.id.action_cb);
        adventure_cb = rootView.findViewById(R.id.adventure_cb);
        animation_cb = rootView.findViewById(R.id.animation_cb);
        comedy_cb = rootView.findViewById(R.id.comedy_cb);
        family_cb = rootView.findViewById(R.id.family_cb);

        fantasy_cb = rootView.findViewById(R.id.fantasy_cb);
        history_cb = rootView.findViewById(R.id.history_cb);
        romance_cb = rootView.findViewById(R.id.romance_cb);
        war_cb = rootView.findViewById(R.id.war_cb);

        notifLayout = rootView.findViewById(R.id.notification_layout);

        saveBtn = rootView.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        loadSettings();

        return rootView;
    }

    private void loadSettings() {
        boolean switchStateBtn = sp.getBoolean("switch_state", false);
        switchBtn.setChecked(switchStateBtn);

        if(switchStateBtn)
            notifLayout.setVisibility(View.VISIBLE);
        else
            notifLayout.setVisibility(View.INVISIBLE);

        action_cb.setChecked(sp.getBoolean("action_cb", false));
        adventure_cb.setChecked(sp.getBoolean("adventure_cb", false));
        animation_cb.setChecked(sp.getBoolean("animation_cb", false));
        comedy_cb.setChecked(sp.getBoolean("comedy_cb", false));
        family_cb.setChecked(sp.getBoolean("family_cb", false));

        fantasy_cb.setChecked(sp.getBoolean("fantasy_cb", false));
        history_cb.setChecked(sp.getBoolean("history_cb", false));
        romance_cb.setChecked(sp.getBoolean("romance_cb", false));
        war_cb.setChecked(sp.getBoolean("war_cb", false));
    }

    private void saveSettings(){

        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("switch_state", switchBtn.isChecked());

        editor.putBoolean("action_cb", action_cb.isChecked());
        editor.putBoolean("adventure_cb", adventure_cb.isChecked());
        editor.putBoolean("animation_cb", animation_cb.isChecked());
        editor.putBoolean("comedy_cb", comedy_cb.isChecked());
        editor.putBoolean("family_cb", family_cb.isChecked());

        editor.putBoolean("fantasy_cb", fantasy_cb.isChecked());
        editor.putBoolean("history_cb", history_cb.isChecked());
        editor.putBoolean("romance_cb", romance_cb.isChecked());
        editor.putBoolean("war_cb", war_cb.isChecked());

        editor.commit();

        dismiss();

    }

    private void pushNotification(){




    }

}