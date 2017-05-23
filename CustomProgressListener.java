package application;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.GenericData;
import com.google.api.services.drive.Drive;

public class CustomProgressListener implements MediaHttpUploaderProgressListener {
	public void progressChanged(MediaHttpUploader uploader) throws IOException {
		switch (uploader.getUploadState()) {
		case INITIATION_STARTED:
			System.out.println("Initiation has started!");
			break;
		case INITIATION_COMPLETE:
			System.out.println("Initiation is complete!");
			break;
		case MEDIA_IN_PROGRESS:
			System.out.println(uploader.getProgress());
			break;
		case MEDIA_COMPLETE:
			System.out.println("Upload is complete!");
		}
	}
	public static void main(String[] args){
		File mediaFile = new File("ErrorEmail");
		InputStreamContent mediaContent=null;
		try {
			mediaContent = new InputStreamContent("image/jpeg",
					new BufferedInputStream(new FileInputStream(mediaFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaContent.setLength(mediaFile.length());


		MediaHttpUploader uploader = new MediaHttpUploader(mediaContent, new NetHttpTransport(), new BasicAuthentication("smcs2019.ssmg","Zahoogle13"));
		uploader.setProgressListener(new CustomProgressListener());
		HttpResponse response=null;
		try {
			response = uploader.upload(new GenericUrl());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!response.isSuccessStatusCode()) {
			System.out.println(response.isSuccessStatusCode());
		}
		
	}
}

