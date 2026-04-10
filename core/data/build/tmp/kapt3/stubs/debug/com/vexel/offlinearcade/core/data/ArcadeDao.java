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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\f\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\fH\'J\u0010\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\bH\'J\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\t0\bH\'J\u0014\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\t0\bH\'J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u001e\u00a8\u0006\u001f"}, d2 = {"Lcom/vexel/offlinearcade/core/data/ArcadeDao;", "", "getStats", "Lcom/vexel/offlinearcade/core/data/GameStatsEntity;", "gameId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeChallengeProgress", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/vexel/offlinearcade/core/data/ChallengeProgressEntity;", "epochDay", "", "observeProfile", "Lcom/vexel/offlinearcade/core/data/PlayerProfileEntity;", "observeStats", "observeThemeUnlocks", "Lcom/vexel/offlinearcade/core/data/ThemeUnlockEntity;", "upsertChallengeProgress", "", "progress", "(Lcom/vexel/offlinearcade/core/data/ChallengeProgressEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertProfile", "profile", "(Lcom/vexel/offlinearcade/core/data/PlayerProfileEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertStats", "stats", "(Lcom/vexel/offlinearcade/core/data/GameStatsEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertThemeUnlock", "themeUnlock", "(Lcom/vexel/offlinearcade/core/data/ThemeUnlockEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
@androidx.room.Dao()
public abstract interface ArcadeDao {
    
    @androidx.room.Query(value = "SELECT * FROM player_profile WHERE profileId = 0")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.vexel.offlinearcade.core.data.PlayerProfileEntity> observeProfile();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertProfile(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.data.PlayerProfileEntity profile, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM game_stats")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.vexel.offlinearcade.core.data.GameStatsEntity>> observeStats();
    
    @androidx.room.Query(value = "SELECT * FROM game_stats WHERE gameId = :gameId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getStats(@org.jetbrains.annotations.NotNull()
    java.lang.String gameId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.vexel.offlinearcade.core.data.GameStatsEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertStats(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.data.GameStatsEntity stats, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM theme_unlocks")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.vexel.offlinearcade.core.data.ThemeUnlockEntity>> observeThemeUnlocks();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertThemeUnlock(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.data.ThemeUnlockEntity themeUnlock, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM challenge_progress WHERE epochDay = :epochDay")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.vexel.offlinearcade.core.data.ChallengeProgressEntity>> observeChallengeProgress(long epochDay);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertChallengeProgress(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.data.ChallengeProgressEntity progress, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}