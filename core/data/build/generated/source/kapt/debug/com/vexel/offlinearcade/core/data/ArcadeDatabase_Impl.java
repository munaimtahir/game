package com.vexel.offlinearcade.core.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ArcadeDatabase_Impl extends ArcadeDatabase {
  private volatile ArcadeDao _arcadeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `player_profile` (`profileId` INTEGER NOT NULL, `coins` INTEGER NOT NULL, `premiumUnlocked` INTEGER NOT NULL, `selectedThemeId` TEXT NOT NULL, `currentStreakDays` INTEGER NOT NULL, `lastPlayedEpochDay` INTEGER, PRIMARY KEY(`profileId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `game_stats` (`gameId` TEXT NOT NULL, `highScore` INTEGER NOT NULL, `sessionsPlayed` INTEGER NOT NULL, `totalPlayMillis` INTEGER NOT NULL, `totalScore` INTEGER NOT NULL, `bestCombo` INTEGER NOT NULL, `bestLines` INTEGER NOT NULL, PRIMARY KEY(`gameId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `theme_unlocks` (`themeId` TEXT NOT NULL, `unlocked` INTEGER NOT NULL, PRIMARY KEY(`themeId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `challenge_progress` (`challengeId` TEXT NOT NULL, `epochDay` INTEGER NOT NULL, `progress` INTEGER NOT NULL, `completed` INTEGER NOT NULL, `rewardClaimed` INTEGER NOT NULL, PRIMARY KEY(`challengeId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '11c365ecd4576beb5f488a7f36527076')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `player_profile`");
        db.execSQL("DROP TABLE IF EXISTS `game_stats`");
        db.execSQL("DROP TABLE IF EXISTS `theme_unlocks`");
        db.execSQL("DROP TABLE IF EXISTS `challenge_progress`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPlayerProfile = new HashMap<String, TableInfo.Column>(6);
        _columnsPlayerProfile.put("profileId", new TableInfo.Column("profileId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayerProfile.put("coins", new TableInfo.Column("coins", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayerProfile.put("premiumUnlocked", new TableInfo.Column("premiumUnlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayerProfile.put("selectedThemeId", new TableInfo.Column("selectedThemeId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayerProfile.put("currentStreakDays", new TableInfo.Column("currentStreakDays", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayerProfile.put("lastPlayedEpochDay", new TableInfo.Column("lastPlayedEpochDay", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlayerProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlayerProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlayerProfile = new TableInfo("player_profile", _columnsPlayerProfile, _foreignKeysPlayerProfile, _indicesPlayerProfile);
        final TableInfo _existingPlayerProfile = TableInfo.read(db, "player_profile");
        if (!_infoPlayerProfile.equals(_existingPlayerProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "player_profile(com.vexel.offlinearcade.core.data.PlayerProfileEntity).\n"
                  + " Expected:\n" + _infoPlayerProfile + "\n"
                  + " Found:\n" + _existingPlayerProfile);
        }
        final HashMap<String, TableInfo.Column> _columnsGameStats = new HashMap<String, TableInfo.Column>(7);
        _columnsGameStats.put("gameId", new TableInfo.Column("gameId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("highScore", new TableInfo.Column("highScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("sessionsPlayed", new TableInfo.Column("sessionsPlayed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("totalPlayMillis", new TableInfo.Column("totalPlayMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("totalScore", new TableInfo.Column("totalScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("bestCombo", new TableInfo.Column("bestCombo", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("bestLines", new TableInfo.Column("bestLines", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGameStats = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGameStats = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGameStats = new TableInfo("game_stats", _columnsGameStats, _foreignKeysGameStats, _indicesGameStats);
        final TableInfo _existingGameStats = TableInfo.read(db, "game_stats");
        if (!_infoGameStats.equals(_existingGameStats)) {
          return new RoomOpenHelper.ValidationResult(false, "game_stats(com.vexel.offlinearcade.core.data.GameStatsEntity).\n"
                  + " Expected:\n" + _infoGameStats + "\n"
                  + " Found:\n" + _existingGameStats);
        }
        final HashMap<String, TableInfo.Column> _columnsThemeUnlocks = new HashMap<String, TableInfo.Column>(2);
        _columnsThemeUnlocks.put("themeId", new TableInfo.Column("themeId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsThemeUnlocks.put("unlocked", new TableInfo.Column("unlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysThemeUnlocks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesThemeUnlocks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoThemeUnlocks = new TableInfo("theme_unlocks", _columnsThemeUnlocks, _foreignKeysThemeUnlocks, _indicesThemeUnlocks);
        final TableInfo _existingThemeUnlocks = TableInfo.read(db, "theme_unlocks");
        if (!_infoThemeUnlocks.equals(_existingThemeUnlocks)) {
          return new RoomOpenHelper.ValidationResult(false, "theme_unlocks(com.vexel.offlinearcade.core.data.ThemeUnlockEntity).\n"
                  + " Expected:\n" + _infoThemeUnlocks + "\n"
                  + " Found:\n" + _existingThemeUnlocks);
        }
        final HashMap<String, TableInfo.Column> _columnsChallengeProgress = new HashMap<String, TableInfo.Column>(5);
        _columnsChallengeProgress.put("challengeId", new TableInfo.Column("challengeId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeProgress.put("epochDay", new TableInfo.Column("epochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeProgress.put("progress", new TableInfo.Column("progress", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeProgress.put("completed", new TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeProgress.put("rewardClaimed", new TableInfo.Column("rewardClaimed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChallengeProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChallengeProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChallengeProgress = new TableInfo("challenge_progress", _columnsChallengeProgress, _foreignKeysChallengeProgress, _indicesChallengeProgress);
        final TableInfo _existingChallengeProgress = TableInfo.read(db, "challenge_progress");
        if (!_infoChallengeProgress.equals(_existingChallengeProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "challenge_progress(com.vexel.offlinearcade.core.data.ChallengeProgressEntity).\n"
                  + " Expected:\n" + _infoChallengeProgress + "\n"
                  + " Found:\n" + _existingChallengeProgress);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "11c365ecd4576beb5f488a7f36527076", "e8a6dfbee817d904872adc9eeb28aa17");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "player_profile","game_stats","theme_unlocks","challenge_progress");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `player_profile`");
      _db.execSQL("DELETE FROM `game_stats`");
      _db.execSQL("DELETE FROM `theme_unlocks`");
      _db.execSQL("DELETE FROM `challenge_progress`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ArcadeDao.class, ArcadeDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ArcadeDao arcadeDao() {
    if (_arcadeDao != null) {
      return _arcadeDao;
    } else {
      synchronized(this) {
        if(_arcadeDao == null) {
          _arcadeDao = new ArcadeDao_Impl(this);
        }
        return _arcadeDao;
      }
    }
  }
}
