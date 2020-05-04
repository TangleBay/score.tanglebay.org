package de.oostech.tanglebayranking.dao;

import de.oostech.tanglebayranking.entity.Command;
import de.oostech.tanglebayranking.entity.CommandID;
import de.oostech.tanglebayranking.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CommandDAO extends JpaRepository<Command, Long> {
	public Command findByCommandID(CommandID commandID);

	@Transactional
	@Query(value = "SELECT DISTINCT * from command where name = :name AND id in (select commands_id from node_commands where node_key = :key);", nativeQuery = true)
	Command findByNameAndNode(@Param("name") String name, @Param("key") String key);

	@Transactional
	@Query(value = "SELECT DISTINCT * from command where id in (select commands_id from node_commands where node_key = :key);", nativeQuery = true)
	Set<Command> findByNode(@Param("key") String key);

	@org.springframework.transaction.annotation.Transactional
	@Query(value = "SELECT DISTINCT * from command where lb_name = :lb_name;", nativeQuery = true)
	Set<Command> findByLB(@Param("lb_name") String lb_name);
}