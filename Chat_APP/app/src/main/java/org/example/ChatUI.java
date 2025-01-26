package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatUI extends JFrame {
    private JPanel chatPanel;           // Displays the chat history.
    private JScrollPane scrollPane;      // A scrollable panel for viewing the chat history.
    private JTextField inputField;       // A text input field for the user to type their message.
    private JButton sendButton;          // A button to send the user’s message.
    private List<String> previousMessages; // Stores the chat history.
    private OpenAI openAI;               // Calls the OpenAI API for generating responses.

    public ChatUI() {
        // Initializes the OpenAI client.
        openAI = new OpenAI();
        // Initializes the chat history.
        previousMessages = new ArrayList<>();

        setTitle("Chat with OpenAI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLayout(new BorderLayout());

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS)); // 垂直布局，添加新消息在底部
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        loadChatHistory();

        // Sets the action to be taken when the send button is clicked.
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userMessage = inputField.getText(); // 获取用户输入
                if (!userMessage.isEmpty()) {
                    // Display the user’s message in the chat interface.
                    addMessageToChat(userMessage, true);
                    //The second parameter true indicates that this is a user-sent message
                    //Store the user’s message in the database for future retrieval or analysis.
                    saveMessageToDatabase(userMessage, true);

                    inputField.setText("");

                    // Use the AskOpenAIConcisely method to send the user’s message and context (previousMessages) to OpenAI
                    // and retrieve the AI’s response.
                    String response = AskOpenAIConcisely(previousMessages, userMessage);

                    //Display the AI’s response in the chat interface.
                    //The second parameter false indicates that this is an AI message (distinguished from the user’s message).
                    //Store the AI’s response in the database for future analysis or record-keeping.
                    addMessageToChat(response, false);
                    saveMessageToDatabase(response, false);
                }
            }
        });
    }
    // Encapsulates the method to call the API.
    public String AskOpenAIConcisely(List<String> previousMessages, String userMessage) {
        userMessage = App.MakeSomeComplexAdditionToTheMessage(userMessage); // 预处理消息
        String response = App.CallOpenAIStub(previousMessages, userMessage);
        previousMessages.add("You: " + userMessage);  // 添加用户消息到对话历史
        previousMessages.add("ChatGPT: " + response); // 添加 AI 回复到对话历史
        return response;
    }
    // Adds the message to the chat area.
    private void addMessageToChat(String message, boolean isUser) {
        // Creates a message panel and sets its alignment.
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));

        JLabel messageLabel = new JLabel("<html>" + message.replaceAll("\n", "<br/>") + "</html>"); // Allows for HTML rendering (for line breaks)
        messageLabel.setOpaque(true); // Make it opaque to support background color

        // Sets different styles based on whether the message is from the user or AI.
        if (isUser) {
            messageLabel.setBackground(new Color(173, 216, 230)); // 用户消息背景色
            messageLabel.setForeground(Color.BLACK);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        } else {
            messageLabel.setBackground(new Color(255, 192, 203)); // AI 消息背景色
            messageLabel.setForeground(Color.BLACK);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        }
        // Ensure the label has a fixed width to allow for word wrapping
        // Ensures the label's size adjusts automatically based on the content.
        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Adds the message label to the message panel and then to the chat panel.
        messagePanel.add(messageLabel);
        chatPanel.add(messagePanel);
        chatPanel.revalidate(); // 刷新聊天面板
        chatPanel.repaint();

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    public static void main(String[] args) {
        // Call the static method initializeDatabase() from the DatabaseHelper class
        //Ensure the application is ready to interact with the database at startup.
        DatabaseHelper.initializeDatabase();

        // 创建并显示聊天界面
        SwingUtilities.invokeLater(() -> {
            ChatUI chatUI = new ChatUI();
            chatUI.setVisible(true);
        });
    }
    //saves a chat message to the database
    private void saveMessageToDatabase(String message, boolean isUser) {
        //Defines an SQL INSERT statement to add a message and its sender type (isUser) into the ChatHistory table.
        String insertSQL = "INSERT INTO ChatHistory (message, isUser) VALUES (?, ?)";
        //Uses DatabaseHelper.getConnection() to establish a database connection.
        //Executes the SQL command to store the data in the database.
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, message);
            pstmt.setBoolean(2, isUser);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //The loadChatHistory method retrieves and displays the chat history stored in the database.
    private void loadChatHistory() {
        //fetch all messages and their sender types (isUser) from the ChatHistory table, ordered by id in ascending order.
        String querySQL = "SELECT message, isUser FROM ChatHistory ORDER BY id ASC";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {
            //display the retrieved messages in the chat interface.
            while (rs.next()) {
                String message = rs.getString("message");
                boolean isUser = rs.getBoolean("isUser");
                addMessageToChat(message, isUser);
            }
            //Catches and logs any SQLException that occurs during the process.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



/*private JTextArea chatArea;       // 显示对话历史
    private JTextField inputField;    // 用户输入框
    private JButton sendButton;       // 发送按钮
    private List<String> previousMessages; // 存储聊天记录
    private OpenAI openAI;            // 调用 OpenAI 的实例

    public ChatUI() {
        // 初始化 OpenAI 客户端
        openAI = new OpenAI();

        // 初始化对话历史
        previousMessages = new ArrayList<>();

        // 设置窗口标题和布局
        setTitle("Chat with OpenAI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // 创建聊天记录区域
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // 创建用户输入框和发送按钮
        inputField = new JTextField();
        sendButton = new JButton("Send");

        // 设置发送按钮的点击事件
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userMessage = inputField.getText();
                if (!userMessage.isEmpty()) {
                    // 显示用户消息并清空输入框
                    chatArea.append("You: " + userMessage + "\n");
                    inputField.setText("");

                    // 发送用户消息并显示 ChatGPT 的回复
                    String response = AskOpenAIConcisely(previousMessages, userMessage);
                    chatArea.append("ChatGPT: " + response + "\n");
                }
            }
        });

        // 添加聊天区域和输入区域到窗口
        add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }

    // 封装的 API 调用方法
    public String AskOpenAIConcisely(List<String> previousMessages, String userMessage) {
        userMessage = App.MakeSomeComplexAdditionToTheMessage(userMessage); // 进行消息的预处理
        String response = App.CallOpenAIStub(previousMessages, userMessage);
        previousMessages.add("You: " + userMessage);  // 添加用户消息到对话历史
        previousMessages.add("ChatGPT: " + response); // 添加回复到对话历史
        return response;
    }

    public static void main(String[] args) {
        // 创建并显示 GUI
        SwingUtilities.invokeLater(() -> {
            ChatUI chatUI = new ChatUI();
            chatUI.setVisible(true);
        });
    }*/


