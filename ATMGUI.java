import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMGUI extends JFrame {
    private ATM atm;

    private JTextField userIdField;
    private JPasswordField pinField;
    private JTextArea displayArea;
    private JComboBox<String> transactionComboBox;
    private JTextField amountField;
    private JButton submitButton;

    public ATMGUI() {
        super("ATM GUI");

        atm = new ATM();

        userIdField = new JTextField(10);
        pinField = new JPasswordField(10);
        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);

        String[] transactions = {"Transactions History", "Withdraw", "Deposit", "Transfer", "Quit"};
        transactionComboBox = new JComboBox<>(transactions);
        amountField = new JTextField(10);
        submitButton = new JButton("Submit");

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));
        loginPanel.add(new JLabel("User ID:"));
        loginPanel.add(userIdField);
        loginPanel.add(new JLabel("PIN:"));
        loginPanel.add(pinField);
        loginPanel.add(new JLabel("Select Transaction:"));
        loginPanel.add(transactionComboBox);
        loginPanel.add(new JLabel("Enter Amount:"));
        loginPanel.add(amountField);

        add(loginPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTransaction();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    private void handleTransaction() {
        String userId = userIdField.getText();
        String pin = new String(pinField.getPassword());

        if (atm.login(userId, pin)) {
            int choice = transactionComboBox.getSelectedIndex() + 1;
            double amount = 0.0; // Default value for transactions without an amount

            // Validate and parse amount for transactions that require an amount
            if (choice != 1 && choice != 5) {
                try {
                    amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        displayArea.setText("Invalid amount. Please enter a positive number.\n");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    displayArea.setText("Invalid amount. Please enter a valid number.\n");
                    return;
                }
            }

            switch (choice) {
                case 1:
                    displayArea.append("Displaying Transactions History...\n");
                    // Implement transaction history logic here
                    break;
                case 2:
                    if (amount > atm.getCurrentUser().getBalance()) {
                        displayArea.append("Insufficient balance.\n");
                        return;
                    }
                    atm.performTransaction(choice, amount);
                    displayArea.append("Withdrawal successful. Remaining balance: " + atm.getCurrentUser().getBalance() + "\n");
                    break;
                case 3:
                    atm.performTransaction(choice, amount);
                    displayArea.append("Deposit successful. New balance: " + atm.getCurrentUser().getBalance() + "\n");
                    break;
                case 4:
                    // Implement transfer logic here
                    break;
                case 5:
                    atm.performTransaction(choice, 0); // amount is not applicable for Quit
                    displayArea.append("Quitting ATM. Thank you!\n");
                    System.exit(0);
                    break;
                default:
                    displayArea.append("Invalid choice. Please try again.\n");
            }
        } else {
            displayArea.setText("Login failed. Please check your user ID and PIN.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ATMGUI();
            }
        });
    }
}
