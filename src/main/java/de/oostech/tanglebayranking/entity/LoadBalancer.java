package de.oostech.tanglebayranking.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class LoadBalancer {

	@Id
	private String id;

	String restartTimestamp;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Node> nodes = new HashSet<>();

	public LoadBalancer() {

	}

	public LoadBalancer(String id, String restartTimestamp) {
		this.id = id;
		this.restartTimestamp = restartTimestamp;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public Set<Node> getNodes() {

		return nodes;
	}

	public void setNodes(Set<Node> nodes) {

		this.nodes = nodes;
	}

	public String getRestartTimestamp() {

		return restartTimestamp;
	}

	public void setRestartTimestamp(String restartTimestamp) {

		this.restartTimestamp = restartTimestamp;
	}
}