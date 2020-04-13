package com.automationanywhere.botcommand.demo;

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.UUID;

/**
 * Responsible to close the python session, started by {@code OpenCommand}
 * It cleans up all running shells for the particular script
 *
 * @author Sunil.Dabhi
 * @author reksambi  upgrading python command to package.template
 */
@com.automationanywhere.commandsdk.annotations.BotCommand
@CommandPkg(
    label = "[[CloseScript.label]]",
    node_label = "[[CloseScript.node_label]]",
    name = "python.commands.closeScript",
    icon = "closescript.svg",
    description = "[[CloseScript.description]]"
)
public class CloseScript {
    private static Logger logger = LogManager.getLogger();

    @Sessions
    private Map<String, Object> sessionMap;

    @com.automationanywhere.commandsdk.annotations.GlobalSessionContext
    private com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext;

    @Execute
    public void close(
        @Idx(index = "1", type = AttributeType.TEXT)
        @Pkg(label = "[[CloseScript.session.label]]", default_value_type = DataType.STRING, default_value = "Default")
        @NotEmpty String session
    ) {

        logger.traceEntry("Python: Close command  session:{}", session);
        UUID functionId = null;
        try {
            if (!sessionMap.containsKey(session)) {
                throw new IllegalStateException(Constants.MESSAGES.getString("cmd.python.session.not.opened", session));
            }
            //Call Close Function
            functionId = (UUID) sessionMap.get(session);
            logger.debug("Python: Close function for external id: {}", functionId.toString());

            globalSessionContext.getExternalEnvironment().closeFunction(functionId);
            logger.debug("Python: session: {}, closeFunction called on external environment for function id: {}", session, functionId);

            sessionMap.remove(session);
            logger.debug("Python: session: {}, removed from the session map", session);

        } catch (Exception e) {
            logger.error("Python Close command exception occurred - ", e);
            throw new BotCommandException(Constants.MESSAGES.getString("cmd.python.error.closing.session", session), e);
        } finally {
            logger.traceExit("Python: Close command execution ended");
        }
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    public void setGlobalSessionContext(com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext) {
        this.globalSessionContext = globalSessionContext;
    }
}
