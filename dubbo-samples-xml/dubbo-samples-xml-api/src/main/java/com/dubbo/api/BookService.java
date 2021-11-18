package com.dubbo.api;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface BookService {
   String  getBookName(long bookId);



//   MultipartFile getBookFile(File file);
}
