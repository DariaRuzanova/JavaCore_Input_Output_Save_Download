import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws RuntimeException {
        StringBuilder log = new StringBuilder();
        String way = "d:\\Daria\\Java\\JavaCore\\Game\\";
        List<String> dirs = new ArrayList<>();
        dirs.add("\\Game");
        dirs.add("\\Game\\src");
        dirs.add("\\Game\\res");
        dirs.add("\\Game\\savegames");
        dirs.add("\\Game\\temp");
        dirs.add("\\Game\\src\\main");
        dirs.add("\\Game\\src\\test");
        dirs.add("\\Game\\res\\drawables");
        dirs.add("\\Game\\res\\vectors");
        dirs.add("\\Game\\res\\icons");

        List<String> files = new ArrayList<>();
        files.add("\\Game\\src\\main\\Main.java");
        files.add("\\Game\\src\\main\\Utils.java");
        files.add("\\Game\\temp\\temp.txt");

        for (String dir : dirs) {
            File folder = new File(way + dir);
            if (folder.mkdir()) {
                log.append("Папка ").append(dir).append(" была успешна создана.\n");
            }
        }
        for (String createFile : files) {
            File file = new File(way, createFile);
            try {
                if (file.createNewFile()) {
                    log.append("Файл ").append(createFile).append(" успешно создан.\n");
                }
            } catch (IOException e) {
                log.append(e.getMessage()).append(" ").append(createFile).append("\n");
            }
        }
        try (FileWriter writer = new FileWriter(way + "\\Game\\temp\\temp.txt", false)) {
            String logFile = log.toString();
            writer.write(logFile);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        String mainWay = "d:\\Daria\\Java\\JavaCore\\Game\\Game\\savegames\\";
        GameProgress game1 = new GameProgress(95, 5, 2, 2.5);
        GameProgress game2 = new GameProgress(85, 4, 1, 5.4);
        GameProgress game3 = new GameProgress(50, 7, 1, 10.3);

        String saveGame1 = "game1.dat";
        String saveGame2 = "game2.dat";
        String saveGame3 = "game3.dat";

        saveGame(mainWay + saveGame1, game1);
        saveGame(mainWay + saveGame2, game2);
        saveGame(mainWay + saveGame3, game3);

        String[] listGames = {mainWay + saveGame1, mainWay + saveGame2, mainWay + saveGame3};
        zipFiles(listGames, mainWay + "gamesSave.zip");
        delete(listGames);

        openZip(mainWay + "gamesSave.zip", mainWay);
        GameProgress gameProgress = openProgress(mainWay + saveGame2);
        System.out.println(gameProgress.toString());

    }

    public static void saveGame(String way, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(way);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            System.out.println("Game has been saved in " + way);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    public static void zipFiles(String[] listGames, String zip) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zip))) {
            for (String game : listGames) {
                FileInputStream fis = new FileInputStream(game);
                ZipEntry entry = new ZipEntry(getFileNameWithoutPath(game));

                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                zout.write(buffer);
                zout.closeEntry();
                System.out.println("File " + game + " added to " + zip);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());


        }
    }

    public static void delete(String[] filesGames) {
        for (String deleteFile : filesGames) {
            File file = new File(deleteFile);
            if (file.delete()) {
                System.out.println("Delete files");
            }
        }
    }

    public static String getFileNameWithoutPath(String fullFilePath) {
        File f = new File(fullFilePath);
        return f.getName();
    }

    public static void openZip(String wayZip, String wayFales) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(wayZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(wayFales + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress openProgress(String waySaveGame) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(waySaveGame);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gameProgress;
    }
}
