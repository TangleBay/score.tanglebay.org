package de.oostech.tanglebayranking.service;

import de.oostech.tanglebayranking.dao.LoadBalancerDAO;
import de.oostech.tanglebayranking.dao.NodeDAO;
import de.oostech.tanglebayranking.entity.LoadBalancer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.slf4j.Logger;


@Service
public class LoadBalancerService {

	@Autowired
	private LoadBalancerDAO loadBalancerDAO;

	@Autowired
	private NodeService nodeService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private Environment env;

	Set<LoadBalancer> tempBalancers = new HashSet<>();

	Logger LOG = LoggerFactory.getLogger(LoadBalancerService.class);

	public List<LoadBalancer> list() {
		List<LoadBalancer> nodes = loadBalancerDAO.findAll();
		return nodes;
	}

	@Scheduled(fixedRateString = "${tanglebay.interval}")
	@Transactional
	public void fetchData() {
			tempBalancers.clear();

			String[] balancers = env.getProperty("tanglebay.balancer").split(";");
			try {
				for(String balancer : balancers) {
					String lBName = new URI(balancer).getHost().split("\\.")[0];
					String uptime = new JSONObject(restTemplate.getForEntity("https://" + lBName + ".tanglebay.org/actuator/health", String.class).getBody()).getJSONObject("components").getJSONObject("uptime").getJSONObject("details").getString("startup");

					LoadBalancer loadBalancer = new LoadBalancer(lBName, uptime);

					Optional<LoadBalancer> storedLB = loadBalancerDAO.findById(lBName);
					JSONArray nodes = new JSONArray(restTemplate.getForEntity(balancer, String.class).getBody());

					if(lBName.equals("lb0")) {
						System.out.println("gsdfg");
					}

					// balancer was restarted
					if(storedLB.isPresent() && !storedLB.get().getRestartTimestamp().equals(uptime)) {
						storedLB.get().setRestartTimestamp(uptime);
						LOG.info(lBName + " restarted: Stored uptime: " + storedLB.get().getRestartTimestamp() + ", new uptime: " + uptime);
						nodeService.overrideNodes(storedLB.get(), nodes, true);
						loadBalancerDAO.save(storedLB.get());
						// balancer was not restarted
					} else if(storedLB.isPresent()){
						nodeService.overrideNodes(storedLB.get(), nodes, false);
						loadBalancerDAO.save(storedLB.get());
					// balancer is new
					} else {
						nodeService.overrideNodes(loadBalancer, nodes, false);
						loadBalancerDAO.save(loadBalancer);
					}
				}
				nodeService.calcNodeScore();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (RestClientException e) {
				LOG.info("PIRI not available");
			}
	}
}