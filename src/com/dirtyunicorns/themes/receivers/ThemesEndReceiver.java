/*
 * Copyright (C) 2020 The Dirty Unicorns Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dirtyunicorns.themes.receivers;

import static android.os.UserHandle.USER_SYSTEM;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_ALARM_END_TIME;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_ALARM_START_TIME;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULE;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULED_END_THEME;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULED_END_THEME_VALUE;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULED_END_TIME;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULED_REPEAT_DAILY;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULED_START_THEME;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULED_START_THEME_VALUE;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULED_START_TIME;
import static com.dirtyunicorns.themes.Schedule.ScheduleFragment.PREF_THEME_SCHEDULED_TOAST;
import static com.dirtyunicorns.themes.utils.Utils.clearAlarms;
import static com.dirtyunicorns.themes.utils.Utils.handleBackgrounds;
import static com.dirtyunicorns.themes.utils.Utils.setEndAlarm;

import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.om.IOverlayManager;
import android.content.SharedPreferences;
import android.os.ServiceManager;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.android.internal.util.cafex.ThemesUtils;
import com.dirtyunicorns.themes.R;

public class ThemesEndReceiver extends BroadcastReceiver {

    private IOverlayManager mOverlayManager;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String scheduledEndThemeValue = mSharedPreferences.getString(PREF_THEME_SCHEDULED_END_THEME_VALUE, null);
        SharedPreferences.Editor sharedPreferencesEditor = mSharedPreferences.edit();

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()) && scheduledEndThemeValue != null) {
            setEndAlarm(context);
        } else if (scheduledEndThemeValue != null) {
            switch (scheduledEndThemeValue) {
                case "1":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.PITCH_BLACK, mOverlayManager);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.SOLARIZED_DARK, mOverlayManager);
                    if (PreferenceManager.getDefaultSharedPreferences(context)
                            .getBoolean(PREF_THEME_SCHEDULED_TOAST, true)) {
                        Toast.makeText(context, context.getString(R.string.theme_type_light) + " "
                                + context.getString(R.string.theme_schedule_applied), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "2":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK, mOverlayManager);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK, mOverlayManager);
                    if (PreferenceManager.getDefaultSharedPreferences(context)
                            .getBoolean(PREF_THEME_SCHEDULED_TOAST, true)) {
                        Toast.makeText(context, context.getString(R.string.theme_type_google_dark) + " "
                                + context.getString(R.string.theme_schedule_applied), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "3":
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK, mOverlayManager);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK, mOverlayManager);
                    if (PreferenceManager.getDefaultSharedPreferences(context)
                            .getBoolean(PREF_THEME_SCHEDULED_TOAST, true)) {
                        Toast.makeText(context, context.getString(R.string.theme_type_pitch_black) + " "
                                + context.getString(R.string.theme_schedule_applied), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "4":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK, mOverlayManager);
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK, mOverlayManager);
                    if (PreferenceManager.getDefaultSharedPreferences(context)
                            .getBoolean(PREF_THEME_SCHEDULED_TOAST, true)) {
                        Toast.makeText(context, context.getString(R.string.theme_type_solarized_dark) + " "
                                + context.getString(R.string.theme_schedule_applied), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            if (!PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(PREF_THEME_SCHEDULED_REPEAT_DAILY, false)) {
                sharedPreferencesEditor.putString(PREF_THEME_SCHEDULE, "1");
                sharedPreferencesEditor.remove(PREF_THEME_SCHEDULED_START_THEME_VALUE);
                sharedPreferencesEditor.remove(PREF_THEME_SCHEDULED_START_THEME);
                sharedPreferencesEditor.remove(PREF_THEME_SCHEDULED_START_TIME);
                sharedPreferencesEditor.remove(PREF_THEME_SCHEDULED_END_THEME_VALUE);
                sharedPreferencesEditor.remove(PREF_THEME_SCHEDULED_END_THEME);
                sharedPreferencesEditor.remove(PREF_THEME_SCHEDULED_END_TIME);
                sharedPreferencesEditor.remove(PREF_THEME_SCHEDULED_REPEAT_DAILY);
                sharedPreferencesEditor.remove(PREF_ALARM_START_TIME);
                sharedPreferencesEditor.remove(PREF_ALARM_END_TIME);
                sharedPreferencesEditor.apply();
                clearAlarms(context);
            }
        }
    }
}
