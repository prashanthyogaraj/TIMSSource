package com.java.tims;



	import java.io.BufferedWriter;
	import java.io.ByteArrayOutputStream;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.HashMap;

	import org.apache.commons.httpclient.HttpClient;
	import org.apache.commons.httpclient.methods.MultipartPostMethod;
	import org.apache.commons.httpclient.methods.multipart.FilePart;
	public class Tims {
	/**
	 * Class that aids in posting an XML to a URL.
	 */
	
	    /**
	     * Given an XML file, post to the given URL.  Saves the request and response.
	     * @param url
	     * @param fileContents
	     * @exception Exception for any errors
	     */
	    private String postXMLToUrl(String url, String fileContents) throws Exception
	    {
	        // write file to filesystem.
	        // make the post
	        // check the response code
	        // return the results as a string
	        saveRequest(url, fileContents);
	        File postFile = writeTempFile(fileContents);
	        MultipartPostMethod filePost = new MultipartPostMethod(url);
	        FilePart xmlPart = new FilePart("xmldata", postFile);
	        filePost.addPart(xmlPart);
	        HttpClient client = new HttpClient();
	        int responseCode = client.executeMethod(filePost);        
	        InputStream is = filePost.getResponseBodyAsStream();
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        byte buffer[] = new byte[8192];
	        int readBytes = 0;
	        while (true){
	            readBytes = is.read(buffer);
	            if (readBytes > 0) 
	            {
	                os.write(buffer, 0, readBytes);
	            }
	            else
	            {
	                break;
	            }
	        }
	        filePost.releaseConnection();
	        /* Hold response as a String in case we need to inspect it for debugging */
	        String response = new String(os.toByteArray());
	        saveResponse(url, response);
	        if (responseCode != 200)
	        {
	            throw new Exception("Server claimed that request failed with response code of " + responseCode);
	        }
	        return response;
	    }
	    
	    /**
	     * Given the meat of a request (the part inside the <CTI/> elements)
	     * save that request, make the post, and save the response.
	     * @param url
	     * @param meatOfRequest
	     * @return
	     * @throws Exception
	     */
	    public String sendToServer(String url, String meatOfRequest) throws Exception
	    {
	        String postXml = getPostHeader() + meatOfRequest + getPostFooter();
	        return postXMLToUrl(url, postXml);
	    }
	    
	    /**
	     * Given a URL that's being posted to, return the friendly
	     * name of the url for writing a file.
	     * @param url
	     * @return
	     */
	    private String getDesiredFileName(String url)
	    {
	        String secondLastPart = getSecondLastURL(url);
	        // now lets skim off the part between the last / and the .        
	        int indexOfSlash = url.lastIndexOf("/")+1;
	        int indexOfDot = url.lastIndexOf(".");
	        return secondLastPart + "_" + url.substring(indexOfSlash, indexOfDot);        
	    }
	    
	    /**
	     * Given a desired filename (something like requirement_write) add the number
	     * on the end that is the count of what's been sent.  The number on the end is
	     * incremented every two times this is called.
	     * @param desiredName
	     * @return
	     */
	    private String addCountToFileName(String desiredName)
	    {
	        Integer count = (Integer) urlCount.get(desiredName);
	        if (count == null)
	        {
	            count = new Integer(0);
	        }
	        urlCount.put(desiredName, new Integer(count.intValue()+1));
	        // dividing by 2 since we will have both a POST and a Response
	        int index = (int) count.intValue()/2;
	        // lets increment so that we're 1 based
	        index++;
	        return desiredName + index; 
	    }
	    
	    /**
	     * Save the sent message for posterity.
	     * @param url
	     * @param message
	     * @throws IOException
	     */
	    private void saveResponse(String url, String message) throws IOException
	    {
	        saveMessage(url, message, "response");
	    }
	    
	    /**
	     * Save the sent message for posterity.
	     * @param url
	     * @param message
	     * @throws IOException
	     */
	    private void saveRequest(String url, String message) throws IOException
	    {
	        saveMessage(url, message, "request");
	    }
	    
	    /**
	     * Save the response to the filesystem if needed
	     * @param url
	     * @param message
	     * @param type
	     * @throws IOException for filesystem errors
	     */
	    private void saveMessage(String url, String message, String type) throws IOException
	    {
//	        if (!TestConstants.WRITE_FILES)
//	        {
//	            return;
//	        }
	        String desiredFilename = getDesiredFileName(url);
	        String filenameWithCount = addCountToFileName(desiredFilename);
//	        writeFile(TestConstants.SENT_DIRECTORY+filenameWithCount + "_" + type + ".xml", message);
	        writeFile("", message);
	    }
	    
	    /**
	     * Get the header that should be put on top of a post.
	     * @return
	     */
	    private String getPostHeader()
	    {
//	        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<Cti xsi:schemaLocation=\"" + 
//	                "http://cti.cisco.com/namespace " + TestConstants.SERVER + "cti/xsd/Cti.xsd\"\n" + 
//	                "xmlns=\"http://cti.cisco.com/namespace\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
//	                "<Credential user=\"dsarisky\" token=\"1234\"/>\n";
	    	return "";
	    }
	    
	    /**
	     * Get the footer for the bottom of a post.
	     * @return
	     */
	    private String getPostFooter()
	    {
	        return "\n</Cti>";
	    }
	    
	    /**
	     * Given an XML that was generated - retrieve the part of it that's "relevant"
	     * This part can have the stock header and footer attached to it and then be 
	     * used as a regular XML.
	     * @param incoming
	     * @return
	     */
	    public String getMeatFromRequest(String incoming)
	    {
	        String searchKey = "XMLSchema-instance\">";
	        int startIndex = incoming.indexOf(searchKey) + searchKey.length();
	        String endSearchKey = "<Timestamp>";
	        int endIndex = incoming.indexOf(endSearchKey);
	        return incoming.substring(startIndex, endIndex);
	    }
	    
	    /**
	     * Given a filename and contents, write the file out to disk.
	     * @param filename fully qualified filename
	     * @param contents
	     * @throws IOException
	     */
	    public static void writeFile(String filename, String contents) throws IOException
	    {
	        FileWriter fw = new FileWriter(filename);
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(contents);
	        bw.flush();
	        bw.close();        
	    }
	    
	    /**
	     * Write a temporary file and return the filename
	     * @param contents fully qualified filename
	     * @return contents
	     * @throws IOException
	     */
	    public static File writeTempFile(String contents) throws IOException
	    {
	        File tempFile = File.createTempFile("temp", "xml");
	        tempFile.deleteOnExit();
	        writeFile(tempFile.getAbsolutePath(), contents);
	        return tempFile;
	    }
	    
	    public static String getSecondLastURL (String s)
	    {
	    	String separator = "";
	        String[] str = s.split(separator);

	        return str[str.length-2];
	    }

	    
	    /**
	     * Map that has String (desired URL) as key and Integer (count) as value.
	     */
	    private HashMap urlCount = new HashMap();
	    
	}


