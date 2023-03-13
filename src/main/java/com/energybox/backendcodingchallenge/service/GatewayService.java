package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import org.springframework.stereotype.Service;

import com.energybox.backendcodingchallenge.repository.GatewayRepository;

import java.util.List;
import java.util.Optional;


@Service
public class GatewayService {

    private final GatewayRepository gatewayRepository;

    public GatewayService(GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    public Gateway createOrPutGateway(Gateway gateway){
        return gatewayRepository.save(gateway);
    }

    public List<Gateway> getAllGateways() {
        return gatewayRepository.findAll();
    }

    public Optional<Gateway> getGatewayById(Long id) { return gatewayRepository.findById(id); }

}
