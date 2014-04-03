package com.thunisoft.videox.mp4.stream;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ChunkStreamMixer {

    private List<Sample> samples = new LinkedList<Sample>();

    public ChunkStreamMixer(ChunkStream... streams) throws IOException {
        for (int i = 0; i < streams.length; i++) {
            ChunkStream stream = streams[i];

            while (stream.hasNext()) {
                Chunk chunk = stream.next();

                List<Sample> samples = chunk.getSamples();
                for (int j = 0; j < samples.size(); j++) {
                    this.samples.add(samples.get(j));
                }
            }
        }


        Collections.sort(samples, new Comparator<Sample>() {
            @Override
            public int compare(Sample o1, Sample o2) {
                if (o1.getTimestamp() == o2.getTimestamp()) {
                    return 0;
                };

                // return o1.getTimestamp() / o1.getTimeScale() < o2.getTimestamp() /
                // o2.getTimeScale() ? -1 : 1;
                return o1.getTimestamp() * o2.getTimeScale() < o2.getTimestamp()
                        * o1.getTimeScale() ? -1 : 1;
            }
        });
    }

    public List<Sample> getSamples() {
        return samples;
    }

}
