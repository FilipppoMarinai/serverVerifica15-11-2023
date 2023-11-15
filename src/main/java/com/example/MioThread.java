package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class MioThread extends Thread{
    Socket s;
    ArrayList<MioThread> client;

    public MioThread(Socket s, ArrayList<MioThread> client){
        this.s = s;
        this.client = client;
    }

    public void run(){
        try{
            if(client.size() < 3){
                System.out.println("Hai bisgono di più giocatori");
            }
            else{
                String[] parole = new String[] {
                    "albero",
                    "quadro",
                    "barile",
                    "natale",
                    "corsa"
                };
                int indexParola = (int)(Math.random() * parole.length);
                String parola = parole[indexParola];
                String asterischi = "";
                for(int i = 0; i < parola.length(); i++){
                    asterischi += "*";
                }
                System.out.print(asterischi + "\n");

                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                int tentativi = 0;
                String provaClient;
                String num;
                int numero = 0;
                char[] chars = asterischi.toCharArray();
                
                do{
                    //prendo la parola/lettera
                    provaClient = in.readLine();
                    num = in.readLine();
                    tentativi++;

                    if(num.equals("1")){
                        char lettera = provaClient.charAt(0);
                        char l = Character.toLowerCase(lettera);
                        System.out.println(chars);
                        //se la lettera è presente
                        for(int i = 0; i < parola.length(); i++){
                            if(parola.charAt(i) == l){
                                chars[i] = l;
                            }
                        }
                        System.out.println(chars);
                    }
                    else{
                        //se indovina la parola
                        if(provaClient.equals(parola)){
                            for (MioThread c : client) {
                                try{
                                    DataOutputStream outClient = new DataOutputStream(c.s.getOutputStream());
                                    outClient.writeBytes("La parola e' stata indovinata in " + tentativi + " tentativi\n");
                                }
                                catch(Exception e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            numero = 1;
                        }
                        else{
                            System.out.println("Riprova");
                        }
                    }

                }while(numero != 1);

                s.close();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
