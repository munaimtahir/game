package com.vexel.offlinearcade.core.data;

import android.content.Context;
import androidx.datastore.preferences.core.PreferenceDataStoreFactory;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import com.vexel.offlinearcade.core.common.ArcadeClock;
import com.vexel.offlinearcade.core.common.ArcadeDispatchers;
import com.vexel.offlinearcade.core.model.GameId;
import com.vexel.offlinearcade.core.model.RunResult;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.io.File;

@org.junit.runner.RunWith(value = org.robolectric.RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk = {34})
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\r\u001a\u00060\u000ej\u0002`\u000fH\u0007J\f\u0010\u0010\u001a\u00060\u000ej\u0002`\u000fH\u0007J\b\u0010\u0011\u001a\u00020\u000eH\u0007J\f\u0010\u0012\u001a\u00060\u000ej\u0002`\u000fH\u0007J\b\u0010\u0013\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepositoryPersistenceTest;", "", "()V", "context", "Landroid/content/Context;", "dataStoreFile", "Ljava/io/File;", "database", "Lcom/vexel/offlinearcade/core/data/ArcadeDatabase;", "dispatcher", "Lkotlinx/coroutines/test/TestDispatcher;", "repository", "Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepository;", "purchaseAndSelectThemePersist", "", "Lkotlinx/coroutines/test/TestResult;", "recordRunUpdatesStatsCoinsStreakAndChallenges", "setUp", "settingsPersistAcrossSnapshotReads", "tearDown", "data_debugUnitTest"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
public final class OfflineArcadeRepositoryPersistenceTest {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.test.TestDispatcher dispatcher = null;
    private android.content.Context context;
    private com.vexel.offlinearcade.core.data.ArcadeDatabase database;
    private java.io.File dataStoreFile;
    private com.vexel.offlinearcade.core.data.OfflineArcadeRepository repository;
    
    public OfflineArcadeRepositoryPersistenceTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setUp() {
    }
    
    @org.junit.After()
    public final void tearDown() {
    }
    
    @org.junit.Test()
    public final void settingsPersistAcrossSnapshotReads() {
    }
    
    @org.junit.Test()
    public final void recordRunUpdatesStatsCoinsStreakAndChallenges() {
    }
    
    @org.junit.Test()
    public final void purchaseAndSelectThemePersist() {
    }
}