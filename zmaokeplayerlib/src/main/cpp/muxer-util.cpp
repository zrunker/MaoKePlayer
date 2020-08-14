#include <jni.h>

extern "C" {
#include <libavformat/avformat.h>
}

// 格式转换具体实现
int main_muxer(const char *inPath, const char *outPath) {
    // 输入输出AVFormatContext对象
    AVFormatContext *in_avfmt_ctx = NULL, *out_avfmt_ctx = NULL;
    // 输出文件格式
    AVOutputFormat *avOutFmt = nullptr;
    // 压缩数据
    AVPacket avPacket;

    const char *pathIn, *pathOut;

    int ret;
    pathIn = inPath;
    pathOut = outPath;

    // 打开一个输入流，并且读取其文件头。编解码器没有打开。
    if ((ret = avformat_open_input(&in_avfmt_ctx, pathIn, 0, 0)) < 0) {
        fprintf(stderr, "Could not open input file '%s'", pathIn);
        goto end;
    }

    /**
     * 读取媒体文件的所有packets，获取流信息。
     * 有些格式(如MPEG)没有文件头，或者没有在其中存储足够的信息，
     * 因此建议您调用avformat_find_stream_info()函数，该函数尝试读取和解码几个帧以查找丢失的信息。
     */
    if ((ret = avformat_find_stream_info(in_avfmt_ctx, 0)) < 0) {
        fprintf(stderr, "Failed to retrieve input stream information");
        goto end;
    }

    /**
     * 打印输入或输出格式的详细信息，
     * is_output:0表示input，1表示output
     */
    av_dump_format(in_avfmt_ctx, 0, pathIn, 0);

    /**
     * 为输出格式初始化AVFormatContext指针
     */
    avformat_alloc_output_context2(&out_avfmt_ctx, NULL, NULL, outPath);
    if (!out_avfmt_ctx) {
        fprintf(stderr, "Could not create output context\n");
        ret = AVERROR_UNKNOWN;
        goto end;
    }

    /**
     * 输出文件的格式，只有在封装时使用，必须在调用avformat_write_header()前初始化
     */
    avOutFmt = out_avfmt_ctx->oformat;

    /**
     * AVFormatContext 结构体中定义了AVStream **streams 数组;
     * nb_streams即为数组元素的个数
     */
    for (int i = 0; i < in_avfmt_ctx->nb_streams; ++i) {
        AVStream *outAS = NULL;
        AVStream *intAS = in_avfmt_ctx->streams[i];
        /**
         * 当前流的编解码参数，
         * avformat_new_stream()调用后会初始化，
         * avformat_free_context()调用后会被释放。
         * 解封装：在创建流的时候或者avformat_find_stream_info()调用后，被初始化；
         * 封装：avformat_write_header()调用前，手动初始化
         */
        AVCodecParameters *inAvcP = intAS->codecpar;
        /**
         * 如果输入多媒体文件的当前遍历到的流的 媒体类型不是音频、视频、字幕，那么stream_mapping[i]赋值为-1
         */
        if (inAvcP->codec_type != AVMEDIA_TYPE_AUDIO
            && inAvcP->codec_type != AVMEDIA_TYPE_VIDEO
            && inAvcP->codec_type != AVMEDIA_TYPE_SUBTITLE) {
            continue;
        }
        /**
         * 创建一个用于输出的AVStream指针对象
         */
        outAS = avformat_new_stream(out_avfmt_ctx, NULL);
        if (!outAS) {
            fprintf(stderr, "Failed allocating output stream\n");
            ret = AVERROR_UNKNOWN;
            goto end;
        }
        /**
         * 输出的AVCodecParameters指针所占内存被释放，然后将输入的AVCodecParameters指针内存拷贝到输出的AVCodecParameters中
         */
        ret = avcodec_parameters_copy(outAS->codecpar, inAvcP);
        if (ret < 0) {
            fprintf(stderr, "Failed to copy codec parameters\n");
            goto end;
        }
        /**
         * Additional information about the codec (corresponds to the AVI FOURCC).
         * uint32_t odec_tag;
         * 为编解码器添加额外信息，这里懵逼了,这行不写，输出视频文件会有毛病
         */
        outAS->codecpar->codec_tag = 0;
    }

    /**
     * 打印输入或输出格式的详细信息，
     * is_output:0表示input，1表示output
     */
    av_dump_format(out_avfmt_ctx, 0, outPath, 1);

    /**
     * 解封装时，AVFormatContext中的AVIOContext *pb，可以在调用avformat_open_input（）之前初始化，
     * 或者通过调用avformat_open_input（）初始化
     * 封装时,AVFormatContext中的AVIOContext *pb，可以在调用avformat_write_header()之前初始化，
     * 完事后必须释放AVFormatContext中的AVIOContext *pb占用的内存
     * 如果ofmt->flags值为AVFMT_NOFILE，就不要初始化AVFormatContext中的AVIOContext *pb，在这种情况下，
     * 解封装器/封装器将会通过其它方式处理I/O，而且AVFormatContext中的AVIOContext *pb为NULL
     */
    if (!(out_avfmt_ctx->flags & AVFMT_NOFILE)) {
        /**
         * 为对应url的文件初始化一个AVIOContext 二级指针对象
         */
        ret = avio_open(&out_avfmt_ctx->pb, outPath, AVIO_FLAG_WRITE);
        if (ret < 0) {
            fprintf(stderr, "Could not open output file '%s'", pathOut);
            goto end;
        }
    }

    // 初始化流的私有数据并将流头写入输出媒体文件
    ret = avformat_write_header(out_avfmt_ctx, NULL);
    if (ret < 0) {
        fprintf(stderr, "Error occurred when opening output file\n");
        goto end;
    }

    while (true) {
        AVStream *outS, *inS;
        /**
         * 返回流的下一帧。
         * 此函数读取存储在文件中的内容到AVPacket *pkt，而不验证是否存在解码器的有效帧。
         * 它将*存储在文件中的内容分割成帧，并为每个调用返回一个AVPacket *pkt。
         * 它不会*省略有效帧之间的无效数据，以便给解码器最大的解码信息。
         * 返回0表示读取一帧成功，返回负数，表示出错了或者已经读到文件末尾了。
         */
        ret = av_read_frame(in_avfmt_ctx, &avPacket);
        if (ret < 0)
            break;

        // 初始化输入的AVStream，AVpacket 中的stream_index定义了流的索引
        inS = in_avfmt_ctx->streams[avPacket.stream_index];
        // 初始化输出的AVStream
        outS = out_avfmt_ctx->streams[avPacket.stream_index];

        /**
         * pkt.pts **乘** in_stream->time_base **除** out_stream->time_base
         * 得到out_stream下pkt的pts，输出文件的一帧数据包的pts
         * 即同步了输入输出的显示时间戳
         * pkt为从输入文件读取的一帧的数据包，
         */
        avPacket.pts = av_rescale_q_rnd(avPacket.pts, inS->time_base, outS->time_base,
                                        static_cast<AVRounding>(AV_ROUND_NEAR_INF |
                                                                AV_ROUND_PASS_MINMAX));

        /**
         * pkt.dts  **乘** in_stream->time_base **除** out_stream->time_base
         * 得到out_stream下pkt的dts ，输出文件的一帧数据包的dts
         * 即同步了输入输出的解压时间戳
         * pkt为从输入文件读取的一帧的数据包，
         */
        avPacket.dts = av_rescale_q_rnd(avPacket.dts, inS->time_base, outS->time_base,
                                        static_cast<AVRounding>(AV_ROUND_NEAR_INF |
                                                                AV_ROUND_PASS_MINMAX));

        /**
         * pkt.duration**乘** in_stream->time_base **除** out_stream->time_base
         * 得到out_stream下pkt的duration ，输出文件的一帧数据包的持续时间
         * 即同步了输入输出的持续时间戳
         * pkt为从输入文件读取的一帧的数据包，
         */
        avPacket.duration = av_rescale_q(avPacket.duration, inS->time_base, outS->time_base);

        /**
         * 将一帧数据包写入输出媒体文件。
         * 此函数将根据需要在内部缓冲数据包，以确保输出文件中的数据包按照dts的顺序正确交叉存储。
         * 调用者进行自己的交叉存储时，应该调用av_write_frame()，而不是这个函数。
         * 使用此函数而不是av_write_frame()可以使muxers提前了解未来的数据包，例如改善MP4对VFR内容在碎片模式下的行为。
         */
        ret = av_interleaved_write_frame(out_avfmt_ctx, &avPacket);
        if (ret < 0) {
            fprintf(stderr, "Error muxing packet\n");
            break;
        }

        // 清除packet占用的内存
        av_packet_unref(&avPacket);
    }

    // *将流的尾部写入输出媒体文件，并且释放其私有数据占用的内存
    av_write_trailer(out_avfmt_ctx);

    end:
    // 关闭打开的input AVFormatContext。释放其所有内容占用的内存，赋值为NULL。
    avformat_close_input(&in_avfmt_ctx);
    /* close output */
    if (out_avfmt_ctx && !(avOutFmt->flags & AVFMT_NOFILE)) {
        // 关闭被AVIOContext使用的资源，释放AVIOContext占用的内存并且置为NULL
        avio_closep(&out_avfmt_ctx->pb);
    }
    // 释输出的放AVFormatContext所有占用的内存
    avformat_free_context(out_avfmt_ctx);

    if (ret < 0 && ret != AVERROR_EOF) {
        return 1;
    }
    return 0;
}


extern "C"
JNIEXPORT jboolean JNICALL
Java_cc_ibooker_android_zmaokeplayerlib_MaoKeMuxerUtil_reMuxer(JNIEnv *env, jclass clazz,
                                                                jstring in_path, jstring out_path) {
    // 获取文件地址指针 - Java的String转为C的字符串
    const char *inPath = env->GetStringUTFChars(in_path, nullptr);
    const char *outPath = env->GetStringUTFChars(out_path, nullptr);
    // 执行格式转换
    int result = main_muxer(inPath, outPath);
    // 回收指针
    env->ReleaseStringUTFChars(in_path, inPath);
    env->ReleaseStringUTFChars(out_path, outPath);
    return static_cast<jboolean>(result == 0);
}

