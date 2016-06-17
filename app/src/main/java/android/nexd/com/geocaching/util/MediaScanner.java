package android.nexd.com.geocaching.util;

import android.content.Context;
import android.media.MediaScannerConnection;

import java.io.File;
import java.util.ArrayList;

public class MediaScanner {
	
    public static void scanFolder(Context context, String folder) {
		ArrayList<String> list = new ArrayList();
		scanFiles(new File(folder),list);
		String[] arr = new String[list.size()];
		MediaScannerConnection.scanFile(context, list.toArray(arr), null,null);
	}
	
    public static void scanFiles(File root,ArrayList<String> list) {
		if (root.exists()) {
			if (root.isFile() && !list.contains(root.getAbsolutePath())) {
				list.add(root.getAbsolutePath());
			} else if (root.isDirectory()) {
				for (File f : root.listFiles()) {
					scanFiles(f, list);
				}
			}
		}
	}
}
