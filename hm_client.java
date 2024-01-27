import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class hm_client {
    public static String word;

    public static int client;

    public static int server;
    private String guessedWord;
    public int guessesLeft;
    private String guessedLetters;
   public static String state;

    public void playGame() {
        Scanner scanner = new Scanner(System.in);

        try {


            // Validate the word
            if (word.isEmpty()) {
                throw new IllegalArgumentException("The word cannot be empty.");
            }

            // Initialize game variables
            guessedWord = "";
            for (int i = 0; i < word.length(); i++) {
                guessedWord += "-";
            }
            guessesLeft = 6;
            guessedLetters = "";

            // Start the game loop
            while (guessesLeft > 0) {
                displayGameState();

                // Prompt for a guess
                System.out.print("Enter your guess: ");
                String guess = getAlphabeticInput(scanner);

                // Check if the letter has already been guessed
                if (isAlreadyGuessed(guess)) {
                    if (guess.matches("[a-zA-Z]+")) {
                        System.out.println("You already guessed that letter. Try again.");
                        continue;
                    }
                    continue;
                }

                // Add the guessed letter to the list
                guessedLetters += guess;

                // Check if the guessed letter is in the word
                boolean correctGuess = false;
                char[] newGuessedWord = new char[word.length()];
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == guess.charAt(0)) {
                        newGuessedWord[i] = guess.charAt(0);
                        correctGuess = true;
                    } else {
                        newGuessedWord[i] = guessedWord.charAt(i);
                    }
                }

                // Update the guessed word
                guessedWord = String.valueOf(newGuessedWord);

                // Check if the word has been completely guessed
                if (isWordGuessed()) {
                    System.out.println("Congratulations! You guessed the word: " + word);
                    state = "Win";
                    return;
                }

                // Decrement the number of guesses left if the guess was incorrect
                if (!correctGuess) {
                    guessesLeft--;
                    System.out.println("Incorrect guess.");
                }
            }

            // Out of guesses
            System.out.println("\nYou ran out of guesses. The word was: " + word);
            state = "Loss";

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
        if (input.isEmpty()) {
            System.out.println("Invalid input. Please enter a single alphabetic character.");
            return "";
        }
        while (true) {
            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Invalid input. Please enter a single alphabetic character:");
                input = scanner.nextLine();
                continue;
            }
            return input.toLowerCase();
        }

    }

  /*
   LINE BETWEEN STATIC VOID MAIN AND OTHER STATIC CLASS
  THIS LINE IS HERE SO I DON'T FORGET WHERE THE MAIN STARTS
  -----------------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------------
  -----------------------------------------------------------------------------------------------------
  */


    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3307/hm_db";  //declaring my port, database name, SQL username and password
        String username="root";
        String password="";
        Statement stmt = null;

            try
            {
                // Create client socket
                Socket s = new Socket("localhost", 888);

                // to send data to the server
                DataOutputStream dos
                        = new DataOutputStream(
                        s.getOutputStream());

                // to read data coming from the server
                BufferedReader br
                        = new BufferedReader(
                        new InputStreamReader(
                                s.getInputStream()));

                // to read data from the keyboard
                BufferedReader kb
                        = new BufferedReader(
                        new InputStreamReader(System.in));
                String str, str1;
 System.out.println("You're now talking to the other player. Once you both type 'start', the game will begin!");
                // repeat as long as Start
                // is not typed at client
                while (!(str = kb.readLine()).equalsIgnoreCase("start")) {

                    // send to the server
                    dos.writeBytes(str + "\n");

                    // receive from the server
                    str1 = br.readLine();

                    System.out.println(str1);
                }

                // close connection.
                dos.close();

                s.close();



            }

            catch (Exception e) {
                System.out.println(e);
                // terminate application
                System.exit(0);
            }

                try {


            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection(url, username, password);  //connect to jdbc

            Statement statement=connection.createStatement();

            ResultSet resultSet=statement.executeQuery("select * from gamestate");



            stmt = connection.createStatement();


            System.out.println("Waiting for other player...");

            while (resultSet.next()) {

                server= resultSet.getInt(5);

            }


            while (server == 1){
                resultSet=statement.executeQuery("select * from gamestate");
                while (resultSet.next()) {

                    server= resultSet.getInt(5);

                }
            }

            Thread.sleep(2000);
            resultSet=statement.executeQuery("select * from gamestate");

            while (resultSet.next()) {
                word = resultSet.getString(3);
            }
            hm_client game = new hm_client();
            game.playGame();

       String  sql = "UPDATE gamestate SET Client='1' WHERE id=1";
            stmt.executeUpdate(sql);

        sql = "UPDATE gamestate SET Result='" + state + "' WHERE id=1";
     stmt.executeUpdate(sql);




            connection.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}