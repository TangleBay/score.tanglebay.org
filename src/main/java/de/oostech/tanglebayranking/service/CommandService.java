package de.oostech.tanglebayranking.service;

import de.oostech.tanglebayranking.dao.CommandDAO;
import de.oostech.tanglebayranking.dao.NodeDAO;
import de.oostech.tanglebayranking.entity.Command;
import de.oostech.tanglebayranking.entity.CommandID;
import de.oostech.tanglebayranking.entity.Node;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommandService {

	@Autowired
	private CommandDAO commandDAO;

	@Autowired
	private NodeDAO nodeDAO;

	@Autowired
	private Environment env;

	List<String> excludedKeys = null;
	List<String> excludedAppNames = null;

	public void overrideCommands(JSONArray commandArray, Node node) {

		excludedKeys = Arrays.asList(env.getProperty("tanglebay.exclude.key").split(";"));
		excludedAppNames = Arrays.asList(env.getProperty("tanglebay.exclude.appName").split(";"));

		Set<Command> commands = new HashSet<>();

		for (int j=0; j < commandArray.length() ; j++) {

			int count = commandArray.getJSONObject(j).getInt("count");
			int millis = commandArray.getJSONObject(j).getInt("millis");
			String commandName = commandArray.getJSONObject(j).getString("command");

			int factor = Integer.parseInt(env.getProperty("factor." + commandName));
			long points = (count*factor) - (millis/1000);
			points = points < 0 ? 0 : points;

/*			if(commandName.equals("broadcastTransactions") && node.getNodeID().getLbName().equals("lb0") && node.getNodeID().getKey().equals("5659c4bd7d")) {
				System.out.println("gsdfg");
			}*/

			Command command = new Command(new CommandID(commandName, node.getNodeID()), millis, count, excludedKeys.contains(node.getNodeID().getKey()) || excludedAppNames.contains(node.getAppName()) ? 0 : points);
			Command oldCommand = commandDAO.findByCommandID(command.getCommandID());

			if(oldCommand != null) {
				command.setCount(command.getCount() + oldCommand.getLastRestartCount());
				command.setLastRestartCount(oldCommand.getLastRestartCount());

				command.setPoints(excludedKeys.contains(node.getNodeID().getKey()) || excludedAppNames.contains(node.getAppName()) ? 0 : (command.getCount()*factor) - (millis/1000));
			}

			commands.add(command);
		}
		node.getCommands().clear();
		node.getCommands().addAll(commands);
	}

	public void updateCommands(JSONArray commandArray, Node node) {

		excludedKeys = Arrays.asList(env.getProperty("tanglebay.exclude.key").split(";"));
		excludedAppNames = Arrays.asList(env.getProperty("tanglebay.exclude.appName").split(";"));

		Set<Command> commands = new HashSet<>();

		for (int j=0; j < commandArray.length() ; j++) {

			int count = commandArray.getJSONObject(j).getInt("count");
			int millis = commandArray.getJSONObject(j).getInt("millis");
			String commandName = commandArray.getJSONObject(j).getString("command");

			int factor = Integer.parseInt(env.getProperty("factor." + commandName));
			long points = (count*factor) - (millis/1000);
			points = points < 0 ? 0 : points;

/*			if(commandName.equals("broadcastTransactions") && node.getNodeID().getLbName().equals("lb0") && node.getNodeID().getKey().equals("5659c4bd7d")) {
				System.out.println("gsdfg");
			}*/

			Command command = new Command(new CommandID(commandName, node.getNodeID()), millis, count, excludedKeys.contains(node.getNodeID().getKey()) || excludedAppNames.contains(node.getAppName()) ? 0 : points);
			Command oldCommand = commandDAO.findByCommandID(command.getCommandID());

			if(oldCommand != null) {
				command.setPoints(command.getPoints() + oldCommand.getPoints());
				command.setCount(command.getCount() + oldCommand.getLastRestartCount());
				command.setLastRestartCount(oldCommand.getCount());
			}
			commands.add(command);
		}
		node.getCommands().clear();
		node.getCommands().addAll(commands);
	}

	public void insertMissingOldCommands(Node node, boolean restarted) {
		Node oldNode = nodeDAO.findByNodeID(node.getNodeID());

		if(oldNode != null) {
			for(Command oldCommand : oldNode.getCommands()) {
				boolean newCommandsContainOldCommand = false;
				for(Command command : node.getCommands()) {
					if(command == null || command.getCommandID() == null || command.getCommandID().getName().equals(oldCommand.getCommandID().getName())) {
						newCommandsContainOldCommand = true;
					}
				}

				if(!newCommandsContainOldCommand) {
					oldCommand.setLastRestartCount(oldCommand.getCount());

					Command newCommand = new Command(oldCommand);

					if(restarted) {
						newCommand.setLastRestartCount(oldCommand.getCount());
					} else {
						newCommand.setLastRestartCount(oldCommand.getLastRestartCount());
					}
					newCommand.setCommandID(new CommandID(oldCommand.getCommandID().getName(), node.getNodeID()));
					node.getCommands().add(newCommand);
				}
			}
		}

	}
}