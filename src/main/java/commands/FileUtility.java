package commands;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.io.File;

@BotCommand
@CommandPkg(label="FileUtility", name="FileUtility", description="File Utility", icon="pkg.svg",
node_label="Get Size of {{fileName}}",
return_type=STRING, return_label="Assign the output to variable", return_required=true)
public class FileUtility {
	
	private static final Messages MESSAGES = MessagesFactory.getMessages("com.automationanywhere.botcommand.demo.messages");
	
	@Execute
	public Value<String> action(@Idx(index = "1", type = TEXT) @Pkg(label = "File Path", default_value_type = STRING) @NotEmpty String filePath) {

		File file = new File(filePath);
		String result = String.valueOf(file.length());

		return new StringValue(result);
	}
}