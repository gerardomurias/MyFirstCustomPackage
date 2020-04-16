package commands;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ExecuteFunctionCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(ExecuteFunctionCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    ExecuteFunction command = new ExecuteFunction();
    if(parameters.get("session") == null || parameters.get("session").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","session"));
    }



    command.setSessionMap(sessionMap);
    command.setGlobalSessionContext(globalSessionContext);
    if(parameters.get("session") != null && parameters.get("session").get() != null && !(parameters.get("session").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","session", "String", parameters.get("session").get().getClass().getSimpleName()));
    }
    if(parameters.get("functionName") != null && parameters.get("functionName").get() != null && !(parameters.get("functionName").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","functionName", "String", parameters.get("functionName").get().getClass().getSimpleName()));
    }
    if(parameters.get("argument") != null && !(parameters.get("argument") instanceof Value)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","argument", "Value", parameters.get("argument").get().getClass().getSimpleName()));
    }
    try {
      Optional<Value> result =  Optional.ofNullable(command.execute(parameters.get("session") != null ? (String)parameters.get("session").get() : (String)null ,parameters.get("functionName") != null ? (String)parameters.get("functionName").get() : (String)null ,parameters.get("argument") != null ? (Value)parameters.get("argument") : (Value)null ));
      logger.traceExit(result);
      return result;
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","execute"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
