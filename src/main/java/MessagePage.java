import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MessagePage extends JPanel {
    private JTextPane chatArea;
    private JTextField messageField;
    private User user;
    private JLabel currentChatLabel;
    private String searchText;
    private String fileName;
    private String reverseFileName;

    public MessagePage(User user) {
        this.user = user;

        // 전체 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 400, 50, 400)); // 여백 설정
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(1500, 1000));

        // mainPanel에 JLabel 추가
        JLabel searchLabel = new JLabel("\"플레이어 이름 + #태그\" 를 입력해 메시지 상대를 검색할 수 있습니다.");
        searchLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13)); // 폰트 설정
        searchLabel.setForeground(Color.GRAY);
        searchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // 수평 가운데 정렬
        mainPanel.add(searchLabel);

        // searchUserPanel : 검색창 및 검색 버튼 패널
        JPanel searchUserPanel = createSearchUserPanel();
        mainPanel.add(searchUserPanel);

        // 현재 채팅 상대 Label 추가
        currentChatLabel = new JLabel("플레이어 검색으로 채팅을 시작하세요.");
        currentChatLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15)); // 폰트 설정
        currentChatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // 수평 가운데 정렬
        mainPanel.add(currentChatLabel);

        mainPanel.add(Box.createVerticalStrut(10));  // 10 픽셀의 수직 여백 추가

        // chatViewPanel: chatArea를 감싸는 패널
        JPanel chatViewPanel = new JPanel();
        chatViewPanel.setLayout(new BoxLayout(chatViewPanel, BoxLayout.Y_AXIS));

        // 채팅 영역
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setPreferredSize(new Dimension(200, 500));  // 크기 설정
        chatArea.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30)); // 여백 설정
        chatArea.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(null); // 테두리 없애기
        scrollPane.setBackground(Color.BLACK);

        chatViewPanel.add(scrollPane);
        chatViewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 설정
        chatViewPanel.setBackground(Color.BLACK);

        // 메시지 입력 필드
        messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(500, 50));  // 크기 설정
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 10), // 내부 여백 설정
                BorderFactory.createEmptyBorder() // 테두리 삭제
        ));

        // 전송 버튼 (Image 버튼으로 변경)
        ImageIcon sendIcon = createResizedImageIcon("/images/btn_msg_send.png", 100, 50); // 리사이징하여 생성
        JButton sendButton = new JButton(sendIcon);
        sendButton.addActionListener(new SendButtonListener());

        // 투명하게 만들기
        sendButton.setContentAreaFilled(false);
        sendButton.setBorderPainted(false);

        // inputPanel : 메시지 입력 필드 + 전송 버튼
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 여백 설정
        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        inputPanel.setOpaque(false);  // 배경을 투명하게 설정

        // chatViewNinputPanel : chatViewPanel + inputPanel
        JPanel chatViewNinputPanel = new JPanel();
        chatViewNinputPanel.setLayout(new BoxLayout(chatViewNinputPanel, BoxLayout.Y_AXIS));
        chatViewNinputPanel.add(chatViewPanel);
        chatViewNinputPanel.add(inputPanel);

        // chatViewNinputPanel의 배경색을 검정색으로 설정
        chatViewNinputPanel.setBackground(Color.BLACK);
        chatViewNinputPanel.setPreferredSize(new Dimension(400, 400));  // 크기 설정
        chatViewNinputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 설정
        mainPanel.add(chatViewNinputPanel);

        mainPanel.add(Box.createVerticalGlue()); // 높이 자동 조절

        // mainPanel 추가
        add(mainPanel);
    }

    private JPanel createSearchUserPanel() {
        // 검색창 패널
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setPreferredSize(new Dimension(400, 40)); // 크기 설정
        searchPanel.setBackground(Color.WHITE);

        // 검색 입력 필드
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 30)); // 크기 설정

        searchField.setBackground(new Color(240, 240, 240)); // 배경색을 밝은 회색으로 설정
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2), // 테두리 설정
                BorderFactory.createEmptyBorder(0, 10, 0, 10)  // 내부 여백 설정
        ));
        searchPanel.add(searchField);

        // 검색 버튼 (Image 버튼으로 변경)
        ImageIcon searchIcon = createResizedImageIcon("/images/btn_search.png", 30, 30); // 리사이징하여 생성
        JButton searchButton = new JButton(searchIcon);
        searchButton.setPreferredSize(new Dimension(40, 30));  // 크기 설정
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 검색 버튼을 눌렀을 때
                searchText = searchField.getText();
                showMessageCheckDialog(searchText);
            }
        });

        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setBackground(Color.WHITE);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    // "메시지 상대 확인" 창을 띄우고, 입력한 메시지를 확인하는 메소드
    private void showMessageCheckDialog(String message) {
        JFrame dialogFrame = new JFrame("메시지 상대 확인");
        dialogFrame.setSize(300, 150);

        JLabel infoLabel = new JLabel("확인을 누르시면 채팅방이 열립니다.");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea messageArea = new JTextArea(message);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // 내부 여백 설정

        // BOLD체로 설정
        Font boldFont = new Font(messageArea.getFont().getFontName(), Font.BOLD, messageArea.getFont().getSize());
        messageArea.setFont(boldFont);

        messageArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton confirmButton = new JButton("확인");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 확인 버튼 리스너
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogFrame.dispose();

                // 파일 이름 설정 및 생성
                fileName = "chat_" + user.getFullName() + "_" + message + ".txt";
                reverseFileName = "chat_" + message + "_" + user.getFullName() + ".txt";

                try {
                    checkAndCreateFile(fileName);
                    checkAndCreateFile(reverseFileName);

                    // 파일을 기반으로 메시지 로드
                    loadMessages(fileName);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                String chatLabelContent = "현재 채팅 상대: " + message;
                currentChatLabel.setText(chatLabelContent);
            }
        });

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 여백 설정
        dialogPanel.add(infoLabel);
        dialogPanel.add(Box.createVerticalStrut(10)); // 여백 추가
        dialogPanel.add(messageArea);
        dialogPanel.add(Box.createVerticalStrut(10)); // 여백 추가
        dialogPanel.add(confirmButton);
        dialogFrame.add(dialogPanel);

        // 화면 가운데 배치
        dialogFrame.setLocationRelativeTo(null);
        dialogFrame.setVisible(true);
    }

    // SendButton 이벤트 리스너 : 항상 2개의 파일에 동시에 저장
    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                String formattedMessage = user.getFullName() + ": " + message;

                // 메시지 저장
                saveMessage(formattedMessage, fileName, reverseFileName);

                // 초기화
                messageField.setText("");
                chatArea.setText("");

                // 메시지 업데이트
                try {
                    // 파일을 기반으로 메시지 로드
                    loadMessages(fileName);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // 이미지 아이콘을 생성하는 메서드
    private ImageIcon createImageIcon(String path) {
        // 이미지 아이콘을 생성하고 반환
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("이미지를 찾을 수 없습니다: " + path);
            return null;
        }
    }

    // 이미지 아이콘을 리사이징하여 생성하는 메서드
    private ImageIcon createResizedImageIcon(String path, int width, int height) {
        ImageIcon originalIcon = createImageIcon(path);
        if (originalIcon != null) {
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } else {
            return null;
        }
    }

    // 파일을 체크하고 생성하는 메소드
    private void checkAndCreateFile(String fileName) throws IOException {
        File file = new File(fileName);

        if (file.exists()) {
            // 파일이 이미 존재하는 경우
            System.out.println("파일이 존재합니다: " + fileName);
        } else {
            // 파일이 존재하지 않는 경우 파일을 생성
            if (file.createNewFile()) {
                System.out.println("파일이 생성되었습니다: " + fileName);
            } else {
                System.out.println("파일 생성에 실패했습니다: " + fileName);
            }
        }
    }

    // 이전 메시지 파일에서 로드
    private void loadMessages(String fileName) throws IOException {
        StyledDocument doc = chatArea.getStyledDocument();
        Style defaultStyle = doc.getStyle(StyleContext.DEFAULT_STYLE);

        // 폰트 크기 조절
        StyleConstants.setFontSize(defaultStyle, 15);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                SimpleAttributeSet style = new SimpleAttributeSet();

                // 로그인한 사용자의 메시지는 WHITE, 상대는 RED
                if (line.startsWith(user.getFullName() + ": ")) {
                    StyleConstants.setForeground(style, Color.WHITE);
                } else if (line.startsWith(searchText + ": ")) {
                    StyleConstants.setForeground(style, Color.RED);
                }

                try {
                    // 메시지 추가
                    doc.insertString(doc.getLength(), line + "\n", style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 메시지 저장
    private void saveMessage(String message, String fileName, String reverseFileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 두 번째 파일에도 동일하게 저장
        try (BufferedWriter reverseWriter = new BufferedWriter(new FileWriter(reverseFileName, true))) {
            reverseWriter.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
