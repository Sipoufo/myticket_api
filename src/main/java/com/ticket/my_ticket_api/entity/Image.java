package com.ticket.my_ticket_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ticket.my_ticket_api.helpers.FileNameHelper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Image", schema = "public")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    private String name;
    private String type;
    private long size;
    private String uuid;
    private String systemName;

    @CreatedDate
    protected Date create_at;

    @LastModifiedDate
    protected Date update_at;

    private byte[] data;

    /**
     * Create new Image class.
     *
     * @return new Image.
     */
    @Transient
    public static Image build() {
        String uuid = UUID.randomUUID().toString();
        Image image = new Image();
        Date now = new Date();
        image.setUuid(uuid);
        image.setCreate_at(now);
        image.setUpdate_at(now);
        image.setSystemName("default");
        return image;
    }

    @Transient
    public void setFiles(MultipartFile file) {
        setType(file.getContentType());
        setSize(file.getSize());
    }

    /**
     * Scale image data with given width and height.
     *
     * @param width  scale width
     * @param height scale height
     * @return scaled image byte array and change to class data.
     */
    @Transient
    public byte[] scale(int width, int height) throws Exception {

        if (width == 0 || height == 0)
            return data;

        ByteArrayInputStream in = new ByteArrayInputStream(data);

        try {
            BufferedImage img = ImageIO.read(in);

            java.awt.Image scaledImage = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            BufferedImage imgBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imgBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imgBuff, "jpg", buffer);
            setData(buffer.toByteArray());
            return buffer.toByteArray();

        } catch (Exception e) {
            throw new Exception("IOException in scale");
        }
    }

    /**
     * @param fileName - filename of the resources.
     *
     * @return inputstream for image
     * */
    private static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = Image.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    /**
     * Generate no context image with `notfound.jpg` image in asset.
     *
     * @return create default image.
     */
    @Transient
    public static Image defaultImage() throws Exception {
        InputStream is = getResourceFileAsInputStream("notfound.jpg");
        String fileType = "image/jpeg";
        byte[] bdata = FileCopyUtils.copyToByteArray(is);
        Image image = Image
                .builder()
                .name(null)
                .type(fileType)
                .size(0)
                .uuid(null)
                .systemName(null)
                .data(bdata)
                .build();
        return image;
    }

    /**
     * Generate scaled no context image with `notfound.jpg` image in asset with
     * given width and height.
     *
     * @param width  scale width
     * @param height scale height
     * @return create scaled default image.
     */
    @Transient
    public static Image defaultImage(int width, int height) throws Exception {
        Image defaultImage = defaultImage();
        defaultImage.scale(width, height);
        return defaultImage;
    }

    /**
     * Generate scaled no context image with `notfound.jpg` image in asset with
     * given width and height.
     *
     * @param file   multipartfile data to build.
     * @param helper filenamehelper class to generate name.
     * @return return new Image class related with file.
     */
    @Transient
    public static Image buildImage(MultipartFile file, FileNameHelper helper) {
        String fileName = helper.generateDisplayName(file.getOriginalFilename());

        Image image = Image.build();
        image.setName(fileName);
        image.setFiles(file);

        try {
            image.setData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
