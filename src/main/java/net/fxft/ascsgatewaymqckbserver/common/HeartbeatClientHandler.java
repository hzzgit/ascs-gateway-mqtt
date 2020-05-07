package net.fxft.ascsgatewaymqckbserver.common;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Slf4j
@ChannelHandler.Sharable
public class HeartbeatClientHandler extends ChannelInboundHandlerAdapter {
	//private static final Logger logger = LoggerFactory.getLogger(HeartbeatClientHandler.class);
	
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
            	log.info("client WRITER_IDLE");
            } else if (e.state() == IdleState.READER_IDLE) {
            	TranDataProtoUtil.writeAndFlushTranData(ctx, TranDataProtoUtil.getPingInstance());
            	
            	log.info("client READER_IDLE");
            	return;
            	
            } else if (e.state() == IdleState.ALL_IDLE) {
            	log.info("client ALL_IDLE");
            	ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       /* if (msg instanceof Heartbeat) {
            if (logger.isDebugEnabled()) {
                logger.debug("Heartbeat received.");
            }

            Server server = ServerContext.getContext().getServer();
            if(server != null) {
                server.getCountInfo().getHeartbeatNum().incrementAndGet();
            }
            return;
        }*/
        super.channelRead(ctx, msg);
    }
}
