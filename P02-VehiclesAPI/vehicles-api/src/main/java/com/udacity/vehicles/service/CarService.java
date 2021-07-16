package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    @Autowired
    private CarRepository repository;

    @Qualifier("maps")
    MapsClient mapClient;

    @Qualifier("pricing")
    PriceClient priceClient;

    /**
     * TODO: Add the Maps and Pricing Web Clients you create
     *   in `VehiclesApiApplication` as arguments and set them here.
     */
    public CarService(CarRepository repository, MapsClient mapClient, PriceClient priceClient) {
        this.repository = repository;
        this.mapClient = mapClient;
        this.priceClient = priceClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        Car car = new Car();
        Optional<Car> optionalCar=repository.findById(id);

        if(optionalCar.isEmpty()){
            throw new CarNotFoundException("You have entered an invalid id");
        }
        car= optionalCar.get();
        String price =priceClient.getPrice(id);
        car.setId(id);
        car.setPrice(price);
        Location address = mapClient.getAddress(car.getLocation());
        car.setLocation(address);
        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setCondition(car.getCondition());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {

         Optional<Car> optionalCar = repository.findById(id);
         if(optionalCar.isEmpty()){
             throw new CarNotFoundException("You have entered an invalid id");
         }

         repository.deleteById(id);

    }
}
