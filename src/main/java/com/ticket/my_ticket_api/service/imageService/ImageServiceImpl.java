package com.ticket.my_ticket_api.service.imageService;

import com.ticket.my_ticket_api.entity.Image;
import com.ticket.my_ticket_api.payload.response.ImageResponse;
import com.ticket.my_ticket_api.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService{
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image save(Image image) throws NullPointerException {
        if (image == null)
            throw new NullPointerException("Image Data NULL");
        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> findById(Long imageId) {
        return this.imageRepository.findById(imageId);
    }

    @Override
    public Image findByFileName(String fileName) {
        return this.imageRepository.findByName(fileName);
    }
}
