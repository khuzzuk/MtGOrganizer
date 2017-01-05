package pl.khuzzuk.mtg.organizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static pl.khuzzuk.mtg.organizer.FileNameManager.getFileName;

class PicIdManager {
    private int currentId;

    synchronized void init() {
        File picsDirectory = new File("db/pics");
        File revPic = new File("db/pics/000000.png");
        if (!picsDirectory.exists() || !revPic.exists()) {
            picsDirectory.mkdirs();
            try {
                Files.copy(new File("000000.png").toPath(), revPic.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File[] files = picsDirectory.listFiles();
        if (files == null) {
            throw new IllegalStateException("no files in db/pics");
        }

        Arrays.sort(files);
        String name = files.length > 0 ? files[files.length - 1].getName() : "0";
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name= name.substring(0, pos);
        }
        currentId = Integer.parseInt(name) + 1;
    }

    synchronized Integer saveFile(File file) {
        if (!file.getPath().endsWith(".jpg") && !file.getPath().endsWith(".jpeg")) {
            return -1;
        }
        File out = new File(getFileName(currentId++));
        try {
            Files.copy(file.toPath(), out.toPath());
            return currentId - 1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    synchronized void removeLast() {
        new File("db/pics/" + --currentId + ".jpg").delete();
    }

    synchronized void removeNumbered(int id) {
        File file = new File("db/pics/" + id + ".jpg");
        if (id == currentId - 1 && file.exists()) {
            currentId--;
            file.delete();
        }
    }
}
