package de.oostech.tanglebayranking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Node {

	private String name;

	@EmbeddedId
	private NodeID nodeID;

	private String address;

	private String appName;

	private String appVersion;

	private long milestone;

	private long solidMilestone;

	private boolean pow;

	private boolean available;

	private long points = 0;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Command> commands = new HashSet<>();

	public Node() {

	}

	public Node(Node node) {
		this.address = node.address;
		this.appName = node.appName;
		this.appVersion = node.appVersion;
		this.available = node.available;
		this.milestone = node.milestone;
		this.name = node.name;
		this.points = node.points;
		this.pow = node.pow;
		this.solidMilestone = node.solidMilestone;
	}

	public Node(String name, NodeID nodeID) {

		this.name = name;
		this.nodeID = nodeID;
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

	public Set<Command> getCommands() {

		return commands;
	}

	public void setCommands(Set<Command> commands) {

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

	public NodeID getNodeID() {

		return nodeID;
	}

	public void setNodeID(NodeID nodeID) {

		this.nodeID = nodeID;
	}
}