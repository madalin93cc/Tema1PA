
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Colezea
 */
public class Sauron {

    private static ArrayList<Integer> stapani; // vectorul cu datele initiale despre stapani
    private static ArrayList<Integer> sclavi; // vectorul cu datele initiale despre sclavi
    private Integer n; //numarul de perechi

    public Sauron(String filename) {
        // citirea datelor din fisier
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename));
            n = scanner.nextInt();
            stapani = new ArrayList<>(n);
            sclavi = new ArrayList<>(n);
            for (int j = 1; j <= n; j++) {
                stapani.add(scanner.nextInt());
                sclavi.add(scanner.nextInt());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (scanner != null) {
                    scanner.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // metoda ce sorteaza perechile dintr-un map dupa valoare 
    private static HashMap<Integer, Integer> sortByValue(HashMap<Integer, Integer> map) {
        List<Integer> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map.Entry<Integer, Integer> e1 = (Map.Entry<Integer, Integer>) o1;
                Map.Entry<Integer, Integer> e2 = (Map.Entry<Integer, Integer>) o2;
                return (-1) * e1.getValue().compareTo(e2.getValue());
            }
        });
        LinkedHashMap<Integer, Integer> result = new LinkedHashMap<>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry<Integer, Integer> entry = (Map.Entry) it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    //metoda ce calculeaza suma minima, formeaza noile perechi si le afiseaza in fisier
    private void suma_minima() throws IOException {
        // map in care se retine in cheie indicele perechii si in valoare diferenta (stapani[i]-sclavi[i])
        HashMap<Integer, Integer> sortat = new HashMap<>(n - 2);
        
        // vectori in care se vor pune noii sclavi respectivi stapani
        ArrayList<Integer> new_sclavi = new ArrayList(n / 2);
        ArrayList<Integer> new_stapani = new ArrayList(n / 2);
        
        /* vector cu elemente din {0,1} 
         *count[i] == 1 -> din perechea i va fi luat sclavul
         *count[i] == 0 -> din perechea i va fi luat stapanul 
        */
        Integer[] count = new Integer[n];
       
        Arrays.fill(count, 0);// initial toate elementele sunt 0
        count[0] = 1;// din prima pereche va fi luat intotdeauna sclavul
        new_sclavi.add(0);// adaug primul sclav 

        // se construieste map-ul 
        for (int i = 1; i < n - 1; i++) {
            sortat.put(i, stapani.get(i) - sclavi.get(i));
        }
        
        // se sorteaza dupa valoare
        sortat = Sauron.sortByValue(sortat);
        int nr_sclavi = 1;// numarul de sclavi adaugati
        int salariu = 0;// salariul, initial 0

        // parcurg map-ul pana cand  gasesc numarul maxim de sclavi = n/2
        for (Map.Entry<Integer, Integer> entry : sortat.entrySet()) {
            if (nr_sclavi < n / 2) {// conditia de oprire
                count[entry.getKey()] = 1;// il consider sclav
                int sum = 0;
                /*se face o suma pe vectorul count astfel:
                 * daca count[i] == 1 se adauga 1 la suma -> daca e sclav
                 * daca count[i] == 0 se scade 1 in cazul in care suma > 0 -> e stapan 
                 * daca la final suma = 0 inseamna ca toti sclavii pot fi grupati cu un stapan
                 * respectand conditia ca indicele perechii stapanului sa fie mai mare ca cel al sclavului
                 */
                for (int i : count) {
                    if (i == 1) {
                        sum += 1;
                    } else if (sum != 0) {
                        sum -= 1;
                    }
                }
                if (sum == 0) {// poate fi lasat sclav si il adaug la vectorul de sclavi
                    nr_sclavi++;
                    new_sclavi.add(entry.getKey());
                } else { // este stapan si resetez contorul deoarece initial il considerasem sclav
                    count[entry.getKey()] = 0;
                }
            } else {
                break;
            }
        }
        
        // se adauga stapanii -> elementele nemarcate
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 0) {
                new_stapani.add(i);
            }
        }
        
        // sortez cei doi vectori pentru a-i putea grupa 
        Collections.sort(new_sclavi);
        Collections.sort(new_stapani);
        
        /* se genereaza salariul astfel:
         * daca elementul a fost marcat atunci 
         * la salariu este adunata valoarea asociata sclavului
         * altfel este adunata valoarea asociata stapanului 
         */
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 1) {
                salariu += sclavi.get(i);
            } else {
                salariu += stapani.get(i);
            }
        }
        
        //fisierul de output
        BufferedWriter output = new BufferedWriter(new FileWriter("date.out"));
        output.write(salariu + "\n");//se scrie pe prima linie salariul
        //se scriu noile perechi generate
        for (int i = 0; i < n / 2; i++) {
            output.write(new_stapani.get(i) + 1 + " " + (new_sclavi.get(i) + 1) + "\n");
        }
        output.close();// inchidere fisier
    }

    public static void main(String args[]) throws IOException {
        Sauron sauron = new Sauron("date.in");
        sauron.suma_minima();
    }
}
