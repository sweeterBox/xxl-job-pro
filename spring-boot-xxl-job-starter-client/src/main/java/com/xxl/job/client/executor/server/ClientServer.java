package com.xxl.job.client.executor.server;

import com.xxl.job.client.executor.Executor;
import com.xxl.job.client.task.InstanceRegistryThread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

/**
 * Copy from : https://github.com/xuxueli/xxl-rpc
 *
 * @author xuxueli 2020-04-11 21:25
 */
public class ClientServer {

    private static final Logger log = LoggerFactory.getLogger(ClientServer.class);

    private final static String PREFIX = "XXL-JOB-Client-Server";

    private final ExecutorService executorService;

    public ClientServer() {
        this.executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName(PREFIX);
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * @param port
     * @param executor
     */
    public void start(final int port, Executor executor) {
        this.executorService.submit(() -> {
            // param
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ThreadPoolExecutor handlerThreadPool = new ThreadPoolExecutor(
                    0,
                    200,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(2000),
                    r -> new Thread(r, "xxl-job-pro-client-thread-pool-" + r.hashCode()),
                    (r, threadPoolExecutor) -> {
                        throw new RuntimeException("xxl-job-pro-client-thread-pool is exhausted!");
                    });
            try {
                // start server
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel channel) {
                                channel.pipeline()
                                        // beat 3N, close if idle
                                        .addLast(new IdleStateHandler(0, 0, 30 * 3, TimeUnit.SECONDS))
                                        .addLast(new HttpServerCodec())
                                        // merge request & reponse to FULL
                                        .addLast(new HttpObjectAggregator(5 * 1024 * 1024))
                                        .addLast(new EmbedHttpServerHandler(executor, handlerThreadPool));
                            }
                        })
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                // bind
                ChannelFuture future = bootstrap.bind(port).sync();
                log.info("xxl-job client server start success, class = {}, port = {}", ClientServer.class, port);
                // wait util stop
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.info("xxl-job remoting server stop. {}", e.getMessage());
            } catch (Exception e) {
                log.error("xxl-job remoting server error. {}", e.getMessage());
            } finally {
                // stop
                try {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public void stop(){
        if (!this.executorService.isShutdown()) {
            this.executorService.shutdown();
        }
        // stop registry
        stopRegistry();
        log.info("xxl-job-pro client server destroy success.");
    }


    private void stopRegistry() {
        // stop registry
        InstanceRegistryThread.getInstance().toStop();
    }
}
