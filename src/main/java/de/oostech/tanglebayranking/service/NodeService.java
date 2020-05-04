package de.oostech.tanglebayranking.service;

import de.oostech.tanglebayranking.dao.LoadBalancerDAO;
import de.oostech.tanglebayranking.dao.NodeDAO;
import de.oostech.tanglebayranking.entity.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NodeService {

	@Autowired
	private NodeDAO nodeDAO;

	@Autowired
	private LoadBalancerDAO loadBalancerDAO;

	@Autowired
	private CommandService commandService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private Environment env;

	List<String> excludedKeys = null;
	List<String> excludedAppNames = null;

	public Set<Node> list() {
		Set<Node> nodes = nodeDAO.findAllSortByPoints();
		return nodes;
	}

	public Node findByNodeID(NodeID nodeID) {

		return nodeDAO.findByNodeID(nodeID);
	}

	@Transactional
	public void overrideNodes(LoadBalancer loadBalancer, JSONArray nodeArray, boolean restarted) {
		try {
			excludedKeys = Arrays.asList(env.getProperty("tanglebay.exclude.key").split(";"));
			excludedAppNames = Arrays.asList(env.getProperty("tanglebay.exclude.appName").split(";"));

			Set<Node> nodes = new HashSet<>();

			for (int i=0; i < nodeArray.length() ; i++) {
				JSONArray commandArray = nodeArray.getJSONObject(i).getJSONArray("commands");

				Node node = parseNodeJSON(nodeArray.getJSONObject(i), loadBalancer.getId());

				if(restarted) {
					commandService.updateCommands(commandArray, node);
				} else {
					commandService.overrideCommands(commandArray, node);
				}
				commandService.insertMissingOldCommands(node, restarted);

				nodes.add(node);
			}
			loadBalancer.getNodes().clear();
			loadBalancer.getNodes().addAll(nodes);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (HttpClientErrorException e) {
			System.out.println("http error");
		}

	}

	public Node parseNodeJSON(JSONObject nodeJSON, String lbName) {
		Node node = new Node();

		node.setName(nodeJSON.getString("name"));
		node.setNodeID(new NodeID(nodeJSON.getString("key"), lbName));
		node.setAppName(nodeJSON.getString("appName"));
		node.setAppVersion(!nodeJSON.isNull("appVersion") ? nodeJSON.getString("appVersion") : null);
		node.setMilestone(nodeJSON.getLong("milestone"));
		node.setSolidMilestone(nodeJSON.getLong("solidMilestone"));
		node.setPow(nodeJSON.getBoolean("pow"));

		boolean available = false;
		if(!nodeJSON.isNull("available")) {
			available = nodeJSON.getBoolean("available");
			node.setAvailable(available);
		}

		String address = null;
		if(!nodeJSON.isNull("address")) {
			address = nodeJSON.getString("address");
			node.setAddress(address);
		}

		return node;
	}

	public void calcNodeScore() {
		Optional<LoadBalancer> allBalancerOpt = loadBalancerDAO.findById("ALL");
		LoadBalancer allBalancer = null;

		if(!allBalancerOpt.isPresent()) {
			allBalancer = new LoadBalancer();
			allBalancer.setId("ALL");
		} else {
			allBalancer = allBalancerOpt.get();
		}

		Set<Node> nodes = nodeDAO.findAllExcept("ALL");
		Set<Node> aggregatedNodes = new HashSet<>();

		for(Node node : nodes) {
			List<Node> nodeSet = aggregatedNodes.stream().filter(c -> c.getNodeID().getKey().equals(node.getNodeID().getKey())).collect(Collectors.toList());

			if(nodeSet.isEmpty()) {
				Node newNode = new Node(node);
				newNode.setNodeID(new NodeID(node.getNodeID().getKey(), "ALL"));

				for(Command command : node.getCommands()) {
					Command newCommand = new Command(command);
					newCommand.setCommandID(new CommandID(command.getCommandID().getName(), newNode.getNodeID()));
					newNode.getCommands().add(newCommand);
				}
				aggregatedNodes.add(newNode);
			} else {
				for(Command command : node.getCommands()) {
					List<Command> commandList = nodeSet.get(0).getCommands().stream().filter(c -> c.getCommandID().getName().equals(command.getCommandID().getName())).collect(Collectors.toList());

					if(commandList.isEmpty()) {
						Command newCommand = new Command(command);
						newCommand.setCommandID(new CommandID(command.getCommandID().getName(), nodeSet.get(0).getNodeID()));
						nodeSet.get(0).getCommands().add(newCommand);
					} else {
						commandList.get(0).setCount(command.getCount() + commandList.get(0).getCount());
						commandList.get(0).setPoints(command.getPoints() + commandList.get(0).getPoints());
						commandList.get(0).setMillis(command.getMillis() + commandList.get(0).getMillis());
					}
				}
			}
		}

		for(Node node : aggregatedNodes) {
			long points = 0;

			for(Command command : node.getCommands()) {
				points += command.getPoints();
			}
			node.setPoints(points);
		}

		allBalancer.getNodes().clear();
		allBalancer.getNodes().addAll(aggregatedNodes);
		loadBalancerDAO.save(allBalancer);
	}

	public Set<Node> findByLB(String name) {
		return nodeDAO.findByLB(name);
	}
}