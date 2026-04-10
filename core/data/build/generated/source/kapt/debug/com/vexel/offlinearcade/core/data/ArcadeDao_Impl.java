package com.vexel.offlinearcade.core.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ArcadeDao_Impl implements ArcadeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlayerProfileEntity> __insertionAdapterOfPlayerProfileEntity;

  private final EntityInsertionAdapter<GameStatsEntity> __insertionAdapterOfGameStatsEntity;

  private final EntityInsertionAdapter<ThemeUnlockEntity> __insertionAdapterOfThemeUnlockEntity;

  private final EntityInsertionAdapter<ChallengeProgressEntity> __insertionAdapterOfChallengeProgressEntity;

  public ArcadeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlayerProfileEntity = new EntityInsertionAdapter<PlayerProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `player_profile` (`profileId`,`coins`,`premiumUnlocked`,`selectedThemeId`,`currentStreakDays`,`lastPlayedEpochDay`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlayerProfileEntity entity) {
        statement.bindLong(1, entity.getProfileId());
        statement.bindLong(2, entity.getCoins());
        final int _tmp = entity.getPremiumUnlocked() ? 1 : 0;
        statement.bindLong(3, _tmp);
        if (entity.getSelectedThemeId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSelectedThemeId());
        }
        statement.bindLong(5, entity.getCurrentStreakDays());
        if (entity.getLastPlayedEpochDay() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastPlayedEpochDay());
        }
      }
    };
    this.__insertionAdapterOfGameStatsEntity = new EntityInsertionAdapter<GameStatsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `game_stats` (`gameId`,`highScore`,`sessionsPlayed`,`totalPlayMillis`,`totalScore`,`bestCombo`,`bestLines`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GameStatsEntity entity) {
        if (entity.getGameId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getGameId());
        }
        statement.bindLong(2, entity.getHighScore());
        statement.bindLong(3, entity.getSessionsPlayed());
        statement.bindLong(4, entity.getTotalPlayMillis());
        statement.bindLong(5, entity.getTotalScore());
        statement.bindLong(6, entity.getBestCombo());
        statement.bindLong(7, entity.getBestLines());
      }
    };
    this.__insertionAdapterOfThemeUnlockEntity = new EntityInsertionAdapter<ThemeUnlockEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `theme_unlocks` (`themeId`,`unlocked`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ThemeUnlockEntity entity) {
        if (entity.getThemeId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getThemeId());
        }
        final int _tmp = entity.getUnlocked() ? 1 : 0;
        statement.bindLong(2, _tmp);
      }
    };
    this.__insertionAdapterOfChallengeProgressEntity = new EntityInsertionAdapter<ChallengeProgressEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `challenge_progress` (`challengeId`,`epochDay`,`progress`,`completed`,`rewardClaimed`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChallengeProgressEntity entity) {
        if (entity.getChallengeId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getChallengeId());
        }
        statement.bindLong(2, entity.getEpochDay());
        statement.bindLong(3, entity.getProgress());
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.getRewardClaimed() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
      }
    };
  }

  @Override
  public Object upsertProfile(final PlayerProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlayerProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertStats(final GameStatsEntity stats,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGameStatsEntity.insert(stats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertThemeUnlock(final ThemeUnlockEntity themeUnlock,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfThemeUnlockEntity.insert(themeUnlock);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertChallengeProgress(final ChallengeProgressEntity progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChallengeProgressEntity.insert(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<PlayerProfileEntity> observeProfile() {
    final String _sql = "SELECT * FROM player_profile WHERE profileId = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"player_profile"}, new Callable<PlayerProfileEntity>() {
      @Override
      @Nullable
      public PlayerProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfCoins = CursorUtil.getColumnIndexOrThrow(_cursor, "coins");
          final int _cursorIndexOfPremiumUnlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "premiumUnlocked");
          final int _cursorIndexOfSelectedThemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedThemeId");
          final int _cursorIndexOfCurrentStreakDays = CursorUtil.getColumnIndexOrThrow(_cursor, "currentStreakDays");
          final int _cursorIndexOfLastPlayedEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedEpochDay");
          final PlayerProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpProfileId;
            _tmpProfileId = _cursor.getInt(_cursorIndexOfProfileId);
            final int _tmpCoins;
            _tmpCoins = _cursor.getInt(_cursorIndexOfCoins);
            final boolean _tmpPremiumUnlocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfPremiumUnlocked);
            _tmpPremiumUnlocked = _tmp != 0;
            final String _tmpSelectedThemeId;
            if (_cursor.isNull(_cursorIndexOfSelectedThemeId)) {
              _tmpSelectedThemeId = null;
            } else {
              _tmpSelectedThemeId = _cursor.getString(_cursorIndexOfSelectedThemeId);
            }
            final int _tmpCurrentStreakDays;
            _tmpCurrentStreakDays = _cursor.getInt(_cursorIndexOfCurrentStreakDays);
            final Long _tmpLastPlayedEpochDay;
            if (_cursor.isNull(_cursorIndexOfLastPlayedEpochDay)) {
              _tmpLastPlayedEpochDay = null;
            } else {
              _tmpLastPlayedEpochDay = _cursor.getLong(_cursorIndexOfLastPlayedEpochDay);
            }
            _result = new PlayerProfileEntity(_tmpProfileId,_tmpCoins,_tmpPremiumUnlocked,_tmpSelectedThemeId,_tmpCurrentStreakDays,_tmpLastPlayedEpochDay);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<GameStatsEntity>> observeStats() {
    final String _sql = "SELECT * FROM game_stats";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"game_stats"}, new Callable<List<GameStatsEntity>>() {
      @Override
      @NonNull
      public List<GameStatsEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGameId = CursorUtil.getColumnIndexOrThrow(_cursor, "gameId");
          final int _cursorIndexOfHighScore = CursorUtil.getColumnIndexOrThrow(_cursor, "highScore");
          final int _cursorIndexOfSessionsPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionsPlayed");
          final int _cursorIndexOfTotalPlayMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPlayMillis");
          final int _cursorIndexOfTotalScore = CursorUtil.getColumnIndexOrThrow(_cursor, "totalScore");
          final int _cursorIndexOfBestCombo = CursorUtil.getColumnIndexOrThrow(_cursor, "bestCombo");
          final int _cursorIndexOfBestLines = CursorUtil.getColumnIndexOrThrow(_cursor, "bestLines");
          final List<GameStatsEntity> _result = new ArrayList<GameStatsEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GameStatsEntity _item;
            final String _tmpGameId;
            if (_cursor.isNull(_cursorIndexOfGameId)) {
              _tmpGameId = null;
            } else {
              _tmpGameId = _cursor.getString(_cursorIndexOfGameId);
            }
            final int _tmpHighScore;
            _tmpHighScore = _cursor.getInt(_cursorIndexOfHighScore);
            final int _tmpSessionsPlayed;
            _tmpSessionsPlayed = _cursor.getInt(_cursorIndexOfSessionsPlayed);
            final long _tmpTotalPlayMillis;
            _tmpTotalPlayMillis = _cursor.getLong(_cursorIndexOfTotalPlayMillis);
            final int _tmpTotalScore;
            _tmpTotalScore = _cursor.getInt(_cursorIndexOfTotalScore);
            final int _tmpBestCombo;
            _tmpBestCombo = _cursor.getInt(_cursorIndexOfBestCombo);
            final int _tmpBestLines;
            _tmpBestLines = _cursor.getInt(_cursorIndexOfBestLines);
            _item = new GameStatsEntity(_tmpGameId,_tmpHighScore,_tmpSessionsPlayed,_tmpTotalPlayMillis,_tmpTotalScore,_tmpBestCombo,_tmpBestLines);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getStats(final String gameId,
      final Continuation<? super GameStatsEntity> $completion) {
    final String _sql = "SELECT * FROM game_stats WHERE gameId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (gameId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gameId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GameStatsEntity>() {
      @Override
      @Nullable
      public GameStatsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGameId = CursorUtil.getColumnIndexOrThrow(_cursor, "gameId");
          final int _cursorIndexOfHighScore = CursorUtil.getColumnIndexOrThrow(_cursor, "highScore");
          final int _cursorIndexOfSessionsPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionsPlayed");
          final int _cursorIndexOfTotalPlayMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPlayMillis");
          final int _cursorIndexOfTotalScore = CursorUtil.getColumnIndexOrThrow(_cursor, "totalScore");
          final int _cursorIndexOfBestCombo = CursorUtil.getColumnIndexOrThrow(_cursor, "bestCombo");
          final int _cursorIndexOfBestLines = CursorUtil.getColumnIndexOrThrow(_cursor, "bestLines");
          final GameStatsEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpGameId;
            if (_cursor.isNull(_cursorIndexOfGameId)) {
              _tmpGameId = null;
            } else {
              _tmpGameId = _cursor.getString(_cursorIndexOfGameId);
            }
            final int _tmpHighScore;
            _tmpHighScore = _cursor.getInt(_cursorIndexOfHighScore);
            final int _tmpSessionsPlayed;
            _tmpSessionsPlayed = _cursor.getInt(_cursorIndexOfSessionsPlayed);
            final long _tmpTotalPlayMillis;
            _tmpTotalPlayMillis = _cursor.getLong(_cursorIndexOfTotalPlayMillis);
            final int _tmpTotalScore;
            _tmpTotalScore = _cursor.getInt(_cursorIndexOfTotalScore);
            final int _tmpBestCombo;
            _tmpBestCombo = _cursor.getInt(_cursorIndexOfBestCombo);
            final int _tmpBestLines;
            _tmpBestLines = _cursor.getInt(_cursorIndexOfBestLines);
            _result = new GameStatsEntity(_tmpGameId,_tmpHighScore,_tmpSessionsPlayed,_tmpTotalPlayMillis,_tmpTotalScore,_tmpBestCombo,_tmpBestLines);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ThemeUnlockEntity>> observeThemeUnlocks() {
    final String _sql = "SELECT * FROM theme_unlocks";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"theme_unlocks"}, new Callable<List<ThemeUnlockEntity>>() {
      @Override
      @NonNull
      public List<ThemeUnlockEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfThemeId = CursorUtil.getColumnIndexOrThrow(_cursor, "themeId");
          final int _cursorIndexOfUnlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "unlocked");
          final List<ThemeUnlockEntity> _result = new ArrayList<ThemeUnlockEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ThemeUnlockEntity _item;
            final String _tmpThemeId;
            if (_cursor.isNull(_cursorIndexOfThemeId)) {
              _tmpThemeId = null;
            } else {
              _tmpThemeId = _cursor.getString(_cursorIndexOfThemeId);
            }
            final boolean _tmpUnlocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUnlocked);
            _tmpUnlocked = _tmp != 0;
            _item = new ThemeUnlockEntity(_tmpThemeId,_tmpUnlocked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ChallengeProgressEntity>> observeChallengeProgress(final long epochDay) {
    final String _sql = "SELECT * FROM challenge_progress WHERE epochDay = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, epochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"challenge_progress"}, new Callable<List<ChallengeProgressEntity>>() {
      @Override
      @NonNull
      public List<ChallengeProgressEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfChallengeId = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeId");
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfRewardClaimed = CursorUtil.getColumnIndexOrThrow(_cursor, "rewardClaimed");
          final List<ChallengeProgressEntity> _result = new ArrayList<ChallengeProgressEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChallengeProgressEntity _item;
            final String _tmpChallengeId;
            if (_cursor.isNull(_cursorIndexOfChallengeId)) {
              _tmpChallengeId = null;
            } else {
              _tmpChallengeId = _cursor.getString(_cursorIndexOfChallengeId);
            }
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final boolean _tmpRewardClaimed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRewardClaimed);
            _tmpRewardClaimed = _tmp_1 != 0;
            _item = new ChallengeProgressEntity(_tmpChallengeId,_tmpEpochDay,_tmpProgress,_tmpCompleted,_tmpRewardClaimed);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
