package pl.khuzzuk.mtg.organizer;

import java.io.*;
import java.nio.file.Files;

class PicIdManager {
    private int currentId;

    void init() {
        File picsDirectory = new File("db/pics");
        if (!picsDirectory.exists()) {
            picsDirectory.mkdirs();
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
        currentId = Integer.parseInt(name);
    }

    Integer saveFile(File file) {
        if (!file.toPath().endsWith(".png")) {
            return -1;
        }
        File out = new File("db/pics/" + currentId++ + ".png");
        try {
            Files.copy(file.toPath(), out.toPath());
            return currentId - 1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    void removeLast() {
        new File("db/pics/" + --currentId + ".png").delete();
    }

    void removeNumbered(int id) {
        if (id == currentId - 1) {
            currentId--;
        }
        new File("db/pics/" + id + ".png").delete();
    }
}
