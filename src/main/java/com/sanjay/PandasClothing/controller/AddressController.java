package com.sanjay.PandasClothing.controller;

import com.sanjay.PandasClothing.entity.Address;
import com.sanjay.PandasClothing.entity.User;
import com.sanjay.PandasClothing.repository.AddressRepository;
import com.sanjay.PandasClothing.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

//    @PostMapping("/api/addresses")
//    public Address addAddress(@RequestBody Address address, Principal principal) {
//
//        User user = userService.findByEmail(principal.getName());
//
//        // delete old addresses of this user
//        addressRepository.deleteByUserId(user.getId());
//
//        address.setUser(user);
//
//        return addressRepository.save(address);
//    }
@PostMapping("/api/addresses")
public Address addAddress(@RequestBody Address address, Principal principal) {

    User user = userService.findByEmail(principal.getName());
    address.setUser(user);

    return addressRepository.save(address);
}
    @PutMapping("/api/addresses/{id}")
    public Address updateAddress(@PathVariable Long id,
                                 @RequestBody Address updated,
                                 Principal principal) {

        User user = userService.findByEmail(principal.getName());

        Address address = addressRepository.findById(id).orElseThrow();

        if(!address.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized");
        }

        address.setName(updated.getName());
        address.setPhone(updated.getPhone());
        address.setAddress(updated.getAddress());
        address.setCity(updated.getCity());
        address.setState(updated.getState());
        address.setPincode(updated.getPincode());

        return addressRepository.save(address);
    }

    @GetMapping
    public List<Address> getAddresses(Principal principal) {

        User user = userService.findByEmail(principal.getName());
        return addressRepository.findByUserId(user.getId());
    }
}