package de.oostech.tanglebayranking.dao;

import de.oostech.tanglebayranking.entity.LoadBalancer;
import de.oostech.tanglebayranking.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LoadBalancerDAO extends JpaRepository<LoadBalancer, String> {

}