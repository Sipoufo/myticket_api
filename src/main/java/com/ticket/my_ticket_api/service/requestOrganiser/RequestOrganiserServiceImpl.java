package com.ticket.my_ticket_api.service.requestOrganiser;

import com.ticket.my_ticket_api.entity.Image;
import com.ticket.my_ticket_api.entity.RequestOrganiser;
import com.ticket.my_ticket_api.entity.Users;
import com.ticket.my_ticket_api.helpers.FileNameHelper;
import com.ticket.my_ticket_api.payload.request.OrganizerRequest;
import com.ticket.my_ticket_api.payload.request.OrganizerRequestResolve;
import com.ticket.my_ticket_api.payload.response.MessageResponse;
import com.ticket.my_ticket_api.repository.RequestOrganiserRepository;
import com.ticket.my_ticket_api.service.imageService.ImageService;
import com.ticket.my_ticket_api.service.imageService.ImageServiceImpl;
import com.ticket.my_ticket_api.service.userService.UserService;
import com.ticket.my_ticket_api.service.userService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RequestOrganiserServiceImpl implements RequestOrganiserService{
    @Autowired
    private final UserService userService = new UserServiceImpl();
    @Autowired
    private RequestOrganiserRepository requestOrganiserRepository;
    @Autowired
    private ImageService imageService = new ImageServiceImpl();

    @Override
    public ResponseEntity<?> save(OrganizerRequest organizerRequest, String token, FileNameHelper helper) {
        Optional<Users> user = userService.getUserByToken(token);
        if (user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User don't exist!"));
        }
        String fileName = helper.generateDisplayName(organizerRequest.getCni_face().getOriginalFilename());

        // CNI Face
        Image cni_face = Image.build();
        cni_face.setName(fileName);
        cni_face.setFiles(organizerRequest.getCni_face());

        // CNI Back
        Image cni_back = Image.build();
        cni_face.setName(fileName);
        cni_face.setFiles(organizerRequest.getCni_back());

        try {
            cni_face.setData(organizerRequest.getCni_face().getBytes());
            cni_back.setData(organizerRequest.getCni_back().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageService.save(cni_face);
        imageService.save(cni_back);

        RequestOrganiser requestOrganiser = RequestOrganiser
                .builder()
                .CNIFace(cni_face)
                .CNIBack(cni_back)
                .user(user.get())
                .build();

        return ResponseEntity.ok(requestOrganiserRepository.save(requestOrganiser));
    }

    @Override
    public ResponseEntity<List<RequestOrganiser>> fetchAllRequestOrganiser(Pageable pageable) {
        return ResponseEntity.ok(requestOrganiserRepository.findAll(pageable).getContent());
    }

    @Override
    public ResponseEntity<?> resolveRequest(OrganizerRequestResolve organizerRequestResolve, Long requestId) {
        Optional<RequestOrganiser> requestOrganiser = requestOrganiserRepository.findById(requestId);
        if (requestOrganiser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Request don't exist!"));
        }

        requestOrganiser.get().setMessage(organizerRequestResolve.getMessage());
        requestOrganiser.get().setAccepted(organizerRequestResolve.isAccepted());

        return ResponseEntity.ok(requestOrganiserRepository.save(requestOrganiser.get()));
    }
}
