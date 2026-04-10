package com.vexel.offlinearcade.core.data;

import com.vexel.offlinearcade.core.model.ArcadeSnapshot;
import com.vexel.offlinearcade.core.model.DailyChallenge;
import com.vexel.offlinearcade.core.model.RunResult;
import com.vexel.offlinearcade.core.model.SettingsState;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00032\u0006\u0010\n\u001a\u00020\u000bH&J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u00a6@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\rH\u00a6@\u00a2\u0006\u0002\u0010\u0019J\"\u0010\u001a\u001a\u00020\u00122\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001d0\u001cH\u00a6@\u00a2\u0006\u0002\u0010\u001eR\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u001f"}, d2 = {"Lcom/vexel/offlinearcade/core/data/ArcadeRepository;", "", "snapshot", "Lkotlinx/coroutines/flow/Flow;", "Lcom/vexel/offlinearcade/core/model/ArcadeSnapshot;", "getSnapshot", "()Lkotlinx/coroutines/flow/Flow;", "challengesForDay", "", "Lcom/vexel/offlinearcade/core/model/DailyChallenge;", "epochDay", "", "purchaseTheme", "", "themeId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recordRun", "", "result", "Lcom/vexel/offlinearcade/core/model/RunResult;", "(Lcom/vexel/offlinearcade/core/model/RunResult;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "selectTheme", "setPremiumUnlocked", "unlocked", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSettings", "transform", "Lkotlin/Function1;", "Lcom/vexel/offlinearcade/core/model/SettingsState;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public abstract interface ArcadeRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.vexel.offlinearcade.core.model.ArcadeSnapshot> getSnapshot();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.vexel.offlinearcade.core.model.DailyChallenge>> challengesForDay(long epochDay);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSettings(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.vexel.offlinearcade.core.model.SettingsState, com.vexel.offlinearcade.core.model.SettingsState> transform, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object recordRun(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.model.RunResult result, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object purchaseTheme(@org.jetbrains.annotations.NotNull()
    java.lang.String themeId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object selectTheme(@org.jetbrains.annotations.NotNull()
    java.lang.String themeId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setPremiumUnlocked(boolean unlocked, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}