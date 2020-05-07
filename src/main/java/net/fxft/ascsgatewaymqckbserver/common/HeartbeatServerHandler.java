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
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
	//private static final Logger logger = LoggerFactory.getLogger(HeartbeatServerHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
            	log.info("WRITER_IDLE");
            } else if (e.state() == IdleState.READER_IDLE) {
            	log.info("READER_IDLE");
                //ctx.channel().close();
            } else if (e.state() == IdleState.ALL_IDLE) {
            	log.info("ALL_IDLE");
            	//
            	ctx.close();
            	return ;
            }
        }
        super.userEventTriggered(ctx, evt);
    }


}
