package com.vexel.offlinearcade.core.data;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.vexel.offlinearcade.core.common.ArcadeClock;
import com.vexel.offlinearcade.core.common.ArcadeDispatchers;
import com.vexel.offlinearcade.core.model.ArcadeSnapshot;
import com.vexel.offlinearcade.core.model.DailyChallenge;
import com.vexel.offlinearcade.core.model.GameId;
import com.vexel.offlinearcade.core.model.GameStats;
import com.vexel.offlinearcade.core.model.PlayerProfile;
import com.vexel.offlinearcade.core.model.RunResult;
import com.vexel.offlinearcade.core.model.SettingsState;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u001e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0082@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u001b0\u000f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0016\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010#J\u0016\u0010$\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0002\u0010!J\u0016\u0010%\u001a\u00020\u00142\u0006\u0010&\u001a\u00020\u001eH\u0096@\u00a2\u0006\u0002\u0010\'J\"\u0010(\u001a\u00020\u00142\u0012\u0010)\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020+0*H\u0096@\u00a2\u0006\u0002\u0010,J\u0018\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020.2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u00060"}, d2 = {"Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepository;", "Lcom/vexel/offlinearcade/core/data/ArcadeRepository;", "database", "Lcom/vexel/offlinearcade/core/data/ArcadeDatabase;", "preferences", "Landroidx/datastore/core/DataStore;", "Landroidx/datastore/preferences/core/Preferences;", "clock", "Lcom/vexel/offlinearcade/core/common/ArcadeClock;", "dispatchers", "Lcom/vexel/offlinearcade/core/common/ArcadeDispatchers;", "(Lcom/vexel/offlinearcade/core/data/ArcadeDatabase;Landroidx/datastore/core/DataStore;Lcom/vexel/offlinearcade/core/common/ArcadeClock;Lcom/vexel/offlinearcade/core/common/ArcadeDispatchers;)V", "dao", "Lcom/vexel/offlinearcade/core/data/ArcadeDao;", "snapshot", "Lkotlinx/coroutines/flow/Flow;", "Lcom/vexel/offlinearcade/core/model/ArcadeSnapshot;", "getSnapshot", "()Lkotlinx/coroutines/flow/Flow;", "applyChallengeProgress", "", "epochDay", "", "result", "Lcom/vexel/offlinearcade/core/model/RunResult;", "(JLcom/vexel/offlinearcade/core/model/RunResult;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "challengesForDay", "", "Lcom/vexel/offlinearcade/core/model/DailyChallenge;", "purchaseTheme", "", "themeId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recordRun", "(Lcom/vexel/offlinearcade/core/model/RunResult;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "selectTheme", "setPremiumUnlocked", "unlocked", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSettings", "transform", "Lkotlin/Function1;", "Lcom/vexel/offlinearcade/core/model/SettingsState;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateStreak", "Lcom/vexel/offlinearcade/core/model/PlayerProfile;", "profile", "data_debug"})
public final class OfflineArcadeRepository implements com.vexel.offlinearcade.core.data.ArcadeRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.vexel.offlinearcade.core.data.ArcadeDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> preferences = null;
    @org.jetbrains.annotations.NotNull()
    private final com.vexel.offlinearcade.core.common.ArcadeClock clock = null;
    @org.jetbrains.annotations.NotNull()
    private final com.vexel.offlinearcade.core.common.ArcadeDispatchers dispatchers = null;
    @org.jetbrains.annotations.NotNull()
    private final com.vexel.offlinearcade.core.data.ArcadeDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.vexel.offlinearcade.core.model.ArcadeSnapshot> snapshot = null;
    
    public OfflineArcadeRepository(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.data.ArcadeDatabase database, @org.jetbrains.annotations.NotNull()
    androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> preferences, @org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.common.ArcadeClock clock, @org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.common.ArcadeDispatchers dispatchers) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.vexel.offlinearcade.core.model.ArcadeSnapshot> getSnapshot() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.vexel.offlinearcade.core.model.DailyChallenge>> challengesForDay(long epochDay) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateSettings(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.vexel.offlinearcade.core.model.SettingsState, com.vexel.offlinearcade.core.model.SettingsState> transform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object recordRun(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.model.RunResult result, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object purchaseTheme(@org.jetbrains.annotations.NotNull()
    java.lang.String themeId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object selectTheme(@org.jetbrains.annotations.NotNull()
    java.lang.String themeId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setPremiumUnlocked(boolean unlocked, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object applyChallengeProgress(long epochDay, com.vexel.offlinearcade.core.model.RunResult result, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.vexel.offlinearcade.core.model.PlayerProfile updateStreak(com.vexel.offlinearcade.core.model.PlayerProfile profile, long epochDay) {
        return null;
    }
}