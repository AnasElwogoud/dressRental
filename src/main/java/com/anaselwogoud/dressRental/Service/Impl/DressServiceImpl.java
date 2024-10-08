package com.anaselwogoud.dressRental.Service.Impl;

import com.anaselwogoud.dressRental.DTO.DressDTO;
import com.anaselwogoud.dressRental.DTO.Response;
import com.anaselwogoud.dressRental.Entity.Dress;
import com.anaselwogoud.dressRental.Exception.GlobalException;
import com.anaselwogoud.dressRental.Repository.BookingRepo;
import com.anaselwogoud.dressRental.Repository.DressRepo;
import com.anaselwogoud.dressRental.Service.Interface.DressService;
import com.anaselwogoud.dressRental.Utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DressServiceImpl implements DressService {
    private final DressRepo dressRepo;
    private final BookingRepo bookingRepo;

    public DressServiceImpl(DressRepo dressRepo, BookingRepo bookingRepo) {
        this.dressRepo = dressRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public Response addNewDress(MultipartFile photo, String dressSize, BigDecimal dressPrice, String description) {
        Response response = new Response();

        try {
            String imageUrl = "";
            Dress dress = new Dress();
            dress.setDressPhotoUrl(imageUrl);
            dress.setPrice(dressPrice);
            dress.setDescription(description);
            dress.setSize(dressSize);
            Dress savedDress = dressRepo.save(dress);
            DressDTO dressDTO = Utils.mapDressToDressDTO(savedDress);
            response.setStatusCode(200);
            response.setMessage("Added a new dress");
            response.setDress(dressDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a dress " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllDressSizes() {
        return dressRepo.findDistinctSize();
    }

    @Override
    public Response getAllDresses() {
        Response response = new Response();

        try {
            List<Dress> dressList = dressRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<DressDTO> dressDTOList = Utils.mapDressListToDressListDTO(dressList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setDressList(dressDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error get a dresses " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteDress(Long dressId) {
        Response response = new Response();

        try {
            dressRepo.findById(dressId).orElseThrow(() -> new GlobalException("Dress Not Found"));
            dressRepo.deleteById(dressId);
            response.setStatusCode(200);
            response.setMessage("Deleted a dress successfully");

        } catch (GlobalException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting a dress " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateDress(Long dressId, String description, String dressSize, BigDecimal dressPrice, MultipartFile photo) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
//                imageUrl = awsS3Service.saveImageToS3(photo);
            }
            Dress dress = dressRepo.findById(dressId).orElseThrow(() -> new GlobalException("Dress Not Found"));
            if (dressSize != null) dress.setSize(dressSize);
            if (dressPrice != null) dress.setPrice(dressPrice);
            if (description != null) dress.setDescription(description);
            if (imageUrl != null) dress.setDressPhotoUrl(imageUrl);

            Dress updatedDress = dressRepo.save(dress);
            DressDTO dressDTO = Utils.mapDressToDressDTO(updatedDress);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setDress(dressDTO);

        } catch (GlobalException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating a dress details " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getDressById(Long dressId) {
        Response response = new Response();

        try {
            Dress dress = dressRepo.findById(dressId).orElseThrow(() -> new GlobalException("Dress Not Found"));
            DressDTO dressDTO = Utils.mapDressToDressDTO(dress);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setDress(dressDTO);

        } catch (GlobalException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting a dress " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableDressesByDataAndSize(LocalDate checkInDate, LocalDate checkOutDate, String dressSize) {
        Response response = new Response();

        try {
            List<Dress> availableDresses = dressRepo.findAvailableDressByDatesAndSize(checkInDate, checkOutDate, dressSize);
            List<DressDTO> dressDTOList = Utils.mapDressListToDressListDTO(availableDresses);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setDressList(dressDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting a dresses " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableDresses() {
        Response response = new Response();

        try {
            List<Dress> dressList = dressRepo.getAllAvailableDress();
            List<DressDTO> dressDTOList = Utils.mapDressListToDressListDTO(dressList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setDressList(dressDTOList);

        } catch (GlobalException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting available dresses " + e.getMessage());
        }
        return response;
    }
}
