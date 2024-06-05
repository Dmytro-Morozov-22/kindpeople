package com.dmtrmrzv.kindpeople.services;

import com.dmtrmrzv.kindpeople.entities.ImageModel;
import com.dmtrmrzv.kindpeople.repositories.ImageRepository;
import com.dmtrmrzv.kindpeople.repositories.PostRepository;
import com.dmtrmrzv.kindpeople.repositories.UserRepository;
import com.dmtrmrzv.kindpeople.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {

    public static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private ImageRepository imageRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private UserService userService;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserRepository userRepository,
                        PostRepository postRepository, UserService userService) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = userService.getUserByPrincipal(principal);
        LOG.info("Uploading image profile to User {}", user.getUsername());

        ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }






    private static byte[] compressBytes(byte[] data) {

        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while(!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressByte(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while(!inflater.finished()){
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    //FIXME The method is miswritten it needs to be rewritten!
    private <T>Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        try {
                            throw new IllegalAccessException();
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return list.get(0);
                }
        );
    }





















}
