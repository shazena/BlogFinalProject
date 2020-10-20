package com.skshazena.blogFinalProject.daos;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 19, 2020
 */
public interface ImageDao {

    public String saveImage(MultipartFile file, String fileName, String directory);

    public String updateImage(MultipartFile file, String fileName, String directory);

    public boolean deleteImage(String fileName);
}
