package mrcpsp.utils

import org.apache.log4j.Logger

final class FileUtils {
	
	static final Logger log = Logger.getLogger(FileUtils.class);

	static String createDirectory(String path) {
        File f = new File(path);
        
        if (!f.exists()) {
            try {
				org.apache.commons.io.FileUtils.forceMkdir(f);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
        }
        
        return f.getAbsolutePath();
    }
	
	static void writeToFile(File file, String data, boolean append) {
        try {
			org.apache.commons.io.FileUtils.writeStringToFile(file, data, append);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
    }
	
	static void removeAllFilesFromFolder(String folderPath) {
		File f = new File(folderPath);
        
		if (f.exists()) {
        
            try {
				org.apache.commons.io.FileUtils.cleanDirectory(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
        }
	}
	
	static List<File> getAllFilesInstances() {
		File baseDir = new File(UrlUtils.instance.urlForInstance);
		List<File> files = new ArrayList<File>();

        // adding the folders of the root folder
        for (File f: baseDir.listFiles()) {        	
        	files.add(f);
        }
        Collections.sort(files);
        return files;
	}

    File loadFile(String fileName) {
        String filePath = UrlUtils.instance.urlForInstanceFile
        return new File(filePath + fileName)
    }
}
