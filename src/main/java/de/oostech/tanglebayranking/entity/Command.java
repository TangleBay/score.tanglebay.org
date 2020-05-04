package de.oostech.tanglebayranking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Command {

	@EmbeddedId
	private CommandID commandID;

	private long count;

	private long millis;

	private long points;

	@JsonIgnore
	private long lastRestartCount = 0;

	public Command(CommandID commandID, long millis, long count, long points) {

		this.commandID = commandID;
		this.millis = millis;
		this.count = count;
		this.points = points;
	}

	public Command(Command command) {
		this.count = command.getCount();
		this.millis = command.getMillis();
		this.points = command.getPoints();
	}

	public Command(long millis, long count) {

		this.millis = millis;
		this.count = count;
	}

	public Command() {

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

	public CommandID getCommandID() {

		return commandID;
	}

	public void setCommandID(CommandID commandID) {

		this.commandID = commandID;
	}

	public long getLastRestartCount() {

		return lastRestartCount;
	}

	public void setLastRestartCount(long lastRestartCount) {

		this.lastRestartCount = lastRestartCount;
	}
}
