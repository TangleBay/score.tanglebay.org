package de.oostech.tanglebayranking.controller;

import de.oostech.tanglebayranking.dto.NodeDTO;
import de.oostech.tanglebayranking.entity.LoadBalancer;
import de.oostech.tanglebayranking.entity.Node;
import de.oostech.tanglebayranking.entity.NodeID;
import de.oostech.tanglebayranking.service.LoadBalancerService;
import de.oostech.tanglebayranking.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class RankingController {

	@Autowired
	NodeService nodeService;

	@Autowired
	LoadBalancerService loadBalancerService;

	@GetMapping("/nodes")
	public List<NodeDTO> index() {
		return convertNodes(nodeService.findByLB("ALL"));
	}

	@GetMapping("/balancer")
	public List<LoadBalancer> findLB() {


		List<LoadBalancer> loadBalancers = loadBalancerService.list();

		for(LoadBalancer loadBalancer : loadBalancers) {

		}
		return loadBalancerService.list();
	}

	@GetMapping("/nodes/{key}")
	public NodeDTO findByKey(@PathVariable String key) {

		return new NodeDTO().parseNode(nodeService.findByNodeID(new NodeID(key, "ALL")));
	}

	private List<NodeDTO> convertNodes(Set<Node> nodes) {
		List<NodeDTO> nodeDTOS = new ArrayList<>();

		for(Node node : nodes) {
			nodeDTOS.add(new NodeDTO().parseNode(node));
		}
		return nodeDTOS;
	}
}