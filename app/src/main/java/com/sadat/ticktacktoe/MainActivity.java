package com.sadat.ticktacktoe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    // Class variables to hold game state
    private int playerTurn = 1; // 1 for Player 1, 2 for Player 2
    private int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // 0 for empty, 1 for Player 1, 2 for Player 2
    private int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
    };
    private boolean gameActive = true;


    // UI elements - declared as class variables so they can be accessed by all methods
    private TextView playerTurnTextView;
    private Button[] gameBoardButtons;
    private Button resetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize UI elements
        playerTurnTextView = findViewById(R.id.playerTurnTextView);
        resetButton = findViewById(R.id.resetButton);
        initializeGameBoardButtons(); //extracted to its own method


        // Set the initial player turn text
        updatePlayerTurnText();
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }


    private void initializeGameBoardButtons() {
        // Initialize the game board buttons.  Find them by ID.
        gameBoardButtons = new Button[9]; //use the class variable
        GridLayout gridLayout = findViewById(R.id.gridLayout); // Corrected ID to "gridLayout"
        for (int i = 0; i < 9; i++) {
            //dynamically find button ids
            String buttonIdName = "button" + (i + 1);
            int buttonId = getResources().getIdentifier(buttonIdName, "id", getPackageName());
            gameBoardButtons[i] = findViewById(buttonId);
            gameBoardButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleCellClick(v);
                }
            });
        }
    }


    // Method to handle a cell (button) click
    private void handleCellClick(View view) {
        if (!gameActive) {
            return; // Prevent clicks if the game is over
        }


        Button clickedButton = (Button) view;
        int buttonId = Integer.parseInt(clickedButton.getTag().toString()); //getting the tag


        if (gameState[buttonId] == 0) {
            //updates the gamestate array
            gameState[buttonId] = playerTurn;


            //update the button text
            if (playerTurn == 1) {
                clickedButton.setText("X");
            } else {
                clickedButton.setText("O");
            }
            clickedButton.setEnabled(false); // Disable the button after it's clicked


            // Check for a winner or a draw
            checkForWin();
            if(gameActive) { // only switch player if game is still active
                switchPlayer();
            }


        }
    }


    // Method to switch the current player
    private void switchPlayer() {
        playerTurn = (playerTurn == 1) ? 2 : 1;
        updatePlayerTurnText();
    }


    private void updatePlayerTurnText(){
        playerTurnTextView.setText("Player " + playerTurn + "'s Turn");
    }


    // Method to check for a winner
    private void checkForWin() {
        for (int[] winningPosition : winningPositions) {
            if (gameState[winningPosition[0]] != 0 &&
                    gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                    gameState[winningPosition[1]] == gameState[winningPosition[2]]) {
                // We have a winner!
                gameActive = false; // Stop the game.
                String winnerMessage = "Player " + gameState[winningPosition[0]] + " wins!";
                playerTurnTextView.setText(winnerMessage);
                disableAllButtons();
                Toast.makeText(this, winnerMessage, Toast.LENGTH_LONG).show();
                return;
            }
        }


        // Check for a draw
        boolean isDraw = true;
        for (int cellState : gameState) {
            if (cellState == 0) {
                isDraw = false;
                break;
            }
        }
        if (isDraw) {
            gameActive = false;
            playerTurnTextView.setText("It's a Draw!");
            Toast.makeText(this, "It's a Draw!", Toast.LENGTH_LONG).show();
            disableAllButtons();
        }
    }


    private void disableAllButtons() {
        for (Button button : gameBoardButtons) {
            button.setEnabled(false);
        }
    }


    // Method to reset the game
    private void resetGame() {
        gameActive = true;
        playerTurn = 1;
        updatePlayerTurnText();


        // Reset the gameState array
        for (int i = 0; i < 9; i++) {
            gameState[i] = 0;
        }


        // Reset the buttons
        for (Button button : gameBoardButtons) {
            button.setText("");
            button.setEnabled(true); // Enable all buttons
        }


        //reset the textview
        playerTurnTextView.setText("Player 1's Turn");
    }
}
