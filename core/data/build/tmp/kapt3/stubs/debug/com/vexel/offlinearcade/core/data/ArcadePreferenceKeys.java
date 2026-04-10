package com.vexel.offlinearcade.core.data;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import com.vexel.offlinearcade.core.model.SettingsState;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007\u00a8\u0006\f"}, d2 = {"Lcom/vexel/offlinearcade/core/data/ArcadePreferenceKeys;", "", "()V", "musicEnabled", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "getMusicEnabled", "()Landroidx/datastore/preferences/core/Preferences$Key;", "soundEnabled", "getSoundEnabled", "vibrationEnabled", "getVibrationEnabled", "data_debug"})
public final class ArcadePreferenceKeys {
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> soundEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> musicEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> vibrationEnabled = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.vexel.offlinearcade.core.data.ArcadePreferenceKeys INSTANCE = null;
    
    private ArcadePreferenceKeys() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getSoundEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getMusicEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.datastore.preferences.core.Preferences.Key<java.lang.Boolean> getVibrationEnabled() {
        return null;
    }
}