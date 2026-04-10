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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0019\b\u0087\b\u0018\u00002\u00020\u0001BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0011JL\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000bH\u00c6\u0001\u00a2\u0006\u0002\u0010\u001fJ\u0013\u0010 \u001a\u00020\u00062\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00d6\u0001J\t\u0010#\u001a\u00020\bH\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0015\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006$"}, d2 = {"Lcom/vexel/offlinearcade/core/data/PlayerProfileEntity;", "", "profileId", "", "coins", "premiumUnlocked", "", "selectedThemeId", "", "currentStreakDays", "lastPlayedEpochDay", "", "(IIZLjava/lang/String;ILjava/lang/Long;)V", "getCoins", "()I", "getCurrentStreakDays", "getLastPlayedEpochDay", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getPremiumUnlocked", "()Z", "getProfileId", "getSelectedThemeId", "()Ljava/lang/String;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(IIZLjava/lang/String;ILjava/lang/Long;)Lcom/vexel/offlinearcade/core/data/PlayerProfileEntity;", "equals", "other", "hashCode", "toString", "data_debug"})
@androidx.room.Entity(tableName = "player_profile")
public final class PlayerProfileEntity {
    @androidx.room.PrimaryKey()
    private final int profileId = 0;
    private final int coins = 0;
    private final boolean premiumUnlocked = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String selectedThemeId = null;
    private final int currentStreakDays = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long lastPlayedEpochDay = null;
    
    public PlayerProfileEntity(int profileId, int coins, boolean premiumUnlocked, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedThemeId, int currentStreakDays, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastPlayedEpochDay) {
        super();
    }
    
    public final int getProfileId() {
        return 0;
    }
    
    public final int getCoins() {
        return 0;
    }
    
    public final boolean getPremiumUnlocked() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSelectedThemeId() {
        return null;
    }
    
    public final int getCurrentStreakDays() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getLastPlayedEpochDay() {
        return null;
    }
    
    public PlayerProfileEntity() {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.vexel.offlinearcade.core.data.PlayerProfileEntity copy(int profileId, int coins, boolean premiumUnlocked, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedThemeId, int currentStreakDays, @org.jetbrains.annotations.Nullable()
    java.lang.Long lastPlayedEpochDay) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}