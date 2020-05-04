package de.oostech.tanglebayranking.dto;

import de.oostech.tanglebayranking.entity.Command;
import de.oostech.tanglebayranking.entity.Node;
import de.oostech.tanglebayranking.entity.NodeID;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

public class NodeDTO {

	private String name;

	private String key;

	private String address;

	private String appName;

	private String appVersion;

	private long milestone;

	private long solidMilestone;

	private boolean pow;

	private boolean available;

	private long points;

	private Set<CommandDTO> commands = new HashSet<>();

	public NodeDTO() {

	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public long getPoints() {

		return points;
	}

	public void setPoints(long points) {

		this.points = points;
	}

	public Set<CommandDTO> getCommands() {

		return commands;
	}

	public void setCommands(Set<CommandDTO> commands) {

		this.commands = commands;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public boolean isAvailable() {

		return available;
	}

	public void setAvailable(boolean available) {

		this.available = available;
	}

	public String getAppName() {

		return appName;
	}

	public void setAppName(String appName) {

		this.appName = appName;
	}

	public String getAppVersion() {

		return appVersion;
	}

	public void setAppVersion(String appVersion) {

		this.appVersion = appVersion;
	}

	public long getMilestone() {

		return milestone;
	}

	public void setMilestone(long milestone) {

		this.milestone = milestone;
	}

	public long getSolidMilestone() {

		return solidMilestone;
	}

	public void setSolidMilestone(long solidMilestone) {

		this.solidMilestone = solidMilestone;
	}

	public boolean isPow() {

		return pow;
	}

	public void setPow(boolean pow) {

		this.pow = pow;
	}

	public String getKey() {

		return key;
	}

	public void setKey(String key) {

		this.key = key;
	}

	public NodeDTO parseNode(Node node) {
		this.address = node.getAddress();
		this.appName = node.getAppName();
		this.appVersion = node.getAppVersion();
		this.available = node.isAvailable();
		this.key = node.getNodeID().getKey();
		this.milestone = node.getMilestone();
		this.name = node.getName();
		this.points = node.getPoints();
		this.pow = node.isPow();
		this.solidMilestone = node.getSolidMilestone();

		Set<CommandDTO> commandDTOS = new HashSet<>();
		for(Command commmand : node.getCommands()) {
			commandDTOS.add(new CommandDTO().parseCommand(commmand));
		}

		this.commands = commandDTOS;

		return this;
	}
}