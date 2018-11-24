package servlet.user;

import java.io.*;
import java.util.*;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

public class UploadPicture extends HttpServlet {
	
	private final String UPLOAD_DIRECTORY = "/3I017/FileServer/";
   private boolean isMultipart;
   private String filePath;
   private int maxFileSize = 50 * 1024;
   private int maxMemSize = 4 * 1024;
   private File file ;

   public void init( ){
      // Get the file location where it would be stored.
      filePath = getServletContext().getInitParameter("file-upload"); 
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
   
	      // Check that we have a file upload request
	      isMultipart = ServletFileUpload.isMultipartContent(request);
	      response.setContentType("application/json");
	      java.io.PrintWriter out = response.getWriter();
	  
	      DiskFileItemFactory factory = new DiskFileItemFactory();
	   
	      factory.setSizeThreshold(maxMemSize);
	   
	      factory.setRepository(new File("c:\\temp"));
	
	      ServletFileUpload upload = new ServletFileUpload(factory);
	  
	      upload.setSizeMax( maxFileSize );
	
	      try { 
	         List fileItems = upload.parseRequest(request);
		
	         // Process the uploaded file items
	         Iterator i = fileItems.iterator();
	
	         while (i.hasNext()) {
	            FileItem fi = (FileItem)i.next();
	            if ( !fi.isFormField () ) {
	               // Get the uploaded file parameters
	               String fieldName = fi.getFieldName();
	               String fileName = fi.getName();
	               String contentType = fi.getContentType();
	               boolean isInMemory = fi.isInMemory();
	               long sizeInBytes = fi.getSize();
	            
	               // Write the file
	               if( fileName.lastIndexOf("\\") >= 0 ) {
	                  file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
	               } else {
	                  file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
	               }
	               fi.write(file) ;
	            }
	         }
	     
         } catch(Exception e) {
          
         }
      }
      
      public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
         throw new ServletException("GET method used with " + getClass( ).getName( )+": POST method required.");
      }
}