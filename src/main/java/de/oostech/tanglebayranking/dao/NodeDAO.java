package de.oostech.tanglebayranking.dao;

import de.oostech.tanglebayranking.entity.Command;
import de.oostech.tanglebayranking.entity.Node;
import de.oostech.tanglebayranking.entity.NodeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface NodeDAO extends JpaRepository<Node, Long> {

	public Node findByNodeID(NodeID nodeID);

	public Node findByName(String name);

	@Transactional
	@Query(value = "SELECT DISTINCT * from node ORDER BY points DESC", nativeQuery = true)
	Set<Node> findAllSortByPoints();

	@Transactional
	@Query(value = "SELECT DISTINCT * from node where lb_name = :lb_name ORDER BY points DESC", nativeQuery = true)
	Set<Node> findByLB(@Param("lb_name") String lb_name);

	@Transactional
	@Query(value = "SELECT DISTINCT * from node where NOT LB_NAME = :lb_name", nativeQuery = true)
	Set<Node> findAllExcept(@Param("lb_name") String lb_name);

}