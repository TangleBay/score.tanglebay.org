package de.oostech.tanglebayranking.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommandID implements Serializable {

	private String name;
	private NodeID nodeID;

	public CommandID() {

	}

	public CommandID(String name, NodeID nodeID) {

		this.name = name;
		this.nodeID = nodeID;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public NodeID getNodeID() {

		return nodeID;
	}

	public void setNodeID(NodeID nodeID) {

		this.nodeID = nodeID;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CommandID commandID = (CommandID) o;
		return Objects.equals(name, commandID.name) && Objects.equals(nodeID, commandID.nodeID);
	}

	@Override
	public int hashCode() {

		return Objects.hash(name, nodeID);
	}
}