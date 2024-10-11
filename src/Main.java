import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();
        ArrayList<Bejegyzes> list = new ArrayList<>();
        list.add(new Bejegyzes("Bob The Builder", "How to make infinite money."));
        list.add(new Bejegyzes("random ember", "random dologok"));

        int input = 0;
        while (true) {
            System.out.print("Bejegyzések száma: ");
            if (sc.hasNextInt()) {
                input = sc.nextInt();
                sc.nextLine();
                if (input < 0) {
                    System.out.println("Természetes számot adj meg!");
                    continue;
                }
                break;
            }
            System.out.println("Ez nem egy szám :c");
            sc.nextLine();
        }

        for (int i = 0; i < input; i++) {
            System.out.println(i+1 + ".bejegyzés:");
            System.out.print("Szerző: ");
            String szerzo = sc.nextLine();
            System.out.print("Tartalom: ");
            String tartalom = sc.nextLine();
            list.add(new Bejegyzes(szerzo, tartalom));
        }

        URL url = Main.class.getResource("bejegyzesek.csv");
        try {
            if (url == null) {
                System.out.println("Nincs meg a file!");
                return;
            }
            File f = new File(url.toURI());
            List<String> lines = Files.readAllLines(f.toPath());
            for (String line : lines) {
                String[] split = line.split(";");
                list.add(new Bejegyzes(split[0], split[1]));
            }

        } catch (URISyntaxException | IOException e) {
            System.out.println("Nem sikerült beolvasni a filet!");
            return;
        }

        for (int i = 0; i < list.size()*20; i++)
            list.get(rnd.nextInt(list.size())).like();

        System.out.println("Második bejegyzés tartalmának módósított szövege:");
        list.get(1).setTartalom(sc.nextLine());

        for (Bejegyzes e : list) System.out.println("\n" + e);

        Bejegyzes mostLiked = list.stream().max(Comparator.comparingInt(Bejegyzes::getLikeok)).orElse(null);
        assert mostLiked != null;
        System.out.printf("Legtöbb like: %d\n", mostLiked.getLikeok());
        if (mostLiked.getLikeok() > 35) System.out.println("Van 35 likenál több.");
        else System.out.println("Nincs 35 likenál több.");

        long lowLike = list.stream().filter(e -> e.getLikeok() < 15).count();
        System.out.printf("%d bejegyzés kapott 15-nél kevesebb likeot.\n", lowLike);

        list.sort(Comparator.comparingInt(Bejegyzes::getLikeok).reversed());

        for (Bejegyzes e : list) System.out.println("\n" + e);

        try {
            FileWriter fw = new FileWriter("bejegyzesek_rendezett.txt", false);
            for (int i = 0; i < list.size(); i++) {
                fw.write(list.get(i).toString());
                if (i != list.size()-1) fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("File írás sikertelen!");
            return;
        }
    }
}