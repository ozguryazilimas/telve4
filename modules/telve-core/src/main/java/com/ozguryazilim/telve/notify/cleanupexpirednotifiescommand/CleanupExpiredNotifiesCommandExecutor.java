package com.ozguryazilim.telve.notify.cleanupexpirednotifiescommand;

import com.ozguryazilim.telve.auth.UserService;
import com.ozguryazilim.telve.messagebus.command.AbstractCommandExecuter;
import com.ozguryazilim.telve.messagebus.command.CommandExecutor;
import com.ozguryazilim.telve.notify.NotifyMessage;
import com.ozguryazilim.telve.notify.NotifyStore;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@CommandExecutor(command = CleanupExpiredNotifiesCommand.class)
public class CleanupExpiredNotifiesCommandExecutor extends AbstractCommandExecuter<CleanupExpiredNotifiesCommand> {

    private static final Logger LOG = LoggerFactory.getLogger(CleanupExpiredNotifiesCommandExecutor.class);

    @Inject
    private UserService userService;

    @Inject
    private NotifyStore notifyStore;

    @Override
    public void execute(CleanupExpiredNotifiesCommand command) {
        LOG.warn("Cleanup Expired Notifies Job started!");
        long startMs = System.currentTimeMillis();

        List<String> userLoginNames = userService.getLoginNames();

        //Bütün kullanıcılara ait notify ları getir
        Map<String, List<NotifyMessage>> notifies = userLoginNames.stream().collect(Collectors.toMap(Function.identity(), u -> notifyStore.getNotifies(u)));

        notifies.forEach((id, notifylist) -> {
            //Kullanıcının zamanı gecen notify ları toplayalım
            List<NotifyMessage> expiredNotifies = notifylist.stream()
                    .filter(n -> Objects.nonNull(n.getTimestamp()) && Objects.nonNull(n.getDuration()) && n.getDuration() > 0 && n.getTimestamp() + n.getDuration() < System.currentTimeMillis())
                    .collect(Collectors.toList());

            //Ilgili kullanıcının notify larını güncelleyelim
            notifyStore.clearNotifiesByNotifies(id, expiredNotifies);
        });

        LOG.warn("Cleanup Expired Notifies Job end! Took {}s", DurationFormatUtils.formatDuration(System.currentTimeMillis() - startMs, "ss.SSS"));
    }

}
