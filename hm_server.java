import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class hm_server {
    public static String word;

    public static int client;
    private String guessedWord;
    public int guessesLeft;
    private String guessedLetters;
   public static String state;

    public void playGame() {
        Scanner scanner = new Scanner(System.in);

        try {
            // Prompt for the word to be guessed
            System.out.print("Enter the word to be guessed: ");
            String input = scanner.nextLine().toLowerCase();
            // Validate the word
            while (!input.matches("[a-zA-Z]+")) {
                System.out.println("Invalid input. Please enter alphabetic characters only.");
                System.out.print("Enter the word to be guessed: ");
                input = scanner.nextLine().toLowerCase();
            }

            word = input;

            // Validate the word
            if (word.isEmpty()) {
                throw new IllegalArgumentException("The word cannot be empty.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private void displayGameState() {
        System.out.println("\nGuesses left: " + guessesLeft);
        System.out.println("Guessed letters: " + guessedLetters);
        System.out.println("Current word: " + guessedWord);
    }

    private boolean isAlreadyGuessed(String guess) {
        return guessedLetters.contains(guess);
    }

    private boolean isWordGuessed() {
        return guessedWord.equals(word);
    }

    private String getAlphabeticInput(Scanner scanner) {
        String input = scanner.nextLine();

        // Validate the input to contain only alphabetic characters

        if (!input.matches("[a-zA-Z]+")) {
            System.out.println("Invalid input. Please enter alphabetic characters only.");
            return "";
        }

        return input.toLowerCase();
    }












  /*
   LINE BETWEEN STATIC VOID MAIN AND OTHER STATIC methods
  THIS LINE IS HERE SO I DON'T FORGET WHERE THE MAIN STARTS
  -----------------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------------
  */


    public static void main(String[] args) throws IOException {
        String url="jdbc:mysql://localhost:3307/hm_db";  //declaring my port, database name, SQL username and password
        String username="root";
        String password="";
        Statement stmt = null;
        try
        {

            // Create server Socket
            ServerSocket ss = new ServerSocket(888);

            // connect it to client socket
            Socket s = ss.accept();
            System.out.println("Connection established");

            // to send data to the client
            PrintStream ps
                    = new PrintStream(s.getOutputStream());

            // to read data coming from the client
            BufferedReader br
                    = new BufferedReader(
                    new InputStreamReader(
                            s.getInputStream()));

            // to read data from the keyboard
            BufferedReader kb
                    = new BufferedReader(
                    new InputStreamReader(System.in));
            System.out.println("You're now talking to the other player. Once you both type 'start', the game will begin!");
            // server executes continuously


                String str, str1;



                // read from client
                while (!(str = kb.readLine()).equalsIgnoreCase("start")) {
                    System.out.println(str);
                    str1 = kb.readLine();

                    // send to client
                    ps.println(str1);
                }

                // close connection

                ss.close();
                s.close();



             // end of while
        }

        catch (Exception e) {
            System.out.println(e);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection(url, username, password);  //connect to jdbc

            Statement statement=connection.createStatement();

            ResultSet resultSet=statement.executeQuery("select * from gamestate");


            stmt = connection.createStatement();
            String sql = "UPDATE gamestate SET Server='1' WHERE id=1";
            stmt.executeUpdate(sql);

                   sql = "UPDATE gamestate SET Client='0' WHERE id=1";
            stmt.executeUpdate(sql);
                    hm_server game = new hm_server();
            game.playGame();
client = 0;




   sql = "UPDATE gamestate SET Word='" + word + "' WHERE id=1";
            stmt.executeUpdate(sql);

            System.out.println("Waiting for other player...");
             sql = "UPDATE gamestate SET Server='0' WHERE id=1";

            stmt.executeUpdate(sql);

while (client == 0){
    resultSet=statement.executeQuery("select * from gamestate");
    while (resultSet.next()) {

        client = resultSet.getInt(4);

    }
}

            Thread.sleep(2000);
            resultSet=statement.executeQuery("select * from gamestate");

            while (resultSet.next()) {
                System.out.println("Result: "+ resultSet.getString(2));
            }
             sql = "UPDATE gamestate SET Server='1' WHERE id=1";
            stmt.executeUpdate(sql);
            connection.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}