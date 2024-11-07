package com.vehiculerentakar.web.service;


import com.vehiculerentakar.web.model.Order;
import com.vehiculerentakar.web.model.Vehicule;
import com.vehiculerentakar.web.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final RestTemplate restTemplate;

    @Value("${service.order.url}")
    private String orderServiceUrl;

    @Value("${service.user.url}")
    private String userServiceUrl;

    @Value("${service.license.url}")
    private String licenseServiceUrl;

    @Autowired
    public VehiculeService(VehiculeRepository vehiculeRepository, RestTemplate restTemplate) {
        this.vehiculeRepository = vehiculeRepository;
        this.restTemplate = restTemplate;
    }

    public List<Vehicule> getAllVehicules(){
        System.out.print("Fetching all Vehiculs ||");
        return vehiculeRepository.findAll();
    }

    public Vehicule getVehiculeById(int id){
        Vehicule vehicule = vehiculeRepository.findById(id).orElse(null);
        if(vehicule == null){
            System.out.println("Vehicule not found");
        }
        System.out.print("Fetching Vehicule by id ||");
        System.out.println("Vehicul with id: " + id + " found  ||");
        System.out.println(vehicule);
        return vehicule;
    }

    public int getDisplacementByid(int id){
        System.out.print("Fetching Displacement by "+id+" ||");
        return vehiculeRepository.getDisplacementByid(id);
    }

    public int getHorsePowerByid(int id){
        System.out.print("Fetching Horsepower by "+id+" ||");
        return vehiculeRepository.getHPById(id);
    }

    public Vehicule saveVehicule(Vehicule vehicule){
        System.out.print("Vehicule " + vehicule.getBrand() + vehicule.getModel() + "with"+ vehicule.getRegistration() + " created ||");
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule getVehiculeByRegistration(String registration){
        System.out.print("Fetching Vehicule by registration ||");
        System.out.println("Vehicule with type: " + registration + " found  ||");
        return vehiculeRepository.findByRegistration(registration).orElse(null);
    }

    public List<Vehicule> findByType(String type){
        System.out.print("Fetching Vehicule by type ||");
        System.out.println("Vehicule with type: " + type + " found  ||");
        return vehiculeRepository.findbyType(type);
    }

    public Vehicule updateVehiculeById(int id, Vehicule vehicule){
        Vehicule vehiculeToUpdate = vehiculeRepository.findById(id).orElse(null);
        if (vehiculeToUpdate == null){
            System.out.println("Vehicule not found");
        } else {
            System.out.print("Vehicule " + vehicule.getBrand() + vehicule.getRegistration()+ " updated");
        return vehiculeRepository.save(vehicule);
    }
    return null;
    }

    public List<Vehicule> getByAvailability(){
        return vehiculeRepository.findByIsAvailable();
    }

    public void deleteVehicule(int id){
        vehiculeRepository.deleteById(id);
    }

    public List<Vehicule> getAvailableVehicles(LocalDate startDate, LocalDate endDate) {
        List<Vehicule> allVehicles = vehiculeRepository.findAll();
        return allVehicles.stream()
                .filter(vehicule -> isVehicleAvailable(vehicule.getId(), startDate, endDate))
                .collect(Collectors.toList());
    }
    public boolean isVehicleAvailable(int vehiculeId, LocalDate startDate, LocalDate endDate) {
        try {// http://localhost:9093/orders
            String url = UriComponentsBuilder.fromHttpUrl(orderServiceUrl + "/vehicule/" + vehiculeId)
                    .build()
                    .toUriString();
            ResponseEntity<List<Order>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Order>>() {}
            );

            List<Order> orders = response.getBody();
            if (orders == null) return true;

            return orders.stream().noneMatch(order ->
                    isDateOverlapping(
                            startDate,
                            endDate,
                            order.getStartDate(),
                            order.getEndDate()
                    )
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification de disponibilité: " + e.getMessage());
            throw new RuntimeException("Impossible de vérifier la disponibilité du véhicule", e);
        }
    }
    private boolean isDateOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return (start1.isBefore(end2) || start1.isEqual(end2))
                && (end1.isAfter(start2) || end1.isEqual(start2));
    }

    public String getTypeById(int id){
        System.out.print("Fetching type by id ||");
        return vehiculeRepository.findTypeById(id);
    }

    public int getVehiculeCargoById(int id){
        System.out.print("Fetching Cargo by id ||");
        return vehiculeRepository.findCargoById(id);
    }

    // On va demander la date de debut la date de fin puis le nombre de kilomètres effectués
    // Cela va aficher un prix avant de créer une reservation, le but ici est d'apporter un aperçu du montant au client
    // avant qu'il ne valide la reservation

    public double calculateAmountCatalog (int id, LocalDate startDate, LocalDate endDate, int estimateKm) {
        double priceBase = calculateDayBooked(startDate,endDate)*((double) vehiculeRepository.getDisplacementByid(id) /10);
        double priceKm = ((double) vehiculeRepository.getHPById(id) /100)*estimateKm;
        if (vehiculeRepository.findTypeById(id).equalsIgnoreCase("voiture")){
            double Amount = priceBase + priceKm;
            return Amount;
        } else if (vehiculeRepository.findTypeById(id).equalsIgnoreCase("moto")){
            double Amount = priceBase + vehiculeRepository.getDisplacementByid(id)*0.001 +priceKm;
            return Amount;
        } else if (vehiculeRepository.findTypeById(id).equalsIgnoreCase("utilitaire")){
            double Amount = priceBase + vehiculeRepository.findCargoById(id)*0.05 + priceKm;
            return Amount;
        }
        throw new IllegalArgumentException("Invalid vehicle type for ID: " + id);

    }
    public int calculateDayBooked(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getDays();
    }
}
