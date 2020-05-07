package net.fxft.ascsgatewaymqckbserver.listen;


import net.fxft.ascsgatewaymqckbserver.common.api.SocketApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.err.println("============Close 执行=========== ");

		Map<String, SocketApplication> result = event.getApplicationContext().getBeansOfType(SocketApplication.class);

		for (Map.Entry<String, SocketApplication> app : result.entrySet()) {
			System.err.println(app.getKey());
			app.getValue().shutdown();
		}
	}
}