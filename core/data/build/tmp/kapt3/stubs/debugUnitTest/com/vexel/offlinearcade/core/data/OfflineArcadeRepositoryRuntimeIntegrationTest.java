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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001:\u0002\u0015\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\n\u001a\u00060\u000bj\u0002`\fH\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\f\u0010\u0011\u001a\u00060\u000bj\u0002`\fH\u0007J\b\u0010\u0012\u001a\u00020\u000bH\u0007J\f\u0010\u0013\u001a\u00060\u000bj\u0002`\fH\u0007J\b\u0010\u0014\u001a\u00020\u000bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepositoryRuntimeIntegrationTest;", "", "()V", "context", "Landroid/content/Context;", "dataStoreFile", "Ljava/io/File;", "databaseFile", "dispatcher", "Lkotlinx/coroutines/test/TestDispatcher;", "challengeGenerationAndProgressLoadFromStorage", "", "Lkotlinx/coroutines/test/TestResult;", "createRuntime", "Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepositoryRuntimeIntegrationTest$RuntimeHarness;", "clock", "Lcom/vexel/offlinearcade/core/common/ArcadeClock;", "roomAndDataStorePersistAcrossRepositoryRecreation", "setUp", "streakAndCoinsFollowRunDays", "tearDown", "MutableClock", "RuntimeHarness", "data_debugUnitTest"})
@kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
public final class OfflineArcadeRepositoryRuntimeIntegrationTest {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.test.TestDispatcher dispatcher = null;
    private android.content.Context context;
    private java.io.File databaseFile;
    private java.io.File dataStoreFile;
    
    public OfflineArcadeRepositoryRuntimeIntegrationTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setUp() {
    }
    
    @org.junit.After()
    public final void tearDown() {
    }
    
    @org.junit.Test()
    public final void roomAndDataStorePersistAcrossRepositoryRecreation() {
    }
    
    @org.junit.Test()
    public final void challengeGenerationAndProgressLoadFromStorage() {
    }
    
    @org.junit.Test()
    public final void streakAndCoinsFollowRunDays() {
    }
    
    private final com.vexel.offlinearcade.core.data.OfflineArcadeRepositoryRuntimeIntegrationTest.RuntimeHarness createRuntime(com.vexel.offlinearcade.core.common.ArcadeClock clock) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\b\u001a\u00020\u0003H\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004\u00a8\u0006\t"}, d2 = {"Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepositoryRuntimeIntegrationTest$MutableClock;", "Lcom/vexel/offlinearcade/core/common/ArcadeClock;", "day", "", "(J)V", "getDay", "()J", "setDay", "currentEpochDay", "data_debugUnitTest"})
    static final class MutableClock implements com.vexel.offlinearcade.core.common.ArcadeClock {
        private long day;
        
        public MutableClock(long day) {
            super();
        }
        
        public final long getDay() {
            return 0L;
        }
        
        public final void setDay(long p0) {
        }
        
        @java.lang.Override()
        public long currentEpochDay() {
            return 0L;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u000f\u001a\u00020\u0010J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0007H\u00c6\u0003J\'\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001c"}, d2 = {"Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepositoryRuntimeIntegrationTest$RuntimeHarness;", "", "repository", "Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepository;", "database", "Lcom/vexel/offlinearcade/core/data/ArcadeDatabase;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "(Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepository;Lcom/vexel/offlinearcade/core/data/ArcadeDatabase;Lkotlinx/coroutines/CoroutineScope;)V", "getDatabase", "()Lcom/vexel/offlinearcade/core/data/ArcadeDatabase;", "getRepository", "()Lcom/vexel/offlinearcade/core/data/OfflineArcadeRepository;", "getScope", "()Lkotlinx/coroutines/CoroutineScope;", "close", "", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "data_debugUnitTest"})
    static final class RuntimeHarness {
        @org.jetbrains.annotations.NotNull()
        private final com.vexel.offlinearcade.core.data.OfflineArcadeRepository repository = null;
        @org.jetbrains.annotations.NotNull()
        private final com.vexel.offlinearcade.core.data.ArcadeDatabase database = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlinx.coroutines.CoroutineScope scope = null;
        
        public RuntimeHarness(@org.jetbrains.annotations.NotNull()
        com.vexel.offlinearcade.core.data.OfflineArcadeRepository repository, @org.jetbrains.annotations.NotNull()
        com.vexel.offlinearcade.core.data.ArcadeDatabase database, @org.jetbrains.annotations.NotNull()
        kotlinx.coroutines.CoroutineScope scope) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vexel.offlinearcade.core.data.OfflineArcadeRepository getRepository() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vexel.offlinearcade.core.data.ArcadeDatabase getDatabase() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.CoroutineScope getScope() {
            return null;
        }
        
        public final void close() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vexel.offlinearcade.core.data.OfflineArcadeRepository component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vexel.offlinearcade.core.data.ArcadeDatabase component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.CoroutineScope component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.vexel.offlinearcade.core.data.OfflineArcadeRepositoryRuntimeIntegrationTest.RuntimeHarness copy(@org.jetbrains.annotations.NotNull()
        com.vexel.offlinearcade.core.data.OfflineArcadeRepository repository, @org.jetbrains.annotations.NotNull()
        com.vexel.offlinearcade.core.data.ArcadeDatabase database, @org.jetbrains.annotations.NotNull()
        kotlinx.coroutines.CoroutineScope scope) {
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
}