package com.automationanywhere.botcommand.demo;

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
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class OpenScriptCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(OpenScriptCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    OpenScript command = new OpenScript();
    if(parameters.get("session") == null || parameters.get("session").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","session"));
    }

    if(parameters.get("scriptOption") == null || parameters.get("scriptOption").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","scriptOption"));
    }
    if(parameters.get("scriptOption") != null && parameters.get("scriptOption").get() != null && !(parameters.get("scriptOption").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","scriptOption", "String", parameters.get("scriptOption").get().getClass().getSimpleName()));
    }
    if(parameters.get("scriptOption") != null) {
      switch((String)parameters.get("scriptOption").get()) {
        case "FILE" : {
          if(parameters.get("file") == null || parameters.get("file").get() == null) {
            throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","file"));
          }
          if(parameters.containsKey("file")) {
            String filePath= ((String)parameters.get("file").get());
            int lastIndxDot = filePath.lastIndexOf(".");
            if (lastIndxDot == -1 || lastIndxDot >= filePath.length()) {
              throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.FileExtension","file","py"));
            }
            String fileExtension = filePath.substring(lastIndxDot + 1);
            if(!Arrays.stream("py".split(",")).anyMatch(fileExtension::equalsIgnoreCase))  {
              throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.FileExtension","file","py"));
            }
            try {
              command.setFile( parameters.get("file") != null ? (String)parameters.get("file").get() : (String) null);
            }
            catch (ClassCastException e) {
              throw new BotCommandException(MESSAGES_GENERIC.getString("generic.ClassCastException","file", "String", parameters.get("file") != null ? (parameters.get("file").get() != null ? parameters.get("file").get().getClass().toString() : "null") : "null"),e);
            }

          }

        } break;
        case "SCRIPT" : {
          if(parameters.get("script") == null || parameters.get("script").get() == null) {
            throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","script"));
          }
          try {
            command.setScript( parameters.get("script") != null ? (String)parameters.get("script").get() : (String) null);
          }
          catch (ClassCastException e) {
            throw new BotCommandException(MESSAGES_GENERIC.getString("generic.ClassCastException","script", "String", parameters.get("script") != null ? (parameters.get("script").get() != null ? parameters.get("script").get().getClass().toString() : "null") : "null"),e);
          }


        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","scriptOption"));
      }
    }

    if(parameters.get("version") == null || parameters.get("version").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","version"));
    }
    if(parameters.get("version") != null && parameters.get("version").get() != null && !(parameters.get("version").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","version", "String", parameters.get("version").get().getClass().getSimpleName()));
    }
    if(parameters.get("version") != null) {
      switch((String)parameters.get("version").get()) {
        case "2" : {

        } break;
        case "3" : {

        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","version"));
      }
    }

    command.setSessionMap(sessionMap);
    command.setParameters(parameters);
    command.setGlobalSessionContext(globalSessionContext);
    if(parameters.get("session") != null && parameters.get("session").get() != null && !(parameters.get("session").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","session", "String", parameters.get("session").get().getClass().getSimpleName()));
    }
    if(parameters.get("scriptOption") != null && parameters.get("scriptOption").get() != null && !(parameters.get("scriptOption").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","scriptOption", "String", parameters.get("scriptOption").get().getClass().getSimpleName()));
    }
    if(parameters.get("version") != null && parameters.get("version").get() != null && !(parameters.get("version").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","version", "String", parameters.get("version").get().getClass().getSimpleName()));
    }
    try {
      command.open(parameters.get("session") != null ? (String)parameters.get("session").get() : (String)null ,parameters.get("scriptOption") != null ? (String)parameters.get("scriptOption").get() : (String)null ,parameters.get("version") != null ? (String)parameters.get("version").get() : (String)null );Optional<Value> result = Optional.empty();
      logger.traceExit(result);
      return result;
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","open"));
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
