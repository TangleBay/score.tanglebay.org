package de.oostech.tanglebayranking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.oostech.tanglebayranking.entity.Command;
import de.oostech.tanglebayranking.entity.CommandID;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

public class CommandDTO {

	private String name;

	private long count;

	private long millis;

	private long points;

	public CommandDTO() {

	}

	public long getCount() {

		return count;
	}

	public void setCount(long count) {

		this.count = count;
	}

	public long getMillis() {

		return millis;
	}

	public void setMillis(long millis) {

		this.millis = millis;
	}

	public long getPoints() {

		return points;
	}

	public void setPoints(long points) {

		this.points = points;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public CommandDTO parseCommand(Command command) {
		this.count = command.getCount();
		this.millis = command.getMillis();
		this.name = command.getCommandID().getName();
		this.points = command.getPoints();

		return this;
	}
}
