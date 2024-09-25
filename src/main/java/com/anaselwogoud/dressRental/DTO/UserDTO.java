package com.anaselwogoud.dressRental.DTO;

import com.anaselwogoud.dressRental.Entity.Bookings;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private List<Bookings> bookings = new ArrayList<>();

}
