package com.vexel.offlinearcade.core.data;

import com.vexel.offlinearcade.core.model.ArcadeThemeCatalog;
import com.vexel.offlinearcade.core.model.ChallengeMetric;
import com.vexel.offlinearcade.core.model.DailyChallenge;
import com.vexel.offlinearcade.core.model.GameId;
import com.vexel.offlinearcade.core.model.GameStats;
import com.vexel.offlinearcade.core.model.PlayerProfile;
import com.vexel.offlinearcade.core.model.ThemeUnlock;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000L\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00050\u0001H\u0000\u001a$\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00012\u0006\u0010\n\u001a\u00020\u000bH\u0000\u001a\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0000\u001a\f\u0010\u0012\u001a\u00020\u0013*\u00020\u0014H\u0000\u001a\f\u0010\u0012\u001a\u00020\u0015*\u00020\u0016H\u0000\u001a\f\u0010\u0017\u001a\u00020\u0014*\u00020\u0013H\u0000\u001a\f\u0010\u0017\u001a\u00020\u0016*\u00020\u0015H\u0000\u00a8\u0006\u0018"}, d2 = {"mergeChallenges", "", "Lcom/vexel/offlinearcade/core/model/DailyChallenge;", "generated", "progress", "Lcom/vexel/offlinearcade/core/data/ChallengeProgressEntity;", "mergeThemes", "Lcom/vexel/offlinearcade/core/model/ThemeUnlock;", "unlocks", "Lcom/vexel/offlinearcade/core/data/ThemeUnlockEntity;", "premiumUnlocked", "", "metricProgress", "", "metric", "Lcom/vexel/offlinearcade/core/model/ChallengeMetric;", "runResult", "Lcom/vexel/offlinearcade/core/model/RunResult;", "toEntity", "Lcom/vexel/offlinearcade/core/data/GameStatsEntity;", "Lcom/vexel/offlinearcade/core/model/GameStats;", "Lcom/vexel/offlinearcade/core/data/PlayerProfileEntity;", "Lcom/vexel/offlinearcade/core/model/PlayerProfile;", "toModel", "data_debug"})
public final class MappersKt {
    
    @org.jetbrains.annotations.NotNull()
    public static final com.vexel.offlinearcade.core.model.PlayerProfile toModel(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.data.PlayerProfileEntity $this$toModel) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.vexel.offlinearcade.core.data.PlayerProfileEntity toEntity(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.model.PlayerProfile $this$toEntity) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.vexel.offlinearcade.core.model.GameStats toModel(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.data.GameStatsEntity $this$toModel) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.vexel.offlinearcade.core.data.GameStatsEntity toEntity(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.model.GameStats $this$toEntity) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.vexel.offlinearcade.core.model.ThemeUnlock> mergeThemes(@org.jetbrains.annotations.NotNull()
    java.util.List<com.vexel.offlinearcade.core.data.ThemeUnlockEntity> unlocks, boolean premiumUnlocked) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.vexel.offlinearcade.core.model.DailyChallenge> mergeChallenges(@org.jetbrains.annotations.NotNull()
    java.util.List<com.vexel.offlinearcade.core.model.DailyChallenge> generated, @org.jetbrains.annotations.NotNull()
    java.util.List<com.vexel.offlinearcade.core.data.ChallengeProgressEntity> progress) {
        return null;
    }
    
    public static final int metricProgress(@org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.model.ChallengeMetric metric, @org.jetbrains.annotations.NotNull()
    com.vexel.offlinearcade.core.model.RunResult runResult) {
        return 0;
    }
}