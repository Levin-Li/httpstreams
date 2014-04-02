package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.codec.amf.AMF0Decoder;
import com.thunisoft.mediax.core.codec.amf.AMFArray;
import com.thunisoft.mediax.core.codec.flv.FlvConsts;
import com.thunisoft.mediax.core.codec.flv.FlvIterator;
import com.thunisoft.mediax.core.codec.flv.FlvMetaData;
import com.thunisoft.mediax.core.codec.flv.TagDecoder;
import com.thunisoft.mediax.core.codec.flv.tag.MetaDataTag;
import com.thunisoft.mediax.core.pseudostreaming.AbstractStreamer;
import com.thunisoft.mediax.core.utils.ByteUtils;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;

/**
 * flv 流化
 * 
 * @since V1.0 2014-3-3
 * @author chenxh
 */
public class FlvStreamer extends AbstractStreamer {


    public FlvStreamer(RandomAccessChannel channel) throws FileNotFoundException {
        super(channel);
    }


    @Override
    public void transfer(double startAt, WritableByteChannel outChannel) throws IOException {
        channel.position(0);
        if (startAt > 0) {
            write(outChannel, startAt);
        } else {
            write(outChannel);
        }

    }

    private void write(WritableByteChannel outChannel) throws IOException {
        channel.transferTo(0, channel.length(), outChannel);

    }

    public void write(WritableByteChannel outChannel, double startAt) throws IOException {
        channel.position(0);

        FlvIterator flvIterator = new FlvIterator(channel);

        // flv head
        ByteBuffer fileHead = flvIterator.getFileHead(); // head
        outChannel.write(fileHead);
        outChannel.write(ByteBuffer.wrap(ByteUtils.wrapUInt32(fileHead.position())));

        // flv data
        ByteBuffer scriptData = flvIterator.next(); // first data tag
        FlvMetaData metadata = decodeMetadata(scriptData);
        long position = metadata.getNearestPosition((long) startAt);

        if (position > 0) {
            channel.transferTo(position, channel.length() - position, outChannel);
        }
    }

    private FlvMetaData decodeMetadata(ByteBuffer scriptData) throws IOException {
        try {
            TagDecoder decoder = new TagDecoder();
            
            if (decoder.typeOf(scriptData) != FlvConsts.TAGTYPE_SCRIPT) {
                throw new IOException("Is Not A Supported Flv File");
            }

            MetaDataTag metadataTag = decoder.decodeMetadata(scriptData);
            return metadataTag;

        } catch (DecoderException e) {
            throw new IOException("Is Not A Supported Flv File");
        }
    }
}
