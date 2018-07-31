package com.zype.android.ui.player;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.chunk.Format;
import com.squareup.otto.Subscribe;
import com.zype.android.DataRepository;
import com.zype.android.Db.Entity.Video;
import com.zype.android.core.settings.SettingsProvider;
import com.zype.android.utils.Logger;
import com.zype.android.webapi.WebApiManager;
import com.zype.android.webapi.builder.ParamsBuilder;
import com.zype.android.webapi.builder.PlayerParamsBuilder;
import com.zype.android.webapi.events.player.PlayerAudioEvent;
import com.zype.android.webapi.model.player.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.zype.android.webapi.builder.PlayerParamsBuilder.AUDIO;

/**
 * Created by Evgeny Cherkasov on 23.07.2018
 */
public class PlayerViewModel extends AndroidViewModel implements CustomPlayer.InfoListener {

    private MutableLiveData<List<PlayerMode>> availablePlayerModes;
    private MutableLiveData<PlayerMode> playerMode;
    private MutableLiveData<String> playerUrl;

    private String videoId;

    public enum PlayerMode {
        AUDIO,
        VIDEO
    }

    DataRepository repo;
    WebApiManager api;

    public PlayerViewModel(Application application) {
        super(application);
        repo = DataRepository.getInstance(application);
        api = WebApiManager.getInstance();
        api.subscribe(this);

        availablePlayerModes = new MutableLiveData<>();
        availablePlayerModes.setValue(new ArrayList<PlayerMode>());
        playerMode = new MutableLiveData<>();
        playerMode.setValue(PlayerMode.VIDEO);
    }

    @Override
    protected void onCleared() {
        api.unsubscribe(this);
        super.onCleared();
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
        init();
    }

    private void init() {
        Video video = repo.getVideoSync(videoId);
        if (availablePlayerModes.getValue().isEmpty()) {
            updateAvailablePlayerModes();
        }
    }

    public MutableLiveData<String> getPlayerUrl() {
        if (playerUrl == null) {
            playerUrl = new MutableLiveData<>();
        }
        Video video = repo.getVideoSync(videoId);
        if (TextUtils.isEmpty(video.playerAudioUrl)) {
            loadAudioPlayerUrl(videoId);
        }
        if (TextUtils.isEmpty(video.playerVideoUrl)) {
//                    loadVideoPlayerUrl(videoId);
        }
        updatePlayerUrl(video);
        return playerUrl;
    }

    private void updatePlayerUrl(Video video) {
        switch (playerMode.getValue()) {
            case AUDIO:
                playerUrl.setValue(video.playerAudioUrl);
                break;
            case VIDEO:
                playerUrl.setValue(video.playerVideoUrl);
                break;
        }
    }

    public MutableLiveData<PlayerMode> getPlayerMode() {
        return playerMode;
    }

    public void setPlayerMode(PlayerMode mode) {
        Video video = repo.getVideoSync(videoId);
        if (video != null) {
            playerMode.setValue(mode);
            updatePlayerUrl(video);
        }
    }

    public MutableLiveData<List<PlayerMode>> getAvailablePlayerModes() {
        return availablePlayerModes;
    }

    private void updateAvailablePlayerModes() {
        List<PlayerMode> result = new ArrayList<>();

        Video video = repo.getVideoSync(videoId);
        if (video != null) {
            if (!TextUtils.isEmpty(video.playerAudioUrl)) {
                result.add(PlayerMode.AUDIO);
            }
            if (!TextUtils.isEmpty(video.playerVideoUrl)) {
                result.add(PlayerMode.VIDEO);
            }
        }

        availablePlayerModes.setValue(result);
    }

    /**
     * Make API request for audio player
     *
     * @param videoId Video id
     */
    private void loadAudioPlayerUrl(String videoId) {
        Logger.d("loadAudioPlayerUrl(): videoId=" + videoId);
        PlayerParamsBuilder builder = new PlayerParamsBuilder();
        if (SettingsProvider.getInstance().isLoggedIn()) {
            builder.addAccessToken();
        }
        else {
            builder.addAppKey();
        }
        builder.addVideoId(videoId);
        builder.addAudio();
        api.executeRequest(WebApiManager.Request.PLAYER_AUDIO, builder.build());
    }

    /**
     * Handles API request for audio player
     *
     * @param event Response event
     */
    @Subscribe
    public void handleAudioPlayerUrl(PlayerAudioEvent event) {
        Logger.d("handleAudioPlayerUrl()");

        Bundle requestOptions = event.getOptions();
        HashMap<String, String> pathParams = (HashMap<String, String>) requestOptions.getSerializable(ParamsBuilder.GET_PARAMS);
        String videoId = pathParams.get(PlayerParamsBuilder.VIDEO_ID);

        List<File> files = event.getEventData().getModelData().getResponse().getBody().getFiles();
        if (files == null || files.isEmpty()) {
            Logger.w("handleAudioPlayerUrl(): Audio source not found");
            return;
        }

        // Take first source in the list
        String url = files.get(0).getUrl();
        Logger.d("handleAudioPlayerUrl(): url=" + url);

        // Save audio url in the local database if it was changed
        Video video = repo.getVideoSync(videoId);
        if (video.playerAudioUrl == null || !video.playerAudioUrl.equals(url)) {
            video.playerAudioUrl = url;
            repo.updateVideo(video);

            if (playerMode.getValue() == PlayerMode.AUDIO) {
                playerUrl.setValue(url);
            }
            updateAvailablePlayerModes();
        }
    }


    @Override
    public void onVideoFormatEnabled(Format format, int trigger, long mediaTimeMs) {
        Logger.i("onVideoFormatEnabled()");
        if (getPlayerMode().getValue() == PlayerMode.VIDEO && format.codecs.equals("mp4a.40.2")) {
            Video video = repo.getVideoSync(videoId);
            video.playerVideoUrl = null;
            repo.updateVideo(video);

            updateAvailablePlayerModes();
            setPlayerMode(PlayerMode.AUDIO);
        }
    }

    @Override
    public void onAudioFormatEnabled(Format format, int trigger, long mediaTimeMs) {
        Logger.i("onAudioFormatEnabled()");
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {
    }

    @Override
    public void onBandwidthSample(int elapsedMs, long bytes, long bitrateEstimate) {
    }

    @Override
    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
    }

    @Override
    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
    }

    @Override
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
    }

    @Override
    public void onAvailableRangeChanged(TimeRange availableRange) {
    }
}