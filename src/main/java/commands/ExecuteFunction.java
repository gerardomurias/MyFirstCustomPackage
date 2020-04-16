package commands;

import com.automationanywhere.bot.service.ExternalInput;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.*;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.VariableType;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import helper.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Function;

/**
 * Responsible for executing a python function with given arguments.
 * it also returns the value returned by the function as {@code Optional<Value>}
 *
 * @author Sunil.Dabhi
 * @author reksambi  upgrading python command to package.template
 */
@com.automationanywhere.commandsdk.annotations.BotCommand
@CommandPkg(
	label = "[[ExecuteFunction.label]]",
	node_label = "[[ExecuteFunction.node_label]]",
	description = "[[ExecuteFunction.description]]",
	name = "python.commands.executeFunction",
	icon = "executescript.svg",
	return_label = "[[ExecuteFunction.return_label]]",
	return_type = DataType.STRING
)
public class ExecuteFunction {
	private static Logger logger = LogManager.getLogger();

	//Primitive value converters
	private Function<StringValue, String> strValueGetter = (str) ->  str.get();
	private Function<NumberValue, Number> numValueGetter = (num) ->  num.get();
	private Function<BooleanValue, Boolean> boolValueGetter = (bVal) ->  bVal.get();

	@Sessions
	private Map<String, Object> sessionMap;

	@com.automationanywhere.commandsdk.annotations.GlobalSessionContext
	private com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext;

	@Execute
	public Value execute(
		@Idx(index = "1", type = AttributeType.TEXT)
		@Pkg(label = "[[ExecuteFunction.session.label]]", default_value_type = DataType.STRING, default_value = "Default")
		@NotEmpty String session,

		@Idx(index = "2", type = AttributeType.TEXT)
		@Pkg(label = "[[ExecuteFunction.functionName.label]]") String functionName,

		@Idx(index = "3", type = AttributeType.VARIABLE)
		@VariableType(DataType.ANY)
		@Pkg(label = "[[ExecuteFunction.argument.label]]", description="[[ExecuteFunction.argument.description]]") Value argument
	) {
		try {
			logger.traceEntry("Python: ExecuteFunction command started - session:{}, functionName:{}, arguments:{}", session, functionName, argument);

			if (!sessionMap.containsKey(session)) {
				throw new IllegalStateException(Constants.MESSAGES.getString("cmd.python.session.not.opened", session));
			}

			UUID functionId = (UUID) sessionMap.get(session);
			Object args = handleFunctionArguments(argument);
			String inputString = JsonUtil.toJson(getExternalInput(functionName, args));
			logger.debug("session: {}, callFunction: {}, input:{}", session, functionId, inputString);

			String result = globalSessionContext.getExternalEnvironment().callFunction(functionId, inputString);
			logger.debug("session: {}, callFunction returned: {}", session, result);

			return new StringValue(String.valueOf(result));

		} catch (Exception ex) {
			logger.error("Python: ExecuteFunction command exception occurred - ", ex);
			throw new BotCommandException(Constants.MESSAGES.getString("cmd.python.failed.execution"), ex);
		} finally {
			logger.traceExit("Python: ExecuteFunction command execution ended.");
		}
	}

	private ExternalInput getExternalInput(String functionName, Object argument) {
		if (Objects.isNull(argument)) {
			return new ExternalInput(functionName);
		}
		return new ExternalInput(functionName, argument);
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public void setGlobalSessionContext(com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext) {
		this.globalSessionContext = globalSessionContext;
	}

	private Object handleFunctionArguments(Value arg) {
		return (arg != null)? convertValueToObject(arg): null;
	}

	private  Object convertValueToObject(Value arg) {
		Object obj;
		if(arg instanceof StringValue){
			obj = strValueGetter.apply((StringValue) arg);
		}
		else if(arg instanceof NumberValue) {
			obj = numValueGetter.apply((NumberValue)arg);
		}
		else if(arg instanceof BooleanValue) {
			obj = boolValueGetter.apply((BooleanValue)arg);
		}
		else if(arg instanceof ListValue) {
			obj = convertListValueToObject((ListValue)arg);
		}
		else if (arg instanceof DictionaryValue) {
			obj = convertDictionaryValueToObject((DictionaryValue) arg);
		}
		else {
			throw new BotCommandException(String.format("Input parameter '%s' is an unsupported type by the Python command", arg.getClass().getSimpleName()));
		}
		return obj;
	}

	private  Object convertListValueToObject(ListValue arg) {
		List<Value> listValue = arg.get();
		List<Object> newList = new ArrayList<>();
		listValue.forEach(val -> {
			newList.add(convertValueToObject(val));
		});
		return newList;
	}

	private  Object convertDictionaryValueToObject(DictionaryValue arg) {
		Map<String, Object> parameters = new HashMap<>();
		Map<String, Value> valueMap = arg.get();
		Set<String> keys = valueMap.keySet();
		for (String key : keys) {
			Object tempObj = convertValueToObject(valueMap.get(key));
			parameters.put(key, tempObj);
		}
		return  parameters;
	}
}
