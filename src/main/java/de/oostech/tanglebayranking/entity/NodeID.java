package de.oostech.tanglebayranking.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Embeddable
public class NodeID implements Serializable {

	private String key;

	private String lbName;

	public NodeID() {

	}
	public NodeID(String key, String lbName) {

		this.key = key;
		this.lbName = lbName;
	}

	public String getKey() {

		return key;
	}

	public void setKey(String key) {

		this.key = key;
	}

	public String getLbName() {

		return lbName;
	}

	public void setLbName(String lbName) {

		this.lbName = lbName;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NodeID nodeID = (NodeID) o;
		return Objects.equals(key, nodeID.key) && Objects.equals(lbName, nodeID.lbName);
	}

	@Override
	public int hashCode() {

		return Objects.hash(key, lbName);
	}
}