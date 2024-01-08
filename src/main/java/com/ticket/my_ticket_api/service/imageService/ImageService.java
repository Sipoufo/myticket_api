package com.ticket.my_ticket_api.service.imageService;

import com.ticket.my_ticket_api.entity.Image;

import java.util.List;
import java.util.Optional;

public interface ImageService {

    public Image save(Image image);

    public Optional<Image> findById(Long imageId);
}
