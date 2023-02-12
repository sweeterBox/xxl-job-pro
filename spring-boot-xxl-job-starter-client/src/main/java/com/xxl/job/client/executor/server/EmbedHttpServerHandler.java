package com.xxl.job.client.executor.server;

import com.xxl.job.client.executor.Executor;
import com.xxl.job.client.executor.model.*;
import com.xxl.job.utils.HttpClient;
import com.xxl.job.client.utils.ThrowableUtil;
import com.xxl.job.utils.JsonUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * netty_http
 * <p>
 * Copy from : https://github.com/xuxueli/xxl-rpc
 *
 * @author xuxueli 2015-11-24 22:25:15
 *
 */
public class EmbedHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(EmbedHttpServerHandler.class);

    private Executor executor;

    private ThreadPoolExecutor handlerThreadPool;

    public EmbedHttpServerHandler(Executor executor, ThreadPoolExecutor handlerThreadPool) {
        this.executor = executor;
        this.handlerThreadPool = handlerThreadPool;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // request parse
        //final byte[] requestBytes = ByteBufUtil.getBytes(msg.content());    // byteBuf.toString(io.netty.util.CharsetUtil.UTF_8);
        String requestData = msg.content().toString(CharsetUtil.UTF_8);
        String uri = msg.uri();
        HttpMethod httpMethod = msg.method();
        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        String accessTokenReq = msg.headers().get(HttpClient.XXL_JOB_ACCESS_TOKEN);

        // invoke
        handlerThreadPool.execute(() -> {
            // do invoke
            Object responseData = process(httpMethod, uri, requestData, accessTokenReq);

            // to json
            String responseJson = JsonUtils.obj2Json(responseData);

            // write response
            writeResponse(ctx, keepAlive, responseJson);
        });
    }

    private Object process(HttpMethod httpMethod, String uri, String requestData, String accessTokenReq) {
        // valid
        if (HttpMethod.POST != httpMethod) {
            log.error("invalid request, HttpMethod not support.");
            return new R<String>(405, "invalid request, HttpMethod not support.");
        }
        if (uri == null || uri.trim().length() == 0) {
            log.error("invalid request, uri-mapping empty.");
            return new R<String>(400, "invalid request, uri-mapping empty.");
        }

        // services mapping
        try {
            switch (uri) {
                case "/beat":
                    return executor.beat();
                case "/idleBeat":
                    IdleBeatParam idleBeatParam = JsonUtils.json2Obj(requestData, IdleBeatParam.class);
                    return executor.idleBeat(idleBeatParam);
                case "/run":
                    TriggerParam triggerParam = JsonUtils.json2Obj(requestData, TriggerParam.class);
                    return executor.run(triggerParam);
                case "/kill":
                    KillParam killParam = JsonUtils.json2Obj(requestData, KillParam.class);
                    return executor.kill(killParam);
                case "/log":
                    LogParam logParam = JsonUtils.json2Obj(requestData, LogParam.class);
                    return executor.log(logParam);
                case "/tasks":
                    return executor.tasks();
                default:
                    return new R<String>(R.FAIL_CODE, "invalid request, uri-mapping(" + uri + ") not found.");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new R<String>(R.FAIL_CODE, "request error:" + ThrowableUtil.toString(e));
        }
    }

    /**
     * write response
     */
    private void writeResponse(ChannelHandlerContext ctx, boolean keepAlive, String responseJson) {
        // write response
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(responseJson, CharsetUtil.UTF_8));   //  Unpooled.wrappedBuffer(responseJson)
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(" xxl-job provider netty_http server caught exception", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();      // beat 3N, close if idle
            log.debug(" xxl-job provider netty_http server close an idle channel.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
