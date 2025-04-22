package itu.p16.crypto.service.cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "crypto")
            );
            String imageUrl = (String) uploadResult.get("url");
            System.out.println("✅ Upload réussi : " + imageUrl);
            return imageUrl;
        } catch (IOException e) {
            System.err.println("❌ Erreur d'upload : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'upload de l'image", e);
        }
    }
    
}