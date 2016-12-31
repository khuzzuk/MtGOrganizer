package pl.khuzzuk.mtg.organizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

        String name = files[files.length - 1].getName();
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
        File out = new File("db/pics/" + currentId++ + ".jpg");
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
        if (id == currentId - 1) {
            currentId--;
        }
        new File("db/pics/" + id + ".jpg").delete();
    }
}
