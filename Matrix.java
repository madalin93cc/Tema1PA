
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.math.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Colezea
 */
public class Matrix {

    private static Integer n, m, k;

    public Matrix(String filename) {
        // citirea datelor din fisier
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filename));
            n = scanner.nextInt();
            m = scanner.nextInt();
            k = scanner.nextInt();
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

    // metoda ce intoarce rezultatul ridicarii la putere a bazei la exponent modulo 40009
    private Integer ridicarePutere(Integer baza, Integer exp) {
        Integer aux = 1;
        while (exp != 0) {
            if (exp % 2 != 0) {
                aux = ((aux % 40009) * (baza % 40009)) % 40009;
            }
            baza = ((baza % 40009) * (baza % 40009)) % 40009;
            exp /= 2;
        }
        return aux;
    }

    // metoda ce intoarce rezultatul inmultirii a doua matrici modulo 40009
    private static int[][] multiplyMatrix(int[][] A, int[][] B) {
        final int N = A.length;
        final int M = B[0].length;
        final int K = A[0].length;

        int[][] Res = new int[N][M];

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                for (int k = 0; k < K; ++k) {
                    Res[i][j] = (Res[i][j] + A[i][k] * B[k][j]) % 40009;
                }
            }
        }
        return Res;
    }

    // metoda ce intoarce vectorul rezultat prin inmultirea unei matrici cu un vector
    private int[] multiplyMatrixVector(int[][] A, int[] v) {
        final int N = A.length;
        final int M = v.length;

        int[] vRes = new int[N];

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                vRes[i] = (vRes[i] + A[i][j] * v[j]) % 40009;
            }
        }
        return vRes;
    }

    // metoda ce intoarce rezultatul ridicarii unei matrici A la o putere p
    public static int[][] logPowMatrix(int[][] A, int p) {
        int[][] Res = new int[A.length][A.length];

        for (int i = 0; i < Res.length; ++i) {
            Res[i][i] = 1;
        }

        if (p == 0) {
            return Res;
        }
        if (p == 1) {
            return A;
        }
        Res = logPowMatrix(A, p / 2);
        if (p % 2 == 0) {
            return multiplyMatrix(Res, Res);
        } else {
            return multiplyMatrix(A, multiplyMatrix(Res, Res));
        }
    }

    // metoda ce calculeaza si afiseaza in fisier numarul de cadre 
    private void numaraCadre() throws IOException {
        /* matrice cu 1 deasupra diagonalei principale si ultima linie, 
        si 0 in rest
        */
        int[][] mat = new int[k + 1][k + 1];
        
        // vector ce contine puterile lui 2 de la 2^0 la 2^k 
        int[] vec = new int[k + 1];

        // generare matrice
        for (int i = 0; i < k; i++) {
            mat[i][i + 1] = 1;
            mat[k][i] = 1;
        }
        mat[k][k] = 1;

        // generare vector
        for (int i = 0; i <= k; i++) {
            vec[i] = ridicarePutere(2, i);
            while (vec[i] >= 40009) {
                vec[i] -= 40009;
            }
        }
        
        // ridic matricea la puterea n-k, metoda avand complexitate logaritmica 
        mat = logPowMatrix(mat, n - k);
        
        // vector in care se retine rezultatul inmultirii matricii cu vectorul initial
        int[] rez = new int[k + 1];
        rez = multiplyMatrixVector(mat, vec);
        
        /* rezultatul cautat se afla pe ultima pozitie din vector
         * pentru a obtine numarul de cadre se ridica rezultatul la puterea m
        */
        int rezultat = ridicarePutere(rez[k], m);
        
        // se afiseaza rezultatul in fisierf
        BufferedWriter output = new BufferedWriter(new FileWriter("date.out"));
        output.write(rezultat + "\n");
        output.close();

    }

    public static void main(String args[]) throws IOException {
        Matrix matrix = new Matrix("date.in");
        matrix.numaraCadre();
    }
}
