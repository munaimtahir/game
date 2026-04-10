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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a\u0016\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00040\u0003\u001a \u0010\u0005\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0007\u001a\u00020\u0002H\u0086@\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2 = {"settingsFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/vexel/offlinearcade/core/model/SettingsState;", "Landroidx/datastore/core/DataStore;", "Landroidx/datastore/preferences/core/Preferences;", "updateSettings", "", "settings", "(Landroidx/datastore/core/DataStore;Lcom/vexel/offlinearcade/core/model/SettingsState;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public final class StorageKt {
    
    @org.jetbrains.annotations.NotNull()
    public static final kotlinx.coroutines.flow.Flow<com.vexel.offlinearcade.core.model.SettingsState> settingsFlow(@org.jetbrains.annotations.NotNull()
    androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> $this$settingsFlow) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.Object updateSettings(@org.jetbrains.annotations.NotNull()
    androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> $this$updateSettings, @org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.model.SettingsState settings, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}