package com.thunisoft.mediax.stream.mp4;

import java.io.File;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.thunisoft.mediax.stream.AbstractFileStreamer;
import com.thunisoft.mediax.stream.IStreamer;


/**
 * MP4 文件流化
 * <p>
 * 
 * @since V1.0 2014-3-3
 * @author chenxh
 */
public class MP4Streamer extends AbstractFileStreamer implements IStreamer {

    public MP4Streamer(File file, double startAt) throws IOException {
        super(file, startAt);
    }

    public String contentType() {
        return "video/mp4";
    }

    @Override
    public void transfer(WritableByteChannel outChannel) throws IOException {
        transfer(outChannel, startAt());
    }

    public void transfer(WritableByteChannel outChannel, double startAt) throws IOException {
        Movie movie = MovieCreator.build(file().getAbsolutePath());

        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        // remove all tracks we will create new tracks from the old

        double startTime = startAt;
        double endTime =
                (double) tracks.get(0).getDuration()
                        / tracks.get(0).getTrackMetaData().getTimescale();

        boolean timeCorrected = false;

        // Here we try to find a track that has sync samples. Since we can only start decoding
        // at such a sample we SHOULD make sure that the start of the new fragment is exactly
        // such a frame
        for (Track track : tracks) {
            if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                if (timeCorrected) {
                    // This exception here could be a false positive in case we have multiple tracks
                    // with sync samples at exactly the same positions. E.g. a single movie
                    // containing
                    // multiple qualities of the same video (Microsoft Smooth Streaming file)
                    throw new RuntimeException(
                            "The startTime has already been corrected by another track with SyncSample. Not Supported.");
                }
                startTime = correctTimeToSyncSample(track, startTime, false);
                endTime = correctTimeToSyncSample(track, endTime, true);
                timeCorrected = true;
            }
        }

        for (Track track : tracks) {
            long currentSample = 0;
            double currentTime = 0;
            long startSample = -1;
            long endSample = -1;

            for (int i = 0; i < track.getSampleDurations().length; i++) {
                long delta = track.getSampleDurations()[i];

                if (currentTime <= startTime) {
                    // current sample is still before the new starttime
                    startSample = currentSample;
                }
                if (currentTime <= endTime) {
                    // current sample is after the new start time and still before the new endtime
                    endSample = currentSample;
                } else {
                    // current sample is after the end of the cropped video
                    break;
                }
                currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
                currentSample++;
            }
            movie.addTrack(new CroppedTrack(track, startSample, endSample));
        }

        Container newContainer = new DefaultMp4Builder().build(movie);
        newContainer.writeContainer(outChannel);
    }


    private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];
            int syncSampleIndex = Arrays.binarySearch(track.getSyncSamples(), currentSample + 1);
            if (syncSampleIndex >= 0) {
                // samples always start with 1 but we start with zero therefore +1
                timeOfSyncSamples[syncSampleIndex] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;
        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
}
