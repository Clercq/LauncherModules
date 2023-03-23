package pro.gravit.launchermodules.sentryl;

import io.sentry.EventProcessor;
import io.sentry.Hint;
import io.sentry.SentryEvent;
import pro.gravit.launchermodules.sentryl.utils.OshiUtils;
import pro.gravit.utils.helper.JVMHelper;

public class SentryEventProcessor implements EventProcessor {
    @Override
    public SentryEvent process(SentryEvent event, Hint hint) {
        if(SentryModule.config.collectMemoryInfo) {
            event.getContexts().put("Memory info", OshiUtils.makeMemoryProperties());
        }
        long uptime = JVMHelper.RUNTIME_MXBEAN.getUptime();
        event.getContexts().put("Uptime", String.format("%ds %dms", uptime / 1000, uptime % 1000));
        return event;
    }
}
