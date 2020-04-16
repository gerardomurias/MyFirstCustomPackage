package commands;

import com.automationanywhere.bot.service.SupportedLanguage;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.CodeType;
import com.automationanywhere.commandsdk.annotations.rules.FileExtension;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.annotations.rules.RepositoryFile;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import helper.AttributeConstant;
import helper.AttributeValueUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import java.util.UUID;
import commands.Constants.*;

import static commands.Constants.*;


/**
 * Responsible to open python script with all runtime checks and
 * making script available as python session to execute methods
 *
 * @author Sunil.Dabhi
 * @author reksambi  upgrading python command to package.template
 */
@BotCommand
@CommandPkg(
	label = "[[OpenScript.label]]",
	node_label = "[[OpenScript.node_label]]",
	name = "python.commands.openScript",
	icon = "openscript.svg",
	description = "[[OpenScript.description]]")
public class OpenScript {

	private static Logger logger = LogManager.getLogger();

	@Idx(index = "2.1.1", type = AttributeType.FILE)
	@Pkg(label = "[[OpenScript.file.label]]")
	@NotEmpty
	@Inject
	@RepositoryFile("")
	@FileExtension("py")
	String file;

	@Idx(index = "2.2.2", type = AttributeType.CODE)
	@Pkg(label = "[[OpenScript.script.label]]")
	@NotEmpty
	@CodeType(value = "text/x-python")
	@Inject
	String script;

	@Parameters
	private Map<String, Value> parameters;

	@Sessions
	Map<String, Object> sessionMap;

	
	@com.automationanywhere.commandsdk.annotations.GlobalSessionContext
	private com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext;

	private AttributeValueUtil _attributeValueUtil = new AttributeValueUtil();

	@Execute
	public void open(
		@Idx(index = "1", type = AttributeType.TEXT)
		@Pkg(label = "[[OpenScript.session.label]]", default_value_type = DataType.STRING, default_value = "Default")
		@NotEmpty String session,

		@Idx(index = "2", type = AttributeType.RADIO, options = {
				@Idx.Option(index = "2.1", pkg = @Pkg(label = "[[OpenScript.import_python_file.label]]", value = FILE)),
				@Idx.Option(index = "2.2", pkg = @Pkg(label = "[[OpenScript.manual_python_script.label]]", value = SCRIPT))
		})
		@Pkg(label = "[[OpenScript.scriptOption.label]]", default_value_type = DataType.STRING, default_value = "FILE")
		@CodeType(value = "text/x-python") String scriptOption,

		@Idx(index = "3", type = AttributeType.RADIO, options = {
				@Idx.Option(index = "3.1", pkg = @Pkg(label = "[[OpenScript.python_version_2.label]]", value = PYTHON_VERSION_2)),
				@Idx.Option(index = "3.2", pkg = @Pkg(label = "[[OpenScript.python_version_3.label]]", value = PYTHON_VERSION_3))
		})
		@Pkg(label = "[[OpenScript.version.label]]", default_value_type = DataType.STRING, default_value = "3")
		@CodeType(value = "text/x-python") String version
	) {

		UUID functionId = null;
		logger.traceEntry("Python: OpenScript command execution started - session:{}, version:{}", session, version);
		try {
			String uuid = AttributeValueUtil.getStringValue(parameters, AttributeConstant.ID);
			logger.debug("uuid: {}", uuid);

			functionId = UUID.fromString(uuid);
			logger.debug("calling globalSessionContext:externalEnvironment:openFunction: id: {}", functionId.toString());
			globalSessionContext.getExternalEnvironment().openFunction(functionId, getLanguage(version));
			logger.debug("external environment -> openFunction() called with session {}", session);
			sessionMap.putIfAbsent(session, functionId);
			logger.debug("functionId is put in sessionMap successfully");

		} catch (Exception e) {

			cleanUp(functionId);
			logger.error("Python: OpenScript command exception occurred - ", e);
			throw new BotCommandException(getErrorMessage(session, scriptOption), e);
		} finally {
			logger.traceExit("Python: OpenScript command execution ended.");
		}
	}

	private SupportedLanguage getLanguage(String version) {
		switch (version) {
			case "2":
				return SupportedLanguage.PYTHON_2;
			case "3":
				return SupportedLanguage.PYTHON_3;
			default:
				throw new BotCommandException(MESSAGES.getString("cmd.python.version.not.supported", version));
		}
	}

	private String getErrorMessage(String sessionName, String scriptOption) {
		StringBuilder errorMsg = new StringBuilder();
		errorMsg.append(MESSAGES.getString("cmd.python.error.opening.file")).append("\"").append(sessionName).append("\".");
		if (scriptOption.equalsIgnoreCase("FILE")) {
			errorMsg.append(MESSAGES.getString("cmd.python.error.opening.file")).append(file);
		} else {
			errorMsg.append(MESSAGES.getString("cmd.python.error.opening.script"));
		}
		return errorMsg.toString();
	}


	public void setFile(String file) {
		this.file = file;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setParameters(Map<String, Value> parameters) {
		this.parameters = parameters;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public void setAttributeValueUtil(AttributeValueUtil attributeValueUtil){
		_attributeValueUtil = attributeValueUtil;
	}

	public void setGlobalSessionContext(com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext) {
		this.globalSessionContext = globalSessionContext;
	}

	private void cleanUp(UUID functionId) {
		if (functionId != null) {
			globalSessionContext.getExternalEnvironment().closeFunction(functionId);
		}
	}
}
