package commands;

import com.automationanywhere.bot.service.ExternalInput;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import helper.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.UUID;

/**
 * Responsible for executing a python function with given arguments.
 * it also returns the value returned by the function as {@code Optional<Value>}
 *
 * @author Sunil.Dabhi
 * @author reksambi  upgrading python command to package.template
 */
@com.automationanywhere.commandsdk.annotations.BotCommand
@CommandPkg(
    label = "[[ExecuteScript.label]]",
    node_label = "",
    description = "[[ExecuteScript.description]]",
    name = "python.commands.executeScript",
    icon = "executescript.svg",
    return_label = "[[ExecuteScript.return_label]]",
    return_type = DataType.STRING
)
public class ExecuteScript {
    private static Logger logger = LogManager.getLogger();

    @Sessions
    private Map<String, Object> sessionMap;

    @com.automationanywhere.commandsdk.annotations.GlobalSessionContext
    private com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext;

    @Execute
    public Value execute(
        @Idx(index = "1", type = AttributeType.TEXT)
        @Pkg(label = "[[ExecuteScript.session.label]]", default_value_type = DataType.STRING, default_value = "Default")
        @NotEmpty String session
    ) {
        UUID functionId = null;
        String argument = "";
        logger.traceEntry("Python: ExecuteScript command started - session:{}", session);
        try {
            if (!sessionMap.containsKey(session)) {
                throw new IllegalStateException(Constants.MESSAGES.getString("cmd.python.session.not.opened", session));
            }

            functionId = (UUID) sessionMap.get(session);
            String inputString = JsonUtil.toJson(getExternalInput(argument));
            logger.debug("session: {}, callFunction: {}, input:{}", session, functionId, inputString);

            String result = globalSessionContext.getExternalEnvironment().callFunction(functionId, inputString);
            logger.debug("session: {}, callFunction returned: {}", session, result);

            return new StringValue(String.valueOf(result));

        } catch (Exception ex) {
            logger.error("Python: ExecuteScript command exception occurred - ", ex);
            throw new BotCommandException(Constants.MESSAGES.getString("cmd.python.failed.execution"), ex);
        } finally {
            logger.traceExit("Python: ExecuteScript command ended.");
        }
    }

    private ExternalInput getExternalInput(String argument) {
        if (argument == null || argument.isEmpty()) {
            return new ExternalInput(null);
        }
        return new ExternalInput(null, argument);
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    public void setGlobalSessionContext(com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext) {
        this.globalSessionContext = globalSessionContext;
    }
}
