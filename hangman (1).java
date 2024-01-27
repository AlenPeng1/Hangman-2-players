package hangman;

import java.util.Scanner;

public class hangman {
	private String word;
	private String guessedWord;
	private int guessesLeft;
	private String guessedLetters;

	public void playGame() {
		Scanner scanner = new Scanner(System.in);

		try {
			// Prompt for the word to be guessed
			System.out.print("Enter the word to be guessed: ");
			String input = scanner.nextLine().toLowerCase();
			while (input.isEmpty()) {
				System.out.println("The word cannot be empty.");
				System.out.print("Enter the word to be guessed: ");
				input = scanner.nextLine().toLowerCase();
			}

			// Validate the word
			while (!input.matches("[a-zA-Z]+")) {
				System.out.println("Invalid input. Please enter alphabetic characters only.");
				System.out.print("Enter the word to be guessed: ");
				input = scanner.nextLine().toLowerCase();
			}

			word = input;

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

	public static void main(String[] args) {
		hangman game = new hangman();
		game.playGame();
	}

}