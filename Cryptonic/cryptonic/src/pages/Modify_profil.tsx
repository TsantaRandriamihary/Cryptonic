import React, { useState } from "react";
import { Camera, CameraResultType } from "@capacitor/camera";
import { ref, uploadBytes, getDownloadURL } from "firebase/storage";

const PhotoUploader: React.FC = () => {
    const [photo, setPhoto] = useState<string | null>(null);

    const takePhoto = async () => {
        try {
            const photo = await Camera.getPhoto({
                resultType: CameraResultType.DataUrl,
            });
            if (photo.dataUrl) {
                setPhoto(photo.dataUrl);
            }
        } catch (error) {
            console.error("Error taking photo:", error);
        }
    };

    const uploadPhoto = async () => {
        if (!photo) return;

        const fileName = `photos/${new Date().getTime()}.jpg`;
        // const photoRef = ref(storage, fileName);

        const response = await fetch(photo);
        const blob = await response.blob();

        // await uploadBytes(photoRef, blob);
        // const url = await getDownloadURL(photoRef);

        // alert("Photo uploaded! URL: " + url);
        setPhoto(null); // Réinitialise la photo après l'envoi.
    };

    return (
        <div>
            <button onClick={takePhoto}>Prendre une photo</button>
            {photo && (
                <div>
                    <img src={photo} alt="Preview" style={{ width: "100%" }} />
                    {/*<button onClick={uploadPhoto}>Envoyer vers Firebase</button>*/}
                </div>
            )}
        </div>
    );
};

export default PhotoUploader;
